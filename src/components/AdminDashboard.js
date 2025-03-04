import React from "react";
import { Container, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import AdminProductGrid from "./AdminProductGrid";

const AdminDashboard = () => {
    return (
        <Container>
            <div className="d-flex justify-content-between align-items-center my-4">
                <h1>Admin Dashboard</h1>
                <Link to="/add-product">
                    <Button variant="primary">+ Create new</Button>
                </Link>
            </div>
            <AdminProductGrid />
        </Container>
    );
};

export default AdminDashboard;
