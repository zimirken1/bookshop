export interface IComment {
    username: string,
    timestamp: string,
    text: string,
    uuid: string,
    replies: IComment[],
    removed: boolean
}