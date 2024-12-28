import React from 'react';
import { Link } from 'react-router-dom';
import './ContentsPage.css';

const ContentsPage = () => {
    const menuItems = [
        {
            id: 1,
            title: "Техничка Анализа",
            path: "/indicator",
            icon: "📊"
        },
        {
            id: 2,
            title: "Фундаментална Анализа",
            path: "/nlp",
            icon: "📈"
        },
        {
            id: 3,
            title: "LSTM Предвидување",
            path: "/lstm",
            icon: "🤖"
        },
        {
            id: 4,
            title: "Податоци за Акции",
            path: "/list",
            icon: "📑"
        }
    ];

    return (
        <div className="contents-page">
            <div className="hero-section">
                <div id="sodrzina-one">Содржина</div>
                <div className="subtitle">Изберете категорија</div>
            </div>

            <div className="menu-grid">
                {menuItems.map((item) => (
                    <Link
                        to={item.path}
                        key={item.id}
                        className="menu-item-link"
                    >
                        <div className="menu-item">
                            <div className="menu-item-content">
                                <span className="icon">{item.icon}</span>
                                <span className="number">{item.id}</span>
                                <h2>{item.title}</h2>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>

            <div className="back-button-container">
                <Link to="/" className="back-button">Назад</Link>
            </div>
        </div>
    );
};

export default ContentsPage;
