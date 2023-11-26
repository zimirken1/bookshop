import React, {useEffect, useState} from 'react';
import Header from "../Header";
import UserTable from "./UserTable";
import AdminService from "../../API/AdminService";
import Pagination from "../util/Pagination";
import SearchBar from "../catalog/SearchBar";
import "../../styles/Admin.css"
import {useTranslation} from "react-i18next";

const AdminPage = () => {
    const [users, setUsers] = useState(null);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(0);
    const {t: i18n} = useTranslation();

    const handlePrevPage = () => {
        setCurrentPage(prev => Math.max(prev - 1, 1));
    }

    const handleNextPage = () => {
        setCurrentPage(prev => prev + 1);
    }

    const fetchUsers = async (searchTerm?: string) => {
        try {
            const response = await AdminService.getUsers(currentPage - 1, searchTerm);
            const data = await response.data;
            setUsers(data.users);
            setTotalPages(response.data.totalPages);
        } catch (e) {
            console.error(i18n('dataLoadingError'));
        }
    }

    useEffect(() => {
        fetchUsers();
    }, []);

    useEffect(() => {
        fetchUsers();
    }, [currentPage]);

    const onBan = (uuid: string) => {
        AdminService.ban(uuid).then(() => {
            fetchUsers()
        });
    }

    const handleSearch = (searchTerm: string) => {
        setCurrentPage(1);
        fetchUsers(searchTerm);
    }

    return (
        <div>
            <Header/>
            <div className="user-table-container-wrapper">
                <div className="search-bar-admin-container">
                    <SearchBar onSearch={handleSearch}/>
                </div>
                <div className="user-table-container">
                    {users && <UserTable users={users} onBan={onBan}/>}
                    <div className="pagination-admin-container">
                        <Pagination
                            currentPage={currentPage}
                            totalPages={totalPages}
                            onPrevPage={handlePrevPage}
                            onNextPage={handleNextPage}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminPage;