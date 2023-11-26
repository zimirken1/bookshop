import React from 'react';
import AddIcon from '@mui/icons-material/Add';
import "../../styles/Catalog.css"
import {FileUploadDialog} from "./FileUploadDialog";

interface AddCardProps {
    getBooksFromServer: () => {};
}

const AddCard:React.FC<AddCardProps> = ({getBooksFromServer}) => {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <div className={"card"}>
            <div className="add-card-icon-container">
                <AddIcon
                    className={"add-card-icon"}
                    onClick={handleClickOpen}
                />
            </div>
            <FileUploadDialog open={open} handleClose={handleClose} getBooksFromServer={getBooksFromServer} />
        </div>
    );
};

export default AddCard;