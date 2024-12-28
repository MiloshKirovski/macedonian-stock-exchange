import React, { useState } from 'react';
import styles from './LSTMPage.module.css';
import { Link } from "react-router-dom";

const LSTMPage = () => {
    const [companyName, setCompanyName] = useState('');
    const [companyPrice, setCompanyPrice] = useState('');
    const [predictedPrice, setPredictedPrice] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await fetch('http://localhost:8080/api/lstm', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    companyName,
                    companyPrice,
                }),
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            setPredictedPrice(data.predictedPrice);
        } catch (err) {
            setError('Error fetching predicted price');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.pageContainer}>
            <div className={styles.content}>
                <div className={styles.header}>
                    <h1>ВИ Маркет Предиктор</h1>
                    <p className={styles.subtitle}>Во овој дел може да искористите техники од машинско учење за да предвидите идни цени на акции од интерес. Моментално располагаме со 161 модел секој посебно граден за компаниите со кои може да работите на апликацијата. Овие модели се обучени на историски податоци за акциите и нудат солидни предвидувања кои ќе ви помогнат да донесете подобри одлуки.</p>
                </div>

                <div className={styles.card}>
                    <form onSubmit={handleSubmit} className={styles.form}>
                        <div className={styles.inputGroup}>
                            <label htmlFor="companyName">Име на компанија</label>
                            <div className={styles.inputWrapper}>
                                <input
                                    type="text"
                                    id="companyName"
                                    value={companyName}
                                    onChange={(e) => setCompanyName(e.target.value)}
                                    required
                                    placeholder="Внесете име на компанија..."
                                />
                                <i className={`${styles.icon} ${styles.companyIcon}`}></i>
                            </div>
                        </div>

                        <div className={styles.inputGroup}>
                            <label htmlFor="companyPrice">Цена на акција</label>
                            <div className={styles.inputWrapper}>
                                <input
                                    type="number"
                                    id="companyPrice"
                                    value={companyPrice}
                                    onChange={(e) => setCompanyPrice(e.target.value)}
                                    required
                                    placeholder="Внесете моментална цена..."
                                    step="0.01"
                                />
                                <i className={`${styles.icon} ${styles.priceIcon}`}></i>
                            </div>
                        </div>

                        <button type="submit" className={styles.submitButton} disabled={loading}>
                            {loading ? (
                                <>
                                    <span className={styles.spinner}></span>
                                    Анализирање на цените на акциите на пазарот...
                                </>
                            ) : (
                                'Предвиди цена'
                            )}
                        </button>
                    </form>

                    {error && (
                        <div className={styles.errorContainer}>
                            <p className={styles.error}>{error}</p>
                        </div>
                    )}

                    {predictedPrice !== null && (
                        <div className={styles.predictionContainer}>
                            <div className={styles.predictionHeader}>Предвидена цена</div>
                            <div className={styles.predictionValue}>
                                ${Number(predictedPrice).toLocaleString('en-US', {
                                minimumFractionDigits: 2,
                                maximumFractionDigits: 2
                            })}
                            </div>
                            <div className={styles.predictionNote}>

                                Проценета идна цена на акциите врз основа на анализа на пазарот                            </div>
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

export default LSTMPage;