import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/OrderSuccess.css';

const OrderSuccess = () => {
    const navigate = useNavigate();

    return (
        <div className="order-success-container">
            <h1>Comanda a fost plasată cu succes!</h1>
            <p>Mulțumim pentru comanda ta!</p>

            <div className="order-actions">
                <button onClick={() => navigate('/')}>Înapoi la pagina principală</button>
            </div>
        </div>
    );
};

export default OrderSuccess;
