import React, {createContext, useContext, useState} from 'react';

interface AuthContextInterface {
    isAuthenticated: boolean;
    login: (roles: string[], login: string) => void;
    logout: () => void;
    roles: string[];
    username: string;
}

const AuthContext = createContext<AuthContextInterface>({
    isAuthenticated: false,
    login: (roles: string[], login: string) => {
    },
    logout: () => {
    },
    roles: [],
    username: ""
});

export const useAuth = () => {
    return useContext(AuthContext);
};

interface AuthProviderProps extends React.PropsWithChildren<{}> {
}

const TOKEN_KEY = "token";
const ROLES_KEY = "roles";
const USERNAME_KEY = "username";

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(
        () => !!localStorage.getItem(TOKEN_KEY)
    );
    const rolesString = localStorage.getItem(ROLES_KEY);
    const [roles, setRoles] = useState<string[]>(rolesString ? rolesString.split(",") : []);
    const usernameString = localStorage.getItem(USERNAME_KEY);
    const [username, setUsername] = useState<string>(usernameString ? usernameString : "")

    const login = (roles: string[], login: string) => {
        setIsAuthenticated(true);
        setUsername(login);
        setRoles(roles);
    };

    const logout = () => {
        setIsAuthenticated(false);
        setRoles([]);
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(ROLES_KEY);
    };

    return (
        <AuthContext.Provider
            value={{isAuthenticated, login, logout, roles, username}}>
            {children}
        </AuthContext.Provider>
    );
};