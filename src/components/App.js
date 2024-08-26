import React from 'react';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import Home from './Home';
import Register from './Register';
import Login from './Login';
import ProductDetails from './ProductDetails';
import Navbar from './Navbar';
import Cart from './Cart';
import Wishlist from './Wishlist';
import Account from './Account';
import Search from './Search';
import Address from "./Address";
import Sidebar from "./Sidebar";
import ForgotPassword from './ForgotPassword';

const AppContent = () => {
    const location = useLocation();
    const showNavbar = !['/login', '/register'].includes(location.pathname);

    return (
        <div className="App">
            {showNavbar && <Navbar />}
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/ProductDetails/:id" element={<ProductDetails />} />
                <Route path="/dresses/:filter" element={<Home />} />
                <Route path="/cart" element={<Cart />} />
                <Route path="/wishlist" element={<Wishlist />} />
                <Route path="/account" element={<Account />} />
                <Route path="/search" element={<Search />} />
                <Route path="/address" element={<Address/>} />
                <Route path="/sidebar" element={<Sidebar />} />
                <Route path="/forgotPassword" element={<ForgotPassword />} />

                <Route path="*" element={<div>Pagina nu a fost gasita</div>} />
            </Routes>
        </div>
    );
};

const App = () => (
    <Router>
        <AppContent />
    </Router>
);

export default App;
