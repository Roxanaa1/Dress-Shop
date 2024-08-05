import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Sidebar.css';

const Sidebar = () => {
    const navigate = useNavigate();

    return (
        <div className="sidebar">
            <button onClick={() => navigate('/dresses/all')}>DRESSES</button>
            <button onClick={() => navigate('/dresses/evening')}>EVENING DRESSES</button>
            <button onClick={() => navigate('/dresses/day')}>DAY DRESSES</button>
            <button onClick={() => navigate('/dresses/office')}>OFFICE LOOKS</button>
        </div>
    );
};

export default Sidebar;
