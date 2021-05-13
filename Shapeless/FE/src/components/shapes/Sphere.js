import "./Sphere.css";

function Sphere(props) {
  function range(start, end) {
    var len = end - start + 1;
    var a = new Array(len);
    for (let i = 0; i < len; i++) a[i] = start + i;
    return a;
  }
  return (
    <div
      className="sphere-container"
      style={{
        "--sphere-size": `${props.size}px`,
      }}
    >
      {range(1, 15).map((index) => {
        let opacity = Math.trunc(Math.random() * 100 + 50);
        let opacityHex = opacity.toString(16);
        return (
          <div
            className="circle"
            key={index}
            style={{
              background: `${props.color}${opacityHex}`,
            }}
          ></div>
        );
      })}
    </div>
  );
}

export default Sphere;
