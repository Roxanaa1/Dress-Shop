import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Account.css';

const Account = () => {
    const [user, setUser] = useState({
        name: "Jane Doe",
        firstName: '',
        lastName: '',
        email: 'jane.doe@example.com',
        phoneNumber: '',
        password: '',
        address: "123 Fashion Ave",
        city: "New York",
        postalCode: "10001",
    });

    const navigate = useNavigate();

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
        console.log('Saved user data:', user);
    };

    return (
        <div className="account-container">
            <div className="sidebar">
                <button className="sidebar-button" onClick={() => navigate('/Account')}>Personal Data</button>
                <button className="sidebar-button" onClick={() => navigate('/Address')}>Addresses</button>
                <button className="sidebar-button" onClick={() => navigate('/Wishlist')}>Wishlist</button>
                <button className="sidebar-button" onClick={handleLogout}>Logout</button>
            </div>

            <div className="content">
                <div className="personal-data-container">
                    <h2>PERSONAL DATA</h2>
                    <div className="personal-info">
                        <h3>Identification Data</h3>
                        <label>Email</label>
                        <input
                            type="email"
                            name="email"
                            value={user.email}
                            onChange={handleInputChange}
                            required
                        />

                        <label>Password</label>
                        <input
                            type="password"
                            name="password"
                            value={user.password}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div className="personal-info">
                        <h3>Your Details</h3>
                        <label>First Name</label>
                        <input
                            type="text"
                            name="firstName"
                            value={user.firstName}
                            onChange={handleInputChange}
                            required
                        />

                        <label>Last Name</label>
                        <input
                            type="text"
                            name="lastName"
                            value={user.lastName}
                            onChange={handleInputChange}
                            required
                        />

                        <label>Phone Number</label>
                        <input
                            type="text"
                            name="phoneNumber"
                            value={user.phoneNumber}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <button onClick={handleSaveProfile}>Save Data</button>
                </div>
            </div>
        </div>
    );
};

export default Account;
