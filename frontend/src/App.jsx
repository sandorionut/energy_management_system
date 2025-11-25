import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
} from 'react-router-dom';
import {ThemeProvider, CssBaseline} from '@mui/material';
import theme from './theme';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import PrivateRoute from './routes/PrivateRoute';
import AdminPage from './pages/AdminPage';
import AdminUsersPage from './pages/AdminUsersPage';
import AdminDevicesPage from './pages/AdminDevicesPage';

export default function App() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <Router>
                <Routes>
                    <Route path="/login" element={<LoginPage/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>
                    <Route
                        path="/dashboard"
                        element={
                            <PrivateRoute>
                                <DashboardPage/>
                            </PrivateRoute>
                        }
                    />
                    <Route path="*" element={<Navigate to="/login" replace/>}/>
                    <Route
                        path="/admin"
                        element={
                            <PrivateRoute requiredRole="ADMIN">
                                <AdminPage/>
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/admin/users"
                        element={
                            <PrivateRoute requiredRole="ADMIN">
                                <AdminUsersPage/>
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/admin/devices"
                        element={
                            <PrivateRoute requiredRole="ADMIN">
                                <AdminDevicesPage/>
                            </PrivateRoute>
                        }
                    />
                </Routes>
            </Router>
        </ThemeProvider>
    );
}
