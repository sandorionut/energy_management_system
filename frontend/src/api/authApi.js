import api from './axiosConfig';

export const login = async (email, password) => {
    const {data} = await api.post('/auth/api/auth/login', {email, password});
    return data.token || data;
};
export const register = async (payload) => {
    const {data} = await api.post('/auth/api/auth/register', payload);
    return data;
};
