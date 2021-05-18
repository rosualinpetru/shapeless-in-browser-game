import { useContext, useState, useEffect } from "react";
import { useParams, useHistory, useLocation } from "react-router-dom";
import { messageType, DESIGNER_HOST } from "../../constants";
import AuthenticationContext from "../../context/authentication";
import SockJsClient from "react-stomp";
import {
  amIPlaying,
  gameData as gameDataRequest,
  getPlayersInGame,
  getPlayersInActualGame,
  getCurrentUser,
} from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";
import { toast } from "react-toastify";
import "./GameRoom.css";
import Lobby from "./lobby/Lobby";
import Game from "./game/Game";
import Victory from "./victory/Victory";

function GameRoom(props) {
  let { gameId } = useParams();
  let [isLoading, setIsLoading] = useState(true);
  let [isStarted, setIsStarted] = useState(false);
  let [clientRef, setClientRef] = useState(null);
  let [gameData, setGameData] = useState(null);
  let [playersList, setPlayersList] = useState([]);
  let [victory, setVictory] = useState(false);
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
    return () => {};
  }, []);

  function onConnect() {
    clientRef.sendMessage(
      `/app/game/${gameId}/join`,
      JSON.stringify(currentUser.id)
    );
  }

  function onDisconnect() {
    getCurrentUser().then((response) => {
      authContext.setUserHandler(response);
    });
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
      case messageType.updateGame:
        getPlayersInActualGame(gameId).then((response) => {
          if (
            response.find((player) => player.id === currentUser.id) ===
              undefined &&
            response.length !== 0
          ) {
            toast("DEFEAT!");
            history.push("/");
          } else {
            if (response.length === 1) {
              toast("VICTORY!");
              setVictory(true);
            }
            setPlayersList(response);
          }
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

  function guess(data) {
    clientRef.sendMessage(`/app/game/${gameId}/guess`, JSON.stringify(data));
  }

  if (isLoading) {
    return <LoadingIndicator />;
  }

  return (
    <div>
      {isStarted ? (
        victory ? (
          <Victory />
        ) : (
          <Game
            playersList={playersList}
            currentUser={currentUser}
            guess={guess}
          />
        )
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
        url={DESIGNER_HOST}
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

export default GameRoom;
