import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: process.env.REACT_APP_API_URL,
});

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    if (error.response.status === 401) {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
            return axiosInstance.post('/auth/refresh', {refreshToken: refreshToken})
                .then(res => {
                    localStorage.setItem('token', res.data.accessToken);
                    error.config.headers['Authorization'] = 'Bearer ' + res.data.accessToken;
                    return axiosInstance(error.config);
                })
                .catch(error => {
                    window.location.href = "/login";
                    return Promise.reject(error).then();
                });
        } else {
            window.location.href = "/login";
            return Promise.reject(error);
        }
    }

    return Promise.reject(error);
});

export default axiosInstance;