import { useState, useEffect } from "react"
import { Link as RouterLink } from "react-router-dom"
import {
    Button,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Modal,
    Card,
    CardContent,
    CardActions,
    Typography,
    TablePagination,
} from "@mui/material"
import { BackendApi } from "../../client/backend-api"
import { useUser } from "../../context/user-context"
import classes from "./styles.module.css"

export const BooksList = () => {

    const [books, setBooks] = useState([]);
    const [nonAdminUsers, setNonAdminUsers] = useState([]);
    const [borrowedBook, setBorrowedBook] = useState([])
    const [page, setPage] = useState(0)
    const [rowsPerPage, setRowsPerPage] = useState(10)
    const [activeBookIsbn, setActiveBookIsbn] = useState("")
    const [openModal, setOpenModal] = useState(false)
    const [openUserModal, setOpenUserModal] = useState(false)
    const { isAdmin, user, token, userId } = useUser()
    const [activeUserId, setActiveUserId] = useState("")

    const deleteBook = () => {
        if (activeBookIsbn && books.length) {
            BackendApi.book.deleteBook(activeBookIsbn, token).then(({ success }) => {
                fetchBooks().catch(console.error)
                setOpenModal(false)
                setActiveUserId("")
            })
        }
    }

    const deleteNonAdminUser = () => {
        if (activeUserId && nonAdminUsers.length) {
            BackendApi.user.deleteUser(activeUserId, token).then(({ success }) => {
                getNonAdminUsers().catch(console.error)
                setOpenUserModal(false)
                setActiveBookIsbn("")
            })
        }
    }

    async function fetchBooks() {
        const books  = await BackendApi.book.getAllBooks()
        setBooks(books)
    }
    
    async function getNonAdminUsers(){
        const nonAdminUsers =  await BackendApi.user.getNonAdminUsers(token);
        setNonAdminUsers(nonAdminUsers)
    }

    useEffect(() => {
        async function fetchUserBook() {
            if(!isAdmin){
                const books = await BackendApi.user.getBorrowBook(userId, token)
                setBorrowedBook(books)  
            }
          }
          getNonAdminUsers();
          fetchUserBook();
          fetchBooks();
            // eslint-disable-next-line       
    }, [user, token, userId, isAdmin])

    return (
        <>
            <div className={`${classes.pageHeader} ${classes.mb2}`}>
                <Typography variant="h5">Book List</Typography>
                {isAdmin && (
                    <Button variant="contained" color="primary" component={RouterLink} to="/admin/books/add">
                        Add Book
                    </Button>
                )}
            </div>
            {books.length > 0 ? (
                <>
                    <div className={classes.tableContainer}>
                        <TableContainer component={Paper}>
                            <Table stickyHeader>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Name</TableCell>
                                        <TableCell align="right">ISBN</TableCell>
                                        <TableCell>Category</TableCell>
                                        <TableCell align="right">Quantity</TableCell>
                                        <TableCell align="right">Author</TableCell>
                                        <TableCell align="right">Publication</TableCell>
                                        <TableCell align="right">Subject</TableCell>
                                        <TableCell>Action</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {(rowsPerPage > 0
                                        ? books.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                        : books
                                    ).map((book) => (
                                        <TableRow key={book.isbn}>
                                            <TableCell component="th" scope="row">
                                                {book.name}
                                            </TableCell>
                                            <TableCell align="right">{book.isbn}</TableCell>
                                            <TableCell>{book.category}</TableCell>
                                            <TableCell align="right">{book.quantity}</TableCell>
                                            <TableCell align="right">{book.author}</TableCell>
                                            <TableCell align="right">{book.publication}</TableCell>
                                            <TableCell align="right">{book.subject}</TableCell>
                                            <TableCell>
                                                <div className={classes.actionsContainer}>
                                                    <Button
                                                        variant="contained"
                                                        component={RouterLink}
                                                        size="small"
                                                        to={`/books/${book.bookId}`}
                                                    >
                                                        View
                                                    </Button>
                                                    {isAdmin && (
                                                        <>
                                                            <Button
                                                                variant="contained"
                                                                color="primary"
                                                                component={RouterLink}
                                                                size="small"
                                                                to={`/admin/books/${book.bookId}/edit`}
                                                            >
                                                                Edit
                                                            </Button>
                                                            <Button
                                                                variant="contained"
                                                                color="secondary"
                                                                size="small"
                                                                onClick={(e) => {
                                                                    setActiveBookIsbn(book.bookId)
                                                                    setOpenModal(true)
                                                                }}
                                                            >
                                                                Delete
                                                            </Button>
                                                        </>
                                                    )}
                                                </div>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                        <TablePagination
                            onRowsPerPageChange={(e) => {
                                setRowsPerPage(parseInt(e.target.value, 10))
                                setPage(0)
                            }}
                            component="div"
                            count={books.length}
                            rowsPerPage={rowsPerPage}
                            page={page}
                            onPageChange={(e, newPage) => setPage(newPage)}
                        />
                        <Modal open={openModal} onClose={(e) => setOpenModal(false)}>
                            <Card className={classes.conf_modal}>
                                <CardContent>
                                    <h2>Are you sure?</h2>
                                </CardContent>
                                <CardActions className={classes.conf_modal_actions}>
                                    <Button variant="contained" onClick={() => setOpenModal(false)}>
                                        Cancel
                                    </Button>
                                    <Button variant="contained" color="secondary" onClick={deleteBook}>
                                        Delete
                                    </Button>
                                </CardActions>
                            </Card>
                        </Modal>
                    </div>
                </>
            ) : (
                <Typography variant="h5">No books found!</Typography>
            )}

            {
                user && !isAdmin && borrowedBook && (
                    <>
                        <div className={`${classes.pageHeader} ${classes.mb2}`}>
                            <Typography variant="h5">Borrowed Books</Typography>
                        </div>
                        {borrowedBook.length > 0 ? (
                            <>
                                <div className={classes.tableContainer}>
                                    <TableContainer component={Paper}>
                                        <Table stickyHeader>
                                            <TableHead>
                                                <TableRow>                                                    
                                                    <TableCell>Name</TableCell>
                                                    <TableCell align="right">ISBN</TableCell>
                                                    <TableCell>Category</TableCell>
                                                    <TableCell align="right">Quantity</TableCell>
                                                    <TableCell align="right">Author</TableCell>
                                                    <TableCell align="right">Publication</TableCell>
                                                    <TableCell align="right">Subject</TableCell>
                                                    <TableCell>Action</TableCell>
                                                </TableRow>
                                            </TableHead>
                                            <TableBody>
                                                {borrowedBook.map((book) => (
                                                    <TableRow key={book.bookId}>
                                                        <TableCell component="th" scope="row">
                                                            {book.name}
                                                        </TableCell>
                                                        <TableCell align="right">{book.isbn}</TableCell>
                                                        <TableCell>{book.category}</TableCell>
                                                        <TableCell align="right">{book.quantity}</TableCell>
                                                        <TableCell align="right">{book.author}</TableCell>
                                                        <TableCell align="right">{book.publication}</TableCell>
                                                        <TableCell align="right">{book.subject}</TableCell>
                                                        <TableCell>
                                                            <div className={classes.actionsContainer}>
                                                                <Button
                                                                    variant="contained"
                                                                    component={RouterLink}
                                                                    size="small"
                                                                    to={`/books/${book.bookId}`}
                                                                >
                                                                    View
                                                                </Button>
                                                            </div>
                                                        </TableCell>
                                                    </TableRow>
                                                ))}
                                            </TableBody>
                                        </Table>
                                    </TableContainer>
                                </div>
                            </>
                        ) : (
                            <Typography variant="h5">No books issued!</Typography>
                        )}
                    </>
                )
            }
            { user && isAdmin && nonAdminUsers ? 
                (<>
                    <div className={`${classes.pageHeader} ${classes.mb2}`}>
                        <Typography variant="h5">User List</Typography>
                        <Button variant="contained" color="primary" component={RouterLink} to="/admin/users/add">
                            Add User
                        </Button>
                    </div>
                    { nonAdminUsers.length > 0 ? (
                        <>
                            <div className={classes.tableContainer}>
                                <TableContainer component={Paper}>
                                    <Table stickyHeader>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>First Name</TableCell>
                                                <TableCell align="right">Last Name</TableCell>
                                                <TableCell>Email</TableCell>
                                                <TableCell align="right">Role</TableCell>

                                                <TableCell>Action</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {(rowsPerPage > 0
                                                ? nonAdminUsers.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                                : nonAdminUsers
                                            ).map((book) => (
                                                <TableRow key={book.email}>
                                                    <TableCell component="th" scope="row">
                                                        {book.firstName}
                                                    </TableCell>
                                                    <TableCell align="right">{book.lastName}</TableCell>
                                                    <TableCell>{book.email}</TableCell>
                                                    <TableCell align="right">{book.role}</TableCell>

                                                    <TableCell>
                                                        <div className={classes.actionsContainer}>
                                                            {isAdmin && (
                                                                <>
                                                                    <Button
                                                                        variant="contained"
                                                                        color="secondary"
                                                                        size="small"
                                                                        onClick={(e) => {
                                                                            setActiveUserId(book.userId)
                                                                            setOpenUserModal(true)
                                                                        }}
                                                                    >
                                                                        Delete
                                                                    </Button>
                                                                </>
                                                            )}
                                                        </div>
                                                    </TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                                <TablePagination
                                    onRowsPerPageChange={(e) => {
                                        setRowsPerPage(parseInt(e.target.value, 10))
                                        setPage(0)
                                    }}
                                    component="div"
                                    count={books.length}
                                    rowsPerPage={rowsPerPage}
                                    page={page}
                                    onPageChange={(e, newPage) => setPage(newPage)}
                                />
                                <Modal open={openUserModal} onClose={(e) => setOpenUserModal(false)}>
                                    <Card className={classes.conf_modal}>
                                        <CardContent>
                                            <h2>Are you sure?</h2>
                                        </CardContent>
                                        <CardActions className={classes.conf_modal_actions}>
                                            <Button variant="contained" onClick={() => setOpenUserModal(false)}>
                                                Cancel
                                            </Button>
                                            <Button variant="contained" color="secondary" onClick={deleteNonAdminUser}>
                                                Delete
                                            </Button>
                                        </CardActions>
                                    </Card>
                                </Modal>
                            </div>
                        </>
                    ) : (
                        <Typography variant="h5">No users found!</Typography>
                    )}
                </>
                )
                :
                (
                    <></>
                )

            }
        </>
    )
}