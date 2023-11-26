export default interface IOrder {
    uuid: string,
    username: string,
    totalPrice: number,
    status: string,
    orderDate: string,
    orderDetails: { uuid: string, unitPrice: number }[],
}