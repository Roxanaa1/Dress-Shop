import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';

function Login() {
    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        document.body.classList.add('login-page');
        return () => {
            document.body.classList.remove('login-page');
        };
    }, []);

    const handleInputChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleLogin = async (event) => {
        event.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials)
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Login failed.');
            }

            console.log('Login response data:', data);
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('cartId', data.cartId);
            localStorage.setItem('role', data.role);
            localStorage.setItem('email', data.email);
            console.log('Login successful:', data);
            localStorage.setItem('userId', data.id);

            setSuccess('Login successful!');
            setError(null);

            const userRole = localStorage.getItem('role');
            const isLoggedIn = localStorage.getItem('isLoggedIn');

            if (isLoggedIn && userRole === 'ADMIN') {
                navigate('/admin-dashboard');
            } else if (isLoggedIn && userRole === 'USER') {
                navigate('/user-dashboard');
            }

        } catch (err) {
            setError(err.message);
            setSuccess(null);
        }
    };

    const handleRegisterRedirect = () => {
        navigate('/register');
    };

    return (
        <div className="login-page">
            <div className="login-container">
                <form onSubmit={handleLogin}>
                    <h2>Login</h2>
                    {error && <p className="error">{error}</p>}
                    <div>
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            required
                            value={credentials.email}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            required
                            value={credentials.password}
                            onChange={handleInputChange}
                        />
                    </div>
                    <button type="submit">Login</button>
                    <div className="register-link">
                        <p>You don't have an account? <button onClick={handleRegisterRedirect}>Sign up</button></p>
                    </div>
                    <div className="forgot-password">
                        <button className="forgot-password" onClick={() => navigate('/ForgotPassword')}>
                            Forgot Password???
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Login;
