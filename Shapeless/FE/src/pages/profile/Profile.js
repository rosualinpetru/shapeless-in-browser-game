import "./Profile.css";
import { useContext } from "react";
import AuthenticationContext from "../../context/authentication";

function Profile(props) {
  const authContext = useContext(AuthenticationContext);
  return (
    <div className="profile-container">
      <div className="container">
        <div className="profile-info">
          <div className="profile-avatar">
            {authContext.currentUser.imageUrl ? (
              <img
                src={authContext.currentUser.imageUrl}
                alt={authContext.currentUser.name}
              />
            ) : (
              <div className="text-avatar">
                <span>
                  {authContext.currentUser.name &&
                    authContext.currentUser.name[0]}
                </span>
              </div>
            )}
          </div>
          <div className="profile-name">
            <h2>{authContext.currentUser.name}</h2>
            <p className="profile-email">{authContext.currentUser.email}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
