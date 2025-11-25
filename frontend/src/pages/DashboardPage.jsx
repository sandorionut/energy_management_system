import {useEffect, useState} from 'react';
import {
    Box,
    Container,
    Paper,
    Stack,
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    CircularProgress,
    TextField,
    IconButton,
    InputAdornment,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
    ToggleButtonGroup,
    ToggleButton,
    Divider,
} from '@mui/material';
import {Search, ElectricBolt, Refresh, ShowChart, BarChart} from '@mui/icons-material';
import AppTopBar from '../components/layout/AppTopBar';
import {getUserDevices, getDeviceByName} from '../api/deviceApi';
import {getDeviceConsumption} from '../api/monitoringApi';
import EnergyChart from '../components/EnergyChart';

export default function DashboardPage() {
    const [devices, setDevices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [query, setQuery] = useState('');
    const [searching, setSearching] = useState(false);

    const [selectedDevice, setSelectedDevice] = useState('');
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
    const [chartType, setChartType] = useState('line');
    const [consumptionData, setConsumptionData] = useState(null);
    const [chartLoading, setChartLoading] = useState(false);
    const [chartError, setChartError] = useState(null);

    useEffect(() => {
        const fetchDevices = async () => {
            try {
                const data = await getUserDevices();
                setDevices(data);
                // Auto-select first device
                if (data.length > 0) {
                    setSelectedDevice(data[0].id);
                }
            } catch (err) {
                console.error('Failed to load devices', err);
            } finally {
                setLoading(false);
            }
        };
        fetchDevices();
    }, []);

    useEffect(() => {
        if (!selectedDevice || !selectedDate) return;

        const fetchConsumption = async () => {
            setChartLoading(true);
            setChartError(null);
            try {
                const data = await getDeviceConsumption(selectedDevice, selectedDate);
                setConsumptionData(data);
            } catch (err) {
                console.error('Failed to load consumption data', err);
                setChartError(err.response?.data?.message || 'Failed to load consumption data');
            } finally {
                setChartLoading(false);
            }
        };

        fetchConsumption();
    }, [selectedDevice, selectedDate]);

    const handleSearch = async () => {
        if (!query.trim()) return;
        setSearching(true);
        try {
            const data = await getDeviceByName(query);
            setDevices(data ? [data] : []);
            if (data) {
                setSelectedDevice(data.id);
            }
        } catch (err) {
            console.error('Search failed', err);
            setDevices([]);
        } finally {
            setSearching(false);
        }
    };

    const handleReset = async () => {
        setQuery('');
        setSearching(true);
        try {
            const data = await getUserDevices();
            setDevices(data);
            // Reselect first device after reset
            if (data.length > 0) {
                setSelectedDevice(data[0].id);
            }
        } finally {
            setSearching(false);
        }
    };

    return (
        <Box
            sx={{
                minHeight: '100vh',
                background: 'linear-gradient(180deg,#f8fafc,#ffffff)',
            }}
        >
            <AppTopBar/>
            <Container sx={{py: 6}}>
                <Typography variant="h4" fontWeight={700} gutterBottom>
                    Dashboard
                </Typography>
                <Typography color="text.secondary" sx={{mb: 4}}>
                    View your devices and energy consumption.
                </Typography>

                <Paper elevation={3} sx={{p: 3, mb: 4}}>
                    <Typography variant="h6" fontWeight={600} gutterBottom>
                        My Devices
                    </Typography>
                    <Stack
                        direction={{xs: 'column', sm: 'row'}}
                        spacing={2}
                        sx={{mb: 3}}
                    >
                        <TextField
                            label="Search device by name"
                            variant="outlined"
                            fullWidth
                            value={query}
                            onChange={(e) => setQuery(e.target.value)}
                            InputProps={{
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton color="primary" onClick={handleSearch}>
                                            <Search/>
                                        </IconButton>
                                    </InputAdornment>
                                ),
                            }}
                        />
                        <IconButton onClick={handleReset} color="secondary">
                            <Refresh/>
                        </IconButton>
                    </Stack>

                    {loading || searching ? (
                        <Stack alignItems="center" sx={{py: 5}}>
                            <CircularProgress/>
                        </Stack>
                    ) : devices.length > 0 ? (
                        <TableContainer component={Paper} elevation={1}>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell sx={{fontWeight: 700}}>Name</TableCell>
                                        <TableCell sx={{fontWeight: 700}}>
                                            Max Consumption (W)
                                        </TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {devices.map((device) => (
                                        <TableRow key={device.id} hover>
                                            <TableCell>
                                                <Stack direction="row" alignItems="center" spacing={1}>
                                                    <ElectricBolt color="primary"/>
                                                    <Typography>{device.name}</Typography>
                                                </Stack>
                                            </TableCell>
                                            <TableCell>{device.maxConsumption}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    ) : (
                        <Typography
                            color="text.secondary"
                            sx={{py: 3, textAlign: 'center'}}
                        >
                            No devices found.
                        </Typography>
                    )}
                </Paper>

                {devices.length > 0 && (
                    <Paper elevation={3} sx={{p: 3}}>
                        <Typography variant="h6" fontWeight={600} gutterBottom>
                            Energy Consumption
                        </Typography>
                        <Divider sx={{mb: 3}} />

                        <Stack
                            direction={{xs: 'column', sm: 'row'}}
                            spacing={2}
                            sx={{mb: 3}}
                            alignItems="center"
                        >
                            <FormControl fullWidth>
                                <InputLabel>Device</InputLabel>
                                <Select
                                    value={selectedDevice}
                                    onChange={(e) => setSelectedDevice(e.target.value)}
                                    label="Device"
                                >
                                    {devices.map((device) => (
                                        <MenuItem key={device.id} value={device.id}>
                                            {device.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>

                            <TextField
                                label="Date"
                                type="date"
                                value={selectedDate}
                                onChange={(e) => setSelectedDate(e.target.value)}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                fullWidth
                            />

                            <ToggleButtonGroup
                                value={chartType}
                                exclusive
                                onChange={(e, newType) => {
                                    if (newType) setChartType(newType);
                                }}
                                aria-label="chart type"
                            >
                                <ToggleButton value="line" aria-label="line chart">
                                    <ShowChart sx={{mr: 1}} />
                                    Line
                                </ToggleButton>
                                <ToggleButton value="bar" aria-label="bar chart">
                                    <BarChart sx={{mr: 1}} />
                                    Bar
                                </ToggleButton>
                            </ToggleButtonGroup>
                        </Stack>

                        <EnergyChart
                            data={consumptionData}
                            loading={chartLoading}
                            error={chartError}
                            chartType={chartType}
                        />
                    </Paper>
                )}
            </Container>
        </Box>
    );
}
