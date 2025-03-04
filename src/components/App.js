import React from 'react';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import UserDashboard from './UserDashboard';
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
import OrderSuccess from './OrderSuccess';
import Verify from "./Verify";
import AdminDashboard from "./AdminDashboard";
import AddProduct from "./AddProduct";
import AdminProductGrid from "./AdminProductGrid";
const AppContent = () => {
    const location = useLocation();
    const showNavbar = !['/login', '/register', '/verify', '/admin-dashboard'].includes(location.pathname);


    return (
        <div className="App">
            {showNavbar && <Navbar />}
            <Routes>
                <Route path="/" element={<UserDashboard />} />
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/ProductDetails/:id" element={<ProductDetails />} />
                <Route path="/dresses/:filter" element={<UserDashboard />} />
                <Route path="/cart" element={<Cart />} />
                <Route path="/wishlist" element={<Wishlist />} />
                <Route path="/account" element={<Account />} />
                <Route path="/search" element={<Search />} />
                <Route path="/address" element={<Address/>} />
                <Route path="/sidebar" element={<Sidebar />} />
                <Route path="/forgotPassword" element={<ForgotPassword />} />
                <Route path="/orderSuccess" element={<OrderSuccess />} />
                <Route path="/verify" element={<Verify />} />
                <Route path="/admin-dashboard" element={<AdminDashboard />} />
                <Route path="/user-dashboard" element={<UserDashboard />} />
                <Route path="/add-product" element={<AddProduct />} />
                <Route path="/admin-product-grid" element={<AdminProductGrid />} />

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
