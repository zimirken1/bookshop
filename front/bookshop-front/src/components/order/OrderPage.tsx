import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import OrderService from "../../API/OrderService";
import Order from "./Order";
import IOrder from "./IOrder";
import Loading from "../Loading";
import "../../styles/Order.css"

const OrderPage = () => {
    let {orderId} = useParams();
    const [order, setOrder] = useState<IOrder>();
    const getOrder = async () => {
        const response = await OrderService.getOrder(orderId!);
        const data = await response.data;
        setOrder(data);
    }

    useEffect(() => {
        getOrder();
    }, [])

    if (!order) {
        return <Loading/>;
    }

    return (
        <div className={"order-page"}>
            <div className="order-container">
                <Order
                    uuid={order.uuid}
                    username={order.username}
                    orderDate={order.orderDate}
                    orderDetails={order.orderDetails}
                    totalPrice={order.totalPrice}
                    status={order.status}
                />
            </div>
        </div>
    );
};

export default OrderPage;