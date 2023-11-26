export interface IOrderDetail {
    uuid: string,
    unitPrice: number,
    updateTotalPrice: (bookPrice: number) => void
}