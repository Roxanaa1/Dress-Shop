import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function ForgotPassword() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        document.body.className = 'forgot-password-page';
        return () => {
            document.body.className = '';
        };
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/users/forgot-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email })
            });

            const text = await response.text();

            if (response.ok) {
                setMessage(text);
                setTimeout(() => {
                    navigate('/ResetPassword');
                }, 2000);
            } else {
                throw new Error(text);
            }
        } catch (error) {
            setMessage(error.message || 'Error sending reset code.');
        }
    };

    return (
        <div className="login-container">
            <h2>Forgot Password</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <button type="submit">Send Reset Code</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
}

export default ForgotPassword;
