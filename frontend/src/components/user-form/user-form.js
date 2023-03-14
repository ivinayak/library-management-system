import React, { useState } from "react"
import * as dayjs from "dayjs"
import utc from "dayjs/plugin/utc"
import { useNavigate } from "react-router-dom"
import { useUser } from "../../context/user-context"

import {
    Paper,
    Container,
    Button,
    TextField,
    FormGroup,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography,
} from "@mui/material"
import { BackendApi } from "../../client/backend-api"
import classes from "./styles.module.css"

dayjs.extend(utc)

export const UserForm = () => {
    const { token } = useUser()
    const navigate = useNavigate()
    const [newUser, setNewUser] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        role:""
    })
    const [errors, setErrors] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        role:""
    })

    const updateUserField = (event) => {
        const field = event.target
        setNewUser((newUser) => ({ ...newUser, [field.name]: field.value }))
    }

    const isInvalid =
        (newUser && newUser.firstName && newUser.lastName && newUser.email && newUser.password && newUser.role) 
        && (newUser.firstName.trim() === "" || newUser.lastName.trim() === "" || newUser.email.trim() === "" || newUser.password.trim() === "" || newUser.role.trim() === "")

    const formSubmit = (event) => {
        event.preventDefault()
        if (!isInvalid) {
            BackendApi.user
                .addUser(newUser, token)
                .then(() => navigate("/"))
        }
    }

    const validateForm = (event) => {
        const { name, value } = event.target
        if (["firstName", "lastName", "email", "password", "role"].includes(name)) {
            setNewUser((prevProd) => ({ ...prevProd, [name]: value.trim() }))
            if (!value.trim().length) {
                setErrors({ ...errors, [name]: `${name} can't be empty` })
            } else {
                setErrors({ ...errors, [name]: "" })
            }
        }
    }

    // useEffect(() => {
    //     async function fetchData() {
    //         if (bookIsbn) {
    //             const book = await BackendApi.book.getBookByName(bookIsbn, token);
    //             if(book){
    //                 console.log("setting book" + book)
    //                 setBook(book)
    //             }
    //             else{
    //                 navigate("/")
    //             }
    //         }
    //       }
    //       fetchData();

    //     // eslint-disable-next-line react-hooks/exhaustive-deps
    // }, [bookIsbn, token])

    return (
        <>
            <Container component={Paper} className={classes.wrapper}>
                <Typography className={classes.pageHeader} variant="h5">
                    Add User
                </Typography>
                <form noValidate autoComplete="off" onSubmit={formSubmit}>
                    <FormGroup>
                        <FormControl className={classes.mb2}>
                            <TextField
                                label="First Name"
                                name="firstName"
                                required
                                value={newUser.firstName}
                                onChange={updateUserField}
                                onBlur={validateForm}
                                error={errors.firstName.length > 0}
                                helperText={errors.firstName}
                            />
                        </FormControl>
                        <FormControl className={classes.mb2}>
                            <TextField
                                label="Last Name"
                                name="lastName"
                                required
                                value={newUser.lastName}
                                onChange={updateUserField}
                                onBlur={validateForm}
                                error={errors.lastName.length > 0}
                                helperText={errors.lastName}
                            />
                        </FormControl>
                        <FormControl className={classes.mb2}>
                            <TextField
                                label="Email"
                                name="email"
                                required
                                value={newUser.email}
                                onChange={updateUserField}
                                onBlur={validateForm}
                                error={errors.email.length > 0}
                                helperText={errors.email}
                            />
                        </FormControl>
                        <FormControl className={classes.mb2}>
                            <InputLabel>Role</InputLabel>
                            <Select name="role" value={newUser.role} onChange={updateUserField} required>
                                <MenuItem value="user">User</MenuItem>
                                <MenuItem value="staff">Staff</MenuItem>
                            </Select>
                        </FormControl>
                        <FormControl className={classes.mb2}>
                            <TextField
                                label="Password"
                                name="password"
                                required
                                value={newUser.password}
                                onChange={updateUserField}
                                onBlur={validateForm}
                                error={errors.password.length > 0}
                                helperText={errors.password}
                            />
                        </FormControl>
                    </FormGroup>
                    <div className={classes.btnContainer}>
                        <Button
                            variant="contained"
                            color="secondary"
                            onClick={() => {
                                navigate(-1)
                            }}
                        >
                            Cancel
                        </Button>
                        <Button type="submit" variant="contained" color="primary" disabled={isInvalid}>
                            Add User
                        </Button>
                    </div>
                </form>
            </Container>
        </>
    )
}
