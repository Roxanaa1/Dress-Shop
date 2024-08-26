import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Account.css';

const Account = () => {
    const [user, setUser] = useState({
        email: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        password: ''
    });
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        if (!userId) {
            console.error('No user ID found, redirecting to login');
            navigate('/login');
            return;
        }

        const fetchData = async () => {
            try {
                const response = await fetch(`http://localhost:8080/users/user/${userId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
                const data = await response.json();

                if (!response.ok) {
                    throw new Error(data.message || 'Failed to fetch user data.');
                }

                setUser(data);
            } catch (err) {
                setError(err.message);
                console.error('Failed to fetch user data:', err);
            }
        };

        fetchData();
    }, [navigate]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUser(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSaveProfile = async () => {
        const userId = localStorage.getItem('userId');
        if (!userId) {
            console.error('No user ID found, cannot save data');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/users/user`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user)
            });
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Failed to update user data.');
            }

            console.log('Updated user data:', data);
        } catch (err) {
            setError(err.message);
            console.error('Failed to update user data:', err);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('userId');
        navigate('/login');
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
