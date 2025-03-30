import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import '../styles/OrderSuccess.css';

const OrderSuccess = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        const cartId = localStorage.getItem('cartId');
        const deliveryAddressId = localStorage.getItem('defaultDeliveryAddress');
        const billingAddressId = localStorage.getItem('defaultBillingAddress');
        const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
        const paymentMethod = localStorage.getItem('paymentMethod') || 'ONLINE';

        const subtotal = cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
        const shippingCost = 15;
        const totalPrice = subtotal + shippingCost;

        const orderDetails = {
            userId: parseInt(userId, 10),
            cartId: parseInt(cartId, 10),
            paymentMethod: paymentMethod,
            deliveryAddress: parseInt(deliveryAddressId, 10),
            invoiceAddress: parseInt(billingAddressId, 10),
            orderDate: new Date().toISOString().split('T')[0],
            totalPrice: totalPrice
        };

        fetch('http://localhost:8080/orders/createOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderDetails)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error saving order');
                return res.json();
            })
            .then(data => {
                fetch(`http://localhost:8080/cart/clear/${cartId}`, {method: 'DELETE'});
                localStorage.removeItem('cartItems');
                localStorage.removeItem('paymentMethod');
                console.log('Order saved successfully:', data);
            })
            .catch(err => {
                console.error('Error saving order:', err);
            });
    }, []);

    return (
        <div className="order-success-container">
            <h1>Your order has been successfully placed!</h1>
            <p>Thank you for your purchase!</p>
            <div className="order-actions">
                <button onClick={() => navigate('/')}>Back to homepage</button>
            </div>
        </div>
    );
};

export default OrderSuccess;
