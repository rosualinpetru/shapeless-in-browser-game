import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { leaderboard as leaderboardRequest } from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";

import "./Leaderboard.css";

function Leaderboard(props) {
  const [isLoading, setIsLoading] = useState(true);
  const [leaderboard, setLeaderboard] = useState([]);

  useEffect(() => {
    leaderboardRequest()
      .then((data) => {
        setLeaderboard(data);
        setIsLoading(false);
      })
      .catch(() => toast.error("There is an error loading the leaderboard!"));
  }, []);

  if (isLoading) {
    return <LoadingIndicator />;
  }

  return (
    <div className="leaderboard-container">
      <div className="container">
        <div className="leaderboard-wrapper">
          <div className="leaderboard-header">
            <h2 className="leaderboard-title">Leaderboard</h2>
          </div>
          <div className="leaderboard-box scrollbar">
            <table className="fl-table">
              <tbody>
                {leaderboard.map((entry) => (
                  <LeaderboardEntry data={entry} key={entry.position} />
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

function LeaderboardEntry(props) {
  return (
    <tr>
      <td>{props.data.position}</td>
      <td>
        <h3>{props.data.name}</h3>
      </td>
      <td>
        {props.data.imageUrl ? (
          <img
            className="img-avatar"
            src={props.data.imageUrl}
            alt={props.data.name}
          />
        ) : (
          <div className="text-avatar">
            <span>{props.data.name && props.data.name[0]}</span>
          </div>
        )}
      </td>
      <td>{props.data.score}</td>
    </tr>
  );
}

export default Leaderboard;
