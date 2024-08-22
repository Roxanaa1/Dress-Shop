import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import Navbar from './Navbar';
import '../styles/Home.css';

const Home = () => {
    const [dresses, setDresses] = useState([]);
    const [error, setError] = useState('');
    const [searchParams] = useSearchParams();
    const filter = searchParams.get('filter') || 'all';
    const searchText = searchParams.get('text') || '';

    useEffect(() => {
        const url = searchText
            ? `http://localhost:8080/products/search?query=${searchText}`
            : filter === 'all'
                ? 'http://localhost:8080/products/getAllProducts'
                : `http://localhost:8080/products/getProductsByCategory?category=${filter}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setDresses(data);
            })
            .catch(error => {
                setError('Failed to load dresses');
                console.error('Error loading dresses:', error);
            });
    }, [filter, searchText]);

    return (
        <div className="Home">
            <Navbar />
            <main className="main-content">
                <h1>DRESS COLLECTION</h1>

                {error && <p className="error">{error}</p>}
                <div className="image-gallery">
                    {dresses.map((dress) => (
                        <div key={dress.id} className="image-wrapper">
                            <a href={`/ProductDetails/${dress.id}`}>
                                <img src={dress.productImages[0]} alt={dress.name} />
                            </a>
                            <div className="dress-info">
                                <p className="dress-name">{dress.name}</p>
                                <p className="dress-price">{`${dress.price} Lei`}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </main>
        </div>
    );
};

export default Home;
