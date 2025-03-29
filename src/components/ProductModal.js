import React from 'react';
import '../styles/ProductModal.css';

const ProductModal = ({ order, onClose }) => {
    if (!order) return null;

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <h3>Products for Order #{order.orderId}</h3>
                <ul className="modal-product-list">
                    {order.products.map((product, i) => (
                        <li key={i} className="modal-product-item">
                            <div className="modal-product-info">
                                <div className="modal-product-text">
                                    <p><strong>Name:</strong> {product.name}</p>
                                    <p><strong>Category:</strong> {product.category}</p>
                                    <p><strong>Description:</strong> {product.description}</p>
                                    <p><strong>Price:</strong> {product.price} RON</p>
                                </div>
                                <img src={product.imageUrl} alt={product.name} className="modal-product-image" />
                            </div>
                        </li>
                    ))}
                </ul>
                <button className="modal-close-btn" onClick={onClose}>Close</button>
            </div>
        </div>
    );
};

export default ProductModal;
