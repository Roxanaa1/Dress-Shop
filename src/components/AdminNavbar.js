import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import '../styles/AdminNavbar.css';

const AdminNavbar = () => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const checkAuthStatus = () => {
            const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
            setIsLoggedIn(loggedIn);
        };

        checkAuthStatus();
        window.addEventListener('storage', checkAuthStatus);

        return () => {
            window.removeEventListener('storage', checkAuthStatus);
        };
    }, []);

    return (
        <header className="navbar">
            <div className="navbar-left">
                <button onClick={() => navigate('/admin-dashboard')} className="home-button">HOME</button>
                <button onClick={() => navigate('/admin-orders')} className="orders-button">ORDERS</button>
                <button onClick={() => navigate('/add-product')} className="create-product-button">CREATE PRODUCT
                </button>
                <button onClick={() => navigate('/admin-charts/users')} className="charts-button">USER CHARTS</button>
                <button onClick={() => navigate('/admin-charts/products')} className="charts-button">PRODUCT CHARTS
                </button>
                <button onClick={() => navigate('/admin-charts/orders')} className="charts-button">ORDER CHARTS</button>
                <button onClick={() => navigate('/admin-reports/products')} className="charts-button">
                    PRODUCT REPORTS
                </button>
                <button onClick={() => navigate('/admin-reports/users')} className="charts-button">
                    USER REPORTS
                </button>
                <button onClick={() => navigate('/admin-reports/orders')} className="charts-button">
                    ORDER REPORTS
                </button>

            </div>

            <div className="navbar-icons">
                <a onClick={() => navigate('/account')} aria-label="Account">
                <i className="fas fa-user"></i>
                </a>
            </div>
        </header>
    );
};

export default AdminNavbar;