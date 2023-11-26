import axiosInstance from "../interceptor/axiosInterceptor"

export default class OrderService {
    static createOrder = async (uuid: string) => {
        return await axiosInstance.post("/order/create", {uuid: uuid}, {withCredentials: true});
    };

    static getOrder = async (uuid: string) => {
        return await axiosInstance.get(`/order/${uuid}`,{withCredentials: true});
    };

    static payOrder = async (uuid: string) => {
        return await axiosInstance.get(`/order/${uuid}/pay`,{withCredentials: true});
    }

    static cancelOrder = async (uuid: string) => {
        return await axiosInstance.get(`/order/${uuid}/cancel`,{withCredentials: true});
    }

    static getOrderPage = async (pageNumber: number) => {
        return await axiosInstance.get(`/order/history`,{params: {
                page: pageNumber,
                size: 3,
            }, withCredentials: true});
    }
}