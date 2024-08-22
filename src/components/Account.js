import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Account.css';

const Account = () => {
    const [user, setUser] = useState({
        name: "Jane Doe",
        email: "jane.doe@example.com",
        address: "123 Fashion Ave",
        city: "New York",
        postalCode: "10001",
    });
    const [orders, setOrders] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchedOrders = [
            { id: 1, date: "2024-08-01", total: "$120", status: "Delivered" },
            { id: 2, date: "2024-07-20", total: "$89", status: "In transit" }
        ];

        setOrders(fetchedOrders);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('isLoggedIn');
        navigate('/login');
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUser(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSaveProfile = () => {
        alert('Profile updated successfully!');
    };

    return (
        <div className="account-container">
            <div className="profile-section">
                <h2>Edit Profile</h2>
                <form className="profile-form">
                    <label>Name:</label>
                    <input type="text" name="name" value={user.name} onChange={handleInputChange} />

                    <label>Email:</label>
                    <input type="email" name="email" value={user.email} onChange={handleInputChange} />

                    <label>Address:</label>
                    <input type="text" name="address" value={user.address} onChange={handleInputChange} />

                    <label>City:</label>
                    <input type="text" name="city" value={user.city} onChange={handleInputChange} />

                    <label>Postal Code:</label>
                    <input type="text" name="postalCode" value={user.postalCode} onChange={handleInputChange} />

                    <button type="button" className="save-button" onClick={handleSaveProfile}>Save Profile</button>
                </form>
            </div>
            <div className="orders-section">
                <h2>Your Orders</h2>
                {orders.length > 0 ? (
                    <table className="orders-table">
                        <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Date</th>
                            <th>Total</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orders.map(order => (
                            <tr key={order.id}>
                                <td>{order.id}</td>
                                <td>{order.date}</td>
                                <td>{order.total}</td>
                                <td>{order.status}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>You have no orders.</p>
                )}
            </div>
            <div className="logout-section">
                <button onClick={handleLogout} className="logout-button">Logout</button>
            </div>
        </div>
    );
};

export default Account;
