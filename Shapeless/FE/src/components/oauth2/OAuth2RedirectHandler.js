import { ACCESS_TOKEN } from "../../constants";
import { useHistory, useLocation } from "react-router-dom";
import { useEffect } from "react";

function OAuth2RedirectHandler() {
  let history = useHistory();
  let location = useLocation();

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
      history.push({
        pathname: "/profile",
        state: { from: location },
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
