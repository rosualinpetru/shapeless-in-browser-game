import { useState, useEffect } from "react";
import { useHistory } from "react-router";
import { toast } from "react-toastify";
import { rooms as roomsRequest } from "../../api/APIUtils";
import LoadingIndicator from "../../components/loading/LoadingIndicator";
import "./Rooms.css";

function joinRoom() {}

function Rooms() {
  const [isLoading, setIsLoading] = useState(true);
  const [refresh, setRefresh] = useState(false);
  const [rooms, setRooms] = useState([]);
  let history = useHistory();

  useEffect(() => {
    roomsRequest()
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        setRooms(data);
        setIsLoading(false);
      })
      .catch(() => toast.error("There is an error with loading the rooms!"));
  }, [refresh]);

  function createRoom() {
    history.push("/create-room");
  }

  function refreshRooms() {
    setRefresh((old) => !old);
  }

  if (isLoading) {
    return <LoadingIndicator />;
  }

  return (
    <div className="rooms-container">
      <div className="container">
        <div className="rooms-wrapper">
          <div className="rooms-header">
            <button
              className="btn btn-success btn-sm btn-create"
              onClick={createRoom}
            >
              +
            </button>
            <button
              className="btn btn-secondary btn-sm btn-refresh"
              onClick={refreshRooms}
            >
              ⟳
            </button>
            <h2>Rooms</h2>
          </div>
          <div className="rooms-box scrollbar">
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
                {rooms.map((entry) => (
                  <Entry data={entry} key={entry.id} />
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
        <h3>{props.data.owner}</h3>
      </td>
      <td>
        <button className="btn btn-primary btn-sm" onClick={joinRoom}>
          Join
        </button>
      </td>
    </tr>
  );
}

export default Rooms;
