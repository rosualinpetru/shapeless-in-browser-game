import "./Profile.css";
import { useContext, useState } from "react";
import { updateImage } from "../../api/APIUtils";
import AuthenticationContext from "../../context/authentication";
import { toast } from "react-toastify";

function Profile(props) {
  const authContext = useContext(AuthenticationContext);
  const [imageUrl, setImageUrl] = useState("");

  function inputChangeHandler(event) {
    setImageUrl(event.target.value);
  }

  function submitHandler(event) {
    event.preventDefault();

    const updateImageRequest = {
      imageUrl: imageUrl,
    };

    updateImage(updateImageRequest).then((response) =>
      toast.success(response.message)
    );

    event.target.reset();
  }

  return (
    <div className="profile-container">
      <div className="container">
        <div className="profile-info">
          <div className="profile-avatar">
            {imageUrl ? (
              <img src={imageUrl} alt={authContext.currentUser.name} />
            ) : authContext.currentUser.imageUrl ? (
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
            <p className="profile-score">
              <b>Score: </b>
              {authContext.currentUser.score}
            </p>
            <br />
          </div>
          <div className="update-form">
            <hr />
            <form onSubmit={submitHandler}>
              <div className="form-item">
                <input
                  type="text"
                  name="imageUrl"
                  className="form-control"
                  placeholder="Image Url"
                  onChange={inputChangeHandler}
                  required
                />
              </div>
              <div className="form-item">
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={imageUrl === ""}
                >
                  Update Image
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
