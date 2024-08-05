import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import Register from './Register';
import Login from './Login';
import ProductDetails from './ProductDetails';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/ProductDetails/:id" element={<ProductDetails />} />
                    <Route path="*" element={<div>Pagina nu a fost găsită</div>} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
