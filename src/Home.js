import React from 'react';
import './Home.css';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

const dresses = [
    { src: '/images/Dress1.png', name: 'ROCHIE MIDI ELEGANTA CU FUNDE DECORATIVE PE UMERI', price: '200 Lei', id: 1 },
    { src: '/images/Dress2.png', name: 'ROCHIE CU MANECI SCURTE BRODATE CU PERLE', price: '150 Lei', id: 2 },
    { src: '/images/Dress3.png', name: 'ROCHIE CU DECOLTEU PATRAT SI PERLE', price: '180 Lei', id: 3 },
    { src: '/images/Dress4.png', name: 'ROCHIE LIME CU FUNDA AMPLA IN CONTRAST', price: '150 Lei', id: 4 },
    { src: '/images/Dress5.png', name: 'ROCHIE CASUAL MIDI EVAZATA CU UMERI GOI', price: '200 Lei', id: 5 },
    { src: '/images/Dress6.png', name: 'ROCHIE CU SPATELE DECUPAT ', price: '180 Lei', id: 6 }
];

const Home = () => {
    return (
        <div className="Home">
            <Navbar />
            <Sidebar />
            <main className="main-content">
                <h1>Colectia de Rochii</h1>
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
