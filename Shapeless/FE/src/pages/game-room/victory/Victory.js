import { useHistory } from "react-router-dom";
import trophy from "./trophy.png";
import "./Victory.css";

function Victory() {
  let history = useHistory();

  async function goHome() {
    history.push("/");
  }

  return (
    <div className="victory-container">
      <div className="victory-content">
        <img src={trophy} className="trophy" />
        <h2 className="victory-msg">Victory!</h2>
        <button className="btn btn-success btn-sm btn-start" onClick={goHome}>
          Back To Profile
        </button>
      </div>
    </div>
  );
}

export default Victory;
