import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import UserNavbar from "./UserNavbar";
import AdminNavbar from "./AdminNavbar";
import '../styles/AddProduct.css';

const AddProduct = () => {
    const [product, setProduct] = useState({
        name: '',
        description: '',
        price: '',
        availableQuantity: '',
        buyingPrice: '',
        category: '',
        categoryDescription: '',
        productImages: [],
    });

    const [attributes, setAttributes] = useState([
        { attributeName: '', values: [''] }
    ]);

    const navigate = useNavigate();
    const role = localStorage.getItem("role")?.toLowerCase();
    const { id } = useParams();

    useEffect(() => {
        if (id) {
            const fetchProduct = async () => {
                try {
                    const response = await fetch(`http://localhost:8080/products/getProductById/${id}`);
                    if (!response.ok) throw new Error("Failed to fetch product");
                    const data = await response.json();

                    setProduct({
                        name: data.name || '',
                        description: data.description || '',
                        price: data.price || '',
                        availableQuantity: data.availableQuantity || '',
                        buyingPrice: data.buyingPrice || '',
                        category: data.category?.name || '',
                        categoryDescription: data.category?.description || '',
                        productImages: data.productImages?.map(img =>
                            img.replace("https://i.postimg.cc/", "").replace(".png", "")
                        ) || [],
                    });
                } catch (error) {
                    console.error("Error loading product:", error);
                }
            };

            fetchProduct();
        }
    }, [id]);

    const handleInputChange = (e) => {
        setProduct({ ...product, [e.target.name]: e.target.value });
    };

    const handleImageChange = (e) => {
        const files = Array.from(e.target.files);
        const imageNames = files.map(file => {
            let name = file.name.replace(/\.[^/.]+$/, "");
            if (name.length > 8) {
                name = name.slice(0, 8) + '/' + name.slice(8);
            }
            return name;
        });

        setProduct(prev => ({
            ...prev,
            productImages: [...prev.productImages, ...imageNames]
        }));
    };

    const handleAttributeChange = (index, key, value) => {
        const updated = [...attributes];
        updated[index][key] = value;
        setAttributes(updated);
    };

    const handleValueChange = (attrIndex, valIndex, value) => {
        const updated = [...attributes];
        updated[attrIndex].values[valIndex] = value;
        setAttributes(updated);
    };

    const addAttribute = () => {
        setAttributes([...attributes, { attributeName: '', values: [''] }]);
    };

    const addValueToAttribute = (attrIndex) => {
        const updated = [...attributes];
        updated[attrIndex].values.push('');
        setAttributes(updated);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userId = localStorage.getItem('userId');

        if (!userId || isNaN(userId)) {
            alert('Invalid user ID! Please log in again.');
            return;
        }

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
            buyingPrice: product.buyingPrice,
            addedDate: new Date().toISOString().split('T')[0],
            category: {
                name: product.category,
                description: product.categoryDescription
            },
            productImages: product.productImages,
            attributes: attributes
        };

        const url = id
            ? `http://localhost:8080/products/updateProduct/${id}`
            : `http://localhost:8080/products/addProduct?userId=${Number(userId)}`;

        const method = id ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productData),
            });

            if (!response.ok) throw new Error(id ? 'Failed to update product' : 'Failed to add product');

            await response.json();

            alert(id ? 'Product updated successfully!' : 'Product added successfully!');
            navigate('/admin-dashboard');

        } catch (error) {
            console.error("Error submitting product:", error);
            alert(id ? 'Error updating product' : 'Error adding product');
        }
        console.log("ProductData to be sent:", productData);

    };

    return (
        <div>
            {role === "admin" ? <AdminNavbar /> : <UserNavbar />}
            <div className="add-product-container">
                <div className="add-product-form">
                    <h2>{id ? "Edit Product" : "Add Product"}</h2>
                    <form onSubmit={handleSubmit}>
                        <input type="text" name="name" placeholder="Product Name" value={product.name} onChange={handleInputChange} required />
                        <input type="text" name="description" placeholder="Description" value={product.description} onChange={handleInputChange} required />
                        <input type="number" name="price" placeholder="Selling Price" value={product.price} onChange={handleInputChange} required />
                        <input type="number" name="availableQuantity" placeholder="Stock Quantity" value={product.availableQuantity} onChange={handleInputChange} required />
                        <input type="number" name="buyingPrice" placeholder="Buying Price" value={product.buyingPrice} onChange={handleInputChange} required />
                        <input type="text" name="category" placeholder="Category Name" value={product.category} onChange={handleInputChange} required />
                        <input type="text" name="categoryDescription" placeholder="Category Description (optional)" value={product.categoryDescription} onChange={handleInputChange} />

                        <input type="file" multiple onChange={handleImageChange} />
                        <ul>
                            {product.productImages.map((img, index) => (
                                <li key={index}>{img}</li>
                            ))}
                        </ul>

                        {attributes.map((attr, attrIndex) => (
                            <div key={attrIndex} className="attribute-section">
                                <input
                                    type="text"
                                    placeholder="Attribute name (e.g. Size)"
                                    value={attr.attributeName}
                                    onChange={(e) => handleAttributeChange(attrIndex, 'attributeName', e.target.value)}
                                />
                                {attr.values.map((val, valIndex) => (
                                    <input
                                        key={valIndex}
                                        type="text"
                                        placeholder="Attribute value (e.g. S, M, L)"
                                        value={val}
                                        onChange={(e) => handleValueChange(attrIndex, valIndex, e.target.value)}
                                    />
                                ))}
                                <div className="attribute-buttons">
                                    <button type="button" onClick={() => addValueToAttribute(attrIndex)}>Add Value</button>
                                </div>
                            </div>
                        ))}
                        <div className="attribute-buttons">
                            <button type="button" onClick={addAttribute}>Add Attribute</button>
                        </div>

                        <button type="submit">{id ? "Save Changes" : "Add Product"}</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default AddProduct;
