import { useContext, useState, useEffect } from "react";
import { useParams, useHistory, useLocation } from "react-router-dom";
import { messageType } from "../../constants";
import AuthenticationContext from "../../context/authentication";
import SockJsClient from "react-stomp";
import { amIPlaying } from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";
import { toast } from "react-toastify";

function GameRoom(props) {
  let { gameId } = useParams();
  let [isLoading, setIsLoading] = useState(true);
  let [clientRef, setClientRef] = useState(null);
  let authContext = useContext(AuthenticationContext);
  let currentUser = authContext.currentUser;
  let history = useHistory();
  let location = useLocation();
  let designer = location.state.designer;

  useEffect(async () => {
    let isPlaying = await amIPlaying()
      .then((data) => data.isPlaying)
      .catch((error) => {
        toast.error(
          error.message || "Oops! There was an error in joining the room!"
        );
        history.push("/");
      });
    if (isPlaying) {
      history.push("/");
      toast.error("You can only play one game at a time!");
    }
    setIsLoading(false);
  }, []);

  if (isLoading) {
    return <LoadingIndicator />;
  }

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
        console.log("update_lobby");
        break;
      case messageType.gameError:
        console.log("error");
        break;
    }
  }

  return (
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
  );
}
export default GameRoom;
