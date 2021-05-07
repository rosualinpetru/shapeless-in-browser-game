import { useRef } from "react";
import { useParams } from "react-router-dom";
import { WS_BASE_URL } from "../../constants";

function GameRoom(props) {
  let params = useParams();
  let clientRef = useRef();

  function onConnect() {
    clientRef.current;
  }

  function onDisconnect() {}

  function onMessage() {}

  console.log(params);
  return (
    <SockJsClient
      url={`${WS_BASE_URL}/ws`}
      topics={[`/topic/${roomId}`]}
      onConnect={onConnect}
      onDisconnect={onDisconnect}
      onMessage={onMessage}
      ref={clientRef}
    />
  );
}
export default GameRoom;
