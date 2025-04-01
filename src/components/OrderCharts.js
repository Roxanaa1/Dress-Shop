import React, { useEffect, useState } from 'react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend, PieChart, Pie, Cell
} from 'recharts';
import axios from 'axios';

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#a4de6c', '#d0ed57', '#ff7f7f'];

const OrderCharts = () => {
    const [ordersByMonth, setOrdersByMonth] = useState([]);
    const [ordersByCounty, setOrdersByCounty] = useState([]);
    const [revenueByMonth, setRevenueByMonth] = useState([]);
    const [statusDistribution, setStatusDistribution] = useState([]);
    const [topCustomers, setTopCustomers] = useState([]);
    const [selectedYear, setSelectedYear] = useState(2024);

    const monthNumberToName = (num) => {
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        return months[num - 1];
    };

    const fillAllMonths = (data, keyName) => {
        const allMonths = Array.from({ length: 12 }, (_, i) => ({
            month: monthNumberToName(i + 1),
            [keyName]: 0
        }));

        data.forEach(item => {
            const index = item.month - 1;
            if (index >= 0 && index < 12) {
                allMonths[index][keyName] = item[keyName];
            }
        });

        return allMonths;
    };

    useEffect(() => {
        axios.get(`http://localhost:8080/orders/orders-by-month?year=${selectedYear}`)
            .then(res => {
                const data = res.data.map(item => ({
                    month: item.month,
                    count: item.count
                }));
                setOrdersByMonth(fillAllMonths(data, 'count'));
            });

        axios.get(`http://localhost:8080/orders/orders-by-county?year=${selectedYear}`)
            .then(res => {
                const data = Object.entries(res.data).map(([county, count]) => ({ county, count }));
                setOrdersByCounty(data);
            });

        axios.get(`http://localhost:8080/orders/revenue-by-month?year=${selectedYear}`)
            .then(res => {
                const data = res.data.map(item => ({
                    month: item.month,
                    total: item.total
                }));
                setRevenueByMonth(fillAllMonths(data, 'total'));
            });

        axios.get(`http://localhost:8080/orders/status-distribution?year=${selectedYear}`)
            .then(res => {
                const data = Object.entries(res.data).map(([name, value]) => ({ name, value }));
                setStatusDistribution(data);
            });

        axios.get(`http://localhost:8080/orders/top-customers?year=${selectedYear}`)
            .then(res => {
                setTopCustomers(res.data);
            });

    }, [selectedYear]);

    return (
        <div style={{ padding: '3rem' }}>
            <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
                <label style={{ marginRight: '1rem' }}>Select year:</label>
                <select value={selectedYear} onChange={(e) => setSelectedYear(parseInt(e.target.value))}>
                    <option value={2025}>2025</option>
                    <option value={2024}>2024</option>
                </select>
            </div>

            <h2 style={{ textAlign: 'center' }}>Orders per Month</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={ordersByMonth}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="month" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#8884d8" name="Orders" />
                </BarChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Order Revenue per Month</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={revenueByMonth}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="month" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="total" fill="#82ca9d" name="Total Revenue" />
                </BarChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Orders by County</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={ordersByCounty}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="county" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#ffc658" name="Orders per County" />
                </BarChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Order Status Distribution</h2>
            <ResponsiveContainer width="100%" height={400}>
                <PieChart>
                    <Pie
                        data={statusDistribution}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                        outerRadius={120}
                        dataKey="value"
                    >
                        {statusDistribution.map((_, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Top 5 Customers</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={topCustomers}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="orders" fill="#d8854f" name="Number of Orders" />
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
};

export default OrderCharts;
