import QuestionMark from "../shapes/QuestionMark";
import Cube from "../shapes/Cube";
import Pyramid from "../shapes/Pyramid";
import Sphere from "../shapes/Sphere";
import { ShapeColors } from "../../constants";
import "./ShapeCard.css";

function ShapeCard(props) {
  function parseColor(color) {
    if (color != null) {
      switch (color) {
        case "RED":
          return ShapeColors.RED;
        case "BLUE":
          return ShapeColors.BLUE;
        case "GREEN":
          return ShapeColors.GREEN;
      }
      return null;
    } else {
      return ShapeColors.GRAY;
    }
  }

  function renderShape(shape, color) {
    let colorResult = parseColor(color);
    if (shape != null) {
      switch (shape) {
        case "CUBE":
          return <Cube color={colorResult} size={60} />;
        case "SPHERE":
          return <Sphere color={colorResult} size={90} />;
        case "PYRAMID":
          return <Pyramid color={colorResult} size={45} />;
      }
    } else {
      return <QuestionMark color={colorResult} size={85} />;
    }
  }

  return (
    <div
      className="shape-card"
      style={{
        background: props.id === props.currentUser.id ? "#DCDCDC" : "white",
        border: props.isChoosing
          ? "2px solid yellow"
          : props.isSelected
          ? "2px solid red"
          : null,
      }}
      onClick={props.id !== props.currentUser.id ? props.select : null}
    >
      <div className="actual-shape">
        {renderShape(props.shape, props.color)}
      </div>
      <div className="shape-card-container">
        <h4>
          {props.name}
          {props.lives !== null ? <i> - {props.lives}</i> : null}
        </h4>
      </div>
    </div>
  );
}

export default ShapeCard;
