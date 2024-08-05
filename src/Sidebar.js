// Sidebar.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Sidebar.css';

const Sidebar = () => {
    const navigate = useNavigate();

    return (
        <div className="sidebar">
            <button onClick={() => navigate('/dresses')}>ROCHII</button>
            <button onClick={() => navigate('/capsule-collection')}>ROCHII DE SEARA</button>
            <button onClick={() => navigate('/new-arrivals')}>ROCHII DE ZI</button>
            <button onClick={() => navigate('/office-looks')}>OFFICE LOOKS</button>

        </div>
    );
};

export default Sidebar;
