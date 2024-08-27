import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Wishlist.css';
import Navbar from './Navbar';

const Wishlist = () => {
    const [wishlistItems, setWishlistItems] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        if (!userId) {
            alert("Trebuie sa fii logat pentru a vedea wishlist-ul.");
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
                        id: item.id,
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
                console.error('Eroare la preluarea wishlist-ului:', error);
                alert('Eroare la preluarea wishlist-ului.');
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
                    setWishlistItems(prevItems => prevItems.filter(item => item.id !== wishlistItemId));
                } else {
                    throw new Error('Failed to remove item');
                }
            })
            .catch(error => {
                console.error('Error removing item:', error);
                alert('Eroare ');
            });
    };

    return (
        <div>
            <Navbar />
            <div className="wishlist-container">
                <h2>WISHLIST</h2>
                {wishlistItems.length === 0 ? (
                    <p>Nu ai produse în wishlist.</p>
                ) : (
                    <div className="wishlist-grid">
                        {wishlistItems.map(item => (
                            <div key={item.id} className="wishlist-item">
                                <img src={item.image} alt={item.name} className="wishlist-image" onClick={() => handleProductClick(item.id)} />
                                <div className="wishlist-details">
                                    <h3>{item.name}</h3>
                                    <p>{item.price}</p>
                                </div>
                                <button className="wishlist-remove-button" onClick={() => handleRemoveClick(item.id)}>
                                    <i className="fas fa-trash"></i>
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Wishlist;
