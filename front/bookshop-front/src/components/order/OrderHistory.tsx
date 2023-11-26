import React, {useEffect, useState} from 'react';
import IOrder from "./IOrder";
import Loading from "../Loading";
import OrderService from "../../API/OrderService";
import Order from "./Order";
import "../../styles/Order.css"
import Pagination from "../util/Pagination";
import {useTranslation} from "react-i18next";

const OrderHistory = () => {
    const [orders, setOrders] = useState<IOrder[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(0);
    const {t: i18n} = useTranslation();

    const getOrders = async () => {
        try {
            const response = await OrderService.getOrderPage(currentPage - 1);
            const data = await response.data.orders;
            setTotalPages(response.data.totalPages);
            setOrders(data);
        } catch (e) {
            console.error(i18n('dataLoadingError'));
        }
    }

    const handlePrevPage = () => {
        setCurrentPage(prev => Math.max(prev - 1, 1));
    }

    const handleNextPage = () => {
        setCurrentPage(prev => prev + 1);
    }

    useEffect(() => {
        getOrders()
    }, [currentPage])

    useEffect(() => {
        getOrders()
    }, [])

    if (!orders) {
        return <Loading/>;
    }
    return (
        <div className={"orders-history-container-wrapper"}>
            <div className={"orders-history-container"}>
                {orders.map(order => (
                    <Order
                        key={order.uuid}
                        uuid={order.uuid}
                        orderDate={order.orderDate}
                        orderDetails={order.orderDetails}
                        status={order.status}
                        totalPrice={order.totalPrice}
                        username={order.username}
                    />
                ))}
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPrevPage={handlePrevPage}
                    onNextPage={handleNextPage}
                />
            </div>
        </div>
    );
};

export default OrderHistory;