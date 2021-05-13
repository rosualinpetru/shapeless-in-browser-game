import "./Home.css";

import Pyramid from "../../components/shapes/Pyramid";
import { ShapeColors } from "../../constants";
import Cube from "../../components/shapes/Cube";
import Sphere from "../../components/shapes/Sphere";

function Home() {
  return (
    <div className="home-container">
      <div className="container">
        <div className="graf-bg-container">
          <div className="graf-layout">
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
            <div className="graf-circle"></div>
          </div>
        </div>
        <br />
        <h1 className="home-title">
          If you think you can, you can. And if you think you can’t, you’re
          right.
        </h1>
      </div>
      <Cube color={ShapeColors.BLUE} size={100} />
      <Pyramid color={ShapeColors.BLUE} size={80} />
      <Sphere color={ShapeColors.BLUE} size={125} />
      <Cube color={ShapeColors.GREEN} size={100} />
      <Pyramid color={ShapeColors.GREEN} size={80} />
      <Sphere color={ShapeColors.GREEN} size={125} />
      <Cube color={ShapeColors.RED} size={100} />
      <Pyramid color={ShapeColors.RED} size={80} />
      <Sphere color={ShapeColors.RED} size={125} />
    </div>
  );
}

export default Home;
