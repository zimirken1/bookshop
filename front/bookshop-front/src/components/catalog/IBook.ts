export default interface IBook {
    title: string,
    genre: string,
    author: string,
    uuid: string,
    description: string,
    price: number,
    isPaid?: boolean
}