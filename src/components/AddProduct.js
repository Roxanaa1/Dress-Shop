import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Navbar from "./Navbar";
import AdminNavbar from "./AdminNavbar";
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
                        category: data.category?.name || '',
                        productImages: data.productImages || [],
                    });

                    setImageName(data.productImages?.[0] || '');
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

        if (!userId || isNaN(userId)) {
            alert('User ID is not valid! Please log in again.');
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
            category: { name: product.category },
            productImages: product.productImages,
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

            if (!response.ok) {
                throw new Error(id ? 'Failed to update product' : 'Failed to add product');
            }

            await response.json();

            alert(id ? 'Produs actualizat cu succes!' : 'Produs adăugat cu succes!');
            navigate('/admin-dashboard');

        } catch (error) {
            console.error("Error submitting product:", error);
            alert(id ? 'Eroare la actualizarea produsului' : 'Eroare la adăugarea produsului');
        }
    };

    return (
        <div>
            {role === "admin" ? <AdminNavbar /> : <Navbar />}

            <div className="add-product-container">
                <div className="add-product-form">
                    <h2>{id ? "Editează Produs" : "Adaugă Produs"}</h2>
                    <form onSubmit={handleSubmit}>
                        <input
                            type="text"
                            name="name"
                            placeholder="Nume produs"
                            value={product.name}
                            onChange={handleInputChange}
                            required
                        />
                        <input
                            type="text"
                            name="description"
                            placeholder="Descriere"
                            value={product.description}
                            onChange={handleInputChange}
                            required
                        />
                        <input
                            type="number"
                            name="price"
                            placeholder="Preț"
                            value={product.price}
                            onChange={handleInputChange}
                            required
                        />
                        <input
                            type="number"
                            name="availableQuantity"
                            placeholder="Cantitate"
                            value={product.availableQuantity}
                            onChange={handleInputChange}
                            required
                        />
                        <input
                            type="text"
                            name="category"
                            placeholder="Categorie"
                            value={product.category}
                            onChange={handleInputChange}
                            required
                        />
                        <input
                            type="file"
                            onChange={handleImageChange}
                        />
                        <button type="submit">{id ? "Salvează modificările" : "Adaugă produs"}</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default AddProduct;
