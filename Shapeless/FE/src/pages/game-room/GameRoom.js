import { useContext, useState, useEffect } from "react";
import { useParams, useHistory } from "react-router-dom";
import { WS_BASE_URL, messageType } from "../../constants";
import AuthenticationContext from "../../context/authentication";
import SockJsClient from "react-stomp";
import { amIPlaying } from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";
import { toast } from "react-toastify";

function GameRoom(props) {
  let { roomId } = useParams();
  let [isLoading, setIsLoading] = useState(true);
  let [clientRef, setClientRef] = useState(null);
  let authContext = useContext(AuthenticationContext);
  let currentUser = authContext.currentUser;
  let history = useHistory();

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
      `/app/room/${roomId}/join`,
      JSON.stringify({
        type: messageType.join,
        senderId: currentUser.id,
        senderData: {
          name: currentUser.name,
          imageUrl: currentUser.imageUrl,
        },
      })
    );
  }

  function onDisconnect() {}

  function onMessage(payload) {
    let message = payload;
    switch (message.type) {
      case messageType.join:
        console.log("join");
        console.log(message);
        break;
      case messageType.leave:
        console.log("leave");
        console.log(message);
        break;
    }
  }

  return (
    <SockJsClient
      url={`${WS_BASE_URL}/ws`}
      topics={[`/topic/${roomId}`]}
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
