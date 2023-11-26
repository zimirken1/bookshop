import React, {useEffect, useState} from 'react';
import IBook from "../catalog/IBook";
import BookService from "../../API/BookService";
import "../../styles/Order.css"
import ImageComponent from "../catalog/Image";
import {IOrderDetail} from "../catalog/IOrderDetail";


const OrderDetail: React.FC<IOrderDetail> = ({uuid, unitPrice, updateTotalPrice}) => {
    const [bookInfo, setBookInfo] = useState<IBook>();
    const quantity = 1;

    const getBookInfo = async () => {
        const response = await BookService.getBook(uuid);
        const data = await response.data;

        setBookInfo(data);
        if (data?.price) {
            updateTotalPrice(data.price);
        }
    }

    useEffect(() => {
        getBookInfo();
    }, []);
    return (
        <div className={"detail-container"}>
            <div className="book-name-order">
                <div className="order-detail-image-container">
                    {bookInfo?.uuid &&
                    <ImageComponent uuid={bookInfo.uuid}/>}
                </div>
                {bookInfo?.title} <br/>
                {bookInfo?.author}
            </div>
            <div>{quantity}</div>
            <div>{unitPrice ? unitPrice : bookInfo?.price}</div>
        </div>
    );
};

export default OrderDetail;