import React, {useEffect, useState} from 'react';
import BookService from "../../API/BookService";
import Loading from "../Loading";
import "../../styles/Catalog.css"
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import {useTranslation} from "react-i18next";

interface PdfPreviewProps {
    uuid: string;
}

const PdfPreview: React.FC<PdfPreviewProps> = ({uuid}) => {
    const [previewImages, setPreviewImages] = useState([]);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const {t: i18n} = useTranslation();

    useEffect(() => {
        const loadPreviewImages = async () => {
            try {
                const response = await BookService.getPreview(uuid);
                const data = await response.data;
                setPreviewImages(data);
                setIsLoading(false);
            } catch (error) {
                console.error(i18n('getPreviewError'), error);
            }
        };

        loadPreviewImages();
    }, []);

    const handleNextImage = () => {
        setCurrentImageIndex((prevIndex) => (prevIndex + 1) % previewImages.length);
    };

    const handlePrevImage = () => {
        setCurrentImageIndex((prevIndex) => (prevIndex - 1 + previewImages.length) % previewImages.length);
    };

    return (
        <div className={"preview-container"}>
            <div className="img-container-preview">
                {isLoading ?
                    <Loading/>
                    :
                    <img
                        src={`data:image/png;base64,${previewImages[currentImageIndex]}`}
                        alt={`Preview Image ${currentImageIndex + 1}`}
                        style={{width: '100%', height: '500px'}}
                    />}
            </div>
            <div className="preview-controls-container">
                <ArrowBackIosNewIcon
                    onClick={handlePrevImage}
                    style={{cursor: "pointer", fontSize: 50}}
                />
                <ArrowForwardIosIcon
                    onClick={handleNextImage}
                    style={{cursor: "pointer", fontSize: 50}}
                />
            </div>
        </div>
    );
};

export default PdfPreview;