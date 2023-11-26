import React from 'react';
import "../styles/Header.css"
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';
import PersonIcon from '@mui/icons-material/Person';
import AutoStoriesIcon from '@mui/icons-material/AutoStories';
import BookOnlineIcon from '@mui/icons-material/BookOnline';
import {useAuth} from './auth/context/AuthContextProvider';
import {Link} from "react-router-dom";
import LanguageSwitcher from "./LanguageSwitcher";
import {useTranslation} from "react-i18next";
import HistoryIcon from '@mui/icons-material/History';
import CartDialog from "./catalog/CartDialog";
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import {Roles} from "../enums/Roles";

const Header = () => {
    const {isAuthenticated, logout, roles} = useAuth();
    const {t: i18n} = useTranslation();

    return (
        <div className={"header-container"}>
            <div className="menu-container">
                <div className="logo-container">
                    <LanguageSwitcher/>
                    <Link to={"/"}>
                        <BookOnlineIcon className={"logo-icon"}/>
                        <span className={"logo-name"}>Books online</span>
                    </Link>
                </div>
                <div className="menu">
                    {isAuthenticated &&
                    <>
                        {roles.includes(Roles.Admin) &&
                        <div className="menu-item-container">
                            <Link to={"/admin"}>
                                <AdminPanelSettingsIcon fontSize={"large"}/>
                                <span className={"menu-item-name"}>{i18n('adminPanel')}</span>
                            </Link>
                        </div>}
                        <CartDialog/>
                        <div className="menu-item-container">
                            <Link to={"/orders"}>
                                <HistoryIcon fontSize={"large"}/>
                                <span className={"menu-item-name"}>{i18n('orders')}</span>
                            </Link>
                        </div>
                        <div className="menu-item-container">
                            <Link to={"/profile"}>
                                <PersonIcon fontSize={"large"}/>
                                <span className={"menu-item-name"}>{i18n("profile")}</span>
                            </Link>
                        </div>
                    </>
                    }
                    <div className="menu-item-container">
                        <Link to={"/catalog"}>
                            <AutoStoriesIcon fontSize={"large"}/>
                            <span className={"menu-item-name"}>{i18n("catalog")}</span>
                        </Link>
                    </div>
                    {!isAuthenticated ?
                        <div className="menu-item-container">
                            <Link to={"/login"}>
                                <LoginIcon fontSize={"large"}/>
                                <span className={"menu-item-name"}>{i18n("login")}</span>
                            </Link>
                        </div>
                        :
                        <div className="menu-item-container"
                             onClick={logout}
                        >
                            <Link to={"/"}>
                                <LogoutIcon fontSize={"large"}/>
                                <span className={"menu-item-name"}>{i18n("logout")}</span>
                            </Link>
                        </div>
                    }
                </div>
            </div>
        </div>
    );
};

export default Header;