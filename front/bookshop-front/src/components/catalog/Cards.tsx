import React, {useEffect, useState} from 'react';
import BookService from "../../API/BookService";
import {useNavigate} from "react-router-dom";
import Loading from "../Loading";
import IBook from "./IBook";
import Card from "./Card";
import "../../styles/Catalog.css"
import AddCard from "./AddCard";
import {useAuth} from "../auth/context/AuthContextProvider";
import {Roles} from "../../enums/Roles";
import SortMenu from "./SortMenu";
import SearchBar from "./SearchBar";
import {useTranslation} from "react-i18next";
import Pagination from "../util/Pagination";


const Cards = () => {
    const {roles} = useAuth();
    const {t: i18n} = useTranslation();
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [books, setBooks] = useState<IBook[]>([]);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [pageSize, setPageSize] = useState<number>(10);
    const [totalPages, setTotalPages] = useState<number>(1);
    const navigate = useNavigate();
    const [sortField, setSortField] = useState<string>("id");
    const [sortDirection, setSortDirection] = useState<string>("ASC");
    const [searchTerm, setSearchTerm] = useState<string | null>(null);

    const handleSearch = (searchWord: string) => {
        setSearchTerm(searchWord);
        getBooksFromServer(currentPage, pageSize, sortField, sortDirection);
    };

    const handleSortChange = (value: string) => {
        const [field, direction] = value.split(':');
        setSortField(field);
        setSortDirection(direction);
        getBooksFromServer(currentPage, pageSize, field, direction);
    };


    async function getBooksFromServer(page: number, size: number, field: string = "id", direction: string = "ASC") {
        try {
            setIsLoading(true);
            const response = await BookService.getBooks(page - 1, size, field, direction, searchTerm);
            const data = await response.data;
            if (data) {
                setBooks(response.data.books);
                if (response.data.totalPages > 0)
                    setTotalPages(response.data.totalPages);
                setIsLoading(false);
            }
        } catch (error) {
            console.error(i18n('getBooksError'), error);
            navigate('/');
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        getBooksFromServer(currentPage, pageSize, sortField, sortDirection);
    }, [searchTerm, currentPage, pageSize, sortField, sortDirection]);

    const incPage = () => {
        setCurrentPage(currentPage + 1);
    }

    const decPage = () => {
        setCurrentPage(currentPage - 1);
    }

    if (isLoading) {
        return <Loading/>;
    }

    return (
        <div className={"cards-container-wrapper"}>
            <div className="cards-sort-container">
                <div className="sort-search-container">
                    <SortMenu sortField={sortField} sortDirection={sortDirection}
                              onSortChange={handleSortChange}/>
                    <SearchBar onSearch={handleSearch}/>
                </div>
                <div className={"cards-container"}>
                    {books.map((book) => (
                        <Card
                            key={book.uuid}
                            author={book.author}
                            title={book.title}
                            genre={book.genre}
                            uuid={book.uuid}
                            price={book.price}
                            description={book.description}
                            isPaid={book.isPaid}
                            getBooksFromServer={() => getBooksFromServer(currentPage, pageSize)}
                        />
                    ))}
                    {roles.includes(Roles.Admin) &&
                    <AddCard getBooksFromServer={() => getBooksFromServer(currentPage, pageSize)}/>}
                </div>
            </div>
            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPrevPage={decPage}
                onNextPage={incPage}
            />
        </div>
    );
};

export default Cards;