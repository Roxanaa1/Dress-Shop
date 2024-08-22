import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Navbar from './Navbar';
import '../styles/Home.css';

const Home = () => {
    const [dresses, setDresses] = useState([]);
    const [error, setError] = useState('');
    const [showSearchBar, setShowSearchBar] = useState(false); // State pt a controla vizibilitatea barei search
    const [searchTerm, setSearchTerm] = useState(''); // State pt a retine termenul de cautare
    const { filter } = useParams();

    useEffect(() =>
    {
        const url = filter === 'all' || !filter
            ? 'http://localhost:8080/products/getAllProducts'
            : `http://localhost:8080/products/getProductsByCategory?category=${filter}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data =>
            {
                setDresses(data);
            })
            .catch(error => {
                setError('Failed to load dresses');
            });
    }, [filter]);

    const handleSearchClick = () => {
        setShowSearchBar(!showSearchBar); // Toggle pt a afisa/ascunde bara de cautare
    };

    const handleSearch = () => {
        if (searchTerm.trim() !== '') {
            fetch(`http://localhost:8080/cart/search?query=${searchTerm}`)
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
                    setError('Failed to fetch search results');
                });
        }
    };
    return (
        <div className="Home">
            <Navbar onSearchClick={handleSearchClick} />
            <main className="main-content">
                <h1>DRESS COLLECTION</h1>
                {showSearchBar && ( // bara de cautare vizibila doar daca showSearchBar este true
                    <div className="search-bar">
                        <input
                            type="text"
                            placeholder="Search..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <button onClick={handleSearch}>Search</button>
                    </div>
                )}
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
