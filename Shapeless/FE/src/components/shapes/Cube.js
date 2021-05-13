import "./Cube.css";

function Cube(props) {
  return (
    <div
      className="cube-container"
      style={{
        "--cube-size": `${props.size}px`,
        "--cube-half": `${props.size / 2}px`,
        "--perspective": `${props.size + 2000}px`,
      }}
    >
      <div className="cube">
        <div
          className="front"
          style={{
            "--cube-size": `${props.size}px`,
            "--cube-half": `${props.size / 2}px`,
          }}
        ></div>
        <div
          className="back"
          style={{
            background: `${props.color}D5`,
            "--cube-size": `${props.size}px`,
            "--cube-half": `${props.size / 2}px`,
          }}
        ></div>
        <div
          className="right"
          style={{
            background: `${props.color}C0`,
            "--cube-size": `${props.size}px`,
            "--cube-half": `${props.size / 2}px`,
          }}
        ></div>
        <div
          className="left"
          style={{
            background: `${props.color}A5`,
            "--cube-size": `${props.size}px`,
            "--cube-half": `${props.size / 2}px`,
          }}
        ></div>
        <div
          className="top"
          style={{
            background: `${props.color}90`,
            "--cube-size": `${props.size}px`,
            "--cube-half": `${props.size / 2}px`,
          }}
        ></div>
        <div
          className="bottom"
          style={{
            background: `${props.color}75`,
            "--cube-size": `${props.size}px`,
            "--cube-half": `${props.size / 2}px`,
          }}
        ></div>
      </div>
    </div>
  );
}

export default Cube;
