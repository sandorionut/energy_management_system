import {useState} from 'react';
import {Link as RouterLink} from 'react-router-dom';
import {register as registerApi} from '../api/authApi';
import AuthShell from '../components/layout/AuthShell';
import PasswordField from '../components/common/PasswordField';
import {
    Alert,
    Box,
    Button,
    Grid,
    Link,
    Stack,
    TextField,
    CircularProgress,
} from '@mui/material';
import {PersonAdd} from '@mui/icons-material';

export default function RegisterPage() {
    const [form, setForm] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        role: 'USER',
    });
    const [ok, setOk] = useState('');
    const [err, setErr] = useState('');
    const [busy, setBusy] = useState(false);

    const change = (e) => setForm({...form, [e.target.name]: e.target.value});

    const submit = async (e) => {
        e.preventDefault();
        setOk('');
        setErr('');
        setBusy(true);
        try {
            await registerApi(form);
            setOk('Registration successful. You can now log in.');
        } catch {
            setErr('Registration failed. Try again.');
        } finally {
            setBusy(false);
        }
    };

    return (
        <AuthShell title="Create account">
            <Box component="form" onSubmit={submit}>
                <Stack spacing={2}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                name="firstName"
                                label="First name"
                                value={form.firstName}
                                onChange={change}
                                fullWidth
                                required
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                name="lastName"
                                label="Last name"
                                value={form.lastName}
                                onChange={change}
                                fullWidth
                                required
                            />
                        </Grid>
                    </Grid>
                    <TextField
                        name="email"
                        label="Email"
                        type="email"
                        value={form.email}
                        onChange={change}
                        fullWidth
                        required
                    />
                    <PasswordField
                        name="password"
                        value={form.password}
                        onChange={change}
                    />
                    {ok && <Alert severity="success">{ok}</Alert>}
                    {err && <Alert severity="error">{err}</Alert>}
                    <Button
                        type="submit"
                        variant="contained"
                        size="large"
                        startIcon={!busy && <PersonAdd/>}
                        disabled={busy}
                    >
                        {busy ? (
                            <CircularProgress size={22} sx={{color: 'common.white'}}/>
                        ) : (
                            'Register'
                        )}
                    </Button>
                    <Link
                        component={RouterLink}
                        to="/login"
                        underline="hover"
                        sx={{textAlign: 'center', mt: 1}}
                    >
                        Already have an account? Sign in
                    </Link>
                </Stack>
            </Box>
        </AuthShell>
    );
}
