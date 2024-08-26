import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Sidebar.css';

const Sidebar = ({ handleLogout }) => {
    const navigate = useNavigate();

    return (
        <div className="sidebar">
            <button className="sidebar-button" onClick={() => navigate('/Account')}>Personal Data</button>
            <button className="sidebar-button" onClick={() => navigate('/Address')}>Addresses</button>
            <button className="sidebar-button" onClick={() => navigate('/Wishlist')}>Wishlist</button>
            <button className="sidebar-button" onClick={handleLogout}>Logout</button>


        </div>
    );
};

export default Sidebar;
