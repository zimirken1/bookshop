import React, {useState} from 'react';
import LoginForm from "./LoginForm";
import "../../styles/Auth.css"
import RegistrationForm from "./RegistrationForm";

const AuthPage = () => {
    const [isLoginForm, setIsLoginForm] = useState<boolean>(true);

    return (
        <div className={"login-form-container"}>
            <div className="login-form-button-container">
                {isLoginForm ?
                    <LoginForm setIsLoginForm={setIsLoginForm}/> :
                    <RegistrationForm setIsLoginForm={setIsLoginForm}/>}
            </div>
        </div>
    );
};

export default AuthPage;