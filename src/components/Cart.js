import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import '../styles/Cart.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const Cart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);
    const [discountCode, setDiscountCode] = useState('');
    const [discount, setDiscount] = useState(0);
    const [deliveryDetails, setDeliveryDetails] = useState({
        firstName: '',
        lastName: '',
        phone: '',
        country: '',
        county: '',
        city: '',
        address: '',
        streetLine: '',
        postalCode: '',
        deliveryAddressId: null,
        invoiceAddressId: null
    });
    const [paymentMethod, setPaymentMethod] = useState('CASH');
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isProcessing, setIsProcessing] = useState(false);

    useEffect(() => {
        const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
        setIsLoggedIn(loggedIn);

        if (!loggedIn) {
            alert("You must be logged in to access the shopping cart.");
            navigate('/login');
        } else {
            const cartId = localStorage.getItem('cartId');
            if (cartId) {
                fetch(`http://localhost:8080/cart/getCartById/${cartId}`)
                    .then(res => res.json())
                    .then(data => {
                        const mapped = data.cartEntries.map(entry => ({
                            id: entry.id,
                            image: entry.product.productImages[0],
                            name: entry.product.name,
                            color: entry.product.productAttributeAttributeValues.find(a => a.productAttribute.name === 'color')?.attributeValue.value || 'N/A',
                            size: entry.product.productAttributeAttributeValues.find(a => a.productAttribute.name === 'size')?.attributeValue.value || 'N/A',
                            price: entry.pricePerPiece,
                            quantity: entry.quantity,
                        }));
                        setCartItems(mapped);
                        localStorage.setItem('cartItems', JSON.stringify(mapped));
                    })
                    .catch(err => console.error('Error loading cart:', err));
            }
        }
    }, [navigate]);

    const handleRemoveItem = (entryId) => {
        fetch(`http://localhost:8080/cart/removeItem/${entryId}`, {
            method: 'DELETE'
        })
            .then(res => {
                if (!res.ok) throw new Error("Error deleting product");
                setCartItems(prev => prev.filter(item => item.id !== entryId));
            })
            .catch(err => {
                console.error("Delete error:", err);
                alert("Failed to remove product from cart.");
            });
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setDeliveryDetails(prev => ({...prev, [name]: value}));
    };

    const handleSaveDeliveryDetails = () => {
        const userId = localStorage.getItem('userId');
        fetch(`http://localhost:8080/users/addresses/${userId}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(deliveryDetails)
        })
            .then(res => res.json())
            .then(() => alert('Delivery details saved successfully!'))
            .catch(() => alert('Failed to save delivery details'));
    };

    const handleStripeCheckout = async () => {
        const requestData = {
            productNames: cartItems.map(item => item.name),
            prices: cartItems.map(item => item.price * 100),
            quantities: cartItems.map(item => item.quantity)
        };

        try {
            const res = await fetch("http://localhost:8080/payment/create-checkout-session", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(requestData)
            });
            if (!res.ok) throw new Error("Stripe error");
            const sessionUrl = await res.text();
            localStorage.setItem('paymentMethod', 'ONLINE');
            window.location.href = sessionUrl;
        } catch (err) {
            console.error(err);
            alert("Online payment error");
        }
    };

    const subtotal = cartItems.reduce((acc, item) => acc + item.price * item.quantity, 0);
    const shippingCost = 15;
    const total = subtotal - (subtotal * discount) + shippingCost;

    const handlePaymentChange = (e) => setPaymentMethod(e.target.value);
    const handleApplyDiscount = () => setDiscount(discountCode === 'DISCOUNT10' ? 0.1 : 0);

    const handleCheckout = () => {
        if (isProcessing) return;
        setIsProcessing(true);

        if (paymentMethod === 'online') {
            handleStripeCheckout();
        } else if (paymentMethod === 'CASH') {
            localStorage.setItem('paymentMethod', 'CASH');
            navigate('/success');
        }
    };

    return (
        <div className="cart-page">
            <h1>SHOPPING CART</h1>
            <div className="top-info">
                <div className="info-item">• Special shipping price: 15 RON</div>
                <div className="info-item">• 14-day return policy</div>
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
                                    <p>Color: {item.color}</p>
                                    <p>Size: {item.size}</p>
                                    <p>Price: {item.price} RON</p>
                                    <input
                                        type="number"
                                        value={item.quantity}
                                        min="1"
                                        onChange={(e) =>
                                            setCartItems(prev =>
                                                prev.map(p =>
                                                    p.id === item.id
                                                        ? {...p, quantity: parseInt(e.target.value)}
                                                        : p
                                                )
                                            )
                                        }
                                    />
                                </div>
                                <button className="remove-button" onClick={() => handleRemoveItem(item.id)}>
                                    <i className="fas fa-trash"></i>
                                </button>
                            </div>
                        ))
                    )}
                </div>
            </div>

            <div className="delivery-payment-container">
                <div className="delivery-details">
                    <h3>Delivery details</h3>
                    {Object.keys(deliveryDetails)
                        .filter(key => key !== 'deliveryAddressId' && key !== 'invoiceAddressId')
                        .map((key, index) => (
                            <div className="form-group" key={index}>
                                <label>{key.charAt(0).toUpperCase() + key.slice(1)}:</label>
                                <input type="text" name={key} value={deliveryDetails[key]}
                                       onChange={handleInputChange}/>
                            </div>
                        ))}
                    <button onClick={handleSaveDeliveryDetails} className="save-delivery-button">Save</button>
                </div>

                <div className="payment-summary-container">
                    <div className="payment-method">
                        <h3>Payment method</h3>
                        <label>
                            <input type="radio" value="CASH" checked={paymentMethod === 'CASH'}
                                   onChange={handlePaymentChange}/>
                            Cash on delivery
                        </label>
                        <label>
                            <input type="radio" value="online" checked={paymentMethod === 'online'}
                                   onChange={handlePaymentChange}/>
                            Online payment
                        </label>
                    </div>

                    <div className="cart-summary">
                        <div className="discount">
                            <input type="text" placeholder="Have a discount code?" value={discountCode}
                                   onChange={(e) => setDiscountCode(e.target.value)}/>
                            <button onClick={handleApplyDiscount}>Apply Discount</button>
                        </div>
                        <div className="summary-details">
                            <p>Subtotal: {subtotal.toFixed(2)} RON</p>
                            <p>Shipping: {shippingCost} RON</p>
                            {discount > 0 && <p>Discount: {discount * 100}%</p>}
                            <p><strong>Total: {total.toFixed(2)} RON</strong></p>
                        </div>
                        <button onClick={handleCheckout} className="checkout-button">
                            {paymentMethod === 'online' ? 'Pay Online' : 'Place Order'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Cart;
