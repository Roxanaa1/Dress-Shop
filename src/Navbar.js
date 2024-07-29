import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
    const navigate = useNavigate(); // Hook-ul useNavigate pentru navigare

    const handleLoginClick = () => {
        navigate('/login');
    };

    return (
        <header className="navbar">
            <div className="navbar-brand"></div>
            <div className="navbar-icons">
                <a onClick={handleLoginClick} aria-label="Profile">
                    <i className="fas fa-user"></i>
                </a>
                <a href="#wishlist" aria-label="Wishlist">
                    <i className="fas fa-heart"></i>
                </a>
                <a href="#search" aria-label="Search">
                    <i className="fas fa-search"></i>
                </a>
                <a href="#menu" aria-label="Menu">
                    <i className="fas fa-bars"></i>
                </a>
            </div>
        </header>
    );
};

export default Navbar;
