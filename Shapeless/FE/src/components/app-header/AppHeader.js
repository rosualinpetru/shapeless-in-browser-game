import { useContext } from "react";
import { Link, NavLink, useHistory } from "react-router-dom";
import { toast } from "react-toastify";
import AuthenticationContext from "../../context/authentication";
import "./AppHeader.css";
import { ACCESS_TOKEN } from "../../constants";

function AppHeader() {
  const authContext = useContext(AuthenticationContext);
  let history = useHistory();

  function logOut() {
    toast.success("You're safely logged out!");
    localStorage.removeItem(ACCESS_TOKEN);
    authContext.deleteUserHandler();
    history.push("/");
  }

  return (
    <header className="app-header">
      <div className="container">
        <div className="app-branding">
          <Link to="/" className="app-title">
            Shapeless
          </Link>
        </div>
        <div className="app-options">
          <nav className="app-nav">
            {authContext.isAuthenticated ? (
              <ul>
                <li>
                  <NavLink to="/profile">Profile</NavLink>
                </li>
                <li>
                  <a onClick={logOut}>Logout</a>
                </li>
              </ul>
            ) : (
              <ul>
                <li>
                  <NavLink to="/login">Login</NavLink>
                </li>
                <li>
                  <NavLink to="/signup">Signup</NavLink>
                </li>
              </ul>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
}

export default AppHeader;
