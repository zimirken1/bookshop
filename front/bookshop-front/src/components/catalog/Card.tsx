import React, {useEffect, useState} from 'react';
import IBook from "./IBook";
import "../../styles/Catalog.css"
import SettingsIcon from '@mui/icons-material/Settings';
import {useAuth} from "../auth/context/AuthContextProvider";
import BookModal from "./EditBookModal";
import {Roles} from "../../enums/Roles";
import ImageComponent from "./Image";
import PreviewModal from "./PreviewModal";
import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from 'react-i18next';
import {Rating} from "./Rating";
import BookService from "../../API/BookService";
import OrderService from "../../API/OrderService";
import {AxiosResponse} from "axios";
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import CartService from "../../API/CartService";

interface CardBookProps extends IBook {
    getBooksFromServer: () => {};
}

const Card: React.FC<CardBookProps> = ({
                                           title,
                                           author,
                                           genre,
                                           uuid,
                                           getBooksFromServer,
                                           description,
                                           price,
                                           isPaid
                                       }) => {
    const {t: i18n} = useTranslation();
    const {roles, isAuthenticated} = useAuth();
    const [openEdit, setOpenEdit] = React.useState(false);
    const [openPreview, setOpenPreview] = React.useState(false);
    const navigate = useNavigate();
    let {bookUuid} = useParams();
    const [rating, setRating] = useState(1.00);

    useEffect(() => {
        getRating()
    }, []);

    const getRating = async () => {
        try {
            const response = await BookService.getRating(uuid);
            const data = await response.data;
            setRating(data.rating);
        } catch (e) {
            console.error(e);
        }
    }

    const updateRating = async (rating: number) => {
        BookService.setRating(uuid, rating).then(getRating);
    }

    const handleClickOpenEdit = () => {
        setOpenEdit(true);
    };

    const handleCloseEdit = () => {
        setOpenEdit(false);
    };

    const handleClickOpenPreview = () => {
        setOpenPreview(true);
    };

    const handleClosePreview = () => {
        setOpenPreview(false);
    };

    const handleAddToCart = () => {
        CartService.addItem(uuid);
    }

    function downloadFile(url: string, filename: string) {
        let link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }

    const handleDescriptionClick = () => {
        navigate(`/catalog/${uuid}`)
    }

    const handleRatingChange = (rating: number) => {
        updateRating(rating);
    }

    const handleDownloadClick = () => {
        downloadFile(`${process.env.REACT_APP_API_URL}/books/${uuid}/download`, title)
    }

    const navigateToOrderPage = (res: AxiosResponse<any>) => {
        navigate(`/order/${res.data.uuid}`)
    }

    return (
        <div className={"card"}>
            <div
                className="img-container"
                onClick={handleClickOpenPreview}
                style={{cursor: "pointer"}}
            >
                <ImageComponent
                    uuid={uuid}
                />
            </div>
            <div className="desc-container"
                 onClick={handleDescriptionClick}
            >
                <span>{i18n('title')}: {title}</span><br/>
                <span>{i18n('author')}: {author}</span><br/>
                <span>{i18n('genre')}: {genre}</span><br/>
                <span>{i18n('price')}: {price}</span><br/>
            </div>
            <Rating
                value={rating}
                onChange={handleRatingChange}
            />
            <div className="card-buttons-container">
                {(isAuthenticated && isPaid) ?
                    <div className="download-button"
                         onClick={handleDownloadClick}>
                        {i18n('download')}
                    </div>
                    :
                    <div className="card-buttons-container">
                        <div className="download-button"
                             onClick={() => {
                                 OrderService.createOrder(uuid)
                                     .then(navigateToOrderPage);
                             }}>
                            {i18n('buy')}
                        </div>
                        {!bookUuid &&
                        <AddShoppingCartIcon onClick={handleAddToCart}/>}
                    </div>
                }
                {roles.includes(Roles.Admin) && bookUuid && <div className="edit-button">
                    <SettingsIcon
                        onClick={handleClickOpenEdit}
                    />
                </div>}
            </div>
            <BookModal
                open={openEdit}
                handleClose={handleCloseEdit}
                getBooksFromServer={getBooksFromServer}
                author={author}
                genre={genre}
                title={title}
                uuid={uuid}
                price={price}
                description={description}
            />
            <PreviewModal open={openPreview} handleClose={handleClosePreview} uuid={uuid}/>
        </div>
    );
};

export default Card;