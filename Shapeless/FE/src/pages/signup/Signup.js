import { useRef, useContext, useEffect } from "react";

import { Link, useLocation, useHistory } from "react-router-dom";
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL } from "../../constants";
import { signup } from "../../api/APIUtils";
import { toast } from "react-toastify";
import AuthenticationContext from "../../context/authentication";

import fbLogo from "../../images/fb-logo.png";
import googleLogo from "../../images/google-logo.png";

import "./Signup.css";

function Signup(props) {
  let location = useLocation();
  let history = useHistory();
  const authContext = useContext(AuthenticationContext);

  useEffect(() => {
    if (authContext.isAuthenticated) {
      history.push({
        pathname: "/profile",
        state: { from: location },
      });
    }
  }, []);

  return (
    <div className="signup-container">
      <div className="signup-content">
        <h1 className="signup-title">Signup with</h1>
        <SocialSignup />
        <div className="or-separator">
          <span className="or-text">OR</span>
        </div>
        <SignupForm {...props} />
        <span className="login-link">
          Already have an account? <Link to="/login">Login!</Link>
        </span>
      </div>
    </div>
  );
}

function SocialSignup() {
  return (
    <div className="social-signup">
      <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
        <img src={googleLogo} alt="Google" /> Sign up with Google
      </a>
      <a className="btn btn-block social-btn facebook" href={FACEBOOK_AUTH_URL}>
        <img src={fbLogo} alt="Facebook" /> Sign up with Facebook
      </a>
    </div>
  );
}

function SignupForm(props) {
  let history = useHistory();

  const nameRef = useRef();
  const emailRef = useRef();
  const passwordRef = useRef();

  function submitHandler(event) {
    event.preventDefault();

    const signUpRequest = {
      name: nameRef.current.value,
      email: emailRef.current.value,
      password: passwordRef.current.value,
    };

    signup(signUpRequest)
      .then(() => {
        toast.success(
          "You're successfully registered. Please login to continue!"
        );
        history.push("/login");
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
          type="text"
          name="name"
          className="form-control"
          placeholder="Name"
          ref={nameRef}
          required
        />
      </div>
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
          Sign Up
        </button>
      </div>
    </form>
  );
}

export default Signup;
