import React, {useState} from 'react';
import {IComment} from "./IComment";
import "../../../styles/BookPage.css"
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import {useAuth} from "../../auth/context/AuthContextProvider";
import {Roles} from "../../../enums/Roles";
import CommentService from "../../../API/CommentService";
import CommentModal from "./CommentModal";
import QuestionAnswerIcon from '@mui/icons-material/QuestionAnswer';

interface CommentProps extends IComment {
    getComments: () => {};
    bookUuid: string;
}

const Comment: React.FC<CommentProps> = ({
                                             username,
                                             text,
                                             timestamp,
                                             uuid,
                                             getComments,
                                             bookUuid,
                                             replies,
                                             removed
                                         }) => {
    const {roles: userRoles, username: userLogin} = useAuth();
    const [openUpdate, setOpenUpdate] = useState(false);
    const [openReply, setOpenReply] = useState(false);

    const handleSave = (comment: string) => {
        CommentService.addComment(bookUuid, comment, uuid).then(getComments);
    }

    const handleDelete = () => {
        CommentService.deleteComment(uuid).then(getComments);
    }

    const handleUpdate = (comment: string) => {
        CommentService.updateComment(uuid, comment).then(getComments);
    };

    const handleClose = () => {
        setOpenUpdate(false);
    };

    const handleClickOpenUpdate = () => {
        setOpenUpdate(true);
    };

    const handleClickOpenReply = () => {
        setOpenReply(true);
    };

    const handleCloseReply = () => {
        setOpenReply(false);
    };

    return (
        <div className={"comment-container-wrapper"}>
            <div className={"comment-container"}>
                {removed ? "COMMENT REMOVED" :
                    <>
                        <div className="comment-credentials">
                            {username} <br/>
                            {timestamp}
                        </div>
                        <div className="comment-text">
                            {text}
                        </div>

                        <div className="comment-controls">
                            <QuestionAnswerIcon
                                style={{marginTop: 2}}
                                onClick={handleClickOpenReply}
                            />
                            {(userLogin === username || userRoles.includes(Roles.Admin)) &&
                            <>
                                <EditIcon
                                    onClick={handleClickOpenUpdate}
                                />
                                <DeleteIcon
                                    onClick={handleDelete}
                                />
                            </>
                            }
                        </div>
                        <CommentModal onSave={handleUpdate} defaultComment={text} open={openUpdate}
                                      handleClose={handleClose}/>
                        <CommentModal onSave={handleSave} defaultComment={""} open={openReply}
                                      handleClose={handleCloseReply}/>
                    </>
                }
            </div>
            {
                replies?.map((comment) => (
                    <div className={"reply-container"}>
                        <Comment
                            key={comment.uuid}
                            bookUuid={bookUuid}
                            uuid={comment.uuid}
                            text={comment.text}
                            timestamp={comment.timestamp}
                            username={comment.username}
                            getComments={getComments}
                            replies={comment.replies}
                            removed={comment.removed}
                        />
                    </div>
                ))
            }
        </div>
    );
};

export default Comment;