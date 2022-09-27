import { Fragment } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import NavBar from "../components/AppBar";
import Login from "../pages/Singin";
import User from "../pages/UserComponent";

const Private = ({ Item }) => {
    let signed = JSON.parse(localStorage.getItem("user") || '{}')

    //console.log(signed)

    return signed ? <Item /> : <Login />;
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