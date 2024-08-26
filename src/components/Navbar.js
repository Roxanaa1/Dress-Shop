import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

const Navbar = () => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');

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

    const handleLoginRedirect = () => {
        navigate('/login');
    };

    const navitateToSearch = () => {
        if (searchTerm.trim() !== '') {
            navigate(`/search?text=${searchTerm}`);
        }
    };

    return (
        <header className="navbar">
            <div className="navbar-left">
                <div className="navbar-brand" onClick={() => navigate('/')}></div>
                <button onClick={() => navigate('/dresses/all')}>DRESSES</button>
                <button onClick={() => navigate('/?filter=EVENING%20DRESSES')}>EVENING DRESSES</button>
                <button onClick={() => navigate('/?filter=DAY%20DRESSES')}>DAY DRESSES</button>

            </div>
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Search..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <button onClick={navitateToSearch}>Search</button>
            </div>
            <div className="navbar-icons">
                {!isLoggedIn && (
                    <>
                        <button onClick={handleLoginRedirect} className="login-button">Login</button>
                    </>
                )}
                <a onClick={() => navigate('/account')} aria-label="Account">
                    <i className="fas fa-user"></i>
                </a>
                <a onClick={() => navigate('/cart')} aria-label="Cart">
                    <i className="fas fa-shopping-cart"></i>
                </a>
                <a onClick={() => navigate('/wishlist')} aria-label="Wishlist">
                    <i className="fas fa-heart"></i>
                </a>
                <a href="#menu" aria-label="Menu">
                    <i className="fas fa-bars"></i>
                </a>
            </div>
        </header>
    );
};

export default Navbar;
