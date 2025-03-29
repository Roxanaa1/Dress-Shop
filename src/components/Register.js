import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Register.css';

function Register() {
    const [userData, setUserData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        password: '',
        birthDate: '',
        role: 'USER'
    });

    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        setUserData({ ...userData, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        document.body.classList.add('register-page');
        document.documentElement.classList.add('register-page');
        document.getElementById('root')?.classList.add('register-page');

        return () => {
            document.body.classList.remove('register-page');
            document.documentElement.classList.remove('register-page');
            document.getElementById('root')?.classList.remove('register-page');
        };
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData)
            });

            if (!response.ok) {
                throw new Error('Register failed.');
            }

            const result = await response.json();
            setSuccess('Te rugăm să-ți verifici emailul pentru a finaliza procesul de înregistrare.');
            setError(null);

            localStorage.setItem('email', userData.email);
            localStorage.setItem('role', userData.role);
            localStorage.setItem('isLoggedIn', 'true');

            navigate(`/verify?email=${userData.email}`);
        } catch (err) {
            setError(err.message);
            setSuccess(null);
        }
    };

    return (
        <div className="register-background">
            <div className="register-form">
                <h2>Register</h2>
                <form onSubmit={handleSubmit} noValidate>
                    <div>
                        <label htmlFor="firstName">First Name:</label>
                        <input
                            type="text"
                            id="firstName"
                            name="firstName"
                            value={userData.firstName}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="lastName">Last Name:</label>
                        <input
                            type="text"
                            id="lastName"
                            name="lastName"
                            value={userData.lastName}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={userData.email}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="phoneNumber">Phone Number:</label>
                        <input
                            type="text"
                            id="phoneNumber"
                            name="phoneNumber"
                            value={userData.phoneNumber}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="birthDate">Date of Birth:</label>
                        <input
                            type="date"
                            id="birthDate"
                            name="birthDate"
                            value={userData.birthDate}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={userData.password}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="role">Role:</label>
                        <select
                            id="role"
                            name="role"
                            value={userData.role}
                            onChange={handleInputChange}
                            required
                        >
                            <option value="USER">User</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                    </div>
                    <button type="submit">Register</button>
                </form>
                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message">{success}</p>}
            </div>
        </div>
    );
}

export default Register;
