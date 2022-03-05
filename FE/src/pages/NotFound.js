import "./NotFound.css";
import { useHistory } from "react-router-dom";

function NotFound() {
  let history = useHistory();
  function redirectBack() {
    history.goBack();
  }
  return (
    <div className="page-not-found">
      <h1 className="title">404</h1>
      <div className="desc">The Page you're looking for was not found.</div>
      <button
        onClick={redirectBack}
        className="go-back-btn btn btn-primary"
        type="button"
      >
        Go Back
      </button>
    </div>
  );
}

export default NotFound;
