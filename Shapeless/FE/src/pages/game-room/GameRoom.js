import { useContext, useState, useEffect } from "react";
import { useParams, useHistory, useLocation } from "react-router-dom";
import { messageType } from "../../constants";
import AuthenticationContext from "../../context/authentication";
import SockJsClient from "react-stomp";
import {
  amIPlaying,
  gameData as gameDataRequest,
  getPlayersInGame,
} from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";
import { toast } from "react-toastify";
import "./GameRoom.css";

function GameRoom(props) {
  let { gameId } = useParams();
  let [isLoading, setIsLoading] = useState(true);
  let [clientRef, setClientRef] = useState(null);
  let [gameData, setGameData] = useState(null);
  let [playersList, setPlayersList] = useState([]);
  let authContext = useContext(AuthenticationContext);
  let currentUser = authContext.currentUser;
  let history = useHistory();
  let location = useLocation();
  let designer = location.state.designer;

  useEffect(async () => {
    let isPlaying = await amIPlaying()
      .then((data) => data.isPlaying)
      .catch(() => {
        toast.error("Oops! There was an error in joining the room!");
        history.push("/");
      });
    await gameDataRequest(gameId)
      .then((response) => {
        setGameData(response);
      })
      .catch(() => {
        toast.error("Oops! There was an error in joining the room!");
        history.push("/");
      });
    if (isPlaying) {
      history.push("/");
      toast.error("You can only play one game at a time!");
    }
    setIsLoading(false);
  }, []);

  function onConnect() {
    clientRef.sendMessage(
      `/app/game/${gameId}/join`,
      JSON.stringify(currentUser.id)
    );
  }

  function onDisconnect() {}

  function onMessage(payload) {
    switch (payload.type) {
      case messageType.updateLobby:
        getPlayersInGame(gameId).then((response) => {
          setPlayersList(response);
        });
        break;
      case messageType.gameError:
        if (payload.id === currentUser.id) {
          toast.error(payload.message);
          history.push("/");
        }
        break;
    }
  }

  function leaveGame() {
    history.push("/");
  }

  if (isLoading) {
    return <LoadingIndicator />;
  }

  function isCurrentUserOwner() {
    let player = playersList.find((player) => player.id === currentUser.id);
    if (player !== null) {
      return player.isOwner;
    }
    return false;
  }

  return (
    <div>
      <div className="playerlist-container">
        <div className="container">
          <div className="playerlist-wrapper">
            <div className="playerlist-header">
              <button
                className="btn btn-danger btn-sm btn-leave"
                onClick={leaveGame}
              >
                ⬅
              </button>
              <div className="counter-header">
                {playersList.length}/{gameData.maxPlayers}
              </div>
              <h2>{gameData.name}</h2> - <i>{gameData.difficulty}</i>
            </div>
            <div className="playerlist-box scrollbar">
              <table className="fl-table">
                <tbody>
                  {playersList.map((entry) => (
                    <PlayerListEntry data={entry} key={entry.id} />
                  ))}
                </tbody>
              </table>
            </div>
            <div className="playerlist-footer">
              {playersList.length === gameData.maxPlayers &&
              isCurrentUserOwner() ? (
                <button
                  className="btn btn-success btn-sm btn-start"
                  onClick={leaveGame}
                >
                  Start
                </button>
              ) : null}
            </div>
          </div>
        </div>
      </div>
      <SockJsClient
        url={`http://${designer}:31600/ws`}
        topics={[`/topic/${gameId}`]}
        onConnect={onConnect}
        onDisconnect={onDisconnect}
        onMessage={onMessage}
        ref={(client) => {
          setClientRef(client);
        }}
      />
    </div>
  );
}

function PlayerListEntry(props) {
  return (
    <tr>
      <td>
        <h3>
          {props.data.isOwner ? "👑 " + props.data.name : props.data.name}
        </h3>
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
export default GameRoom;
