export type UserLoginCredentials = {
    username: string;
    password: string;
};

export type UserRegistrationCredentials = {
    username: string;
    password: string;
    email: string;
};

export type CredentialsToUpdate = {
    email: string,
    password: string
}