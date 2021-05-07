import { useContext } from "react";
import { Route, useLocation, Redirect } from "react-router-dom";
import AuthenticationContext from "../../context/authentication";

function PrivateRoute(props) {
  const authContext = useContext(AuthenticationContext);
  return (
    <div>
      {authContext.isAuthenticated ? (
        <Route path={props.path}>{props.children}</Route>
      ) : (
        <Redirect to="/login" />
      )}
    </div>
  );
}

export default PrivateRoute;
