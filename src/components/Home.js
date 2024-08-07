import React, { useState, useEffect } from 'react';
import '../styles/Home.css';

const Home = () => {
    const [dresses, setDresses] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        fetch('http://localhost:8080/products/getAllProducts')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => setDresses(data))
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
                setError('Failed to load dresses');
            });
    }, []);

    return (
        <div className="Home">
            <main className="main-content">
                <h1>Colectia de Rochii</h1>
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
