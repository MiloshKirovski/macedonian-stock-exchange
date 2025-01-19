import React, { useState } from 'react';
import styles from './NLPage.module.css';
import { Link } from 'react-router-dom';

const ResultsTable = ({ results }) => (
    <div className={styles.resultsContainer}>
        <div className={styles.resultsHeader}>
            Резултати за компанијата
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
);

const NLPage = () => {
    const [companyName, setCompanyName] = useState('');
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const API_URL = process.env.REACT_APP_API_URL ;

    const companyNames = [
        "TTK", "REPL", "MZHE", "VROS", "ZPOG", "ALK", "CKB", "MPTE", "MTUR", "NOSK", "MODA",
        "SIL", "VITA", "FAKM", "PKB", "KONZ", "OILK", "PPIV", "UNIPO2", "GECK", "GECT", "USJE"
    ];

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const response = await fetch(`${API_URL}/api/sentiment-analysis`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ companyName }),
            });

            if (!response.ok) {
                throw new Error('Error fetching data');
            }

            const data = await response.json();
            setResults([data]);
        } catch (error) {
            console.error('Error fetching data:', error);
            setError('Неуспешно вчитување на податоци');
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
                    <form onSubmit={handleSubmit} className={styles.form}>
                        <div className={styles.inputGroup}>
                            <label htmlFor="companyName">Изберете име на компанија</label>
                            <select
                                id="companyName"
                                value={companyName}
                                onChange={(e) => setCompanyName(e.target.value)}
                                required
                            >
                                <option value="">Изберете компанија...</option>
                                {companyNames.map((name, index) => (
                                    <option key={index} value={name}>
                                        {name}
                                    </option>
                                ))}
                            </select>
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

                    {error && (
                        <div className={styles.errorMessage}>
                            {error}
                        </div>
                    )}

                    {results.length > 0 && !error && <ResultsTable results={results} />}
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
