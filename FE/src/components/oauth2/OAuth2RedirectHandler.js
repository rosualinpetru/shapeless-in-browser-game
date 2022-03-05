import { ACCESS_TOKEN } from "../../constants";
import { useHistory, useLocation } from "react-router-dom";
import { useEffect, useContext } from "react";
import { toast } from "react-toastify";
import { getCurrentUser } from "../../api/APIUtils";
import AuthenticationContext from "../../context/authentication";

function OAuth2RedirectHandler() {
  let history = useHistory();
  let location = useLocation();
  let authContext = useContext(AuthenticationContext);

  function getUrlParameter(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");

    var results = regex.exec(location.search);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  }

  const token = getUrlParameter("token");
  const error = getUrlParameter("error");

  useEffect(() => {
    if (token) {
      localStorage.setItem(ACCESS_TOKEN, token);
      getCurrentUser()
        .then((response) => {
          authContext.setUserHandler(response);
          toast.success("You're successfully logged in!");
          history.push({
            pathname: "/profile",
            state: { from: location },
          });
        })
        .catch((error) => {
          toast.error(error.message || "Oops! There was an error with OAuth2!");
        });
    } else {
      history.push({
        pathname: "/login",
        state: {
          from: location,
          error: error,
        },
      });
    }
  }, []);

  return null;
}

export default OAuth2RedirectHandler;
