import "./Pyramid.css";

function Pyramid(props) {
  const p116 = 0.83;
  const p346 = 1.73;
  const p_400 = -2;
  const p_200 = -1;
  const p326 = 1.63;
  return (
    <div
      className="pyramid-container"
      style={{
        "--pyramid-size": `${props.size}`,
      }}
    >
      <div className="pyramid">
        <div
          style={{
            "--face-color": `${props.color}C0`,
          }}
        ></div>
        <div
          style={{
            "--face-color": `${props.color}A0`,
          }}
        ></div>
        <div
          style={{
            "--face-color": `${props.color}80`,
          }}
        ></div>
        <div
          style={{
            "--face-color": `${props.color}60`,
          }}
        ></div>
      </div>
    </div>
  );
}

export default Pyramid;
