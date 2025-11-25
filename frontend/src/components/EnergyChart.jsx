import { Line, Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Box, CircularProgress, Typography } from '@mui/material';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend
);

export default function EnergyChart({ data, loading, error, chartType = 'line' }) {
    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 5 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Typography color="error" sx={{ py: 3, textAlign: 'center' }}>
                {error}
            </Typography>
        );
    }

    if (!data || data.hourlyData.length === 0) {
        return (
            <Typography color="text.secondary" sx={{ py: 3, textAlign: 'center' }}>
                No consumption data available for this date.
            </Typography>
        );
    }

    const hours = Array.from({ length: 24 }, (_, i) => i);
    const consumptionMap = {};
    
    data.hourlyData.forEach(item => {
        consumptionMap[item.hour] = item.totalConsumption;
    });

    const consumptionValues = hours.map(hour => consumptionMap[hour] || 0);

    const chartData = {
        labels: hours.map(h => `${h}:00`),
        datasets: [
            {
                label: 'Energy Consumption (kWh)',
                data: consumptionValues,
                borderColor: 'rgb(75, 192, 192)',
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                tension: 0.2,
            },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: `Energy Consumption for ${data.date}`,
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        return `${context.parsed.y.toFixed(2)} kWh`;
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Energy (kWh)',
                },
            },
            x: {
                title: {
                    display: true,
                    text: 'Hour of Day',
                },
            },
        },
    };

    return (
        <Box sx={{ height: 400, width: '100%' }}>
            {chartType === 'line' ? (
                <Line data={chartData} options={options} />
            ) : (
                <Bar data={chartData} options={options} />
            )}
        </Box>
    );
}
