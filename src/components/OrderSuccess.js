import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/OrderSuccess.css';

const OrderSuccess = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const order = location.state?.order;

    console.log("Order Data in OrderSuccess:", order); // Log pentru depanare

    if (!order) {
        return <div>Detalii de livrare lipsă!</div>;
    }

    // Verifică dacă deliveryDetails există înainte de a accesa firstName
    if (!order.deliveryDetails) {
        return <div>Detalii de livrare lipsă!</div>;
    }

    return (
        <div className="order-success-container">
            <h1>Comanda a fost plasată cu succes!</h1>
            <p>Mulțumim pentru comanda ta, {order.deliveryDetails.firstName}!</p>
            <p>Numărul tău de comandă este: <strong>{order.id}</strong></p>

            <div className="order-actions">
                <button onClick={() => navigate('/')}>Înapoi la pagina principală</button>
            </div>
        </div>
    );
};

export default OrderSuccess;
