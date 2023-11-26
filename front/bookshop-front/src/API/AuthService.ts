import axiosInstance from "../interceptor/axiosInterceptor"
import {UserLoginCredentials, UserRegistrationCredentials} from "../types/Credentials";

export default class AuthService {
    static login = async (credentials: UserLoginCredentials) => {
        return await axiosInstance.post("/auth/signin", credentials,{withCredentials: true});
    };

    static register = async (credentials: UserRegistrationCredentials) => {
        return await axiosInstance.post("/auth/register", credentials,{withCredentials: true});
    };
}