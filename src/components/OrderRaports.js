import React, { useEffect, useState } from "react";
import axios from "axios";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import "../styles/Raports.css";

const luni = [
  "", "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie",
  "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"
];

const OrderReports = () => {
  const [ordersByMonth, setOrdersByMonth] = useState([]);
  const [revenueByMonth, setRevenueByMonth] = useState([]);
  const [ordersByCounty, setOrdersByCounty] = useState([]);
  const [statusDistribution, setStatusDistribution] = useState([]);
  const [topCustomers, setTopCustomers] = useState([]);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

  useEffect(() => {
    axios.get("http://localhost:8080/orders/orders-by-month", { params: { year: selectedYear } })
      .then(res => {
        const complet = Array.from({ length: 12 }, (_, i) => {
          const gasit = res.data.find(item => item.month === i + 1);
          return {
            month: i + 1,
            count: gasit ? gasit.count : 0
          };
        });
        setOrdersByMonth(complet);
      });

    axios.get("http://localhost:8080/orders/revenue-by-month", { params: { year: selectedYear } })
      .then(res => setRevenueByMonth(res.data));

    axios.get("http://localhost:8080/orders/orders-by-county", { params: { year: selectedYear } })
      .then(res => {
        const lista = Object.entries(res.data).map(([judet, count]) => ({ judet, count }));
        setOrdersByCounty(lista);
      });

    axios.get("http://localhost:8080/orders/status-distribution", { params: { year: selectedYear } })
      .then(res => {
        const lista = Object.entries(res.data).map(([status, count]) => ({ status, count }));
        setStatusDistribution(lista);
      });

    axios.get("http://localhost:8080/orders/top-customers", { params: { year: selectedYear } })
      .then(res => setTopCustomers(res.data));

  }, [selectedYear]);

  const aniDisponibili = [];
  for (let an = 2024; an <= new Date().getFullYear(); an++) {
    aniDisponibili.push(an);
  }

  const exportToPDF = () => {
    const doc = new jsPDF();
    doc.setFontSize(18);
    doc.text(`Raport Comenzi - ${selectedYear}`, 14, 15);

    autoTable(doc, {
      startY: 25,
      head: [["Luna", "Comenzi"]],
      body: ordersByMonth.map(row => [luni[row.month], row.count])
    });

    doc.text("Venituri pe Lună", 14, doc.lastAutoTable.finalY + 10);
    autoTable(doc, {
      startY: doc.lastAutoTable.finalY + 15,
      head: [["Luna", "Venit (RON)"]],
      body: revenueByMonth.map(row => [luni[row.month], row.total])
    });

    doc.text("Comenzi pe Județe", 14, doc.lastAutoTable.finalY + 10);
    autoTable(doc, {
      startY: doc.lastAutoTable.finalY + 15,
      head: [["Județ", "Număr"]],
      body: ordersByCounty.map(row => [row.judet, row.count])
    });

    doc.text("Statusuri Comenzi", 14, doc.lastAutoTable.finalY + 10);
    autoTable(doc, {
      startY: doc.lastAutoTable.finalY + 15,
      head: [["Status", "Număr"]],
      body: statusDistribution.map(row => [row.status, row.count])
    });

    doc.text("Top Clienți", 14, doc.lastAutoTable.finalY + 10);
    autoTable(doc, {
      startY: doc.lastAutoTable.finalY + 15,
      head: [["Client", "Comenzi"]],
      body: topCustomers.map(row => [`${row.firstname} ${row.lastname}`, row.orderCount])
    });

    doc.save(`raport-comenzi-${selectedYear}.pdf`);
  };

  return (
    <div className="report-container">
      <div className="report-header">
        <h2 className="report-title">📦 Rapoarte Comenzi</h2>
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
          <h3 className="report-subtitle">Comenzi pe Lună</h3>
          <button onClick={exportToPDF} className="report-pdf-btn small">⬇ PDF</button>
        </div>
        <table className="report-table">
          <thead><tr><th>Luna</th><th>Comenzi</th></tr></thead>
          <tbody>
            {ordersByMonth.map((row, i) => (
              <tr key={i}><td>{luni[row.month]}</td><td>{row.count}</td></tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="report-table-wrapper">
        <h3 className="report-subtitle">Venituri pe Lună</h3>
        <table className="report-table">
          <thead><tr><th>Luna</th><th>Venit (RON)</th></tr></thead>
          <tbody>
            {revenueByMonth.map((row, i) => (
              <tr key={i}><td>{luni[row.month]}</td><td>{row.total}</td></tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="report-table-wrapper">
        <h3 className="report-subtitle">Comenzi pe Județe</h3>
        <table className="report-table">
          <thead><tr><th>Județ</th><th>Număr</th></tr></thead>
          <tbody>
            {ordersByCounty.map((row, i) => (
              <tr key={i}><td>{row.judet}</td><td>{row.count}</td></tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="report-table-wrapper">
        <h3 className="report-subtitle">Statusuri Comenzi</h3>
        <table className="report-table">
          <thead><tr><th>Status</th><th>Număr</th></tr></thead>
          <tbody>
            {statusDistribution.map((row, i) => (
              <tr key={i}><td>{row.status}</td><td>{row.count}</td></tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="report-table-wrapper">
        <h3 className="report-subtitle">Top Clienți</h3>
        <table className="report-table">
          <thead><tr><th>Nume</th><th>Comenzi</th></tr></thead>
          <tbody>
            {topCustomers.map((row, i) => (
              <tr key={i}><td>{row.firstname} {row.lastname}</td><td>{row.orderCount}</td></tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default OrderReports;