import React from 'react';
import {
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Button
} from '@mui/material';
import {User} from "./IUser";
import {Roles} from "../../enums/Roles";

interface UserTableProps {
    users: User[] | undefined;
    onBan: (userId: string) => void;
}

const UserTable: React.FC<UserTableProps> = ({users, onBan}) => {
    return (

        <TableContainer component={Paper}>
            <Table aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Username</TableCell>
                        <TableCell>Email</TableCell>
                        <TableCell>Date of Registration</TableCell>
                        <TableCell>Roles</TableCell>
                        <TableCell>Action</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {users?.map((user) => (
                        <TableRow key={user.uuid}>
                            <TableCell>{user.userName}</TableCell>
                            <TableCell>{user.email}</TableCell>
                            <TableCell>{user.regDate}</TableCell>
                            <TableCell>{user.role}</TableCell>
                            <TableCell>
                                {user.role !== Roles.Banned ?
                                    <Button variant="outlined" color="secondary"
                                            onClick={() => onBan(user.uuid)}>
                                        Ban
                                    </Button>
                                    :
                                    <Button variant="outlined" color="primary"
                                            onClick={() => onBan(user.uuid)}>
                                        Unban
                                    </Button>
                                }
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default UserTable;