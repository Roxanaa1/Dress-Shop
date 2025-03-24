import React from "react";
import { Container } from "react-bootstrap";
import AdminProductGrid from "./AdminProductGrid";

const AdminDashboard = () => {
    return (
        <div className="admin-dashboard-wrapper">
            <Container style={{ marginTop: '100px' }}>
                <div style={{ marginTop: '-100px' }}>
                    <div style={{ transform: 'scale(0.9)', marginTop: '-20px' }}>
                        <AdminProductGrid />
                    </div>
                </div>
            </Container>
        </div>
    );
};

export default AdminDashboard;
