import {Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField} from "@mui/material";
import * as React from 'react';
import {useRef, useState} from "react";
import BookService from "../../API/BookService";
import {useTranslation} from "react-i18next";
import Modal from "../util/Modal";

interface FileUploadDialogProps {
    open: boolean;
    handleClose: () => void;
    getBooksFromServer: () => {};
}


export const FileUploadDialog: React.FC<FileUploadDialogProps> = ({
                                                                      open,
                                                                      handleClose,
                                                                      getBooksFromServer
                                                                  }) => {
    const formRef = useRef<HTMLFormElement>(null);
    const [openModal, setOpenModal] = useState(false);
    const [modalText, setModalText] = useState('');
    const {t: i18n} = useTranslation();
    const [priceInput, setPriceInput] = useState<string>("");
    const [price, setPrice] = useState<number | null>(null);

    const validatePrice = (value: string): boolean => {
        const regex = /^\d*\.?\d{0,2}$/;
        return regex.test(value);
    };

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        if (price === null) return;
        if (formRef.current) {
            const formData = new FormData();
            const fileField = formRef.current.elements.namedItem('file') as HTMLInputElement;
            if (fileField.files) {
                formData.append('file', fileField.files[0]);
                formData.append('price', price.toFixed(2));
                BookService.uploadBook(formData).then(getBooksFromServer)
                    .catch(() => {
                        setModalText("File error");
                        setOpenModal(true);
                    });
            }
        }
        handleClose();
    };

    const handleCloseModal = () => {
        setOpenModal(false);
    }

    const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const value = e.target.value;
        if (validatePrice(value)) {
            setPriceInput(value);
            const priceValue = parseFloat(value);
            setPrice(isNaN(priceValue) ? null : priceValue);
        }
    };

    return (
        <>
            <Dialog open={open} onClose={handleClose}>
                <form onSubmit={handleSubmit} ref={formRef}>
                    <DialogTitle>{i18n("uploadDialogTitle")}</DialogTitle>
                    <DialogContent>
                        <TextField
                            variant="outlined"
                            type="file"
                            inputProps={{accept: '.pdf,.epub'}}
                            name="file"
                            style={{marginTop: 10}}
                        />
                        <TextField
                            variant="outlined"
                            label={i18n("price")}
                            value={priceInput}
                            onChange={handlePriceChange}
                            error={price === null}
                            helperText={price === null ? i18n("priceValidationError") : ""}
                            style={{marginTop: 10}}
                        />
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleClose}>{i18n('cancel')}</Button>
                        <Button type="submit">{i18n('upload')}</Button>
                    </DialogActions>
                </form>
            </Dialog>
            <Modal open={openModal} modalText={modalText} handleClose={handleCloseModal}/>
        </>
    );
};