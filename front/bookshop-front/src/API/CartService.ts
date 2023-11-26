import axiosInstance from "../interceptor/axiosInterceptor";

export default class CartService {
    static addItem = async (uuid: string) => {
        return await axiosInstance.post(`/cart/add`, {bookUuid: uuid}, {withCredentials: true});
    };

    static getItems = async () => {
        return await axiosInstance.get(`/cart/list`, {withCredentials: true});
    };

    static createOrder = async () => {
        return await axiosInstance.get(`/cart/order`, {withCredentials: true});
    };

    static removeItem = async (uuid: string) => {
        return await axiosInstance.post(`/cart/remove`, {bookUuid: uuid}, {withCredentials: true});
    }
}