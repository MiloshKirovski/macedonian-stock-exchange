import React, { useState } from 'react';
import styles from './NLPage.module.css';
import { Link } from "react-router-dom";

const NLPage = () => {
    const [companyName, setCompanyName] = useState('');
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/sentiment-analysis', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ companyName }),
            });

            const data = await response.json();
            setResults([data]);
        } catch (error) {
            console.error('Error fetching data:', error);
            setResults([]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.pageContainer}>
            <div className={styles.content}>
                <div className={styles.header}>
                    <h1>Фундаментална Анализа</h1>
                    <p className={styles.subtitle}>Напредна анализа на сентимент на компании</p>
                </div>

                <div className={styles.card}>
                    <div className={styles.infoSection}>
                        <p className={styles.description}>
                            Во оваа секција може да користите модел за анализа на сентимент, со овој модел
                            направивме анализа на сите вести поврзани со компаниите со кои работиме. Веќе не
                            мора да ги читате сите вести за сите компаниите за да одлучите дали истите се
                            позитивни или негативни.
                        </p>
                    </div>

                    <form onSubmit={handleSubmit} className={styles.form}>
                        <div className={styles.inputGroup}>
                            <label htmlFor="companyName">Внесете име на компанија</label>
                            <div className={styles.inputWrapper}>
                                <input
                                    type="text"
                                    id="companyName"
                                    value={companyName}
                                    onChange={(e) => setCompanyName(e.target.value)}
                                    required
                                    placeholder="На пример: TTK"
                                />
                                <i className={`${styles.icon} ${styles.searchIcon}`}></i>
                            </div>
                        </div>

                        <button type="submit" className={styles.submitButton} disabled={loading}>
                            {loading ? (
                                <>
                                    <span className={styles.spinner}></span>
                                    Анализирам податоци...
                                </>
                            ) : (
                                'Започни Анализа'
                            )}
                        </button>
                    </form>

                    {results.length > 0 && (
                        <div className={styles.resultsContainer}>
                            <div className={styles.resultsHeader}>
                                Резултати за компанијата: {companyName}
                            </div>
                            <div className={styles.tableWrapper}>
                                <table className={styles.table}>
                                    <thead>
                                    <tr>
                                        <th>Компанија</th>
                                        <th>Вкупен број на известувања</th>
                                        <th>Позитивни %</th>
                                        <th>Негативни %</th>
                                        <th>Неутрални %</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {results.map((item, index) => (
                                        <tr key={index}>
                                            <td>{item["Company"]}</td>
                                            <td>{item["Total number of notifications"]}</td>
                                            <td className={styles.positiveValue}>
                                                {parseFloat(item["Positive %"]).toFixed(2)}%
                                            </td>
                                            <td className={styles.negativeValue}>
                                                {parseFloat(item["Negative %"]).toFixed(2)}%
                                            </td>
                                            <td className={styles.neutralValue}>
                                                {parseFloat(item["Neutral %"]).toFixed(2)}%
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}
                </div>

                <Link to="/contents" className={styles.backButton}>
                    <span className={styles.backIcon}></span>
                    Назад
                </Link>
            </div>
        </div>
    );
};

export default NLPage;