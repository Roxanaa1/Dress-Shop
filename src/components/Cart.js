import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Cart.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const Cart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [cart, setCart] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);
    const [cartEntry, setCartEntry] = useState([]);
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

    useEffect(() => {
        const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
        setIsLoggedIn(loggedIn);

        if (!loggedIn) {
            alert("Trebuie sa fii logat pentru a accesa cosul de cumparaturi.");
            navigate('/login');
        } else {
            const cartId = localStorage.getItem('cartId');
            if (cartId) {
                fetch(`http://localhost:8080/cart/getCartById/${cartId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.cartEntries) {
                            const mappedItems = data.cartEntries.map(entry => ({
                                id: entry.id,
                                image: entry.product.productImages[0],
                                name: entry.product.name,
                                color: entry.product.productAttributeAttributeValues.find(attr => attr.productAttribute.name === 'color')?.attributeValue.value || 'Culoare indisponibilă',
                                size: entry.product.productAttributeAttributeValues.find(attr => attr.productAttribute.name === 'size')?.attributeValue.value || 'Mărime indisponibilă',
                                price: entry.pricePerPiece,
                                quantity: entry.quantity,
                                totalPricePerEntry: entry.totalPricePerEntry
                            }));
                            setCartItems(mappedItems);
                        } else {
                            setCartItems([]);
                        }
                    })
                    .catch(error => {
                        console.error('Error fetching cart:', error);
                    });
            }
        }
    }, [navigate]);

    const handleRemoveItem = (id) => {
        fetch(`http://localhost:8080/cart/removeItem/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    const updatedItems = cartItems.filter(item => item.id !== id);
                    setCartItems(updatedItems);
                } else {
                    console.error('Failed to remove item from cart');
                }
            })
            .catch(error => {
                console.error('Error removing item:', error);
            });
    };
    const handleSaveDeliveryDetails = () => {
        const userId = localStorage.getItem('userId');

        fetch(`http://localhost:8080/users/addresses/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(deliveryDetails)
        })
            .then(response => {
                console.log('Response from server:', response);
                if (!response.ok) {
                    throw new Error('Failed to save delivery details');
                }
                return response.json();
            })
            .then(data => {
                console.log('Server returned data:', data);
                alert('Delivery details saved successfully!');
            })
            .catch(error => {
                console.error('Error saving delivery details:', error);
                alert('Failed to save delivery details');
            });
    };



    const handleQuantityChange = (id, quantity) => {
        if (quantity < 1) {
            return;
        }

        fetch(`http://localhost:8080/cart/updateQuantity/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ quantity })
        })
            .then(response => {
                if (response.ok) {
                    const updatedItems = cartItems.map(item =>
                        item.id === id ? { ...item, quantity } : item
                    );
                    setCartItems(updatedItems);
                } else {
                    console.error('Failed to update item quantity');
                }
            })
            .catch(error => {
                console.error('Error updating item quantity:', error);
            });
    };

    const handleApplyDiscount = () => {
        if (discountCode === 'DISCOUNT10') {
            setDiscount(0.1);
        } else {
            setDiscount(0);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setDeliveryDetails(prevDetails => ({
            ...prevDetails,
            [name]: value,
        }));
    };

    const handlePaymentChange = (e) => {
        setPaymentMethod(e.target.value);
    };

    const subtotal = cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    const shippingCost = 15;
    const total = subtotal - (subtotal * discount) + shippingCost;

    const handleCheckout = () => {
        if (cartItems.length === 0) {
            alert("Coșul tău este gol!");
            return;
        }

        const userId = localStorage.getItem('userId');
        const cartId = localStorage.getItem('cartId');

        const orderDetails = {
            userId: parseInt(userId, 10),
            cartId: parseInt(cartId, 10),
            paymentMethod: paymentMethod.toUpperCase(),
            deliveryAddress: deliveryDetails.deliveryAddressId || parseInt(localStorage.getItem('defaultDeliveryAddress'), 10),
            invoiceAddress: deliveryDetails.invoiceAddressId || parseInt(localStorage.getItem('defaultBillingAddress'), 10),
            totalPrice: total,
            orderDate: new Date().toISOString().split('T')[0]
        };

        fetch('http://localhost:8080/orders/createOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(orderDetails)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Eroare la plasarea comenzii');
                }
            })
            .then(data => {
                console.log('Comanda a fost plasată cu succes:', data);
                clearCart();

                navigate('/OrderSuccess');

            })
            .catch(error => {
                console.error('Eroare la plasarea comenzii:', error);
                alert('Eroare la plasarea comenzii: ' + error.message);
            });
    };
    const clearCart = () => {
        setCartItems([]);
        setCart([]);
        setTotalPrice(0);
        setCartEntry([]);
    };

    useEffect(() => {
        console.log('Cart updated:', cart);
        console.log('Cart items updated:', cartItems);
        console.log('Total price updated:', totalPrice);
        console.log('Cart entry updated:', cartEntry);
    }, [cart, cartItems, totalPrice, cartEntry]);


    return (
        <div className="cart-page">
            <h1>SHOPPING CART</h1>
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
                                    <p>Color: {item.color}</p>
                                    <p>Size: {item.size}</p>
                                    <p>Price: {item.price} Lei</p>
                                    <div className="quantity-container">
                                        <input
                                            type="number"
                                            value={item.quantity}
                                            min="1"
                                            onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                                        />
                                    </div>
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
                    <div className="form-group">
                        <label>First Name:</label>
                        <input
                            type="text"
                            name="firstName"
                            value={deliveryDetails.firstName}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Last Name:</label>
                        <input
                            type="text"
                            name="lastName"
                            value={deliveryDetails.lastName}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Phone Number:</label>
                        <input
                            type="text"
                            name="phone"
                            value={deliveryDetails.phone}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Country:</label>
                        <input
                            type="text"
                            name="country"
                            value={deliveryDetails.country}
                            onChange={handleInputChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>County:</label>
                        <input
                            type="text"
                            name="county"
                            value={deliveryDetails.county}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>City:</label>
                        <input
                            type="text"
                            name="city"
                            value={deliveryDetails.city}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Address:</label>
                        <input
                            type="text"
                            name="address"
                            value={deliveryDetails.address}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Street Line:</label>
                        <input
                            type="text"
                            name="streetLine"
                            value={deliveryDetails.streetLine}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="form-group">
                        <label>Postal Code:</label>
                        <input
                            type="text"
                            name="postalCode"
                            value={deliveryDetails.postalCode}
                            onChange={handleInputChange}
                        />
                    </div>
                    <button onClick={handleSaveDeliveryDetails} className="save-delivery-button">
                        Save
                    </button>


                </div>

                <div className="payment-summary-container">
                    <div className="payment-method">
                        <h3>Payment method</h3>
                        <label>
                        <input
                                type="radio"
                                value="CASH"
                                checked={paymentMethod === 'CASH'}
                                onChange={handlePaymentChange}
                            />
                            Plata la livrare (Ramburs)
                        </label>
                        <label>
                            <input
                                type="radio"
                                value="online"
                                checked={paymentMethod === 'online'}
                                onChange={handlePaymentChange}
                            />
                            Plata online
                        </label>
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
                        </div>
                        <button onClick={handleCheckout} className="checkout-button">Trimite Comanda</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Cart;
