import React, { useEffect, useState } from 'react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend, PieChart, Pie, Cell
} from 'recharts';
import axios from 'axios';
import '../styles/AdminCharts.css';

const COLORS = ['#82ca9d', '#ff7f7f'];
const COUNTY_COLORS = ['#8884d8', '#8dd1e1', '#ffc658', '#a4de6c', '#d0ed57', '#d8854f'];

const UserCharts = () => {
    const [userData, setUserData] = useState([]);
    const [ageData, setAgeData] = useState([]);
    const [verificationData, setVerificationData] = useState([]);
    const [countyData, setCountyData] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/users/users-by-month')
            .then(response => {
                const rawData = response.data;
                const allMonths = Array.from({ length: 12 }, (_, i) => ({
                    month: monthNumberToName(i + 1),
                    count: 0
                }));
                rawData.forEach(item => {
                    const index = item.month - 1;
                    allMonths[index].count = item.count;
                });
                setUserData(allMonths);
            })
            .catch(error => {
                console.error('Error fetching user data:', error);
            });

        axios.get('http://localhost:8080/users/age-distribution')
            .then(response => {
                const formatted = Object.entries(response.data).map(([ageGroup, count]) => ({
                    ageGroup,
                    count
                }));
                setAgeData(formatted);
            })
            .catch(error => {
                console.error('Error fetching age distribution:', error);
            });

        axios.get('http://localhost:8080/users/verification-status')
            .then(response => {
                const formatted = Object.entries(response.data).map(([key, value]) => ({
                    name: key,
                    value
                }));
                setVerificationData(formatted);
            })
            .catch(error => {
                console.error('Error fetching verification data:', error);
            });

        axios.get('http://localhost:8080/users/users-by-county')
            .then(response => {
                const formatted = Object.entries(response.data).map(([county, count]) => ({
                    county,
                    count
                }));
                setCountyData(formatted);
            })
            .catch(error => {
                console.error('Error fetching county data:', error);
            });
    }, []);

    const monthNumberToName = (num) => {
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        return months[num - 1];
    };

    return (
        <div style={{ padding: '3rem' }}>
            <h2 style={{ textAlign: 'center' }}>User Registrations Per Month</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={userData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="month" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#8884d8" name="Users Registered" />
                </BarChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>User Age Distribution</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={ageData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="ageGroup" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#82ca9d" name="Users per Age Group" />
                </BarChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Verified vs Unverified Users</h2>
            <ResponsiveContainer width="100%" height={400}>
                <PieChart>
                    <Pie
                        data={verificationData}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                        outerRadius={120}
                        dataKey="value"
                    >
                        {verificationData.map((_, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>User Distribution by County</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={countyData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="county" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#ffc658" name="Users per County" />
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
};

export default UserCharts;
