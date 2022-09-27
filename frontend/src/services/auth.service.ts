import axios from "axios";
import { BASE_URL_LOCAL } from "../utils/request";

const API_URL = `${BASE_URL_LOCAL}`;

const signup = (username: any, password: any) => {
    return axios
        .post(API_URL + "/signup", {
            username,
            password,
        })
        .then((response) => {
            if (response.data.token) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }
            console.log(response.data.token)
            return response.data;
        });
};

const login = (username: any, password: any) => {
    return axios.post(API_URL + "/login",
        JSON.stringify({
            username: username,
            password: password,
        }),
        { headers: { 'Content-Type': 'application/json' } })
        .then((response) => {
            if (response.data.token) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }
            //let user = (JSON.parse(localStorage.getItem("user") || '{}'))
            //console.log(user)
            return response.data;
        });
};

const logout = () => {
    localStorage.removeItem("user");
};

const getToken = () => {
    let user = JSON.parse(localStorage.getItem("user") || '{}')
    return 'Bearer ' + user.token;
};

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem("user") || '{}');
};

function isSigned(){
    return getToken() !== 'Bearer undefined';
}

const authService = {
    signup,
    login,
    logout,
    getCurrentUser,
    getToken,
    isSigned,
};

export default authService;