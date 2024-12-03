import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './components/HomePage';
import ContentsPage from './components/ContentsPage';
import List from './components/List';
import 'bootstrap/dist/css/bootstrap.min.css';
// Google Fonts import

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} /> {}
                <Route path="/contents" element={<ContentsPage />} /> {}
                <Route path="/list" element={<List />} /> {}
            </Routes>
        </Router>
    );
};

export default App;
