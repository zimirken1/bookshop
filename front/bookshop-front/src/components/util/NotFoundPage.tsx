import React from 'react';
import {Button} from "@mui/material";
import "../../styles/Error.css"
import {Link} from "react-router-dom";

const NotFoundPage: React.FC = () => {
    return (
        <div className={"error-page-container"}>
            <div className={"error-text-container"}>
                <h1>404: Страница не найдена</h1>
                <p>Извините, но страница, которую вы ищете, не существует.</p>
                <Button variant="outlined">
                    <Link to={"/"}>
                        На главную
                    </Link>
                </Button>
            </div>
        </div>
    );
}

export default NotFoundPage;