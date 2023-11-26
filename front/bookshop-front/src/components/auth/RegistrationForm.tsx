import React, {Dispatch, SetStateAction, useState} from 'react';
import {Button, Grid, Paper, TextField, Typography} from '@mui/material';
import AuthService from "../../API/AuthService";
import Modal from "../util/Modal";
import {useTranslation} from "react-i18next";

interface RegisterFormProps {
    setIsLoginForm: Dispatch<SetStateAction<boolean>>;
}

const RegisterForm: React.FC<RegisterFormProps> = ({setIsLoginForm}) => {
    const {t} = useTranslation();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [open, setOpen] = useState(false);
    const [modalText, setModalText] = useState('');

    const [usernameError, setUsernameError] = useState(false);
    const [passwordError, setPasswordError] = useState(false);
    const [confirmPasswordError, setConfirmPasswordError] = useState(false);
    const [emailError, setEmailError] = useState(false);

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
        const newValue = event.target.value;
        if (/^[A-Za-z\d@$!%*?&]*$/.test(newValue)) {
            setPassword(newValue);
            setPasswordError(newValue.length < 5);
        } else {
            setPasswordError(true);
        }
    };

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newValue = event.target.value;
        setConfirmPassword(newValue);
        setConfirmPasswordError(newValue !== password);
    };

    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newValue = event.target.value;
        setEmail(newValue);
        if (newValue.length > 0 && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(newValue)) {
            setEmailError(true);
        } else {
            setEmailError(false);
        }
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        if (!usernameError && !passwordError && !confirmPasswordError && !emailError) {
            try {
                let response = await AuthService.register({username, password, email});
                let data = await response.data;
                setModalText(data.message);
                setOpen(true);
            } catch (e: any) {
                setModalText(e.response.data);
                setOpen(true);
            }
        }
    };

    const handleChangeFormCLick = () => {
        setIsLoginForm(true);
    }

    return (
        <Grid container justifyContent="center">
            <Grid item xs={12} sm={8} md={6} lg={4}>
                <Paper style={{padding: 16, textAlign: "center"}}>
                    <Typography variant="h5" component="h2">
                        Register
                    </Typography>
                    <form onSubmit={handleSubmit} noValidate autoComplete="off">
                        <TextField
                            error={usernameError}
                            helperText={usernameError ? t('usernameError') : ""}
                            fullWidth
                            margin="normal"
                            label="Username"
                            value={username}
                            onChange={handleUsernameChange}
                        />
                        <TextField
                            error={passwordError}
                            helperText={passwordError ? t('passwordError') : ""}
                            fullWidth
                            margin="normal"
                            type="password"
                            label="Password"
                            value={password}
                            onChange={handlePasswordChange}
                        />
                        <TextField
                            error={confirmPasswordError}
                            helperText={confirmPasswordError ? t('confirmPassError') : ""}
                            fullWidth
                            margin="normal"
                            type="password"
                            label="Confirm Password"
                            value={confirmPassword}
                            onChange={handleConfirmPasswordChange}
                        />
                        <TextField
                            error={emailError}
                            helperText={emailError ? t('emailError') : ""}
                            fullWidth
                            margin="normal"
                            label="Email"
                            value={email}
                            onChange={handleEmailChange}
                        />
                        <Button
                            fullWidth
                            variant="contained"
                            color="primary"
                            type="submit"
                            style={{marginTop: 16}}
                            disabled={usernameError || passwordError || confirmPasswordError || emailError}
                        >
                            Register
                        </Button>
                    </form>
                    <Button
                        fullWidth
                        variant="contained"
                        color="primary"
                        type="button"
                        style={{marginTop: 16}}
                        onClick={handleChangeFormCLick}
                    >
                        LOGIN
                    </Button>
                </Paper>
            </Grid>
            <Modal open={open} modalText={modalText} handleClose={handleClose}/>
        </Grid>
    );
};

export default RegisterForm;