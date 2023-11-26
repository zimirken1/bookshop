import axiosInstance from "../interceptor/axiosInterceptor"
import {CredentialsToUpdate} from "../types/Credentials";

export default class ProfileService {
    static getProfile = async () => {
        return await axiosInstance.get("/profile/", {withCredentials: true});
    };

    static updateCredentials = async (credentials: CredentialsToUpdate) => {
        return await axiosInstance.post("/profile/update", credentials, {withCredentials: true});
    }

    static getAvatar = async () => {
        return await axiosInstance.get("/profile/avatar", {withCredentials: true, responseType: "arraybuffer"});
    };

    static uploadAvatar = async (formData: FormData) => {
        return await axiosInstance.post(`${process.env.REACT_APP_API_URL}/profile/avatar/upload`, formData, {withCredentials: true});
    }
}