import React from 'react';
import Dialog, {DialogProps} from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';

interface MyModalProps extends DialogProps {
    modalText: string;
    open: boolean;
    handleClose: () => void;
}

const Modal: React.FC<MyModalProps> = ({ modalText, open, handleClose }) => {
    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Error</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {modalText}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="primary">
                    Ok
                </Button>
            </DialogActions>
        </Dialog>
    )
}
export default Modal;