import React, { useState } from 'react';
import './List.css';

const List = () => {
    const [data, setData] = useState([]);
    const [companyName, setCompanyName] = useState('');
    const [date, setDate] = useState('');

    const fetchDataByCompanyAndDate = () => {
        fetch(`http://localhost:8080/api/currency/company/${companyName}/date/${date}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                console.log('Fetched data:', data);
                setData(data);
            })
            .catch((error) => console.error('Error fetching data:', error));
    };

    return (
        <div className="list-container">
            <h1 className="title">Податоци за Акции</h1>

            <div className="input-group">
                <input
                    type="text"
                    placeholder="Внеси име на Компанија"
                    value={companyName}
                    onChange={(e) => setCompanyName(e.target.value)}
                    className="input-field"
                />
                <input
                    type="text"
                    placeholder="Внеси Датум (e.g., 16.9.2020)"
                    value={date}
                    onChange={(e) => setDate(e.target.value)}
                    className="input-field"
                />
                <button onClick={fetchDataByCompanyAndDate} className="fetch-button">
                    Преземи податоци
                </button>
            </div>

            {data.length > 0 ? (
                <div className="data-list">
                    {data.map((obj) => (
                        <div key={obj.companyName + '-' + obj.date} className="data-item">
                            <div className="data-card">
                                <h3 className="company-name">{obj.companyName}</h3>
                                <p className="data-date">Датум: {obj.date}</p>
                                <p><strong>Последна Трансакциска Цена:</strong> {obj.priceOfLastTransaction}</p>
                                <p><strong>Просечна цена:</strong> {obj.avgPrice}</p>
                                <p><strong>Процентуална Промена:</strong> {obj.percentChange}</p>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <p className="no-data">Нема податоци.</p>
            )}
        </div>
    );
};

export default List;
