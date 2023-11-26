import * as React from 'react';
import {Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle} from '@mui/material';
import {useTranslation} from "react-i18next";

interface MyDialogProps {
    onSave: (comment: string) => void;
    defaultComment: string;
    open: boolean;
    handleClose: () => void;
}

const CommentModal: React.FC<MyDialogProps> = ({onSave, defaultComment, open, handleClose}) => {
    const [comment, setComment] = React.useState<string>(defaultComment);
    const {t: i18n} = useTranslation();

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setComment(event.target.value);
    };

    const handleSave = () => {
        onSave(comment);
        handleClose();
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>{i18n('commentDialogTitle')}</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    label={i18n('commentFormLabel')}
                    type="text"
                    fullWidth
                    value={comment}
                    onChange={handleChange}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>{i18n('cancel')}</Button>
                <Button onClick={handleSave}>{i18n('save')}</Button>
            </DialogActions>
        </Dialog>
    );
};

export default CommentModal;