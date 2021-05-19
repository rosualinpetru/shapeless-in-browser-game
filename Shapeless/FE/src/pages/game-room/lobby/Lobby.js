import "./Lobby.css";

function Lobby(props) {
  let playersList = props.playersList;
  let gameData = props.gameData;
  let currentUser = props.currentUser;

  function isCurrentUserOwner() {
    let player = playersList.find((player) => player.id === currentUser.id);
    if (player !== null) {
      return player.isOwner;
    }
    return false;
  }

  return (
    <div className="playerlist-container">
      <div className="container">
        <div className="playerlist-wrapper">
          <div className="playerlist-header">
            <button
              className="btn btn-danger btn-sm btn-leave"
              onClick={props.leaveGame}
            >
              ⬅
            </button>
            <div className="counter-header">
              {playersList.length}/{gameData.maxPlayers}
            </div>
            <h2 class="room-name">{gameData.name}</h2> - <i>{gameData.difficulty}</i>
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
                onClick={props.startGame}
              >
                Start
              </button>
            ) : null}
          </div>
        </div>
      </div>
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

export default Lobby;
