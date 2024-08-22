import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import Navbar from './Navbar';
import '../styles/Search.css';

const Search = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [dresses, setDresses] = useState([]);
    const [error, setError] = useState('');
    const [searchParams] = useSearchParams();
    const searchText = searchParams.get('text') || '';

    useEffect(() => {
        if (searchText) {
            fetch(`http://localhost:8080/products/search?query=${searchText}`)
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
                            image: item.productImages?.[0] || 'https://via.placeholder.com/150',
                            name: item.name || 'N/A',
                            price: item.price !== undefined ? `${item.price} RON` : 'N/A',
                        }));
                        setDresses(mappedItems);
                    } else {
                        setDresses([]);
                    }
                })
                .catch(error => {
                    setError('Failed to fetch search results');
                    console.error('Error fetching search results:', error);
                });
        }
    }, [searchText]);

    const handleProductClick = (productId) => {
        window.location.href = `/ProductDetails/${productId}`;
    };

    return (
        <div>
            <Navbar />
            <div className="wishlist-container">
                <h2>SEARCH RESULTS</h2>
                {error && <p className="error">{error}</p>}
                <div className="wishlist-grid">
                    {dresses.length === 0 ? (
                        <p>No results found</p>
                    ) : (
                        dresses.map((dress) => (
                            <div key={dress.id} className="wishlist-item" onClick={() => handleProductClick(dress.id)}>
                                <img
                                    src={dress.image}
                                    alt={dress.name}
                                    className="wishlist-image"
                                />
                                <div className="wishlist-details">
                                    <h3>{dress.name}</h3>
                                    <p>{dress.price}</p>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default Search;
