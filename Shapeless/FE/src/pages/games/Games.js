import { useState, useEffect } from "react";
import { useHistory, useLocation } from "react-router";
import { toast } from "react-toastify";
import {
  games as gamesRequest,
  amIPlaying,
  gamesData,
} from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";
import "./Games.css";

function Games() {
  const [isLoading, setIsLoading] = useState(true);
  const [refresh, setRefresh] = useState(false);
  const [games, setGames] = useState([]);
  let history = useHistory();
  let location = useLocation();

  useEffect(() => {
    gamesRequest()
      .then((data) => {
        setGames(data);
        setIsLoading(false);
      })
      .catch(() => toast.error("There is an error with loading the games!"));
  }, [refresh]);

  function createRoom() {
    history.push("/create-game");
  }

  function refreshGames() {
    setRefresh((old) => !old);
  }

  function joinRoom(id, designer) {
    amIPlaying().then((response) => {
      if (response.isPlaying) {
        toast.error("You can play only one game at a time!");
        return;
      }
      history.push({
        pathname: `/game/${id}`,
        state: {
          from: location,
          designer: designer,
        },
      });
    });
  }

  if (isLoading) {
    return <LoadingIndicator />;
  }

  return (
    <div className="games-container">
      <div className="container">
        <div className="games-wrapper">
          <div className="games-header">
            <button
              className="btn btn-primary btn-create"
              onClick={createRoom}
            >
              +
            </button>
            <button
              className="btn btn-primary btn-refresh"
              onClick={refreshRooms}
            >
              ⟳
            </button>
            <h2 className="roomsHeader" >Rooms</h2>
          </div>
          <div className="games-box scrollbar">
            <table className="fl-table">
              <thead>
                <tr>
                  <th>Difficulty</th>
                  <th>Room name</th>
                  <th>Owner</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {games.map((entry) => (
                  <Entry
                    data={entry}
                    key={entry.id}
                    joinRoom={() => joinRoom(entry.id, entry.designer)}
                  />
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

function Entry(props) {
  return (
    <tr>
      <td>{props.data.difficulty}</td>
      <td>
        <h3>{props.data.name}</h3>
      </td>
      <td>
        <h3>{props.data.ownerName}</h3>
      </td>
      <td>
        <button className="btn btn-primary btn-join" onClick={props.joinRoom}>
          Join
        </button>
      </td>
    </tr>
  );
}

export default Games;
