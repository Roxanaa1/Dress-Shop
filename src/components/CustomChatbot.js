import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';

const CustomChatbot = ({ onClose }) => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    const messageEndRef = useRef(null);

    useEffect(() => {
        if (messageEndRef.current) {
            messageEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]);

    useEffect(() => {
        const fetchWelcome = async () => {
            try {
                const response = await axios.post('http://localhost:8080/chatbot', { message: '' });
                setMessages([{ sender: 'bot', text: '' }]);
                typeMessage(response.data, setMessages);
            } catch (err) {
                setMessages([{ sender: 'bot', text: 'Eroare la conectare. Încearcă din nou.' }]);
            }
        };
        fetchWelcome();
    }, []);

    const formatMessage = (text) => {
        const urlRegex = /(https?:\/\/[^\s]+)/g;
        return text.replace(urlRegex, url => `<a href="${url}" target="_blank" style="color:#2f2f80;font-weight:bold">${url}</a>`);
    };

    const typeMessage = (text, callback) => {
        let i = 0;
        const interval = setInterval(() => {
            callback((prev) => {
                const lastMsg = prev[prev.length - 1];
                const newText = (lastMsg?.text || '') + text[i];
                const updated = [...prev.slice(0, -1), { ...lastMsg, text: newText }];
                return updated;
            });
            i++;
            if (i >= text.length) clearInterval(interval);
        }, 30);
    };

    const handleSend = async () => {
        if (!input.trim()) return;

        const userMessage = { sender: 'user', text: input };
        setMessages((prev) => [...prev, userMessage]);

        setInput('');
        try {
            const response = await axios.post('http://localhost:8080/chatbot', { message: input });
            setMessages((prev) => [...prev, { sender: 'bot', text: '' }]);
            typeMessage(response.data, setMessages);
        } catch (error) {
            const errorMessage = { sender: 'bot', text: 'Eroare la conectare. Încearcă din nou.' };
            setMessages((prev) => [...prev, errorMessage]);
        }
    };

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            height: '100%',
            fontFamily: 'Arial, sans-serif',
            position: 'relative'
        }}>
            <button
                onClick={onClose}
                style={{
                    position: 'absolute',
                    top: '-10px',
                    right: '-160px',
                    background: 'transparent',
                    border: 'none',
                    fontSize: '20px',
                    cursor: 'pointer',
                    zIndex: 10
                }}
            >
                ❌
            </button>

            <div style={{
                backgroundColor: '#2f2f80',
                color: 'white',
                padding: '14px 20px',
                borderTopLeftRadius: '16px',
                borderTopRightRadius: '16px',
                fontWeight: 'bold',
                fontSize: '16px'
            }}>
                🤖 Dress Assistant
            </div>

            <div style={{
                flex: 1,
                overflowY: 'auto',
                padding: '16px',
                backgroundColor: '#f5f5f5'
            }}>
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        style={{
                            display: 'flex',
                            justifyContent: msg.sender === 'user' ? 'flex-end' : 'flex-start',
                            marginBottom: '12px',
                            alignItems: 'flex-end'
                        }}
                    >
                        {msg.sender === 'bot' && (
                            <div style={{ display: 'flex', alignItems: 'center', marginRight: '8px' }}>
                                <div style={{
                                    width: '36px',
                                    height: '36px',
                                    borderRadius: '50%',
                                    backgroundColor: '#2f2f80',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    fontSize: '18px',
                                    color: 'white'
                                }}>
                                    🤖
                                </div>
                            </div>
                        )}

                        <div style={{
                            backgroundColor: msg.sender === 'user' ? '#884e7d' : '#ffffff',
                            color: msg.sender === 'user' ? '#fff' : '#333',
                            padding: '10px 14px',
                            borderRadius: '16px',
                            maxWidth: '70%',
                            lineHeight: '1.5',
                            boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
                            wordBreak: 'break-word',
                            overflowWrap: 'break-word',
                            whiteSpace: 'pre-wrap'
                        }}>
                            <span dangerouslySetInnerHTML={{
                                __html: msg.text && msg.sender === 'bot' ? formatMessage(msg.text) : msg.text
                            }} />
                        </div>

                        {msg.sender === 'user' && (
                            <div style={{ display: 'flex', alignItems: 'center', marginLeft: '8px' }}>
                                <div style={{
                                    width: '36px',
                                    height: '36px',
                                    borderRadius: '50%',
                                    backgroundColor: '#884e7d',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    fontSize: '18px',
                                    color: 'white'
                                }}>
                                    🧑
                                </div>
                            </div>
                        )}
                    </div>
                ))}
                <div ref={messageEndRef} />
            </div>

            <div style={{
                display: 'flex',
                flexDirection: 'column',
                padding: '12px',
                borderTop: '1px solid #ddd',
                backgroundColor: '#fff',
                marginTop: 'auto',
                gap: '8px'
            }}>
                <input
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                    placeholder="Scrie o întrebare..."
                    style={{
                        flex: 1,
                        padding: '10px 14px',
                        borderRadius: '20px 0 0 20px',
                        border: '1px solid #ccc',
                        outline: 'none',
                        fontSize: '14px',
                        height: '40px'
                    }}
                />
                <button
                    onClick={handleSend}
                    style={{
                        height: '40px',
                        padding: '0 20px',
                        borderRadius: '0 20px 20px 0',
                        backgroundColor: '#884e7d',
                        color: 'white',
                        border: '1px solid #884e7d',
                        fontWeight: 'bold',
                        fontSize: '14px',
                        cursor: 'pointer'
                    }}
                >
                    Trimite
                </button>
            </div>
        </div>
    );
};

export default CustomChatbot;
