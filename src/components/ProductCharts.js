import React, { useEffect, useState } from 'react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend, PieChart, Pie, Cell, LineChart, Line
} from 'recharts';
import axios from 'axios';
import '../styles/AdminCharts.css';

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff7f7f', '#a4de6c', '#d8854f'];

const ProductCharts = () => {
    const [monthlyProducts, setMonthlyProducts] = useState([]);
    const [categoryData, setCategoryData] = useState([]);
    const [topProducts, setTopProducts] = useState([]);
    const [salesEvolution, setSalesEvolution] = useState([]);
    const [mostSold, setMostSold] = useState(null);
    const [mostProfitable, setMostProfitable] = useState(null);
    const [selectedProductId, setSelectedProductId] = useState(null);
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
        axios.get(`http://localhost:8080/products/by-month?year=${selectedYear}`)
            .then(response => {
                const data = response.data.map(item => ({
                    month: item.month,
                    count: item.count
                }));
                setMonthlyProducts(fillAllMonths(data, 'count'));
            });

        axios.get(`http://localhost:8080/products/by-category`)
            .then(response => {
                const data = Object.entries(response.data).map(([category, count]) => ({ category, count }));
                setCategoryData(data);
            });

        axios.get(`http://localhost:8080/products/top-sold`)
            .then(response => {
                setTopProducts(response.data);
                if (response.data.length > 0) {
                    setSelectedProductId(response.data[0].productid);
                }
            });

        axios.get(`http://localhost:8080/products/most-sold`)
            .then(response => setMostSold(response.data));

        axios.get(`http://localhost:8080/products/most-profitable`)
            .then(response => setMostProfitable(response.data));
    }, [selectedYear]);

    useEffect(() => {
        if (selectedProductId) {
            axios.get(`http://localhost:8080/products/sales-evolution/${selectedProductId}?year=${selectedYear}`)
                .then(response => {
                    const data = response.data.map(item => ({
                        month: item.month,
                        sales: item.sales
                    }));
                    setSalesEvolution(fillAllMonths(data, 'sales'));
                });
        }
    }, [selectedProductId, selectedYear]);

    const mostSoldData = mostSold ? [
        { name: mostSold.productname, value: mostSold.unitssold }
    ] : [];

    const mostProfitableData = mostProfitable ? [
        { name: mostProfitable.productname, value: mostProfitable.totalprofit }
    ] : [];

    return (
        <div style={{ padding: '3rem' }}>
            <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
                <label style={{ marginRight: '1rem' }}>Select year:</label>
                <select value={selectedYear} onChange={(e) => setSelectedYear(parseInt(e.target.value))}>
                    <option value={2025}>2025</option>
                    <option value={2024}>2024</option>
                </select>
            </div>

            <h2 style={{ textAlign: 'center' }}>Products Added Per Month</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={monthlyProducts}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="month" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="count" fill="#8884d8" name="Products Added" />
                </BarChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Products per Category</h2>
            <ResponsiveContainer width="100%" height={400}>
                <PieChart>
                    <Pie
                        data={categoryData}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        label={({ category, count }) => `${category}: ${count}`}
                        outerRadius={120}
                        dataKey="count"
                    >
                        {categoryData.map((_, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>

            <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Top 5 Sold Products</h2>
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={topProducts}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="productname" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="unitssold" fill="#82ca9d" name="Units Sold" />
                </BarChart>
            </ResponsiveContainer>

            {mostSoldData.length > 0 && (
                <>
                    <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Most Sold Product (Pie)</h2>
                    <ResponsiveContainer width="100%" height={400}>
                        <PieChart>
                            <Pie
                                data={mostSoldData}
                                cx="50%"
                                cy="50%"
                                labelLine={false}
                                label={({ name, value }) => `${name}: ${value} sold`}
                                outerRadius={120}
                                dataKey="value"
                            >
                                {mostSoldData.map((_, index) => (
                                    <Cell key={`cell-sold-${index}`} fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Tooltip />
                            <Legend />
                        </PieChart>
                    </ResponsiveContainer>
                </>
            )}

            {mostProfitableData.length > 0 && (
                <>
                    <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Most Profitable Product (Pie)</h2>
                    <ResponsiveContainer width="100%" height={400}>
                        <PieChart>
                            <Pie
                                data={mostProfitableData}
                                cx="50%"
                                cy="50%"
                                labelLine={false}
                                label={({ name, value }) => `${name}: ${value.toFixed(2)} lei`}
                                outerRadius={120}
                                dataKey="value"
                            >
                                {mostProfitableData.map((_, index) => (
                                    <Cell key={`cell-profit-${index}`} fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Tooltip />
                            <Legend />
                        </PieChart>
                    </ResponsiveContainer>
                </>
            )}

            {salesEvolution.length > 0 && (
                <>
                    <h2 style={{ textAlign: 'center', marginTop: '4rem' }}>Monthly Sales Evolution</h2>
                    <ResponsiveContainer width="100%" height={400}>
                        <LineChart data={salesEvolution}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="month" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Line type="monotone" dataKey="sales" stroke="#8884d8" name="Sales" />
                        </LineChart>
                    </ResponsiveContainer>
                </>
            )}
        </div>
    );
};

export default ProductCharts;
