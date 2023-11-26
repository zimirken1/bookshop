import React from 'react';
import {UserInfo} from "./UserInfo";
import "../../styles/Profile.css"
import ProfileAvatar from "./ProfileAvatar";
import {useTranslation} from "react-i18next";

interface ProfileInfoProps {
    profileInfo: UserInfo;
}

const ProfileInfo: React.FC<ProfileInfoProps> = ({profileInfo}) => {
    const {t: i18n} =useTranslation();
    return (
        <div className={"info-container"}>
            <div className="avatar-container">
                <ProfileAvatar/>
            </div>
            <div className="credentials">
                <div className={"credentials-info"}>
                    <b>{i18n('username')}:</b> <br/>
                    {profileInfo.username}<br/>
                </div>
                <div className={"credentials-info"}>
                    <b> {i18n('email')}:</b> <br/>
                    {profileInfo.email}<br/>
                </div>
                <div className={"credentials-info"}>
                    <b>{i18n('regDate')}: </b> <br/>
                    {profileInfo.regDate}<br/>
                </div>
            </div>
        </div>
    );
};

export default ProfileInfo;