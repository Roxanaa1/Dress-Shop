import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Sidebar.css';

const Sidebar = ({ handleLogout }) => {
    const navigate = useNavigate();

    return (
        <div>
            <div className="sidebar">
                <button className="sidebar-button" onClick={() => navigate('/Account')}>Date Personale</button>
                <button className="sidebar-button" onClick={() => navigate('/Address')}>Adrese</button>
                <button className="sidebar-button" onClick={() => navigate('/Wishlist')}>Wishlist</button>
            </div>
            <div className="logout">
                <button className="sidebar-button" onClick={handleLogout}>Logout</button>
            </div>
        </div>
    );
};

export default Sidebar;
