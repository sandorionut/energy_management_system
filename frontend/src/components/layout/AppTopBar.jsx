import {AppBar, Toolbar, Button, Box} from '@mui/material';
import {Logout} from '@mui/icons-material';
import {useNavigate} from 'react-router-dom';

export default function AppTopBar() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const logout = () => {
        localStorage.removeItem('token');
        navigate('/', {replace: true});
    };

    return (
        <AppBar
            position="sticky"
            color="transparent"
            elevation={0}
            sx={{
                backdropFilter: 'saturate(180%) blur(8px)',
                borderBottom: '1px solid #eef2f7',
            }}
        >
            <Toolbar>
                <Box sx={{flex: 1}}/>
                {token && (
                    <Button variant="outlined" startIcon={<Logout/>} onClick={logout}>
                        Logout
                    </Button>
                )}
            </Toolbar>
        </AppBar>
    );
}
