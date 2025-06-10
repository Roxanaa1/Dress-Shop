import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, useLocation, Navigate } from 'react-router-dom';
import UserDashboard from './UserDashboard';
import Register from './Register';
import Login from './Login';
import ProductDetails from './ProductDetails';
import UserNavbar from './UserNavbar';
import Cart from './Cart';
import Wishlist from './Wishlist';
import Account from './Account';
import Search from './Search';
import Address from "./Address";
import Sidebar from "./Sidebar";
import OrderSuccess from './OrderSuccess';
import Verify from "./Verify";
import AdminDashboard from "./AdminDashboard";
import AdminNavbar from './AdminNavbar';
import AddProduct from "./AddProduct";
import AdminProductGrid from "./AdminProductGrid";
import Orders from "./Orders";
import UserCharts from "./UserCharts";
import StripeSuccess from "./StripeSucces";
import StripeCancel from "./StripeCancel";
import ProductCharts from "./ProductCharts";
import OrderCharts from "./OrderCharts";
import ProductReports from "./ProductReports";
import UserReports from "./UserRaports";
import OrderRaports from "./OrderRaports";
import ForgotPassword from './ForgotPassword';
import ResetPassword from './ResetPassword';
import ChangePasswordFromAccount from "./ChangePasswordFromAccount";
import Contact from "./Contact";

const AppContent = () => {
    const location = useLocation();
    const [role, setRole] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const storedRole = localStorage.getItem('role');
        setRole(storedRole);
        setIsLoading(false);
    }, [location.pathname]);


    if (isLoading) return null;

    const hideNavbarRoutes = ['/login', '/register', '/verify','/forgotPassword'];
    const showNavbar = !hideNavbarRoutes.includes(location.pathname);

    return (
        <div className="App">
            {showNavbar && (role === 'ADMIN' ? <AdminNavbar /> : <UserNavbar />)}

            <Routes>
                {/* USER routes */}
                <Route path="/" element={role === 'ADMIN' ? <Navigate to="/admin-dashboard" /> : <UserDashboard />} />
                <Route path="/user-dashboard" element={<UserDashboard />} />
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/ProductDetails/:id" element={<ProductDetails />} />
                <Route path="/dresses/:filter" element={<UserDashboard />} />
                <Route path="/cart" element={<Cart />} />
                <Route path="/wishlist" element={<Wishlist />} />
                <Route path="/account" element={<Account />} />
                <Route path="/search" element={<Search />} />
                <Route path="/address" element={<Address />} />
                <Route path="/sidebar" element={<Sidebar />} />
                <Route path="/forgotPassword" element={<ForgotPassword />} />
                <Route path="/orderSuccess" element={<OrderSuccess />} />
                <Route path="/verify" element={<Verify />} />
                <Route path="/change-password-account" element={<ChangePasswordFromAccount />} />
                <Route path="/ResetPassword" element={<ResetPassword />} />
                {/* ADMIN routes */}
                <Route path="/admin-dashboard" element={<AdminDashboard />} />
                <Route path="/add-product" element={<AddProduct />} />
                <Route path="/edit-product/:id" element={<AddProduct />} />
                <Route path="/admin-product-grid" element={<AdminProductGrid />} />
                <Route path="/admin-orders" element={<Orders />} />
                <Route path="/admin-charts/users" element={<UserCharts />} />
                <Route path="/admin-charts/products" element={<ProductCharts />} />
                <Route path="/admin-charts/orders" element={<OrderCharts />} />
                <Route path="/admin-reports/products" element={<ProductReports />} />
                <Route path="/admin-reports/users" element={<UserReports />} />
                <Route path="/admin-reports/orders" element={<OrderRaports />} />

                {/* Payments */}
                <Route path="/success" element={<StripeSuccess />} />
                <Route path="/cancel" element={<StripeCancel />} />

                <Route path="/contact" element={<Contact />} />
                {/* Fallback */}
                <Route path="*" element={<div>Pagina nu a fost găsită</div>} />
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
