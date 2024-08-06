import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/ProductDetails.css';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

const products = [
    { id: 1, src: '/images/Dress1.png', name: 'ROCHIE MIDI ELEGANTA CU FUNDE DECORATIVE PE UMERI', price: '200 Lei', description: 'Rochie de seara foarte eleganta', sizes: ['34', '36', '38', '40'], color: 'blue', images: ['/images/Dress1.png', '/images/Dress1-2.png'], inStock: true },
    { id: 2, src: '/images/Dress2.png', name: 'ROCHIE CU MANECI SCURTE BRODATE CU PERLE', price: '150 Lei', description: 'Rochie de seară elegantă, perfectă pentru evenimente speciale ', sizes: ['34', '36', '38'], color: 'white', images: ['/images/Dress2.png',  '/images/Dress2-2.png'], inStock: false },
    { id: 3, src: '/images/Dress3.png', name: 'ROCHIE CU DECOLTEU PATRAT SI PERLE', price: '180 Lei', description: 'Rochie de zi lejeră, perfectă pentru ținute casual și confortabile.', sizes: ['34', '36', '38', '40'], color: 'black', images: ['/images/Dress3.png',  '/images/Dress3-2.png'], inStock: true },
    { id: 4, src: '/images/Dress4.png', name: 'ROCHIE LIME CU FUNDA AMPLA IN CONTRAST', price: '150 Lei', description: 'Rochie de zi eleganta', sizes: ['34', '36', '38', '40'], color: 'yellow', images: ['/images/Dress4.png',  '/images/Dress4-2.png'], inStock: true },
    { id: 5, src: '/images/Dress5.png', name: 'ROCHIE CASUAL MIDI EVAZATA CU UMERI GOI', price: '200 Lei', description: 'Rochie midi elegantă', sizes: ['34', '36', '38', '40'], color: 'pink', images: ['/images/Dress5.png',  '/images/Dress5-2.png'], inStock: false },
    { id: 6, src: '/images/Dress6-1.png', name: 'ROCHIE VERDE SATINATA CU CROI EVAZAT ', price: '180 Lei', description: 'Rochie eleganta ', sizes: ['34', '36', '38', '40'], color: 'green', images: ['/images/Dress6-1.png',  '/images/Dress6.png'], inStock: true }
];

const ProductDetails = () => {
    const { id } = useParams();
    const product = products.find(p => p.id === parseInt(id));
    const [selectedImage, setSelectedImage] = useState(product.images[0]);

    if (!product) return <div>Product not found</div>;

    return (
        <div className="ProductDetails">
            <Navbar />
            <Sidebar />
            <div className="details-container">
                <div className="image-gallery">
                    <img src={selectedImage} alt={product.name} className="main-image" />
                    <div className="thumbnail-gallery">
                        {product.images.map((image, index) => (
                            <img
                                key={index}
                                src={image}
                                alt={`Thumbnail ${index}`}
                                className={`thumbnail ${selectedImage === image ? 'selected' : ''}`}
                                onClick={() => setSelectedImage(image)}
                            />
                        ))}
                    </div>
                </div>
                <div className="product-info">
                    <h1>{product.name}</h1>
                    <p className="price">{product.price}</p>
                    <p className="description">{product.description}</p>
                    <p className={`stock-status ${product.inStock ? 'in-stock' : 'out-of-stock'}`}>
                        {product.inStock ? 'In Stock' : 'Out of Stock'}
                    </p>
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
                    <button className="add-to-cart" disabled={!product.inStock}>Add to cart</button>
                    <button className="wishlist">Save to Wishlist</button>
                </div>
            </div>
        </div>
    );
};

export default ProductDetails;
