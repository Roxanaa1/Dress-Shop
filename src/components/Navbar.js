import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CustomChatbot from './CustomChatbot';
import '../styles/Navbar.css';

const Navbar = () => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [showChatbot, setShowChatbot] = useState(false);

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

    useEffect(() => {
        const handleEsc = (e) => {
            if (e.key === 'Escape') setShowChatbot(false);
        };
        window.addEventListener('keydown', handleEsc);
        return () => window.removeEventListener('keydown', handleEsc);
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
        <>
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
                        <button onClick={handleLoginRedirect} className="login-button">Login</button>
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

                    {/* 👇 Buton chatbot */}
                    <button
                        onClick={() => setShowChatbot(true)}
                        aria-label="Chatbot"
                        style={{
                            background: 'none',
                            border: 'none',
                            cursor: 'pointer',
                            marginLeft: '8px'
                        }}
                    >
                        <i className="fas fa-comments" style={{ fontSize: '18px', color: '#fff' }}></i>
                    </button>

                    <a href="#menu" aria-label="Menu">
                        <i className="fas fa-bars"></i>
                    </a>
                </div>
            </header>

            {showChatbot && (
                <div
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100vw',
                        height: '100vh',
                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        zIndex: 9999,
                    }}
                    onClick={() => setShowChatbot(false)}
                >
                    <div
                        onClick={(e) => e.stopPropagation()}
                        style={{
                            backgroundColor: '#fff',
                            borderRadius: '16px',
                            width: '360px',
                            height: '520px',
                            boxShadow: '0 10px 25px rgba(0,0,0,0.3)',
                            overflow: 'hidden',
                            position: 'relative',
                            display: 'flex',
                            flexDirection: 'column'
                        }}
                    >
                        <CustomChatbot onClose={() => setShowChatbot(false)} />
                    </div>
                </div>
            )}
        </>
    );
};

export default Navbar;
