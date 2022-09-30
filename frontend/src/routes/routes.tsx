import { Fragment, useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import NavBar from "../components/AppBar";
import Login from "../pages/Singin";
import User from "../pages/UserComponent";
import authService from "../services/auth.service";



const RoutesApp = () => {
    const Private = ({ Item }) => {
        return authService.isSigned() ? <Item /> : <Login />;
    };
    return (
        <BrowserRouter>
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