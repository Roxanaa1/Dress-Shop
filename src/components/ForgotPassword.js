import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/ForgotPassword.css';
import Sidebar from "./Sidebar";

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleResetPassword = async () => {

        try {

            const response = await fetch('http://localhost:8080/reset-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email }),
            });

            if (!response.ok) {
                throw new Error('Failed to send reset password email');
            }

            const data = await response.json();
            setMessage(data.message);
        } catch (error) {
            setMessage('Failed to send email. Please try again later.');
        }
    };

    return (
        <div className="forgot-password-page">

            <h1>Reset Your Password</h1>
            <p>Please enter your email address below to receive instructions for resetting your password.</p>
            <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter your email"
            />
            <button onClick={handleResetPassword}>Send Instructions</button>
            {message && <p>{message}</p>}
        </div>
    );
};

export default ForgotPassword;
