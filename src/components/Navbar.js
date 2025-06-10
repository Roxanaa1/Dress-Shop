import React, { useEffect, useState } from 'react';
import AdminNavbar from './AdminNavbar';
import UserNavbar from './UserNavbar';

const Navbar = () => {
    const [role, setRole] = useState(null);

    useEffect(() => {
        const savedRole = localStorage.getItem('role');
        setRole(savedRole);
    }, []);

    if (role === null) {
        return null;
    }

    return role === 'ADMIN' ? <AdminNavbar /> : <UserNavbar />;
};

export default Navbar;
