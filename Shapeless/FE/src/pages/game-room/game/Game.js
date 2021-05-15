import ShapeCard from "../../../components/card/ShapeCard";
import "./Game.css";
import { useState, useRef } from "react";

function Game(props) {
  const [selected, setSelected] = useState("");

  function select(id) {
    if (props.currentUser.id !== id) setSelected(id);
  }

  function amIChoosing() {
    let p = props.playersList.find(
      (entry) => entry.id === props.currentUser.id
    );
    if (p != null) return p.isChoosing;
    else false;
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
          selected !== "" ? (
            <div className="guess-container">
              <div className="guess-content">
                <h1 className="guess-title">Take a Guess</h1>
                <GuessForm selected={selected} />
              </div>
            </div>
          ) : (
            <h1>Click on a player card!</h1>
          )
        ) : (
          <h1>Wait your turn, bitch!</h1>
        )}
      </div>
    </div>
  );
}

function GuessForm(props) {
  const shapeGuess = useRef();
  const colorGuess = useRef();

  return (
    <form>
      <div className="form-item">
        <input
          type="hidden"
          name="name"
          className="form-control"
          placeholder="Player"
          value={props.selected}
          required
          readOnly
        />
      </div>
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
      <div className="form-item">
        <button type="submit" className="btn btn-block btn-primary">
          Guess
        </button>
      </div>
    </form>
  );
}

export default Game;
