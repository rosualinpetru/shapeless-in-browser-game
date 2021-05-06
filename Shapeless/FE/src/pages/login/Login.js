import { useEffect, useRef, useContext } from "react";
import {
  GOOGLE_AUTH_URL,
  FACEBOOK_AUTH_URL,
  ACCESS_TOKEN,
} from "../../constants";
import { toast } from "react-toastify";
import { Link, useHistory, useLocation } from "react-router-dom";
import { login, getCurrentUser } from "../../api/APIUtils";
import AuthenticationContext from "../../context/authentication";
import fbLogo from "../../images/fb-logo.png";
import googleLogo from "../../images/google-logo.png";

import "./Login.css";

function Login(props) {
  const authContext = useContext(AuthenticationContext);
  const history = useHistory();
  const location = useLocation();

  useEffect(() => {
    if (location.state && location.state.error) {
      setTimeout(() => {
        toast.error(location.state.error);
        history.replace({
          pathname: location.pathname,
          state: {},
        });
      }, 100);
    }
    if (authContext.isAuthenticated) {
      history.goBack();
    }
  }, []);

  return (
    <div className="login-container">
      <div className="login-content">
        <h1 className="login-title">Login with</h1>
        <SocialLogin />
        <div className="or-separator">
          <span className="or-text">OR</span>
        </div>
        <LoginForm {...props} />
        <span className="signup-link">
          New user? <Link to="/signup">Sign up!</Link>
        </span>
      </div>
    </div>
  );
}

function SocialLogin(props) {
  return (
    <div className="social-login">
      <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
        <img src={googleLogo} alt="Google" /> Log in with Google
      </a>
      <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
        <img src={fbLogo} alt="Facebook" /> Log in with Facebook
      </a>
    </div>
  );
}

function LoginForm(props) {
  const history = useHistory();
  const authContext = useContext(AuthenticationContext);
  const emailRef = useRef();
  const passwordRef = useRef();

  function submitHandler(event) {
    event.preventDefault();

    const loginRequest = {
      email: emailRef.current.value,
      password: passwordRef.current.value,
    };

    login(loginRequest)
      .then((response) => {
        localStorage.setItem(ACCESS_TOKEN, response.accessToken);
      })
      .then(() => {
        return getCurrentUser();
      })
      .then((response) => {
        toast.success("You're successfully logged in!");
        authContext.setUserHandler(response);
        history.push("/profile");
      })
      .catch((error) => {
        toast.error(
          error.message || "Oops! Something went wrong. Please try again!"
        );
      });
  }

  return (
    <form onSubmit={submitHandler}>
      <div className="form-item">
        <input
          type="email"
          name="email"
          className="form-control"
          placeholder="Email"
          ref={emailRef}
          required
        />
      </div>
      <div className="form-item">
        <input
          type="password"
          name="password"
          className="form-control"
          placeholder="Password"
          ref={passwordRef}
          required
        />
      </div>
      <div className="form-item">
        <button type="submit" className="btn btn-block btn-primary">
          Login
        </button>
      </div>
    </form>
  );
}

export default Login;
