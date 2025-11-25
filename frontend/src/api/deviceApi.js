import api from './axiosConfig';

export const getUserDevices = async () => {
    const res = await api.get('/device/api/device');
    return res.data;
};

export const getDeviceByName = async (name) => {
    const res = await api.get(`/device/api/device/${name}`);
    return res.data;
};

export const getAllDevices = async () => {
    const res = await api.get('/device/api/device/all');
    return res.data;
};

export const createDevice = async (payload) => {
    const res = await api.post('/device/api/device', payload);
    return res.data;
};

export const updateDevice = async (id, payload) => {
    const res = await api.put(`/device/api/device/${id}`, payload);
    return res.data;
};

export const deleteDeviceById = async (id) => {
    const res = await api.delete(`/device/api/device/${id}`);
    return res.data;
};

export const assignDeviceToUser = async (deviceId, userId) => {
    const res = await api.post(`/device/api/device/${deviceId}/assign/${userId}`);
    return res.data;
};
