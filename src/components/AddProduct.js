import React, { useState } from 'react';
import '../styles/AddProduct.css';

const AddProduct = () => {
    const [product, setProduct] = useState({
        name: '',
        description: '',
        price: '',
        availableQuantity: '',
        category: '',
        productImages: [],
    });

    const [imageName, setImageName] = useState('');

    const handleInputChange = (e) => {
        setProduct({ ...product, [e.target.name]: e.target.value });
    };

    const handleImageChange = (e) => {
        const selectedImage = e.target.files[0];
        if (selectedImage) {
            let imageNameWithoutExtension = selectedImage.name.replace(/\.[^/.]+$/, "");

            if (imageNameWithoutExtension.length > 8) {
                imageNameWithoutExtension = imageNameWithoutExtension.slice(0, 8) + '/' + imageNameWithoutExtension.slice(8);
            }

            setImageName(imageNameWithoutExtension);
            setProduct((prevState) => ({
                ...prevState,
                productImages: [imageNameWithoutExtension],
            }));
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();
        const userId = localStorage.getItem('userId');
        console.log("User ID from localStorage:", userId);

        if (!userId || isNaN(userId)) {
            alert('User ID is not valid! Please log in again.');
            return;
        }

        console.log("Adding product with userId:", userId);

        if (!product.category) {
            alert("Category is required!");
            return;
        }

        const productData = {
            userId: Number(userId),
            name: product.name,
            description: product.description,
            price: product.price,
            availableQuantity: product.availableQuantity,
            category: { name: product.category },
            productImages: product.productImages,
        };

        console.log("Product data sent to backend:", productData);

        try {
            const response = await fetch(`http://localhost:8080/products/addProduct?userId=${Number(userId)}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productData),
            });

            if (!response.ok) {
                throw new Error('Failed to add product');
            }

            const newProduct = await response.json();
            console.log("Product added successfully:", newProduct);
            alert('Product added successfully');
        } catch (error) {
            console.error("Error adding product:", error);
            alert('Error adding product');
        }
    };


    return (
        <div className="add-product-container">
            <div className="add-product-form">
                <h2>Add Product</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="name"
                        placeholder="Product Name"
                        onChange={handleInputChange}
                        required
                    />
                    <input
                        type="text"
                        name="description"
                        placeholder="Description"
                        onChange={handleInputChange}
                        required
                    />
                    <input
                        type="number"
                        name="price"
                        placeholder="Price"
                        onChange={handleInputChange}
                        required
                    />
                    <input
                        type="number"
                        name="availableQuantity"
                        placeholder="Quantity"
                        onChange={handleInputChange}
                        required
                    />
                    <input
                        type="text"
                        name="category"
                        placeholder="Category"
                        onChange={handleInputChange}
                        required
                    />
                    <input
                        type="file"
                        onChange={handleImageChange}
                    />
                    <button type="submit">Add Product</button>
                </form>
            </div>
        </div>
    );
};

export default AddProduct;
