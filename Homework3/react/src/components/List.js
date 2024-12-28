import React, { useState } from 'react';
import './List.css';
import {Link} from "react-router-dom";
import styles from "./LSTMPage.module.css";

const StockList = () => {
    const [data, setData] = useState([]);
    const [companyName, setCompanyName] = useState('');
    const [date, setDate] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const fetchData = async () => {
        setIsLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/api/currency/company/${companyName}/date/${date}`);
            const result = await response.json();
            setData(result);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
        setIsLoading(false);
    };

    return (
        <div className="container" id="con1">
            <header className="header">
                <h1>Информации за акции</h1>
            </header>
            <main className="main-content">
                <section className="search-section">
                    <div className="form-group">
                        <input
                            type="text"
                            value={companyName}
                            onChange={(e) => setCompanyName(e.target.value)}
                            placeholder="Внесете име на компанија пр.BANA"
                            className="input-field"
                        />
                        <input
                            type="text"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
                            placeholder="Внесете датум (dd.mm.yyyy)"
                            className="input-field"
                        />
                        <button
                            onClick={fetchData}
                            className="fetch-button"
                            disabled={isLoading}
                        >
                            {isLoading ? 'Loading...' : 'Извлечи информации'}
                        </button>
                    </div>
                </section>
                <section className="results-section">

                        <div className="results-grid">
                            {data.map((stock, index) => (
                                <div key={index} className="stock-card">
                                    <h2 className="stock-title">{stock.companyName}</h2>
                                    <p className="stock-date">{stock.date}</p>
                                    <p className="stock-detail">Последна цена: ${stock.priceOfLastTransaction}</p>
                                    <p className="stock-detail">Просечна цена: ${stock.avgPrice}</p>
                                    <p className={`stock-detail ${Number(stock.percentChange) >= 0 ? 'positive' : 'negative'}`}>
                                        Промена: {stock.percentChange}%
                                    </p>
                                </div>
                            ))}
                        </div>
                </section>
            </main>
            <Link to="/contents" className={styles.backButton}>
                <span className={styles.backIcon}></span>
                Назад
            </Link>
        </div>
    );
};
export default StockList;