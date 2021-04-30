import React from "react"
import logo from './logo.svg';
import './App.css';
import background from "./bkg.png";


import Facebook from './components/Facebook';

function App() {
  return (
    <div className="App" style={{ backgroundImage: `url(${background})` }}>
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
           <h2>Shapeless's Facebook Login</h2>
        </p>
        <p>
          To get started, authenticate with Facebook.
        </p>
        <Facebook />
      </header>
    </div>
  );
}

export default App;
