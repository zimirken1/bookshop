import axiosInstance from "../interceptor/axiosInterceptor"

export default class AdminService {
    static getUsers = async (pageNum: number, searchTerm?: string) => {
        return await axiosInstance.get("/admin/list", {
            params: {
                page: pageNum,
                size: 10,
                searchTerm: searchTerm
            }, withCredentials: true
        });
    };

    static ban = async (uuid: String) => {
        return await axiosInstance.post("/admin/ban", {uuid: uuid}, {withCredentials: true});
    }
}