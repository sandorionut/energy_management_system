import {useEffect, useState} from 'react';
import {
    Box,
    Container,
    Typography,
    Button,
    Paper,
    Stack,
} from '@mui/material';
import AppTopBar from '../components/layout/AppTopBar';

export default function AdminPage() {
    const [message, setMessage] = useState('');

    useEffect(() => {
        setMessage('Welcome, Admin. Here you can manage users and devices.');
    }, []);

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
                    Admin Dashboard
                </Typography>
                <Typography color="text.secondary" sx={{mb: 4}}>
                    {message}
                </Typography>

                <Paper elevation={3} sx={{p: 3}}>
                    <Stack spacing={2}>
                        <Button variant="contained" href="/admin/users">
                            Manage Users
                        </Button>
                        <Button variant="contained" href="/admin/devices">
                            Manage Devices
                        </Button>
                    </Stack>
                </Paper>
            </Container>
        </Box>
    );
}
