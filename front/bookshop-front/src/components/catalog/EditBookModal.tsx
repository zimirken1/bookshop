import React, {useState, useEffect} from 'react';
import {Dialog, DialogTitle, DialogContent, TextField, DialogActions, Button} from '@mui/material';
import BookService from "../../API/BookService";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";

interface BookModalProps {
    open: boolean;
    handleClose: () => void;
    title: string;
    genre: string;
    author: string;
    uuid: string;
    price?: number;
    description: string;
    getBooksFromServer: () => {};
}

const BookModal: React.FC<BookModalProps> = ({
                                                 open,
                                                 handleClose,
                                                 title,
                                                 genre,
                                                 author,
                                                 uuid,
                                                 price,
                                                 getBooksFromServer,
                                                 description
                                             }) => {
    const {t: i18n} = useTranslation();
    const [bookTitle, setBookTitle] = useState(title);
    const [bookGenre, setBookGenre] = useState(genre);
    const [bookAuthor, setBookAuthor] = useState(author);
    const [bookPrice, setBookPrice] = useState<number | undefined>(price);
    const [priceInput, setPriceInput] = useState<string>(price !== undefined ? price.toString() : ""); // Для ввода цены пользователем
    const [value, setValue] = useState(description);
    const nav = useNavigate();

    useEffect(() => {
        setBookTitle(title);
        setBookGenre(genre);
        setBookAuthor(author);
        setBookPrice(price);
        setPriceInput(price !== undefined ? price.toString() : "");
    }, [title, genre, author, price]);

    const validateInput = (input: string) => {
        return /^[A-Za-zА-Яа-я\s]+$/.test(input);
    };

    const handleSaveClick = () => {
        if (validateInput(bookTitle) &&
            validateInput(bookGenre) &&
            validateInput(bookAuthor)) {
            BookService.updateBook({
                title: bookTitle,
                genre: bookGenre,
                author: bookAuthor,
                price: bookPrice,
                description: value
            }, uuid)
                .then(getBooksFromServer);
            handleClose();
        } else {
            alert(i18n('editBookError'));
        }
    };

    function handleDelete() {
        BookService.deleteBook(uuid).then(() => {
            nav("/catalog")
        });
    }

    const handleDescChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.value.toString().length < 512) {
            setValue(e.target.value);
        }
    }

    const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const inputValue = e.target.value;
        const isValidPriceInput = /^(\+)?([0-9]*\.?[0-9]*)$/.test(inputValue);

        if (isValidPriceInput &&
            inputValue.toString().substring(inputValue.toString().indexOf('.')).length < 4 &&
            inputValue.toString().length < 7) {
            setPriceInput(inputValue);
            const priceValue = parseFloat(inputValue);
            setBookPrice(!isNaN(priceValue) ? priceValue : undefined);
        }
    }

    const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setBookTitle(e.target.value);
    }

    const handleGenreChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setBookGenre(e.target.value);
    }

    const handleAuthorChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setBookAuthor(e.target.value);
    }

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>{i18n("editBook")}</DialogTitle>
            <DialogContent>
                <TextField
                    value={bookTitle}
                    onChange={handleTitleChange}
                    label={i18n("title")}
                    fullWidth
                    style={{marginBottom: 10, marginTop: 10}}
                />
                <TextField
                    value={bookGenre}
                    onChange={handleGenreChange}
                    label={i18n("genre")}
                    fullWidth
                    style={{marginBottom: 10}}
                />
                <TextField
                    value={bookAuthor}
                    onChange={handleAuthorChange}
                    label={i18n("author")}
                    fullWidth
                    style={{marginBottom: 10}}
                />
                <TextField
                    value={priceInput}
                    onChange={handlePriceChange}
                    label={i18n("price")}
                    fullWidth
                    style={{marginBottom: 10}}
                />
                <TextField
                    autoFocus
                    margin="dense"
                    id="description"
                    label={i18n("description")}
                    type="text"
                    fullWidth
                    multiline
                    value={value}
                    onChange={handleDescChange}
                />
            </DialogContent>
            <DialogActions>
                <Button color="primary" onClick={handleSaveClick}>{i18n("save")}</Button>
                <Button color="secondary" onClick={handleDelete}>{i18n("delete")}</Button>
            </DialogActions>
        </Dialog>
    );
};

export default BookModal;