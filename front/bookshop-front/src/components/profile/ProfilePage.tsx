import React, {useEffect, useState} from 'react';
import Header from "../Header";
import ProfileService from "../../API/ProfileService";
import {UserInfo} from "./UserInfo";
import {useNavigate} from "react-router-dom";
import Loading from "../Loading";
import ProfileInfo from "./ProfileInfo";
import "../../styles/Profile.css"
import UserForm from "./UserForm";
import {useTranslation} from "react-i18next";

const ProfilePage = () => {
    const [displayInfo, setDisplayInfo] = useState<boolean>(true);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
    const navigate = useNavigate();
    const {t: i18n} = useTranslation();

    async function getProfileFromServer() {
        try {
            setIsLoading(true);
            const response = await ProfileService.getProfile();
            const data = await response.data;
            if (data) {
                setUserInfo(response.data);
            }
        } catch (error) {
            console.error(i18n('getProfileDataError'), error);
            navigate('/');
        }
    }

    useEffect(() => {
        getProfileFromServer()
    }, []);

    if (!userInfo) {
        return <Loading/>;
    }

    const handleSwitchClick = () => {
        setDisplayInfo(!displayInfo);
    }

    return (
        <div>
            <Header/>
            <div className="profile-page">
                <div className="profile-container">
                    {userInfo && (displayInfo ? <ProfileInfo profileInfo={userInfo}/> :
                        <UserForm initialEmail={userInfo.email}/>)}
                    <div
                        className="profile-switch-button"
                        onClick={handleSwitchClick}>
                        {displayInfo ? i18n('changeCreds') : i18n('viewProfile')}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;