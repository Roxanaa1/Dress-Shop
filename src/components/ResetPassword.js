import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function ResetPassword() {
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/users/reset-password', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, code, newPassword })
            });

            const data = await response.text();
            setMessage(data);
            if (response.ok) {
                setTimeout(() => navigate('/login'), 2000);
            }
        } catch (error) {
            setMessage("Error resetting password.");
        }
    };

    return (
        <div className="login-page">
            <div className="login-container">
                <h2>Reset Password</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="email"
                        placeholder="Your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Reset code"
                        value={code}
                        onChange={(e) => setCode(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="New password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        required
                    />
                    <button type="submit">Reset Password</button>
                </form>
                {message && <p>{message}</p>}
            </div>
        </div>
    );
}

export default ResetPassword;
