import React, { useEffect, useState } from "react";
import axios from "axios";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import "../styles/Raports.css";

const luni = [
    "", "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie",
    "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"
];

const UserReports = () => {
    const [monthlyData, setMonthlyData] = useState([]);
    const [ageData, setAgeData] = useState([]);
    const [statusData, setStatusData] = useState([]);
    const [countyData, setCountyData] = useState([]);
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

    useEffect(() => {
        axios.get("http://localhost:8080/users/users-by-month", {
            params: { year: selectedYear }
        }).then(res => {
            const complet = Array.from({ length: 12 }, (_, i) => {
                const gasit = res.data.find(item => item.month === i + 1);
                return {
                    month: i + 1,
                    count: gasit ? gasit.count : 0
                };
            });
            setMonthlyData(complet);
        });

        axios.get("http://localhost:8080/users/age-distribution")
            .then(res => {
                const lista = Object.entries(res.data).map(([grupa, count]) => ({ grupa, count }));
                setAgeData(lista);
            });

        axios.get("http://localhost:8080/users/verification-status")
            .then(res => {
                console.log("VERIFICATION STATUS RAW:", res.data);

                const lista = Object.entries(res.data).map(([status, count]) => {
                    const lower = status.toLowerCase();
                    const formatted = lower === "verified" ? "Verificat" :
                        lower === "unverified" ? "Neverificat" :
                            status;
                    return { status: formatted, count };
                });

                setStatusData(lista);
            });


        axios.get("http://localhost:8080/users/users-by-county")
            .then(res => {
                const lista = Object.entries(res.data).map(([judet, count]) => ({ judet, count }));
                setCountyData(lista);
            });

    }, [selectedYear]);

    const aniDisponibili = [];
    for (let an = 2024; an <= new Date().getFullYear(); an++) {
        aniDisponibili.push(an);
    }

    const exportToPDF = () => {
        const doc = new jsPDF();
        doc.setFontSize(18);
        doc.text(`Raport Utilizatori - ${selectedYear}`, 14, 15);

        autoTable(doc, {
            startY: 25,
            head: [["Luna", "Înregistrări"]],
            body: monthlyData.map(row => [luni[row.month], row.count])
        });

        doc.text("Distribuție pe grupe de vârstă", 14, doc.lastAutoTable.finalY + 10);
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 15,
            head: [["Grupa", "Utilizatori"]],
            body: ageData.map(row => [row.grupa, row.count])
        });

        doc.text("Status Verificare Conturi", 14, doc.lastAutoTable.finalY + 10);
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 15,
            head: [["Status", "Număr"]],
            body: statusData.map(row => [row.status, row.count])
        });

        doc.text("Utilizatori pe Județe", 14, doc.lastAutoTable.finalY + 10);
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 15,
            head: [["Județ", "Număr"]],
            body: countyData.map(row => [row.judet, row.count])
        });

        doc.save(`raport-utilizatori-${selectedYear}.pdf`);
    };

    return (
        <div className="report-container">
            <div className="report-header">
                <h2 className="report-title">👤 Rapoarte Utilizatori</h2>
                <select
                    value={selectedYear}
                    onChange={(e) => setSelectedYear(Number(e.target.value))}
                    className="year-select"
                >
                    {aniDisponibili.map((an) => (
                        <option key={an} value={an}>{an}</option>
                    ))}
                </select>
            </div>

            <div className="report-table-wrapper">
                <div className="report-table-header">
                    <h3 className="report-subtitle">Înregistrări pe Lună</h3>
                    <button onClick={exportToPDF} className="report-pdf-btn small">⬇ PDF</button>
                </div>
                <table className="report-table">
                    <thead><tr><th>Luna</th><th>Număr</th></tr></thead>
                    <tbody>
                    {monthlyData.map((row, i) => (
                        <tr key={i}><td>{luni[row.month]}</td><td>{row.count}</td></tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="report-table-wrapper">
                <h3 className="report-subtitle">Distribuție pe Grupe de Vârstă</h3>
                <table className="report-table">
                    <thead><tr><th>Grupă</th><th>Număr</th></tr></thead>
                    <tbody>
                    {ageData.map((row, i) => (
                        <tr key={i}><td>{row.grupa}</td><td>{row.count}</td></tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="report-table-wrapper">
                <h3 className="report-subtitle">Status Verificare</h3>
                <table className="report-table">
                    <thead><tr><th>Status</th><th>Număr</th></tr></thead>
                    <tbody>
                    {statusData.map((row, i) => (
                        <tr key={i}><td>{row.status}</td><td>{row.count}</td></tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="report-table-wrapper">
                <h3 className="report-subtitle">Distribuție pe Județe</h3>
                <table className="report-table">
                    <thead><tr><th>Județ</th><th>Număr</th></tr></thead>
                    <tbody>
                    {countyData.map((row, i) => (
                        <tr key={i}><td>{row.judet}</td><td>{row.count}</td></tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserReports;
