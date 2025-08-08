import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
            <div style={{ width: '100%', height: '800px' }}>
              <iframe
                src="https://sahlas.github.io/swag-labs-java-playwright-cucumber/allure-report/index.html"
                title="Allure Report"
                style={{ width: '100%', height: '100%', border: 'none' }}
              />
            </div>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
