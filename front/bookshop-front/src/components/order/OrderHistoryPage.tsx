import React from 'react';
import Header from "../Header";
import OrderHistory from "./OrderHistory";

const OrderHistoryPage = () => {
    return (
        <div className={"order-history-page"}>
            <Header/>
            <OrderHistory/>
        </div>
    );
};

export default OrderHistoryPage;