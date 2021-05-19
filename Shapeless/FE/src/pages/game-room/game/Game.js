import ShapeCard from "../../../components/card/ShapeCard";
import "./Game.css";
import { useState, useRef, useEffect } from "react";

function Game(props) {
  const [selected, setSelected] = useState("");

  function select(id) {
    if (props.currentUser.id !== id) setSelected(id);
  }

  function amIChoosing() {
    let p = props.playersList.find(
      (entry) => entry.id === props.currentUser.id
    );
    if (p !== undefined) return p.isChoosing;
    else false;
  }

  if (selected !== "" && selected !== null && selected !== undefined) {
    let isStillInGame = props.playersList.find(
      (player) => player.id == selected
    );
    if (isStillInGame === undefined) {
      setSelected("");
    }
  }

  return (
    <div className="game">
      <div>
        <table className="cards">
          <tbody>
            <tr>
              {amIChoosing()
                ? props.playersList.map((entry) => {
                    return (
                      <td className="card-cell" key={entry.id}>
                        <ShapeCard
                          id={entry.id}
                          name={entry.name}
                          shape={entry.shape}
                          color={entry.color}
                          lives={entry.lives}
                          isChoosing={entry.isChoosing}
                          currentUser={props.currentUser}
                          isSelected={entry.id === selected}
                          select={() => select(entry.id)}
                        />
                      </td>
                    );
                  })
                : props.playersList.map((entry) => {
                    return (
                      <td className="card-cell" key={entry.id}>
                        <ShapeCard
                          id={entry.id}
                          name={entry.name}
                          shape={entry.shape}
                          color={entry.color}
                          lives={entry.lives}
                          isChoosing={entry.isChoosing}
                          currentUser={props.currentUser}
                        />
                      </td>
                    );
                  })}
            </tr>
          </tbody>
        </table>
        {amIChoosing() ? (
          selected !== "" && selected !== undefined && selected !== null ? (
            <div className="guess-container">
              <div className="guess-content">
                <h1 className="guess-title">Take a Guess</h1>
                <GuessForm
                  selected={selected}
                  setSelected={setSelected}
                  guess={props.guess}
                  currentUser={props.currentUser}
                  playersList={props.playersList}
                />
              </div>
            </div>
          ) : (
            <h1 className="announcements">Click on a player card!</h1>
          )
        ) : (
          <h1 className="announcements">Wait for your turn!</h1>
        )}
      </div>
    </div>
  );
}

function GuessForm(props) {
  const shapeGuess = useRef();
  const colorGuess = useRef();
  let selectedPlayer = props.playersList.find(
    (player) => player.id === props.selected
  );

  function submitHandler(event) {
    event.preventDefault();

    let color;
    let shape;
    if (selectedPlayer.color !== null) {
      color = selectedPlayer.color;
    } else {
      color = colorGuess.current.value;
    }

    if (selectedPlayer.shape !== null) {
      shape = selectedPlayer.shape;
    } else {
      shape = shapeGuess.current.value;
    }

    props.guess({
      guesserId: props.currentUser.id,
      guessedId: props.selected,
      color: color,
      shape: shape,
    });
    props.setSelected("");
  }

  return (
    <form onSubmit={submitHandler}>
      {selectedPlayer.shape === null ? (
        <div className="form-item form-group">
          <select
            className="form-control"
            name="maxPlayers"
            ref={shapeGuess}
            required
          >
            <option value="">-- shape --</option>
            <option value="SPHERE">sphere</option>
            <option value="CUBE">cube</option>
            <option value="PYRAMID">pyramid</option>
          </select>
        </div>
      ) : null}
      {selectedPlayer.color === null ? (
        <div className="form-item form-group">
          <select
            className="form-control"
            name="difficulty"
            ref={colorGuess}
            required
          >
            <option value="">-- color --</option>
            <option value="RED">red</option>
            <option value="GREEN">green</option>
            <option value="BLUE">blue</option>
          </select>
        </div>
      ) : null}
      <div className="form-item">
        <button type="submit" className="btn btn-block btn-primary">
          Guess
        </button>
      </div>
    </form>
  );
}

export default Game;
