import {Box, Container, Paper, Stack, Typography} from '@mui/material';

export default function AuthShell({title, subtitle, children}) {
    return (
        <Box
            sx={{
                minHeight: '100vh',
                display: 'grid',
                placeItems: 'center',
                background:
                    'radial-gradient(1200px 600px at 10% -10%, #dbeafe 10%, transparent), radial-gradient(1200px 600px at 110% 110%, #dcfce7 10%, transparent), linear-gradient(180deg, #f8fafc, #ffffff)',
            }}
        >
            <Container maxWidth="sm">
                <Paper elevation={6} sx={{p: 4}}>
                    <Stack spacing={2} alignItems="center" sx={{mb: 1}}>
                        <Typography variant="h4">{title}</Typography>
                        {subtitle && (
                            <Typography variant="body2" color="text.secondary" align="center">
                                {subtitle}
                            </Typography>
                        )}
                    </Stack>
                    {children}
                </Paper>
            </Container>
        </Box>
    );
}
