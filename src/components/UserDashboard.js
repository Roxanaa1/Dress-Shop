import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import '../styles/Home.css';

const UserDashboard = () => {
    const [dresses, setDresses] = useState([]);
    const [error, setError] = useState('');
    const [searchParams] = useSearchParams();
    const filter = searchParams.get('filter') || 'all';
    const searchText = searchParams.get('text') || '';
    const [sortOrder, setSortOrder] = useState(null);
    const [selectedSize, setSelectedSize] = useState('');
    const [selectedColor, setSelectedColor] = useState('');

    useEffect(() => {
        let url = searchText
            ? `http://localhost:8080/products/search?query=${searchText}`
            : filter === 'all'
                ? 'http://localhost:8080/products/getAllProducts'
                : `http://localhost:8080/products/getProductsByCategory?category=${filter}`;


        if (selectedSize) url += `&size=${selectedSize}`;
        if (selectedColor) url += `&color=${selectedColor}`;

        console.log('Fetching URL:', url);

        fetch(url)
            .then(response => {
                console.log('API Response:', response);
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Fetched Data:', data);
                setDresses(data);
                setError('');
            })
            .catch(error => {
                setError(`Failed to load dresses: ${error.message}`);
                console.error('Error loading dresses:', error);
            });
    }, [filter, searchText, selectedSize, selectedColor]);

    useEffect(() => {
        if (sortOrder) {
            const sortedDresses = [...dresses].sort((a, b) => sortOrder === 'asc' ? a.price - b.price : b.price - a.price);
            setDresses(sortedDresses);
        }
    }, [sortOrder]);

    const handleSortChange = (order) => {
        setSortOrder(order);
    };

    const handleSizeFilterChange = (size) => {
        setSelectedSize(size);
        setSortOrder(null);
    };

    const handleColorFilterChange = (color) => {
        setSelectedColor(color);
        setSortOrder(null);
    };

    return (
        <div className="Home">
            <main className="main-content">

                <div className="main-header">
                    <h1>DRESS COLLECTION</h1>
                    <div className="filter-sort-buttons">
                        <button onClick={() => handleSortChange('asc')}>ascending price</button>
                        <button onClick={() => handleSortChange('desc')}>descending price</button>
                        {/*<select value={selectedSize} onChange={(e) => handleSizeFilterChange(e.target.value)}>*/}
                        {/*    <option value="">size</option>*/}
                        {/*    <option value="XS">XS</option>*/}
                        {/*    <option value="S">S</option>*/}
                        {/*    <option value="M">M</option>*/}
                        {/*    <option value="L">L</option>*/}
                        {/*    <option value="XL">XL</option>*/}
                        {/*</select>*/}
                        {/*<select value={selectedColor} onChange={(e) => handleColorFilterChange(e.target.value)}>*/}
                        {/*    <option value="">color</option>*/}
                        {/*    <option value="Red">Roșu</option>*/}
                        {/*    <option value="Blue">Albastru</option>*/}
                        {/*    <option value="Black">Negru</option>*/}
                        {/*    <option value="White">Alb</option>*/}
                        {/*</select>*/}
                    </div>
                </div>

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

export default UserDashboard;
