import { useContext } from "react";
import { Route, useHistory, useLocation } from "react-router-dom";
import AuthenticationContext from "../../context/authentication";

function PrivateRoute(props) {
  const history = useHistory();
  const location = useLocation();
  const authContext = useContext(AuthenticationContext);
  return (
    <Route>
      {authContext.isAuthenticated
        ? props.children
        : history.push({
            pathname: "/login",
            state: { from: location },
          })}
    </Route>
  );
}

export default PrivateRoute;
