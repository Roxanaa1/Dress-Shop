import React, { useState } from 'react';
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

    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setAddress((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Address submitted:', address);
        // Poți adăuga logica pentru a salva adresa pe backend aici
    };

    const handleLogout = () => {
        localStorage.removeItem('isLoggedIn');
        navigate('/login');
    };

    return (
        <div className="account-container">
            <Sidebar handleLogout={handleLogout} />
            <div className="content">
                <div className="add-address-container">
                    <h2>ADD A NEW ADDRESS</h2>
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
