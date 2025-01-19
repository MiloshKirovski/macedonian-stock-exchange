import React, { useState } from 'react';
import styles from './List.module.css';
import { Link } from 'react-router-dom';

const StockList = () => {
    const [data, setData] = useState([]);
    const [companyName, setCompanyName] = useState('');
    const [date, setDate] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const companies = [
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

    const API_URL = process.env.REACT_APP_API_URL;

    const fetchData = async () => {
        setIsLoading(true);
        try {
            const response = await fetch(`${API_URL}/api/currency/company/${companyName}/date/${date}`);
            if (response.ok) {
                const result = await response.json();
                setData(result);
            } else {
                console.error('Failed to fetch data:', await response.text());
            }
        } catch (error) {
            console.error('Error fetching data:', error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <h1>Информации за акции</h1>
            </header>

            <main className={styles.mainContent}>
                <section className={styles.searchSection}>
                    <div className={styles.formGroup}>
                        <select
                            value={companyName}
                            onChange={(e) => setCompanyName(e.target.value)}
                            className={styles.inputField}
                        >
                            <option value="" disabled>Одберете компанија</option>
                            {companies.map((company, index) => (
                                <option key={index} value={company}>{company}</option>
                            ))}
                        </select>
                        <input
                            type="text"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
                            placeholder="Внесете датум (dd.mm.yyyy)"
                            className={styles.inputField}
                        />
                        <button
                            onClick={fetchData}
                            className={styles.fetchButton}
                            disabled={isLoading || !companyName || !date}
                        >
                            {isLoading ? 'Loading...' : 'Извлечи информации'}
                        </button>
                    </div>
                </section>

                <section className={styles.resultsSection}>
                    {data.length > 0 ? (
                        <div className={styles.resultsGrid}>
                            {data.map((stock, index) => (
                                <div key={index} className={styles.stockCard}>
                                    <h2 className={styles.stockTitle}>{stock.companyName}</h2>
                                    <p className={styles.stockDate}>{stock.date}</p>
                                    <p className={styles.stockDetail}>Последна цена: ${stock.priceOfLastTransaction}</p>
                                    <p className={styles.stockDetail}>Просечна цена: ${stock.avgPrice}</p>
                                    <p className={`${styles.stockDetail} ${Number(stock.percentChange) >= 0 ? styles.positive : styles.negative}`}>
                                        Промена: {stock.percentChange}%
                                    </p>
                                </div>
                            ))}
                        </div>
                    ) : (
                        !isLoading && <p></p>
                    )}
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