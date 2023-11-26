import React, {useEffect, useState} from 'react';
import IOrder from "./IOrder";
import OrderDetail from "./OrderDetail";
import OrderService from "../../API/OrderService";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {Status} from "../../enums/Status";

const Order: React.FC<IOrder> = ({
                                     uuid,
                                     username,
                                     totalPrice,
                                     status,
                                     orderDate,
                                     orderDetails
                                 }) => {
    const navigate = useNavigate();
    const [price, setPrice] = useState(0);
    const {t: i18n} = useTranslation();

    const updateTotalPrice = (bookPrice: number) => {
        setPrice(prevPrice => prevPrice + bookPrice);
    };

    const handleCancelClick = () => {
        OrderService.cancelOrder(uuid).then(() => {
            navigate("/catalog")
        })
    }

    const handlePayClick = () => {
        OrderService.payOrder(uuid).then(() => {
            navigate("/catalog")
        })
    }

    return (
        <div className={"order-info-container"}>
            {i18n('orderId')}: {uuid} <br/>
            {i18n('username')}: {username} <br/>
            {i18n('createdAt')}: {orderDate} <br/>
            {i18n('status')}: <span className={status.toLowerCase()}>{status}</span> <br/>
            {orderDetails.map((detail) => {
                return <OrderDetail
                    key={detail.uuid}
                    uuid={detail.uuid}
                    unitPrice={detail.unitPrice}
                    updateTotalPrice={updateTotalPrice}/>
            })}
            <div className="total">Total: {totalPrice ? totalPrice : price}</div>
            {status === Status.Pending &&
            <div className="order-buttons-container">
                <div
                    className="cancel-button"
                    onClick={handleCancelClick}>{i18n("cancel")}
                </div>
                <div
                    className="pay-button"
                    onClick={handlePayClick}
                >{i18n('pay')}
                </div>
            </div>
            }
        </div>
    );
};

export default Order;