import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button, Card, Container, Row, Col } from 'react-bootstrap';
import '../styles/AdminProductGrid.css';

const AdminProductGrid = () => {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        fetch('http://localhost:8080/products/getAllProducts')
            .then((response) => response.json())
            .then((data) => {
                setProducts(data);
                setError('');
            })
            .catch((error) => {
                setError('Failed to load products');
                console.error('Error loading products:', error);
            });
    }, []);

    const handleDelete = async (productId) => {
        try {
            const response = await fetch(`http://localhost:8080/products/deleteProduct/${productId}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                setProducts(products.filter((product) => product.id !== productId));
            } else {
                console.error('Failed to delete product');
            }
        } catch (error) {
            console.error('Error deleting product:', error);
        }
    };

    return (
        <Container className="wishlist-container">
            <h2 className="my-4 text-center">Products Grid</h2>
            {error && <p className="text-danger text-center">{error}</p>}
            <Row className="wishlist-grid">
                {products.map((product) => (
                    <Col key={product.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
                        <div className="wishlist-item">
                            <img
                                src={product.productImages[0] || "https://via.placeholder.com/150"}
                                alt={product.name}
                                className="wishlist-image"
                            />
                            <div className="wishlist-details">
                                <h3>{product.name}</h3>
                                <p><strong>{product.price} Lei</strong></p>
                            </div>
                            <div className="d-flex justify-content-between">
                                <Link to={`/edit-product/${product.id}`} className="btn btn-warning">
                                    Edit
                                </Link>
                                <Button variant="danger" onClick={() => handleDelete(product.id)}>
                                    Delete
                                </Button>
                            </div>
                        </div>
                    </Col>
                ))}
            </Row>
        </Container>
    );
};

export default AdminProductGrid;
