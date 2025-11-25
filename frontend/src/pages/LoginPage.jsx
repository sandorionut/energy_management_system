import {useState} from 'react';
import {Link as RouterLink} from 'react-router-dom';
import {login} from '../api/authApi';
import AuthShell from '../components/layout/AuthShell';
import PasswordField from '../components/common/PasswordField';
import {
    Alert,
    Box,
    Button,
    Link,
    Stack,
    TextField,
    CircularProgress,
} from '@mui/material';
import {Login as LoginIcon} from '@mui/icons-material';
import {jwtDecode} from 'jwt-decode';

export default function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [busy, setBusy] = useState(false);

    const submit = async (e) => {
        e.preventDefault();
        setError('');
        setBusy(true);
        try {
            const data = await login(email, password);
            const token = data.token || data;
            localStorage.setItem('token', token);

            const {role} = jwtDecode(token);
            if (role === 'ADMIN') {
                window.location.href = '/admin';
            } else {
                window.location.href = '/dashboard';
            }
        } catch {
            setError('Invalid email or password.');
        } finally {
            setBusy(false);
        }
    };

    return (
        <AuthShell title="Sign in">
            <Box component="form" onSubmit={submit}>
                <Stack spacing={2}>
                    <TextField
                        label="Email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        fullWidth
                        required
                    />
                    <PasswordField
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    {error && <Alert severity="error">{error}</Alert>}
                    <Button
                        type="submit"
                        variant="contained"
                        size="large"
                        startIcon={!busy && <LoginIcon/>}
                        disabled={busy}
                    >
                        {busy ? (
                            <CircularProgress size={22} sx={{color: 'common.white'}}/>
                        ) : (
                            'Sign In'
                        )}
                    </Button>
                    <Link
                        component={RouterLink}
                        to="/register"
                        underline="hover"
                        sx={{textAlign: 'center', mt: 1}}
                    >
                        Don’t have an account? Create one
                    </Link>
                </Stack>
            </Box>
        </AuthShell>
    );
}
