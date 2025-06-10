// Contact.js
import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import UserNavbar from './UserNavbar';

const position = [45.7489, 21.2087];

const Contact = () => {
    const markerIcon = new L.Icon({
        iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
    });

    return (
        <>
            <UserNavbar />
            <div style={{ padding: '4rem' }}>
                <h2>📞 Contact Us</h2>
                <p><strong>Address:</strong> Piața Unirii, Timișoara</p>
                <p><strong>Phone:</strong> 0740 123 456</p>
                <p><strong>Email:</strong> contact@edressshop.ro</p>

                <div style={{ height: '400px', width: '100%', marginTop: '20px' }}>
                    <MapContainer center={position} zoom={15} style={{ height: '100%', width: '100%' }}>
                        <TileLayer
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                            attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a>'
                        />
                        <Marker position={position} icon={markerIcon}>
                            <Popup>
                                eDressShop HQ<br /> Timișoara
                            </Popup>
                        </Marker>
                    </MapContainer>
                </div>
            </div>
        </>
    );
};

export default Contact;
