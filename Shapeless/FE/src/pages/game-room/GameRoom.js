import { useContext, useState, useEffect } from "react";
import { useParams, useHistory, useLocation } from "react-router-dom";
import { messageType } from "../../constants";
import AuthenticationContext from "../../context/authentication";
import SockJsClient from "react-stomp";
import {
  amIPlaying,
  gameData as gameDataRequest,
  getPlayersInGame,
  getPlayersInActualGame,
} from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";
import { toast } from "react-toastify";
import "./GameRoom.css";
import Lobby from "./lobby/Lobby";
import Game from "./game/Game";

function GameRoom(props) {
  let { gameId } = useParams();
  let [isLoading, setIsLoading] = useState(true);
  let [isStarted, setIsStarted] = useState(false);
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

  function onMessage(payload) {
    switch (payload.type) {
      case messageType.updateLobby:
        if (isStarted) {
          getPlayersInActualGame(gameId).then((response) => {
            setPlayersList(response);
          });
        } else {
          getPlayersInGame(gameId).then((response) => {
            setPlayersList(response);
          });
        }
        break;
      case messageType.gameError:
        if (payload.id === currentUser.id) {
          toast.error(payload.message);
          history.push("/");
        }
        break;
      case messageType.start:
        getPlayersInActualGame(gameId).then((response) => {
          setPlayersList(response);
          setIsStarted(true);
        });
        break;
    }
  }

  function leaveGame() {
    history.push("/");
  }

  function startGame() {
    clientRef.sendMessage(
      `/app/game/${gameId}/start`,
      JSON.stringify(currentUser.id)
    );
  }

  if (isLoading) {
    return <LoadingIndicator />;
  }

  return (
    <div>
      {isStarted ? (
        <Game
          gameData={gameData}
          playersList={playersList}
          currentUser={currentUser}
        />
      ) : (
        <Lobby
          gameData={gameData}
          playersList={playersList}
          leaveGame={leaveGame}
          startGame={startGame}
          currentUser={currentUser}
        />
      )}
      <SockJsClient
        url={`http://${designer}:31600/ws`}
        topics={[`/topic/${gameId}`]}
        onConnect={onConnect}
        onMessage={onMessage}
        ref={(client) => {
          setClientRef(client);
        }}
      />
    </div>
  );
}

export default GameRoom;
