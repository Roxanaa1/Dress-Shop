import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col } from 'react-bootstrap';
import '../styles/AdminProductGrid.css';
import '../styles/Wishlist.css';

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
        <div className="admin-product-container">
            {error && <p className="text-danger text-center">{error}</p>}
            <div className="admin-product-grid">
                {products.map((product) => (
                    <div key={product.id} className="admin-product-grid-item">
                        <div className="admin-product-item">
                            <div className="admin-product-actions-top">
                                <Link to={`/edit-product/${product.id}`} className="btn btn-warning btn-sm me-2">
                                    <i className="fas fa-edit"></i>
                                </Link>
                                <button
                                    className="btn btn-danger btn-sm"
                                    onClick={() => handleDelete(product.id)}
                                    title="Delete"
                                >
                                    <i className="fas fa-trash"></i>
                                </button>
                            </div>
                            <img
                                src={product.productImages[0] || "https://via.placeholder.com/150"}
                                alt={product.name}
                                className="admin-product-image"
                            />
                            <div className="admin-product-details">
                                <h3>{product.name}</h3>
                                <p><strong>{product.price} Lei</strong></p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default AdminProductGrid;
