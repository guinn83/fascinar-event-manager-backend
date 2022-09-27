import { Fragment } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import NavBar from "../components/AppBar";
import Login from "../pages/Singin";
import User from "../pages/UserComponent";
import authService from "../services/auth.service";

const Private = ({ Item }) => {
    return authService.isSigned() ? <Item /> : <Login />;
};

const RoutesApp = () => {
    return (
        <BrowserRouter>
            <NavBar />
            <Fragment>
                <Routes>
                    <Route path="/user" element={<Private Item={User} />} />
                    <Route path="/" element={<Login />} />
                    <Route path="*" element={<Login />} />
                </Routes>
            </Fragment>
        </BrowserRouter>
    );
};

export default RoutesApp;