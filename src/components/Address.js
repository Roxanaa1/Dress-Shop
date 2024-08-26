import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Sidebar from './Sidebar';
import '../styles/Address.css';

const Address = () => {
    const [address, setAddress] = useState({
        streetLine: '',
        postalCode: '',
        city: '',
        county: '',
        country: '',
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

        const fetchAddress = async () => {
            try {
                const response = await fetch(`http://localhost:8080/addresses/${userId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch address data.');
                }

                const data = await response.json();
                setAddress(data);
            } catch (err) {
                console.error('Failed to fetch address:', err);
                setError('Failed to load address. Please try again later.');
            }
        };

        fetchAddress();
    }, [navigate]);


    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setAddress((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userId = localStorage.getItem('userId');
        if (!userId) {
            console.error('No user ID found, cannot save address');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/addresses/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(address),
            });

            if (!response.ok) {
                throw new Error('Failed to update address.');
            }

            console.log('Address updated successfully');
            setError(null);
        } catch (err) {
            console.error('Failed to update address:', err);
            setError('Failed to update address. Please try again later.');
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('userId');
        navigate('/login');
    };

    return (
        <div className="account-container">
            <Sidebar handleLogout={handleLogout} />
            <div className="content">
                <div className="add-address-container">
                    <h2>ADD A NEW ADDRESS</h2>
                    {error && <p className="error-message">{error}</p>}
                    <form className="add-address-form" onSubmit={handleSubmit}>
                        <label>Street Address*</label>
                        <input
                            type="text"
                            name="streetLine"
                            value={address.streetLine}
                            onChange={handleInputChange}
                            required
                        />

                        <label>Postal Code*</label>
                        <input
                            type="text"
                            name="postalCode"
                            value={address.postalCode}
                            onChange={handleInputChange}
                            required
                        />

                        <label>City*</label>
                        <input
                            type="text"
                            name="city"
                            value={address.city}
                            onChange={handleInputChange}
                            required
                        />

                        <label>County*</label>
                        <input
                            type="text"
                            name="county"
                            value={address.county}
                            onChange={handleInputChange}
                            required
                        />

                        <label>Country*</label>
                        <input
                            type="text"
                            name="country"
                            value={address.country}
                            onChange={handleInputChange}
                            required
                        />

                        <button type="submit">Save Address</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Address;
