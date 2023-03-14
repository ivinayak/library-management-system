import { useState, useEffect, createContext, useContext } from "react"
import { NotificationManager } from "react-notifications"
import { BackendApi } from "../client/backend-api"

const UserContext = createContext({
    user: null,
    token:null,
    userId:null,
    loginUser: () => { },
})

const useUser = () => useContext(UserContext);

const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [isAdmin, setIsAdmin] = useState(false);
    const [token, setToken] = useState(null);
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        setIsAdmin(user && user.role === 'ROLE_STAFF')
        if(user && user.userId){
            setUserId(user.userId)
        }
    }, [user])

    useEffect(() => {
        BackendApi.user.getProfile().then(({ user, error }) => {
            if (error) {
                console.error(error)
            } else {
                setUser(user)
            }
        }).catch(console.error)
    }, [])

    const loginUser = async (username, password) => {
        const user = await BackendApi.user.login(username, password)
        if (user) {
            
            NotificationManager.success("Logged in successfully")
            console.log("user found " + user);
            setUser(user)
            setToken(user.token)      
        }
    }

    const logoutUser = async () => {
        setUser(null)
        await BackendApi.user.logout()
    }

    return (
        <UserContext.Provider value={{ user, loginUser, logoutUser, isAdmin, token, userId }}>
            {children}
        </UserContext.Provider>
    )
}

export { useUser, UserProvider }