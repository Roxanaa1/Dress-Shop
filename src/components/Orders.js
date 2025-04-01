import React, { useEffect, useState } from 'react';
import '../styles/Orders.css';
import ProductModal from './ProductModal';

const Orders = () => {
    const [orders, setOrders] = useState([]);
    const [selectedOrder, setSelectedOrder] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/orders/getAllOrders')
            .then(res => res.json())
            .then(data => setOrders(data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="table-container">
            <h2 className="table-title">All Orders</h2>
            <table className="orders-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Client</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Date</th>
                    <th>Payment</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                {orders.map((order, index) => (
                    <tr key={index}>
                        <td>{order.orderId}</td>
                        <td>{order.firstName} {order.lastName}</td>
                        <td>{order.email}</td>
                        <td>{order.phone}</td>
                        <td>{order.orderDate}</td>
                        <td>{order.paymentMethod}</td>
                        <td>{order.totalPrice} RON</td>
                        <td>{order.orderStatus}</td>
                        <td>
                            <button className="details-btn" onClick={() => setSelectedOrder(order)}>
                                View
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {selectedOrder && (
                <ProductModal
                    order={selectedOrder}
                    onClose={() => setSelectedOrder(null)}
                />
            )}
        </div>
    );
};

export default Orders;
