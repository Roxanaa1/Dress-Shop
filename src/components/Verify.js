import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../styles/Verify.css';

function Verify() {
    const [verificationCode, setVerificationCode] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const emailFromUrl = queryParams.get('email');
        if (emailFromUrl) {
            setEmail(emailFromUrl);
        }
    }, [location.search]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/users/verify', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email: email, code: verificationCode })
            });

            if (!response.ok) {
                const result = await response.text();
                throw new Error(result);
            }

            const result = await response.text();
            setSuccess(result);
            setError(null);

            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError(err.message);
            setSuccess(null);
        }
    };

    return (
        <div className="verify-page">
            {/* Titlul este acum în afara containerului */}
            <h2 className="verify-title">Verify Your Account</h2>
            <div className="verify-container">
                {success && <p className="success-message">{success}</p>}
                {error && <p className="error-message">{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Email:</label>
                        <input type="email" value={email} disabled />
                    </div>
                    <div>
                        <label htmlFor="verificationCode">Verification Code:</label>
                        <input
                            type="text"
                            id="verificationCode"
                            value={verificationCode}
                            onChange={(e) => setVerificationCode(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit">Verify</button>
                </form>
            </div>
        </div>
    );
}

export default Verify;
