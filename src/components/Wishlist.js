import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import '../styles/Wishlist.css';
import Navbar from './Navbar';

const Wishlist = () => {
    const [wishlistItems, setWishlistItems] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        if (!userId) {
            alert("You must be logged in to view your wishlist.");
            navigate('/login');
            return;
        }

        fetch(`http://localhost:8080/wishlist/user/${userId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.length > 0) {
                    const mappedItems = data.map(item => ({
                        wishlistItemId: item.id,
                        productId: item.productDTO?.id,
                        image: item.productDTO?.productImages?.[0] || 'https://via.placeholder.com/150',
                        name: item.productDTO?.name || 'N/A',
                        price: item.productDTO?.price !== undefined ? `${item.productDTO.price} RON` : 'N/A',
                    }));
                    setWishlistItems(mappedItems);
                } else {
                    setWishlistItems([]);
                }
            })
            .catch(error => {
                console.error('Error fetching wishlist:', error);
                alert('Error fetching wishlist.');
            });
    }, [navigate]);

    const handleProductClick = (productId) => {
        navigate(`/ProductDetails/${productId}`);
    };

    const handleRemoveClick = (wishlistItemId) => {
        fetch(`http://localhost:8080/wishlist/removeItem/${wishlistItemId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    setWishlistItems(prevItems => prevItems.filter(item => item.wishlistItemId !== wishlistItemId));
                } else {
                    throw new Error('Failed to remove item');
                }
            })
            .catch(error => {
                console.error('Error removing item:', error);
                alert('Error while removing item');
            });
    };

    return (
        <div>
            <Navbar/>
            <div className="wishlist-container">
                <h2>WISHLIST</h2>
                {wishlistItems.length === 0 ? (
                    <p>Your wishlist is empty.</p>
                ) : (
                    <div className="wishlist-grid">
                        {wishlistItems.map(item => (
                            <div key={item.wishlistItemId} className="wishlist-item">
                                <img
                                    src={item.image}
                                    alt={item.name}
                                    className="wishlist-image"
                                    onClick={() => handleProductClick(item.productId)}
                                />
                                <div className="wishlist-details">
                                    <h3>{item.name}</h3>
                                    <p>{item.price}</p>
                                </div>
                                <div className="wishlist-actions">
                                    <button
                                        className="wishlist-remove-button"
                                        onClick={() => handleRemoveClick(item.wishlistItemId)}
                                    >
                                        <i className="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Wishlist;
