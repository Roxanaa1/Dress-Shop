import React from 'react';
import { useParams } from 'react-router-dom';
import './ProductDetails.css';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

const products = [
    { id: 1, src: '/images/Dress1.png', name: 'ROCHIE MIDI ELEGANTA CU FUNDE DECORATIVE PE UMERI', price: '200 Lei', description: 'Rochie de seara foarte eleganta', sizes: ['34', '36', '38', '40'], color: 'blue' },
    { id: 2, src: '/images/Dress2.png', name: 'ROCHIE CU MANECI SCURTE BRODATE CU PERLE', price: '150 Lei', description: 'Rochie de seară elegantă, perfectă pentru evenimente speciale ', sizes: ['34', '36', '38'], color: 'white' },
    { id: 3, src: '/images/Dress3.png', name: 'ROCHIE CU DECOLTEU PATRAT SI PERLE', price: '180 Lei', description: 'Rochie de zi lejeră, perfectă pentru ținute casual și confortabile.', sizes: ['34', '36', '38', '40'], color: 'black' },
    { id: 4,src: '/images/Dress4.png', name: 'ROCHIE LIME CU FUNDA AMPLA IN CONTRAST', price: '150 Lei', description: 'Rochie de zi eleganta', sizes: ['34', '36', '38', '40'], color: 'yellow' },
    {id: 5, src: '/images/Dress5.png', name: 'ROCHIE CASUAL MIDI EVAZATA CU UMERI GOI', price: '200 Lei', description: 'Rochie midi elegantă', sizes: ['34', '36', '38', '40'], color: 'pink' },
    {id: 6, src: '/images/Dress6.png', name: 'ROCHIE CU SPATELE DECUPAT ', price: '180 Lei', description: 'Rochie eleganta ', sizes: ['34', '36', '38', '40'], color: 'white' }
];

const ProductDetails = () => {
    const { id } = useParams();
    const product = products.find(p => p.id === parseInt(id));

    if (!product) return <div>Product not found</div>;

    return (
        <div className="ProductDetails">
            <Navbar />
            <Sidebar/>
            <div className="details-container">
                <div className="image-gallery">
                    <img src={product.src} alt={product.name} className="main-image" />

                </div>
                <div className="product-info">
                    <h1>{product.name}</h1>
                    <p className="price">{product.price}</p>
                    <p className="description">{product.description}</p>
                    <div className="options">
                        <div className="colors">
                            <span>Color:</span>
                            <span className="color-sample" style={{ backgroundColor: product.color }}></span>
                        </div>
                        <div className="sizes">
                            <span>Size:</span>
                            {product.sizes.map(size => (
                                <button key={size} className="size-button">{size}</button>
                            ))}
                        </div>
                    </div>
                    <button className="add-to-cart">Add to cart</button>
                    <button className="wishlist"> Save to Wishlist</button>
                </div>
            </div>
        </div>
    );
};

export default ProductDetails;
