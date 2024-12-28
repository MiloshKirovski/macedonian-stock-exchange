import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './components/HomePage';
import ContentsPage from './components/ContentsPage';
import List from './components/List';
import 'bootstrap/dist/css/bootstrap.min.css';
import IndicatorForm from "./components/IndicatorForm";
import LSTMPage from './components/LSTMPage';
import NLPage from './components/NLPage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} /> {}
                <Route path="/contents" element={<ContentsPage />} /> {}
                <Route path="/indicator" element={<IndicatorForm />} /> {}
                <Route path="/list" element={<List />} /> {}
                <Route path="/lstm" element={<LSTMPage />} /> {}
                <Route path="/nlp" element={<NLPage />} /> {}
            </Routes>
        </Router>
    );
};

export default App;
