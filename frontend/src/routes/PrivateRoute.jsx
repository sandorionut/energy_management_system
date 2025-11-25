import {Navigate} from 'react-router-dom';
import {jwtDecode} from 'jwt-decode';

export default function PrivateRoute({children, requiredRole}) {
    const token = localStorage.getItem('token');

    if (!token) return <Navigate to="/login"/>;

    try {
        const decoded = jwtDecode(token);
        const role = decoded.role || decoded.Role || decoded['X-Role'];

        if (requiredRole && role !== requiredRole) {
            return <Navigate to="/dashboard"/>;
        }

        return children;
    } catch {
        localStorage.removeItem('token');
        return <Navigate to="/login"/>;
    }
}
