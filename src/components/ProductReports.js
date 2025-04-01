import React, { useEffect, useState } from "react";
import axios from "axios";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import "../styles/Raports.css";

const luni = [
    "", "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie",
    "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"
];

const ProductReports = () => {
    const [reportData, setReportData] = useState([]);
    const [categoryData, setCategoryData] = useState([]);
    const [topSold, setTopSold] = useState([]);
    const [mostSold, setMostSold] = useState(null);
    const [mostProfitable, setMostProfitable] = useState(null);
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

    useEffect(() => {
        axios.get("http://localhost:8080/products/by-month", { params: { year: selectedYear } })
            .then(res => {
                const completatCuZero = Array.from({ length: 12 }, (_, i) => {
                    const lunaGasita = res.data.find(item => item.month === i + 1);
                    return { month: i + 1, count: lunaGasita ? lunaGasita.count : 0 };
                });
                setReportData(completatCuZero);
            });

        axios.get("http://localhost:8080/products/by-category")
            .then(res => {
                const lista = Object.entries(res.data).map(([categorie, count]) => ({ categorie, count }));
                setCategoryData(lista);
            });

        axios.get("http://localhost:8080/products/top-sold")
            .then(res => {
                console.log("TOP SOLD:", res.data);
                setTopSold(res.data);
            });

        axios.get("http://localhost:8080/products/most-sold")
            .then(res => {
                console.log("MOST SOLD:", res.data);
                setMostSold(res.data);
            });

        axios.get("http://localhost:8080/products/most-profitable")
            .then(res => {
                console.log("MOST PROFITABLE:", res.data);
                setMostProfitable(res.data);
            });
    }, [selectedYear]);

    const aniDisponibili = [];
    for (let an = 2024; an <= new Date().getFullYear(); an++) {
        aniDisponibili.push(an);
    }

    const exportToPDF = () => {
        const doc = new jsPDF();
        doc.setFontSize(18);
        doc.text(`Raport Produse pe Lună (${selectedYear})`, 14, 15);

        autoTable(doc, {
            startY: 25,
            head: [["Luna", "Număr produse"]],
            body: reportData.map(row => [luni[row.month], row.count])
        });

        doc.text("Produse pe Categorii", 14, doc.lastAutoTable.finalY + 10);
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 15,
            head: [["Categorie", "Număr produse"]],
            body: categoryData.map(d => [d.categorie, d.count])
        });

        doc.text("Top 5 Produse Vândute", 14, doc.lastAutoTable.finalY + 10);
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 15,
            head: [["Produs", "Cantitate"]],
            body: topSold.map(d => [d.productname, d.unitssold])
        });

        if (mostSold) {
            doc.text(`Cel mai vândut produs: ${mostSold.productname} (${mostSold.unitssold} buc.)`, 14, doc.lastAutoTable.finalY + 15);
        }

        if (mostProfitable) {
            doc.text(`Cel mai profitabil produs: ${mostProfitable.productname} (${mostProfitable.totalprofit} RON)`, 14, doc.lastAutoTable.finalY + 25);
        }

        doc.save(`raport-produse-${selectedYear}.pdf`);
    };

    return (
        <div className="report-container">
            <div className="report-header">
                <h2 className="report-title">📄 Rapoarte Produse</h2>
                <select
                    value={selectedYear}
                    onChange={(e) => setSelectedYear(Number(e.target.value))}
                    className="year-select"
                >
                    {aniDisponibili.map(an => (
                        <option key={an} value={an}>{an}</option>
                    ))}
                </select>
            </div>

            <div className="report-table-wrapper">
                <div className="report-table-header">
                    <h3 className="report-subtitle">Raport Produse pe Lună ({selectedYear})</h3>
                    <button onClick={exportToPDF} className="report-pdf-btn small">⬇ PDF</button>
                </div>
                <table className="report-table">
                    <thead><tr><th>Luna</th><th>Număr produse</th></tr></thead>
                    <tbody>
                    {reportData.map((row, i) => (
                        <tr key={i}><td>{luni[row.month]}</td><td>{row.count}</td></tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="report-table-wrapper">
                <h3 className="report-subtitle">Produse pe Categorii</h3>
                <table className="report-table">
                    <thead><tr><th>Categorie</th><th>Număr produse</th></tr></thead>
                    <tbody>
                    {categoryData.map((row, i) => (
                        <tr key={i}><td>{row.categorie}</td><td>{row.count}</td></tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="report-table-wrapper">
                <h3 className="report-subtitle">Top 5 Produse Vândute</h3>
                <table className="report-table">
                    <thead><tr><th>Produs</th><th>Cantitate</th></tr></thead>
                    <tbody>
                    {topSold.map((row, i) => (
                        <tr key={i}><td>{row.productname}</td><td>{row.unitssold}</td></tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {mostSold && (
                <div className="report-table-wrapper">
                    <h3 className="report-subtitle">Cel mai vândut produs</h3>
                    <p>{mostSold.productname} — {mostSold.unitssold} buc.</p>
                </div>
            )}

            {mostProfitable && (
                <div className="report-table-wrapper">
                    <h3 className="report-subtitle">Cel mai profitabil produs</h3>
                    <p>{mostProfitable.productname} — {mostProfitable.totalprofit} RON</p>
                </div>
            )}
        </div>
    );
};

export default ProductReports;
