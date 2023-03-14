import { useEffect, useState } from "react"
import { useParams, useNavigate, Link as RouterLink } from "react-router-dom"
import {
    Button,
    Card,
    CardContent,
    CardActions,
    Typography,
    Tabs,
    Tab,
    Table,
    TableBody,
    TableCell,
    TableRow,
} from "@mui/material"
import { NotificationManager } from "react-notifications"
import { BackendApi } from "../../client/backend-api"
import { useUser } from "../../context/user-context"
import { TabPanel } from "../tabs/tab"
import classes from "./styles.module.css"

export const Book = () => {
    const { bookIsbn } = useParams()
    const { user, isAdmin, token } = useUser()
    const navigate = useNavigate()
    const [book, setBook] = useState(null)
    // const [chartOptions, setChartOptions] = useState(null)
    const [openTab, setOpenTab] = useState(0)

    const borrowBook = () => {
        if (book && user) {
            BackendApi.user
                .borrowBook(book.isbn, token)
                .then(({ book, error }) => {
                    if (error) {
                        NotificationManager.error(error)
                    } else {
                        fetchData();
                    }
                })
                .catch(console.error)
        }
    }

    const returnBook = () => {
        if (book && user) {
            BackendApi.user
                .returnBook(book.isbn, token)
                .then(({ book, error }) => {
                    if (error) {
                        NotificationManager.error(error)
                    } else {
                        fetchData();
                    }
                })
                .catch(console.error)
        }
    }

    const renewBook = () => {
        if (book && user) {
            BackendApi.user
                .renewBook(book.isbn, token)
                .then(({ book, error }) => {
                    if (error) {
                        NotificationManager.error(error)
                    } else {
                        fetchData();
                    }
                })
                .catch(console.error)
        }
    }

    async function fetchData() {
        if (bookIsbn) {
            const book = await BackendApi.book.getBookByName(bookIsbn, token);
            if(book){
                console.log("setting book" + book)
                setBook(book)
            }
            else{
                NotificationManager.error("Error while fetching book details")
            }
        }
      }

    useEffect(() => {       

        
          fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [bookIsbn, token])

    return (
        book && (
            <div className={classes.wrapper}>
                <Typography variant="h5" align="center" style={{ marginBottom: 20 }}>
                    Book Details
                </Typography>
                <Card>
                    <Tabs
                        value={openTab}
                        indicatorColor="primary"
                        textColor="primary"
                        onChange={(e, tabIndex) => {
                            setOpenTab(tabIndex)
                        }}
                        centered
                    >
                        <Tab label="Book Details" tabIndex={0} />
                    </Tabs>

                    <TabPanel value={openTab} index={0}>
                        <CardContent>
                            <Table>
                                <TableBody>
                                    <TableRow>
                                        <TableCell variant="head" component="th" width="200">
                                            Name
                                        </TableCell>
                                        <TableCell>{book.name}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell variant="head" component="th">
                                            ISBN
                                        </TableCell>
                                        <TableCell>{book.isbn}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell variant="head" component="th">
                                            Category
                                        </TableCell>
                                        <TableCell>{book.category}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell variant="head" component="th">
                                            Quantity
                                        </TableCell>
                                        <TableCell>{book.quantity}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell variant="head" component="th">
                                            Author
                                        </TableCell>
                                        <TableCell>{book.author}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell variant="head" component="th">
                                            Publication
                                        </TableCell>
                                        <TableCell>{book.publication}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell variant="head" component="th">
                                            Subject
                                        </TableCell>
                                        <TableCell>{book.subject}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </CardContent>
                    </TabPanel>

                    <CardActions disableSpacing>
                        <div className={classes.btnContainer}>
                            {isAdmin ? (
                                <Button
                                    variant="contained"
                                    color="secondary"
                                    component={RouterLink}
                                    to={`/admin/books/${bookIsbn}/edit`}
                                >
                                    Edit Book
                                </Button>
                            ) : (
                                <>
                                    <Button
                                        variant="contained"
                                        onClick={borrowBook}
                                        disabled={book && user && book?.borrowedBy?.includes(user.userId)}
                                    >
                                        Borrow
                                    </Button>
                                    <Button
                                        variant="contained"
                                        onClick={returnBook}
                                        disabled={book && user && !book?.borrowedBy?.includes(user.userId)}
                                    >
                                        Return
                                    </Button>
                                    <Button
                                        variant="contained"
                                        onClick={renewBook}
                                        disabled={book && user && !book?.borrowedBy?.includes(user.userId)}
                                    >
                                        Renew
                                    </Button>
                                </>
                            )}
                            <Button type="submit" variant="text" color="primary" onClick={() => navigate(-1)}>
                                Go Back
                            </Button>
                        </div>
                    </CardActions>
                </Card>
            </div>
        )
    )
}