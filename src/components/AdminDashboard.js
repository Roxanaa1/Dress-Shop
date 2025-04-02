import React from "react";
import { Container } from "react-bootstrap";
import AdminProductGrid from "./AdminProductGrid";

const AdminDashboard = () => {
    return (
        <div className="admin-dashboard-wrapper" style={{paddingTop: "70px"}}>
            <Container>
                <AdminProductGrid/>
            </Container>
        </div>
    );
};

export default AdminDashboard;
