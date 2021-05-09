import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import { AuthenticationContextProvider } from "./context/authentication";

import "./index.css";

ReactDOM.render(
  <div className="background">
    <div className="blur">
      <BrowserRouter>
        <AuthenticationContextProvider>
          <App />
        </AuthenticationContextProvider>
      </BrowserRouter>
    </div>
  </div>,
  document.getElementById("root")
);
