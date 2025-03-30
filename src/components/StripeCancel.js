import React from 'react';
import {useNavigate} from 'react-router-dom';

const StripeCancel = () => {
    const navigate = useNavigate();

    return (
        <div style={{textAlign: 'center', marginTop: '100px'}}>
            <h1>Payment was canceled</h1>
            <p>Your order was not processed.</p>
            <button onClick={() => navigate('/cart')} style={{
                padding: '10px 20px',
                backgroundColor: '#A7727D',
                border: 'none',
                color: '#fff',
                borderRadius: '4px',
                cursor: 'pointer'
            }}>
                Înapoi la coș
            </button>
        </div>
    );
};

export default StripeCancel;
