import React from 'react';
import {useTranslation} from "react-i18next";

interface PaginationProps {
    currentPage: number;
    totalPages: number;
    onPrevPage: () => void;
    onNextPage: () => void;
}

const Pagination: React.FC<PaginationProps> = ({currentPage, totalPages, onPrevPage, onNextPage}) => {
    const {t: i18n} = useTranslation();

    return (
        <div className="pagination">
            <button
                disabled={currentPage === 1}
                onClick={onPrevPage}
            >
                {i18n('prev')}
            </button>
            <span>{currentPage} / {totalPages}</span>
            <button
                disabled={currentPage === totalPages}
                onClick={onNextPage}
            >
                {i18n('next')}
            </button>
        </div>
    );
};

export default Pagination;