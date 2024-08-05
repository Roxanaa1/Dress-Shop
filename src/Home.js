import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import './Home.css';

const localDresses = [
    { src: '/images/Dress1.png', name: 'ROCHIE MIDI ELEGANTA CU FUNDE DECORATIVE PE UMERI', price: '200 Lei', id: 1, description: 'Rochie de seara eleganta' },
    { src: '/images/Dress2.png', name: 'ROCHIE CU MANECI SCURTE BRODATE CU PERLE', price: '150 Lei', id: 2, description: 'Rochie de zi eleganta' },
    { src: '/images/Dress3.png', name: 'ROCHIE CU DECOLTEU PATRAT SI PERLE', price: '180 Lei', id: 3, description: 'Rochie de zi' },
    { src: '/images/Dress4.png', name: 'ROCHIE LIME CU FUNDA AMPLA IN CONTRAST', price: '150 Lei', id: 4, description: 'Rochie de zi' },
    { src: '/images/Dress5.png', name: 'ROCHIE CASUAL MIDI EVAZATA CU UMERI GOI', price: '200 Lei', id: 5, description: 'Rochie de zi casual' },
    { src: '/images/Dress6-1.png', name: 'ROCHIE CU SPATELE DECUPAT ', price: '180 Lei', id: 6, description: 'Rochie de seara' }
];

const Home = () => {
    const [dresses, setDresses] = useState(localDresses);
    const [error, setError] = useState(null);
    const location = useLocation();
    const filter = new URLSearchParams(location.search).get('filter') || 'all';

    useEffect(() => {
        const fetchFilteredDresses = async () => {
            try {
                const response = await fetch(`http://localhost:8080/products/filter?filter=${filter}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch filtered dresses');
                }

                const data = await response.json();
                setDresses([...localDresses, ...data]);
                setError(null);
            } catch (err) {
                setError(err.message);
                setDresses(localDresses); // fallback to local dresses if fetch fails
            }
        };

        fetchFilteredDresses();
    }, [filter]);

    const filteredDresses = dresses.filter(dress => {
        if (filter === 'all') {
            return true;
        } else if (filter === 'evening') {
            return dress.description.toLowerCase().includes('seara');
        } else if (filter === 'day') {
            return dress.description.toLowerCase().includes('zi');
        } else if (filter === 'office') {
            return dress.description.toLowerCase().includes('office');
        } else {
            return false;
        }
    });

    return (
        <div className="Home">
            <main className="main-content">
                <h1>Colectia de Rochii</h1>
                {error && <p className="error">{error}</p>}
                <div className="image-gallery">
                    {filteredDresses.map((dress) => (
                        <div key={dress.id} className="image-wrapper">
                            <a href={`/ProductDetails/${dress.id}`}>
                                <img src={dress.src} alt={dress.name} />
                            </a>
                            <div className="dress-info">
                                <p className="dress-name">{dress.name}</p>
                                <p className="dress-price">{dress.price}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </main>
        </div>
    );
};

export default Home;
