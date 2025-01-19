import React from 'react';
import { Link } from 'react-router-dom';
import './HomePage.css';

const HomePage = () => {
    return (
        <div className="home-page-container">
            <div className="content-container">
                <div className="header-container">
                    <h1 className="page-header text-dark mb-3" id="naslov_one">
                        Добредојдовте на Македонскиот Волстрит!
                    </h1>
                    <p className="lead text-muted mb-4">
                        Оваа апликација е создадена за да ви помогне да донесете информирани одлуки за тргување со акции.
                        Апликацијата комбинира мануелно истражување, техничка анализа, фундаментална анализа и напредни предвидувања со помош на вештачка интелигенција за да обезбеди сеопфатен преглед на пазарните трендови.
                    </p>
                    <Link to="/contents">
                        <button className="btn-primary btn-lg">Содржина</button>
                    </Link>
                </div>

                <div className="image-container">
                    <img src="/img.png" alt="Logo" className="home-page-image" />
                </div>
            </div>
        </div>
    );
};

export default HomePage;
