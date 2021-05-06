import { useRef } from "react";
import { toast } from "react-toastify";
import { createRoom } from "../../api/APIUtils";
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

  function submitHandler(event) {
    event.preventDefault();

    const signUpRequest = {
      name: nameRef.current.value,
      difficulty: difficultyRef.current.value,
      maxPlayers: maxPlayersRef.current.value,
    };

    createRoom(signUpRequest).catch((error) => {
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
          <option value="4">4</option>
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
