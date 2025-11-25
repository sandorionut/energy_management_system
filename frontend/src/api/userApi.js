import api from './axiosConfig';

export const getAllUsers = async () => {
    const res = await api.get('/user/api/users');
    return res.data;
};

export const getUserById = async (id) => {
    const res = await api.get(`/user/api/users/${id}`);
    return res.data;
};

export const updateUser = async (id, payload) => {
    const res = await api.put(`/user/api/users/${id}`, payload);
    return res.data;
};

export const deleteUserById = async (id) => {
    const res = await api.delete(`/user/api/users/${id}`);
    return res.data;
};
