import { useRef } from "react";
import { useHistory, useLocation } from "react-router";
import { toast } from "react-toastify";
import { createGame } from "../../api/APIUtils";
import "./CreateRoom.css";

function CreateRoom() {
  return (
    <div className="create-room-container">
      <div className="create-room-content">
        <h1 className="create-room-title">Create Room</h1>
        <CreateRoomForm />
      </div>
    </div>
  );
}

function CreateRoomForm() {
  const nameRef = useRef();
  const difficultyRef = useRef();
  const maxPlayersRef = useRef();
  const history = useHistory();
  const location = useLocation();

  function submitHandler(event) {
    event.preventDefault();

    const createRoomRequest = {
      name: nameRef.current.value,
      difficulty: difficultyRef.current.value,
      maxPlayers: maxPlayersRef.current.value,
    };

    createGame(createRoomRequest)
      .then((response) => {
        history.push({
          pathname: `/game/${response.gameId}`,
          state: {
            from: location,
            designer: response.location,
          },
        });
      })
      .catch((error) => {
        toast.error(
          error.message || "Oops! Something went wrong. Please try again!"
        );
      });
  }

  return (
    <form onSubmit={submitHandler}>
      <div className="form-item">
        <input
          type="text"
          name="name"
          className="form-control"
          placeholder="Room Name"
          ref={nameRef}
          required
        />
      </div>
      <div className="form-item form-group">
        <select
          className="form-control"
          name="maxPlayers"
          ref={maxPlayersRef}
          required
        >
          <option value="3">3</option>
          <option value="5">5</option>
          <option value="6">6</option>
        </select>
      </div>
      <div className="form-item form-group">
        <select
          className="form-control"
          name="difficulty"
          ref={difficultyRef}
          required
        >
          <option value="EASY">easy</option>
          <option value="MEDIUM">medium</option>
          <option value="HARD">hard</option>
        </select>
      </div>
      <div className="form-item">
        <button type="submit" className="btn btn-block btn-primary">
          Create Room
        </button>
      </div>
    </form>
  );
}

export default CreateRoom;
