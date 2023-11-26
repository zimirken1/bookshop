import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from '@mui/material';
import React, {useState} from 'react';
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import {useTranslation} from "react-i18next";
import "../../styles/Header.css"
import CartService from "../../API/CartService";
import Loading from "../Loading";
import "../../styles/Catalog.css"
import {useNavigate} from "react-router-dom";
import {AxiosResponse} from "axios";
import CartItem from "./CartItem";

interface BookInfo {
    uuid: string;
    price: number;
}

function CartDialog() {
    const [open, setOpen] = useState(false);
    const {t: i18n} = useTranslation();
    const [cartItemsInfo, setCartItemsInfo] = useState<BookInfo[] | null>(null);
    const [price, setPrice] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const nav = useNavigate();

    const updateTotalPrice = (bookPrice: number) => {
        setPrice(prevPrice => prevPrice + bookPrice);
    };

    const decrementTotalPrice = (bookPrice: number) => {
        setPrice(prevPrice => prevPrice - bookPrice);
    };

    const getItemsInfo = async () => {
        setIsLoading(true);
        try {
            const response = await CartService.getItems();
            const data = await response.data;
            setCartItemsInfo(data);
            setPrice(0);
            setIsLoading(false);
        } catch (e) {
            console.error(i18n('dataLoadingError'));
        }

    }


    const handleOpen = () => {
        getItemsInfo().then(() => {
            setOpen(true)
        })
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleCreate = () => {
        CartService.createOrder().then((res: AxiosResponse<any>) => {
            nav(`/order/${res.data.uuid}`)
        })
    }

    return (
        <div style={{display: "flex", alignItems: "center", justifyContent: "center"}}>
            <div className={"menu-item-container"} onClick={handleOpen}>
                <ShoppingCartIcon fontSize={"large"}/>
                <span className={"menu-item-name"}>{i18n('cart')}</span>
            </div>

            <Dialog open={open} onClose={handleClose} maxWidth="lg" fullWidth={true}>
                <DialogTitle>{i18n('yourCart')}</DialogTitle>
                <DialogContent>
                    <div className="cart-container">
                        {isLoading ? (
                            <Loading/>
                        ) : (
                            cartItemsInfo &&
                            <div>
                                {cartItemsInfo.map((item) => {
                                        return <CartItem
                                            key={item.uuid}
                                            uuid={item.uuid}
                                            unitPrice={item.price}
                                            updateTotalPrice={updateTotalPrice}
                                            getItemsInfo={getItemsInfo}
                                            decrementTotalPrice={decrementTotalPrice}
                                        />
                                    }
                                )}
                                <div
                                    className={"total-price-container-cart"}>{i18n('total')}: {price}</div>
                            </div>
                        )}
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        {i18n('close')}
                    </Button>
                    <Button onClick={handleCreate} color="primary" autoFocus>
                        {i18n('createOrder')}
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default CartDialog;