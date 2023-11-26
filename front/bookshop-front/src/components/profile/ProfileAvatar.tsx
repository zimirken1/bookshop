import React, {useEffect, useRef, useState} from 'react';
import {Avatar, Button} from "@mui/material";
import ProfileService from "../../API/ProfileService";
import Loading from "../Loading";
import avatarDefault from "../../assets/img/avatarDefault.png"
import {useTranslation} from "react-i18next";


const ProfileAvatar = () => {
    const [avatarUrl, setAvatarUrl] = useState<string | null>(null);
    const [isHovered, setIsHovered] = useState(false);
    const handleMouseOver = () => setIsHovered(true);
    const handleMouseOut = () => setIsHovered(false);
    const inputFileRef = useRef<HTMLInputElement>(null);
    const {t: i18n} = useTranslation();

    useEffect(() => {
        loadAvatarFromServer();
    }, []);

    const loadAvatarFromServer = async () => {
        try {
        const response = await ProfileService.getAvatar();
        const blob = new Blob([response.data], {type: response.headers['content-type']});
        const imageSrc = URL.createObjectURL(blob);
        setAvatarUrl(imageSrc);
        } catch (e) {
            setAvatarUrl(avatarDefault);
        }
    }

    const handleClick = () => {
        if (inputFileRef.current) {
            inputFileRef.current.click();
        }
    };

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files.length > 0) {
            const file = event.target.files[0];
            const formData = new FormData();
            formData.append('avatar', file);
            ProfileService.uploadAvatar(formData).then(() => {
                loadAvatarFromServer();
            }).catch((error) => {
                console.log(i18n('uploadError'), error);
            });
        }
    };

    if (!avatarUrl) {
        return <Loading/>;
    }

    return (
        <div
            onMouseOver={handleMouseOver}
            onMouseOut={handleMouseOut}
            style={{position: 'relative'}}
        >
            <Avatar
                src={avatarUrl}
                alt="ProfileAvatar"
                sx={{width: 130, height: 130}}
                style={{
                    opacity: isHovered ? 0.5 : 1
                }}
            />

            {isHovered && (
                <>
                    <Button
                        variant="contained"
                        onClick={handleClick}
                        style={{
                            position: 'absolute',
                            top: '50%',
                            left: '50%',
                            transform: 'translate(-50%, -50%)'
                        }}
                    >
                        Изменить
                    </Button>
                    <input
                        type="file"
                        ref={inputFileRef}
                        style={{display: 'none'}}
                        onChange={handleFileChange}
                    />
                </>
            )}
        </div>
    );
}

export default ProfileAvatar;