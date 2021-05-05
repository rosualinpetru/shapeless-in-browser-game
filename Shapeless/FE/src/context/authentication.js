import { createContext, useState } from "react";

const AuthenticationContext = createContext({
  currentUser: null,
  isAuthenticated: false,
  setUserHandler: (_user) => {},
  deleteUserHandler: () => {},
});

export function AuthenticationContextProvider(props) {
  const [currentUser, setCurrentUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const context = {
    currentUser: currentUser,
    isAuthenticated: isAuthenticated,
    setUserHandler: setUserHandler,
    deleteUserHandler: deleteUserHandler,
  };

  function setUserHandler(user) {
    setCurrentUser(user);
    setIsAuthenticated(true);
  }

  function deleteUserHandler() {
    setCurrentUser(null);
    setIsAuthenticated(false);
  }

  return (
    <AuthenticationContext.Provider value={context}>
      {props.children}
    </AuthenticationContext.Provider>
  );
}

export default AuthenticationContext;
