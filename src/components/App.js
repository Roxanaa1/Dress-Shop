import React from 'react';
import {BrowserRouter as Router, Route, Routes, useLocation} from 'react-router-dom';
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
import AdminNavbar from "./AdminNavbar";
import Orders from "./Orders";
import UserCharts from "./UserCharts";
import StripeSuccess from "./StripeSucces";
import StripeCancel from "./StripeCancel";
import ProductCharts from "./ProductCharts";
import OrderCharts from "./OrderCharts";
import ProductReports from "./ProductReports";
import UserReports from "./UserRaports";
import OrderRaports from "./OrderRaports";
const AppContent = () => {
    const location = useLocation();

    const isAdminRoute = location.pathname.startsWith('/admin');

    const showUserNavbar = !isAdminRoute && !['/login', '/register', '/verify'].includes(location.pathname);

    return (
        <div className="App">
            {showUserNavbar && <Navbar/>}
            {isAdminRoute && <AdminNavbar/>}

            <Routes>
                <Route path="/" element={<UserDashboard/>}/>
                <Route path="/user-dashboard" element={<UserDashboard/>}/>
                <Route path="/register" element={<Register/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/ProductDetails/:id" element={<ProductDetails/>}/>
                <Route path="/dresses/:filter" element={<UserDashboard/>}/>
                <Route path="/cart" element={<Cart/>}/>
                <Route path="/wishlist" element={<Wishlist/>}/>
                <Route path="/account" element={<Account/>}/>
                <Route path="/search" element={<Search/>}/>
                <Route path="/address" element={<Address/>}/>
                <Route path="/sidebar" element={<Sidebar/>}/>
                <Route path="/forgotPassword" element={<ForgotPassword/>}/>
                <Route path="/orderSuccess" element={<OrderSuccess/>}/>
                <Route path="/verify" element={<Verify/>}/>

                <Route path="/admin-dashboard" element={<AdminDashboard/>}/>
                <Route path="/add-product" element={<AddProduct/>}/>
                <Route path="/edit-product/:id" element={<AddProduct/>}/>
                <Route path="/admin-product-grid" element={<AdminProductGrid/>}/>
                <Route path="/admin-orders" element={<Orders/>}/>
                <Route path="/admin-charts/users" element={<UserCharts/>}/>
                <Route path="/success" element={<StripeSuccess/>}/>
                <Route path="/cancel" element={<StripeCancel/>}/>
                <Route path="/admin-charts/products" element={<ProductCharts />} />
                <Route path="/admin-charts/orders" element={<OrderCharts />} />
                <Route path="/admin-reports/products" element={<ProductReports />} />
                <Route path="/admin-reports/users" element={<UserReports />} />
                <Route path="/admin-reports/orders" element={<OrderRaports />} />

                <Route path="*" element={<div>Pagina nu a fost gasita</div>}/>
            </Routes>
        </div>
    );
};

const App = () => (
    <Router>
        <AppContent/>
    </Router>
);

export default App;
