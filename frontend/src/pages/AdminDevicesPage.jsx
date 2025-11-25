import {useEffect, useState} from 'react';
import {
    Box,
    Container,
    Typography,
    Paper,
    Button,
    Stack,
    CircularProgress,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
    Divider,
    ToggleButtonGroup,
    ToggleButton
} from '@mui/material';
import {DataGrid} from '@mui/x-data-grid';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {ShowChart, BarChart} from '@mui/icons-material';
import {useNavigate} from 'react-router-dom';
import AppTopBar from '../components/layout/AppTopBar';
import {
    getAllDevices,
    createDevice,
    updateDevice,
    deleteDeviceById,
    assignDeviceToUser,
} from '../api/deviceApi';
import {getAllUsers} from "../api/userApi";
import {getDeviceConsumption} from '../api/monitoringApi';
import EnergyChart from '../components/EnergyChart';

export default function AdminDevicesPage() {
    const [rows, setRows] = useState([]);
    const [loading, setLoading] = useState(true);
    const [openEdit, setOpenEdit] = useState(false);
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [openCreate, setOpenCreate] = useState(false);
    const [newDevice, setNewDevice] = useState({name: '', maxConsumption: ''});
    const [assignData, setAssignData] = useState({deviceId: '', userId: ''});
    const [openAssign, setOpenAssign] = useState(false);
    const [users, setUsers] = useState([]);
    const [devicesList, setDevicesList] = useState([]);
    const navigate = useNavigate();

    const [chartSelectedDevice, setChartSelectedDevice] = useState('');
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
    const [chartType, setChartType] = useState('line');
    const [consumptionData, setConsumptionData] = useState(null);
    const [chartLoading, setChartLoading] = useState(false);
    const [chartError, setChartError] = useState(null);

    const fetchDevices = async () => {
        try {
            const data = await getAllDevices();
            setRows(data);
            setDevicesList(data);
            if (data.length > 0 && !chartSelectedDevice) {
                setChartSelectedDevice(data[0].id);
            }
        } catch (err) {
            console.error('Failed to fetch devices', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDevices();

        const fetchUsers = async () => {
            try {
                const usersData = await getAllUsers();
                setUsers(usersData);
            } catch (err) {
                console.error("Failed to fetch users for dropdowns", err);
            }
        };

        fetchUsers();
    }, []);

    useEffect(() => {
        if (!chartSelectedDevice || !selectedDate) return;

        const fetchConsumption = async () => {
            setChartLoading(true);
            setChartError(null);
            try {
                const data = await getDeviceConsumption(chartSelectedDevice, selectedDate);
                setConsumptionData(data);
            } catch (err) {
                console.error('Failed to load consumption data', err);
                setChartError(err.response?.data?.message || 'Failed to load consumption data');
            } finally {
                setChartLoading(false);
            }
        };

        fetchConsumption();
    }, [chartSelectedDevice, selectedDate]);

    const handleDelete = async (id) => {
        if (!window.confirm('Delete this device?')) return;
        try {
            await deleteDeviceById(id);
            setRows(rows.filter((d) => d.id !== id));
            setDevicesList(devicesList.filter((d) => d.id !== id));
            if (chartSelectedDevice === id) {
                setChartSelectedDevice('');
                setConsumptionData(null);
            }
        } catch (err) {
            console.error('Failed to delete device', err);
        }
    };

    const handleSaveEdit = async () => {
        try {
            await updateDevice(selectedDevice.id, {
                name: selectedDevice.name,
                maxConsumption: selectedDevice.maxConsumption,
            });
            setOpenEdit(false);
            fetchDevices();
        } catch (err) {
            console.error('Failed to update device', err);
        }
    };

    const handleCreate = async () => {
        try {
            await createDevice({
                name: newDevice.name,
                maxConsumption: parseFloat(newDevice.maxConsumption),
            });
            setOpenCreate(false);
            setNewDevice({name: '', maxConsumption: ''});
            fetchDevices();
        } catch (err) {
            console.error('Failed to create device', err);
        }
    };

    const handleAssign = async () => {
        try {
            await assignDeviceToUser(assignData.deviceId, assignData.userId);
            setOpenAssign(false);
            fetchDevices();
        } catch (err) {
            console.error('Failed to assign device', err);
        }
    };

    const columns = [
        {field: 'id', headerName: 'ID', width: 260},
        {field: 'name', headerName: 'Name', width: 200},
        {field: 'maxConsumption', headerName: 'Max Consumption (W)', width: 200},
        {
            field: 'actions',
            headerName: 'Actions',
            width: 200,
            sortable: false,
            headerAlign: 'center',
            align: 'center',
            renderCell: (params) => (
                <Box
                    sx={{
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        gap: 1,
                        width: '100%',
                        height: '100%',
                    }}
                >
                    <Button
                        size="small"
                        variant="outlined"
                        onClick={() => {
                            setSelectedDevice(params.row);
                            setOpenEdit(true);
                        }}
                    >
                        Edit
                    </Button>
                    <Button
                        size="small"
                        color="error"
                        variant="outlined"
                        onClick={() => handleDelete(params.row.id)}
                    >
                        Delete
                    </Button>
                </Box>
            ),
        },
    ];

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
                    Manage Devices
                </Typography>

                <Button
                    startIcon={<ArrowBackIcon/>}
                    variant="outlined"
                    color="primary"
                    onClick={() => navigate('/admin')}
                    sx={{mb: 3}}
                >
                    Back to Admin Dashboard
                </Button>

                <Stack direction="row" spacing={2} sx={{mb: 2}}>
                    <Button variant="contained" onClick={() => setOpenCreate(true)}>
                        Add Device
                    </Button>
                    <Button variant="outlined" onClick={() => setOpenAssign(true)}>
                        Assign Device
                    </Button>
                </Stack>

                <Paper sx={{height: 500, p: 2, mb: 4}}>
                    {loading ? (
                        <Stack
                            alignItems="center"
                            justifyContent="center"
                            sx={{height: 1}}
                        >
                            <CircularProgress/>
                        </Stack>
                    ) : (
                        <DataGrid
                            rows={rows}
                            columns={columns}
                            getRowId={(row) => row.id}
                        />
                    )}
                </Paper>

                <Paper elevation={3} sx={{p: 3}}>
                    <Typography variant="h6" fontWeight={600} gutterBottom>
                        Device Energy Consumption
                    </Typography>
                    <Divider sx={{mb: 3}} />

                    <Stack
                        direction={{xs: 'column', sm: 'row'}}
                        spacing={2}
                        sx={{mb: 3}}
                        alignItems="center"
                    >
                        <FormControl fullWidth>
                            <InputLabel>Select Device</InputLabel>
                            <Select
                                value={chartSelectedDevice}
                                onChange={(e) => setChartSelectedDevice(e.target.value)}
                                label="Select Device"
                            >
                                {devicesList.map((device) => (
                                    <MenuItem key={device.id} value={device.id}>
                                        {device.name} ({device.id})
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

                <Dialog
                    open={openEdit}
                    onClose={() => setOpenEdit(false)}
                    maxWidth="sm"
                    fullWidth
                >
                    <DialogTitle>Edit Device</DialogTitle>
                    <DialogContent>
                        {selectedDevice && (
                            <Stack spacing={2} sx={{mt: 2}}>
                                <TextField
                                    label="Name"
                                    value={selectedDevice.name}
                                    onChange={(e) =>
                                        setSelectedDevice({
                                            ...selectedDevice,
                                            name: e.target.value,
                                        })
                                    }
                                />
                                <TextField
                                    label="Max Consumption"
                                    type="number"
                                    value={selectedDevice.maxConsumption}
                                    onChange={(e) =>
                                        setSelectedDevice({
                                            ...selectedDevice,
                                            maxConsumption: e.target.value,
                                        })
                                    }
                                />
                            </Stack>
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setOpenEdit(false)}>Cancel</Button>
                        <Button variant="contained" onClick={handleSaveEdit}>
                            Save
                        </Button>
                    </DialogActions>
                </Dialog>

                <Dialog
                    open={openCreate}
                    onClose={() => setOpenCreate(false)}
                    maxWidth="sm"
                    fullWidth
                >
                    <DialogTitle>Create Device</DialogTitle>
                    <DialogContent>
                        <Stack spacing={2} sx={{mt: 2}}>
                            <TextField
                                label="Name"
                                value={newDevice.name}
                                onChange={(e) =>
                                    setNewDevice({...newDevice, name: e.target.value})
                                }
                            />
                            <TextField
                                label="Max Consumption"
                                type="number"
                                value={newDevice.maxConsumption}
                                onChange={(e) =>
                                    setNewDevice({...newDevice, maxConsumption: e.target.value})
                                }
                            />
                        </Stack>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setOpenCreate(false)}>Cancel</Button>
                        <Button variant="contained" onClick={handleCreate}>
                            Create
                        </Button>
                    </DialogActions>
                </Dialog>

                <Dialog
                    open={openAssign}
                    onClose={() => setOpenAssign(false)}
                    maxWidth="sm"
                    fullWidth
                >
                    <DialogTitle>Assign Device to User</DialogTitle>
                    <DialogContent>
                        <Stack spacing={2} sx={{mt: 2}}>
                            <Select
                                fullWidth
                                value={assignData.deviceId}
                                displayEmpty
                                onChange={(e) => setAssignData({...assignData, deviceId: e.target.value})}
                            >
                                <MenuItem value="" disabled>Select Device</MenuItem>
                                {devicesList.map((d) => (
                                    <MenuItem key={d.id} value={d.id}>
                                        {d.name} ({d.id})
                                    </MenuItem>
                                ))}
                            </Select>

                            <Select
                                fullWidth
                                value={assignData.userId}
                                displayEmpty
                                onChange={(e) => setAssignData({...assignData, userId: e.target.value})}
                            >
                                <MenuItem value="" disabled>Select User</MenuItem>
                                {users.map((u) => (
                                    <MenuItem key={u.id} value={u.id}>
                                        {u.fullName || u.email} ({u.id})
                                    </MenuItem>
                                ))}
                            </Select>
                        </Stack>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setOpenAssign(false)}>Cancel</Button>
                        <Button variant="contained" onClick={handleAssign}>
                            Assign
                        </Button>
                    </DialogActions>
                </Dialog>
            </Container>
        </Box>
    );
}
