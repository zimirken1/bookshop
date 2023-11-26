import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import BookService from "../../API/BookService";
import IBook from "../catalog/IBook";
import Card from "../catalog/Card";
import "../../styles/BookPage.css"
import Header from "../Header";
import CommentService from "../../API/CommentService";
import {IComment} from "./comment/IComment";
import Comment from "./comment/Comment";
import {useAuth} from "../auth/context/AuthContextProvider";
import CommentModal from "./comment/CommentModal";
import {useTranslation} from "react-i18next";

const BookPage = () => {
    const {roles, isAuthenticated} = useAuth();
    let {bookUuid} = useParams();
    const [bookInfo, setBookInfo] = useState<IBook>();
    const [comments, setComments] = useState<IComment[]>();
    const [open, setOpen] = useState(false);
    const {t: i18n} = useTranslation();

    const handleClose = () => {
        setOpen(false);
    };

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClickSave = (comment: string) => {
        CommentService.addComment(bookInfo!.uuid, comment, null).then(getComments);
    }

    useEffect(() => {
        getBookInfoFromServer();
        getComments();
    }, [])

    const getBookInfoFromServer = async () => {
        try {
            let response = await BookService.getBook(bookUuid!);
            let data = await response.data;
            setBookInfo(data);
        } catch (e) {
            console.log(e);
        }
    }

    const getComments = async () => {
        try {
            let response = await CommentService.getComments(bookUuid!);
            let data = await response.data;
            const nestedUuids = new Set();
            data.forEach((comment: IComment) => {
                comment.replies?.forEach(reply => {
                    nestedUuids.add(reply.uuid);
                });
            });
            const filteredComments = data.filter((comment: IComment) => !nestedUuids.has(comment.uuid));
            setComments(filteredComments);
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <div>
            <Header/>
            <div className={"book-page"}>
                {bookInfo &&
                <div className={"card-desc-container"}>
                    <Card
                        getBooksFromServer={getBookInfoFromServer}
                        title={bookInfo.title}
                        genre={bookInfo.genre}
                        author={bookInfo.author}
                        uuid={bookInfo.uuid}
                        price={bookInfo.price}
                        description={bookInfo.description}
                        isPaid={bookInfo.isPaid}
                    />
                    <div className="book-page-desc-container">
                        <div className={"desc"}>
                            {bookInfo.description ? bookInfo.description : i18n('emptyDescription')}
                        </div>
                        <div className="comments-container">
                            {
                                comments?.map((comment) => {
                                    return <Comment
                                        key={comment.uuid}
                                        bookUuid={bookInfo?.uuid}
                                        uuid={comment.uuid}
                                        text={comment.text}
                                        timestamp={comment.timestamp}
                                        username={comment.username}
                                        getComments={getComments}
                                        replies={comment.replies}
                                        removed={comment.removed}
                                    />
                                })
                            }
                        </div>
                        {isAuthenticated &&
                        <div className="add-comment-button-container">
                            <div
                                className="download-button"
                                style={{marginTop: 10}}
                                onClick={handleClickOpen}
                            >
                                {i18n('addComment')}
                            </div>
                        </div>
                        }
                    </div>
                    <CommentModal onSave={handleClickSave} defaultComment={""} open={open}
                                  handleClose={handleClose}/>
                </div>
                }
            </div>
        </div>
    );
};

export default BookPage;