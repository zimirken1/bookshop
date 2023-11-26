import React from 'react';
import Dialog, {DialogProps} from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import PdfPreview from "./PdfView";

interface MyModalProps extends DialogProps {
    open: boolean;
    handleClose: () => void;
    uuid: string
}

const Modal: React.FC<MyModalProps> = ({open, handleClose, uuid}) => {
    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Book Preview</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    <PdfPreview uuid={uuid}/>
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