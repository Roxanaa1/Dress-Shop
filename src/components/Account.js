import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import UserNavbar from "./UserNavbar";
import AdminNavbar from "./AdminNavbar";
import '../styles/Account.css';

const Account = () => {
    const [user, setUser] = useState({
        email: '',
        firstName: '',
        lastName: '',
        phoneNumber: ''
    });
    const [error, setError] = useState(null);
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();
    const role = localStorage.getItem("role");

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        if (!userId) {
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

                if (!response.ok) throw new Error(data.message || 'Failed to fetch user data.');
                setUser(data);
            } catch (err) {
                setError(err.message);
                console.error('Fetch error:', err);
            }
        };

        fetchData();
    }, [navigate]);

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setUser(prev => ({...prev, [name]: value}));
    };

    const handleSaveProfile = async () => {
        const userId = localStorage.getItem('userId');
        if (!userId) return;

        try {
            const response = await fetch(`http://localhost:8080/users/user`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(user)
            });

            const data = await response.json();
            if (!response.ok) throw new Error(data.message || 'Failed to update user.');
            alert("Data has been updated successfully.!");
        } catch (err) {
            alert(err.message);
        }
    };

    const handleChangePassword = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            alert("The new passwords do not match.");
            return;
        }

        const userId = localStorage.getItem("userId");

        try {
            const response = await fetch(`http://localhost:8080/users/changePassword/${userId}`, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    oldPassword: currentPassword,
                    newPassword: newPassword
                })
            });

            if (response.ok) {
                alert("Password changed successfully!");
                setCurrentPassword('');
                setNewPassword('');
                setConfirmPassword('');
            } else {
                const data = await response.text();
                alert(data || "Error");
            }
        } catch (err) {
            alert("Error ");
            console.error(err);
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div>
            {role === "ADMIN" ? <AdminNavbar/> : <UserNavbar/>}

            <div className="account-container">
                <div className="sidebar">
                    <button className="sidebar-button" onClick={() => navigate('/Account')}>Personal Data</button>
                    <button className="sidebar-button" onClick={() => navigate('/Address')}>Addresses</button>
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
                        </div>

                        <div className="personal-info">
                            <h3>Change Password</h3>
                            <form onSubmit={handleChangePassword}>
                                <label>Current Password</label>
                                <input
                                    type="password"
                                    value={currentPassword}
                                    onChange={(e) => setCurrentPassword(e.target.value)}
                                    required
                                />
                                <label>New Password</label>
                                <input
                                    type="password"
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    required
                                />
                                <label>Confirm New Password</label>
                                <input
                                    type="password"
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    required
                                />
                                <button type="submit">Change Password</button>
                            </form>
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
        </div>
    );
};

export default Account;
