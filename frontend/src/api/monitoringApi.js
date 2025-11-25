import axiosInstance from './axiosConfig';

export const getDeviceConsumption = async (deviceId, date) => {
    const response = await axiosInstance.get(`/monitoring/api/monitoring/devices/${deviceId}/consumption`, {
        params: { date }
    });
    return response.data;
};
