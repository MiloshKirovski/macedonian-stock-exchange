import React, { useState } from 'react';
import styles from './IndicatorForm.module.css';
import { Link } from "react-router-dom";

const IndicatorForm = () => {
    const [companyName, setCompanyName] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [indicators, setIndicators] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const companyNames = [
        "ADIN", "ALK", "ALKB", "AMEH", "APTK", "ATPP", "AUMK", "BANA", "BGOR", "BIKF", "BIM", "BLTU",
        "CBNG", "CDHV", "CEVI", "CKB", "CKBKO", "DEBA", "DIMI", "EDST", "ELMA", "ELNC", "ENER", "EUHA",
        "EUMK", "EVRO", "FAKM", "FERS", "FKTL", "FROT", "FUBT", "GALE", "GDKM", "GECK", "GECT", "GIMS",
        "GRDN", "GRNT", "GRSN", "GRZD", "GTC", "GTRG", "IJUG", "INB", "INHO", "INOV", "INPR", "INTP",
        "JAKO", "JULI", "JUSK", "KARO", "KDFO", "KJUBI", "KKST", "KLST", "KMB", "KMPR", "KOMU", "KONF",
        "KONZ", "KORZ", "KPSS", "KULT", "KVAS", "LAJO", "LHND", "LOTO", "LOZP", "MAGP", "MAKP", "MAKS",
        "MB", "MERM", "MKSD", "MLKR", "MODA", "MPOL", "MPT", "MPTE", "MTUR", "MZHE", "MZPU", "NEME",
        "NOSK", "OBPP", "OILK", "OKTA", "OMOS", "OPFO", "OPTK", "ORAN", "OSPO", "OTEK", "PELK", "PGGV",
        "PKB", "POPK", "PPIV", "PROD", "PTRS", "RADE", "REPL", "RIMI", "RINS", "RZEK", "RZIT", "RZIZ",
        "RZLE", "RZLV", "RZTK", "RZUG", "RZUS", "SBT", "SDOM", "SIL", "SKON", "SKP", "SLAV", "SNBT",
        "SNBTO", "SOLN", "SPAZ", "SPAZP", "SPOL", "SSPR", "STB", "STBP", "STIL", "STOK", "TAJM", "TBKO",
        "TEAL", "TEHN", "TEL", "TETE", "TIKV", "TKPR", "TKVS", "TNB", "TRDB", "TRPS", "TRUB", "TSMP",
        "TSZS", "TTK", "TTKO", "UNI", "USJE", "VARG", "VFPM", "VITA", "VROS", "VSC", "VTKS", "ZAS", "ZILU",
        "ZILUP", "ZIMS", "ZKAR", "ZPKO", "ZPOG", "ZUAS"
    ];

    const API_URL = process.env.REACT_APP_API_URL ;

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await fetch(`${API_URL}/api/indicators/calculate`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    companyName,
                    startDate,
                    endDate
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Failed to fetch indicators');
            }

            const data = await response.json();
            setIndicators(data);
        } catch (error) {
            setError(error.message || 'An error occurred while fetching the data');
        } finally {
            setLoading(false);
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return new Intl.DateTimeFormat('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        }).format(date);
    };

    return (
        <div className={styles.pageContainer}>
            <div className={styles.content}>
                <div className={styles.header}>
                    <h1>Техничка Анализа</h1>
                    <p className={styles.subtitle}>
                        Оваа алатка за анализа на акции користи 10 клучни технички индикатори, вклучувајќи RSI, MACD, SMA, EMA,
                        Болинџерови ленти и On-Balance Volume, за да ви обезбеди детални увиди за перформансите на акциите.
                        Ги пресметува дневните, неделните и месечните трендови за сите индикатори во даден периор.
                    </p>
                </div>

                <div className={styles.card}>
                    <form onSubmit={handleSubmit} className={styles.form}>
                        <div className={styles.inputGroup}>
                            <label htmlFor="companyName">Име на компанија</label>
                            <div className={styles.inputWrapper}>
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
                                <i className={`${styles.icon} ${styles.companyIcon}`}></i>
                            </div>
                        </div>

                        <div className={styles.dateGroup}>
                            <div className={styles.inputGroup}>
                                <label htmlFor="startDate">Почетен датум</label>
                                <div className={styles.inputWrapper}>
                                    <input
                                        type="date"
                                        id="startDate"
                                        value={startDate}
                                        onChange={(e) => setStartDate(e.target.value)}
                                        required
                                    />
                                    <i className={`${styles.icon} ${styles.dateIcon}`}></i>
                                </div>
                            </div>

                            <div className={styles.inputGroup}>
                                <label htmlFor="endDate">Краен датум</label>
                                <div className={styles.inputWrapper}>
                                    <input
                                        type="date"
                                        id="endDate"
                                        value={endDate}
                                        onChange={(e) => setEndDate(e.target.value)}
                                        required
                                    />
                                    <i className={`${styles.icon} ${styles.dateIcon}`}></i>
                                </div>
                            </div>
                        </div>

                        <button type="submit" className={styles.submitButton} disabled={loading}>
                            {loading ? (
                                <>
                                    <span className={styles.spinner}></span>
                                    Анализирање на индикатори...
                                </>
                            ) : (
                                'Калкулирај индикатори'
                            )}
                        </button>
                    </form>

                    {error && (
                        <div className={styles.errorContainer}>
                            <p className={styles.error}>{error}</p>
                        </div>
                    )}

                    {indicators && (
                        <div className={styles.resultsContainer}>
                            {['daily', 'weekly', 'monthly'].map((period) => (
                                <div key={period} className={styles.periodSection}>
                                    <h3 className={styles.periodTitle}>
                                        {period === 'daily' ? 'Дневен Индикатор' :
                                            period === 'weekly' ? 'Неделен Индикатор' :
                                                'Месечен Индикатор'}
                                    </h3>
                                    <div className={styles.tableWrapper}>
                                        <table className={styles.dataTable}>
                                            <thead>
                                            <tr>
                                                <th>Датум</th>
                                                <th>Цена</th>
                                                <th>Волумен</th>
                                                <th>RSI</th>
                                                <th>Stochastic</th>
                                                <th>MACD</th>
                                                <th>CCI</th>
                                                <th>ROC</th>
                                                <th>SMA</th>
                                                <th>EMA</th>
                                                <th>WMA</th>
                                                <th>BB Upper</th>
                                                <th>BB Lower</th>
                                                <th>OBV</th>
                                                <th>Сигнал</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {indicators[period].map((item, index) => (
                                                <tr key={index}>
                                                    <td>{formatDate(item.Date)}</td>
                                                    <td>{item.Price}</td>
                                                    <td>{item.Volume}</td>
                                                    <td>{item.RSI}</td>
                                                    <td>{item.Stochastic}</td>
                                                    <td>{item.MACD}</td>
                                                    <td>{item.CCI}</td>
                                                    <td>{item.ROC}</td>
                                                    <td>{item.SMA}</td>
                                                    <td>{item.EMA}</td>
                                                    <td>{item.WMA}</td>
                                                    <td>{item.Bollinger_Bands_Upper}</td>
                                                    <td>{item.Bollinger_Bands_Lower}</td>
                                                    <td>{item.OBV}</td>
                                                    <td>
                                                            <span className={`${styles.signalBadge} ${styles[item.Signal.toLowerCase()]}`}>
                                                                {item.Signal}
                                                            </span>
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            ))}
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

export default IndicatorForm;
