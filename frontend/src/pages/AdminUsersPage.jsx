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
    IconButton,
    InputAdornment,
} from '@mui/material';
import {DataGrid} from '@mui/x-data-grid';
import AppTopBar from '../components/layout/AppTopBar';
import SearchIcon from '@mui/icons-material/Search';
import {
    getAllUsers,
    getUserById,
    updateUser,
    deleteUserById,
} from '../api/userApi';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useNavigate} from 'react-router-dom';

export default function AdminUsersPage() {
    const [rows, setRows] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedUser, setSelectedUser] = useState(null);
    const [openEdit, setOpenEdit] = useState(false);
    const [searchId, setSearchId] = useState('');
    const [searchLoading, setSearchLoading] = useState(false);

    const navigate = useNavigate();

    const fetchUsers = async () => {
        try {
            const data = await getAllUsers();
            setRows(data);
        } catch (err) {
            console.error('Failed to fetch users', err);
        } finally {
            setLoading(false);
        }
    };

    const deleteUser = async (id) => {
        if (!window.confirm('Delete this user?')) return;
        try {
            await deleteUserById(id);
            setRows(rows.filter((u) => u.id !== id));
        } catch (err) {
            console.error('Failed to delete user', err);
        }
    };

    const handleEdit = async (id) => {
        try {
            const user = await getUserById(id);
            setSelectedUser(user);
            setOpenEdit(true);
        } catch (err) {
            console.error('Failed to fetch user details', err);
        }
    };

    const handleSave = async () => {
        const payload = {
            firstName: selectedUser.firstName,
            lastName: selectedUser.lastName,
            email: selectedUser.email,
            password: selectedUser.password || '1234',
            role: selectedUser.role,
        };

        try {
            await updateUser(selectedUser.id, payload);
            setOpenEdit(false);
            fetchUsers();
        } catch (err) {
            console.error('Failed to update user', err);
        }
    };
    const handleSearch = async () => {
        if (!searchId.trim()) return;
        setSearchLoading(true);
        try {
            const user = await getUserById(searchId.trim());
            setRows([user]);
        } catch (err) {
            console.error('User not found', err);
            setRows([]);
        } finally {
            setSearchLoading(false);
        }
    };

    const handleResetSearch = () => {
        setSearchId('');
        fetchUsers();
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const columns = [
        {field: 'id', headerName: 'ID', width: 250},
        {field: 'firstName', headerName: 'First Name', width: 150},
        {field: 'lastName', headerName: 'Last Name', width: 150},
        {field: 'email', headerName: 'Email', width: 200},
        {field: 'role', headerName: 'Role', width: 120},
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
                        onClick={() => handleEdit(params.row.id)}
                    >
                        Edit
                    </Button>
                    <Button
                        size="small"
                        color="error"
                        variant="outlined"
                        onClick={() => deleteUser(params.row.id)}
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
                    Manage Users
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

                <Box sx={{display: 'flex', alignItems: 'center', gap: 2, mb: 2}}>
                    <TextField
                        label="Search by User ID"
                        variant="outlined"
                        size="small"
                        value={searchId}
                        onChange={(e) => setSearchId(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
                        sx={{flex: 1}}
                        InputProps={{
                            endAdornment: (
                                <InputAdornment position="end">
                                    <IconButton onClick={handleSearch} disabled={searchLoading}>
                                        <SearchIcon/>
                                    </IconButton>
                                </InputAdornment>
                            ),
                        }}
                    />
                    <Button
                        onClick={handleResetSearch}
                        variant="outlined"
                        color="secondary"
                    >
                        Reset
                    </Button>
                </Box>

                <Paper sx={{height: 520, p: 2}}>
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

                <Dialog
                    open={openEdit}
                    onClose={() => setOpenEdit(false)}
                    maxWidth="sm"
                    fullWidth
                >
                    <DialogTitle>Edit User</DialogTitle>
                    <DialogContent>
                        {selectedUser && (
                            <Stack spacing={2} sx={{mt: 2}}>
                                <TextField
                                    label="First Name"
                                    value={selectedUser.firstName || ''}
                                    onChange={(e) =>
                                        setSelectedUser({
                                            ...selectedUser,
                                            firstName: e.target.value,
                                        })
                                    }
                                />
                                <TextField
                                    label="Last Name"
                                    value={selectedUser.lastName || ''}
                                    onChange={(e) =>
                                        setSelectedUser({
                                            ...selectedUser,
                                            lastName: e.target.value,
                                        })
                                    }
                                />
                                <TextField
                                    label="Email"
                                    value={selectedUser.email || ''}
                                    onChange={(e) =>
                                        setSelectedUser({...selectedUser, email: e.target.value})
                                    }
                                />
                                <TextField
                                    label="Role"
                                    value={selectedUser.role || ''}
                                    onChange={(e) =>
                                        setSelectedUser({...selectedUser, role: e.target.value})
                                    }
                                />
                                <TextField
                                    label="New Password (optional)"
                                    type="password"
                                    value={selectedUser.password || ''}
                                    onChange={(e) =>
                                        setSelectedUser({
                                            ...selectedUser,
                                            password: e.target.value,
                                        })
                                    }
                                />
                            </Stack>
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setOpenEdit(false)}>Cancel</Button>
                        <Button onClick={handleSave} variant="contained">
                            Save
                        </Button>
                    </DialogActions>
                </Dialog>
            </Container>
        </Box>
    );
}
