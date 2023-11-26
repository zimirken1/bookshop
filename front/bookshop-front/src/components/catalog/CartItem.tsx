import React from 'react';
import {IOrderDetail} from "./IOrderDetail";
import OrderDetail from "../order/OrderDetail";
import "../../styles/Catalog.css"
import DeleteIcon from '@mui/icons-material/Delete';
import CartService from "../../API/CartService";

interface CartItemProps extends IOrderDetail {
    getItemsInfo: () => {};
    decrementTotalPrice: (bookPrice: number) => void;
}

const CartItem: React.FC<CartItemProps> = ({
                                               uuid,
                                               unitPrice,
                                               updateTotalPrice,
                                               getItemsInfo,
                                               decrementTotalPrice
                                           }) => {
    const handleDeleteClick = () => {
        CartService.removeItem(uuid).then(() => {
            decrementTotalPrice(unitPrice);
            getItemsInfo();
        });
    }
    return (
        <div className={"cart-order-detail-wrapper"}>
            <OrderDetail
                uuid={uuid}
                unitPrice={unitPrice}
                updateTotalPrice={updateTotalPrice}
            />
            <DeleteIcon
                onClick={handleDeleteClick}
            />
        </div>
    );
};

export default CartItem;