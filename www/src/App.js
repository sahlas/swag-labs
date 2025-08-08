import logo from './logo.svg';
import './App.css';
import React from 'react';

const AllureReportViewer = () => {
  return (
    <div style={{ width: '100%', height: '800px' }}>
      <iframe
        src="https://sahlas.github.io/swag-labs-java-playwright-cucumber/allure-report/index.html"
        title="Allure Report"
        style={{ width: '100%', height: '100%', border: 'none' }}
      />
    </div>
  );
};

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <AllureReportViewer />
      </header>
    </div>
  );
}

export default App;