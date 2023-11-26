import React from 'react';
import { useTranslation } from 'react-i18next';
import ruFlag from "../assets/img/ru.png"
import enFlag from "../assets/img/en.png"
import {Avatar} from "@mui/material";
import "../styles/Header.css"

const LanguageSwitcher: React.FC = () => {
    const { i18n } = useTranslation();

    return (
        <div className={"flag-container"}>
            <Avatar
                src={enFlag}
                onClick={() => i18n.changeLanguage('english')}
                sx={{ width: 28, height: 28 }}
                style={{cursor: "pointer"}}
            />
            <Avatar
                src={ruFlag}
                onClick={() => i18n.changeLanguage('russian')}
                sx={{ width: 28, height: 28 }}
                style={{cursor: "pointer"}}
            />
        </div>
    );
};

export default LanguageSwitcher;