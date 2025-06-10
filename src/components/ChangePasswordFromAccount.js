import React, { useState } from 'react';

const ChangePasswordFromAccount = () => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');

    const userId = localStorage.getItem("userId");

    const handleChangePassword = async () => {
        if (newPassword !== confirmPassword) {
            setMessage("Parolele noi nu coincid.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/users/changePassword/${userId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    oldPassword,
                    newPassword
                }),
            });

            const text = await response.text();
            if (response.ok) {
                setMessage("Parola a fost schimbată cu succes!");
                setOldPassword('');
                setNewPassword('');
                setConfirmPassword('');
            } else {
                setMessage(text);
            }
        } catch (error) {
            setMessage("Eroare la schimbarea parolei.");
        }
    };

    return (
        <div className="forgot-password-page">
            <h1>Change Password</h1>
            <input
                type="password"
                value={oldPassword}
                onChange={(e) => setOldPassword(e.target.value)}
                placeholder="Old Password"
            />
            <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                placeholder="New Password"
            />
            <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Confirm New Password"
            />
            <button onClick={handleChangePassword}>Change Password</button>
            {message && <p>{message}</p>}
        </div>
    );
};

export default ChangePasswordFromAccount;
