import axiosInstance from "../interceptor/axiosInterceptor";

export default class CommentService {
    static getComments = async (uuid: string) => {
        return await axiosInstance.get(`/comments/${uuid}`, {withCredentials: true});
    };

    static deleteComment = async (uuid: string) => {
        return await axiosInstance.delete(`/comments/${uuid}`, {withCredentials: true});
    };

    static updateComment = async (uuid: string, text: string) => {
        return await axiosInstance.post(`/comments/${uuid}/update`, {text: text}, {withCredentials: true});
    };

    static addComment = async (uuid: string, text: string, parentUuid: string | null) => {
        return await axiosInstance.post(`/comments/add`,
            {text: text, bookUuid: uuid, parentUuid: parentUuid},
            {withCredentials: true});
    };
}