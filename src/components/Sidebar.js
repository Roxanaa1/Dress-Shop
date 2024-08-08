import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Sidebar.css';

const Sidebar = () => {
    const navigate = useNavigate();

    return (
        <div className="sidebar">
            <button onClick={() => navigate('/dresses/all')}>DRESSES</button>
            <button onClick={() => navigate('/dresses/EVENING%20DRESSES')}>EVENING DRESSES</button>
            <button onClick={() => navigate('/dresses/DAY%20DRESSES')}>DAY DRESSES</button>
        </div>
    );
};

export default Sidebar;
