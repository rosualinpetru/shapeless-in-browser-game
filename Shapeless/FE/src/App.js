import { useState, useEffect, useContext } from "react";
import { Route, Switch } from "react-router-dom";
import { ToastContainer, Slide } from "react-toastify";

import PrivateRoute from "./components/private-route/PrivateRoute";
import AppHeader from "./components/app-header/AppHeader";
import LoadingIndicator from "./components/loading/LoadingIndicator";
import OAuth2RedirectHandler from "./components/oauth2/OAuth2RedirectHandler";

import Home from "./pages/home/Home";
import Login from "./pages/login/Login";
import Signup from "./pages/signup/Signup";
import Profile from "./pages/profile/Profile";
import Leaderboard from "./pages/leaderboard/Leaderboard";
import CreateRoom from "./pages/create-room/CreateRoom";
import Rooms from "./pages/rooms/Rooms";
import NotFound from "./pages/NotFound";

import { getCurrentUser } from "./api/APIUtils";
import AuthenticationContext from "./context/authentication";

import "react-toastify/dist/ReactToastify.css";

function App() {
  const authContext = useContext(AuthenticationContext);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    getCurrentUser()
      .then((response) => {
        authContext.setUserHandler(response);
        setIsLoading(false);
      })
      .catch(() => {
        authContext.deleteUserHandler();
        setIsLoading(false);
      });
  }, []);

  if (isLoading) {
    return <LoadingIndicator />;
  }

  return (
    <div className="app">
      <div className="app-top-box">
        <AppHeader />
      </div>
      <div className="app-body">
        <Switch>
          <Route exact path="/">
            <Home />
          </Route>
          <PrivateRoute path="/profile">
            <Profile />
          </PrivateRoute>
          <PrivateRoute path="/leaderboard">
            <Leaderboard />
          </PrivateRoute>
          <PrivateRoute path="/rooms">
            <Rooms />
          </PrivateRoute>
          <PrivateRoute path="/create-room">
            <CreateRoom />
          </PrivateRoute>
          <PrivateRoute path="/rooms">
            <Leaderboard />
          </PrivateRoute>
          <Route path="/login">
            <Login />
          </Route>
          <Route path="/signup">
            <Signup />
          </Route>
          <Route path="/oauth2/redirect">
            <OAuth2RedirectHandler />
          </Route>
          <Route>
            <NotFound />
          </Route>
        </Switch>
      </div>
      <ToastContainer
        position="bottom-right"
        transition={Slide}
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover={false}
      />
    </div>
  );
}

export default App;
