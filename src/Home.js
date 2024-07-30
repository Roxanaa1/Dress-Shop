import React from 'react';
import './Home.css';
import Navbar from './Navbar';

const dresses = [
    { src: '/images/Dress1.png', name: 'ROCHIE MIDI ELEGANTA CU FUNDE DECORATIVE PE UMERI', price: '200 Lei' },
    { src: '/images/Dress2.png', name: 'ROCHIE CU MANECI SCURTE BRODATE CU PERLE', price: '150 Lei' },
    { src: '/images/Dress3.png', name: 'ROCHIE CU DECOLTEU PATRAT SI PERLE', price: '180 Lei' }
];

const Home = () => {
    return (
        <div className="Home">
            <Navbar />
            <main>
                <h1>Welcome to Home Page</h1>
                <div className="image-gallery">
                    {dresses.map((dress, index) =>
                        (
                        <div key={index} className="image-wrapper">
                            <img src={dress.src} alt={`Dress ${index}`} />
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
