import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/ProductDetails.css';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

const ProductDetails = () => {
    const { id } = useParams(); // extrage id-ul prod din URL
    const [product, setProduct] = useState(null); // init starea pt produs ca fiind null
    const [selectedImage, setSelectedImage] = useState(''); // init starea pt img selectata

    useEffect(() => {
        // fetch pt a optine detaliile prod bazat pe id
        fetch(`http://localhost:8080/products/getProductById/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                setProduct(data); // set starea prod cu datele primite
                setSelectedImage(data.productImages[0]); // set img initial selectata ca fiind prima img din array
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
    }, [id]); // efectul se va executa din nou dacă id-ul prod se schimba

    if (!product) return <div>Product not found</div>;

    return (
        <div className="ProductDetails">
            <Navbar />
            <Sidebar />
            <div className="details-container">
                <div className="image-gallery">

                    {selectedImage && <img src={selectedImage} alt={product.name} className="main-image" />}
                    <div className="thumbnail-gallery">

                        {product.productImages && product.productImages.map((image, index) => (
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

                            {product.sizes && product.sizes.map(size => (
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
