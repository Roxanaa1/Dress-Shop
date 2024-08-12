import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Cart.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const Cart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [discountCode, setDiscountCode] = useState('');
    const [discount, setDiscount] = useState(0);
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
        setIsLoggedIn(loggedIn);

        if (!loggedIn) {
            alert("Trebuie să fii logat pentru a accesa coșul de cumpărături.");
            navigate('/login');
        } else {
            const savedCartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
            setCartItems(savedCartItems);
        }
    }, [navigate]);

    const handleRemoveItem = (id) => {
        const updatedItems = cartItems.filter(item => item.id !== id);
        setCartItems(updatedItems);
        localStorage.setItem('cartItems', JSON.stringify(updatedItems));
    };

    const handleQuantityChange = (id, quantity) => {
        const updatedItems = cartItems.map(item =>
            item.id === id ? { ...item, quantity } : item
        );
        setCartItems(updatedItems);
        localStorage.setItem('cartItems', JSON.stringify(updatedItems));
    };

    const handleApplyDiscount = () => {
        if (discountCode === 'DISCOUNT10') {
            setDiscount(0.1);
        } else {
            setDiscount(0);
        }
    };

    const subtotal = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    const shippingCost = 15;
    const total = subtotal - (subtotal * discount) + shippingCost;

    const handleCheckout = () => {
        navigate('/checkout');
    };

    return (
        <div className="cart-page">
            <h1>Shopping Cart</h1>
            <div className="top-info">
                <div className="info-item">• Special shipping price: 15 lei</div>
                <div className="info-item">• 14-day return period</div>
                <div className="info-item">• FAST delivery 24-48H</div>
            </div>
            <div className="cart-content">
                <div className="cart-items">
                    {cartItems.length === 0 ? (
                        <p>Your cart is empty</p>
                    ) : (
                        cartItems.map(item => (
                            <div className="cart-item" key={item.id}>
                                <img src={item.image} alt={item.name}/>
                                <div className="item-details">
                                    <h2>{item.name}</h2>
                                    <p>Nr. art.: {item.sku}</p>
                                    <p>Culoare: {item.color}</p>
                                    <p>Mărime: {item.size}</p>
                                    <p>Preț: {item.price} Lei</p>
                                    <div className="quantity-container">
                                        <input
                                            type="number"
                                            value={item.quantity}
                                            min="1"
                                            onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                                        />
                                    </div>
                                    <button onClick={() => handleRemoveItem(item.id)} className="remove-button">Șterge
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>
                <div className="cart-summary">
                    <div className="discount">
                        <input
                            type="text"
                            placeholder="Ai un cod de reducere?"
                            value={discountCode}
                            onChange={(e) => setDiscountCode(e.target.value)}
                        />
                        <button onClick={handleApplyDiscount}>Aplică Reducerea</button>
                    </div>
                    <div className="summary-details">
                        <p>Valoare comandă: {subtotal.toFixed(2)} Lei</p>
                        <p>Livrare: {shippingCost === 0 ? 'GRATUIT' : `${shippingCost} Lei`}</p>
                        {discount > 0 && <p>Discount: {discount * 100}%</p>}
                        <p><strong>Total: {total.toFixed(2)} Lei</strong></p>
                        <button onClick={handleCheckout} className="checkout-button">Continuă la pagina de finalizare a
                            comenzii
                        </button>
                    </div>
                    <div className="payment-info">
                        <p>Acceptăm</p>
                        <div className="payment-icons">
                            <i className="fab fa-cc-maestro"></i>
                            <i className="fab fa-cc-mastercard"></i>
                            <i className="fab fa-cc-visa"></i>
                            <i className="fas fa-gift"></i>
                        </div>
                        <p className="additional-info">
                            Prețurile și costurile de livrare nu sunt confirmate până când nu ajungeți la pagina de
                            finalizare a comenzii.
                        </p>
                        <p className="return-policy">
                            Detalii despre returnare în 30 zile și politica de anulare a comenzii. <a href="#">returnare
                            și rambursare</a>.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Cart;
