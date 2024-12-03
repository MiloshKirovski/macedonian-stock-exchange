import './ContentsPage.css';
import React from 'react';
import { Link } from 'react-router-dom';

const ContentsPage = () => {
    return (
        <div className="contents-page">
            <h1>Содржина</h1>
            <ul>
                <li><button>1.Техничка Анализа</button></li>
                <li><button>2.Фундаментална Анализа</button></li>
                <li><button>3.LSTM Предвидување на Акциски Цени</button></li>
                <li>
                    <Link to="/list">
                        <button>4.Податоци за Акции</button> {}
                    </Link>
                </li>
            </ul>
        </div>
    );
};
export default ContentsPage;
