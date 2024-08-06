import React, { useState } from 'react';
import '../styles/Home.css';

const localDresses = [
    { src: '/images/Dress1.png', name: 'ROCHIE MIDI ELEGANTA CU FUNDE DECORATIVE PE UMERI', price: '200 Lei', id: 1, description: 'Rochie de seara eleganta' },
    { src: '/images/Dress2.png', name: 'ROCHIE CU MANECI SCURTE BRODATE CU PERLE', price: '150 Lei', id: 2, description: 'Rochie de zi eleganta' },
    { src: '/images/Dress3.png', name: 'ROCHIE CU DECOLTEU PATRAT SI PERLE', price: '180 Lei', id: 3, description: 'Rochie de zi' },
    { src: '/images/Dress4.png', name: 'ROCHIE LIME CU FUNDA AMPLA IN CONTRAST', price: '150 Lei', id: 4, description: 'Rochie de zi' },
    { src: '/images/Dress5.png', name: 'ROCHIE CASUAL MIDI EVAZATA CU UMERI GOI', price: '200 Lei', id: 5, description: 'Rochie de zi casual' },
    { src: '/images/Dress6-1.png', name: 'ROCHIE CU SPATELE DECUPAT ', price: '180 Lei', id: 6, description: 'Rochie de seara' }
];

const Home = () => {
    const [dresses] = useState(localDresses);
    const [error] = useState(null);

    return (
        <div className="Home">
            <main className="main-content">
                <h1>Colectia de Rochii</h1>
                {error && <p className="error">{error}</p>}
                <div className="image-gallery">
                    {dresses.map((dress) => (
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
