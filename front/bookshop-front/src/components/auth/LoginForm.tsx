import React, {Dispatch, SetStateAction, useContext, useEffect, useState} from 'react';
import {useAuth} from './context/AuthContextProvider';
import {Button, Grid, Paper, TextField, Typography} from '@mui/material';
import AuthService from "../../API/AuthService";
import Modal from "../util/Modal"
import {useNavigate} from "react-router-dom";

interface LoginFormProps {
    setIsLoginForm: Dispatch<SetStateAction<boolean>>;
}

const LoginForm: React.FC<LoginFormProps> = ({setIsLoginForm}) => {
    const {login, isAuthenticated} = useAuth();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [usernameError, setUsernameError] = useState(false);
    const [open, setOpen] = useState(false);
    const [modalText, setModalText] = useState('');
    const navigate = useNavigate();
    const handleClose = () => {
        setOpen(false);
    };

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newValue = event.target.value;
        if (/^[a-zA-Z0-9]*$/.test(newValue)) {
            setUsername(newValue);
            setUsernameError(false);
        } else {
            setUsernameError(true);
        }
    };

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    }

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        try {
            let response = await AuthService.login({username, password});
            let data = await response.data;
            localStorage.setItem('token', data.accessToken);
            localStorage.setItem('roles', data.roles);
            localStorage.setItem('username', data.username);
            localStorage.setItem('refreshToken', data.refreshToken);
            login(data.roles, data.username);
            setTimeout(() => {
                navigate('/catalog');
            }, 3000);
        } catch (e: any) {
            setModalText(e.response.data);
            setOpen(true);
        }
    }

    const handleChangeFormClick = () => {
        setIsLoginForm(false);
    }

    return (
        <Grid container justifyContent="center">
            <Grid item xs={12} sm={8} md={6} lg={4}>
                <Paper style={{padding: 16, textAlign: "center"}}>
                    <Typography variant="h5" component="h2">
                        Login
                    </Typography>
                    <form onSubmit={handleSubmit} noValidate autoComplete="off">
                        <TextField
                            error={usernameError}
                            helperText={usernameError ? "Please enter only English alphabets" : ""}
                            fullWidth
                            margin="normal"
                            label="Username"
                            value={username}
                            onChange={handleUsernameChange}
                        />
                        <TextField
                            fullWidth
                            margin="normal"
                            type="password"
                            label="Password"
                            value={password}
                            onChange={handlePasswordChange}
                        />
                        <Button
                            fullWidth
                            variant="contained"
                            color="primary"
                            type="submit"
                            style={{marginTop: 16}}
                            disabled={usernameError}
                        >
                            Login
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            color="primary"
                            type="button"
                            onClick={handleChangeFormClick}
                            style={{marginTop: 16}}
                        >
                            Register
                        </Button>
                    </form>
                </Paper>
            </Grid>
            <Modal open={open} modalText={modalText} handleClose={handleClose}/>
        </Grid>
    );
};

export default LoginForm;