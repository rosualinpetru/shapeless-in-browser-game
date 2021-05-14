import "./QuestionMark.css";

function QuestionMark(props) {
  return (
    <div
      className="qm"
      style={{ "--qm-color": props.color, "--qm-size": props.size }}
    >
      ?
    </div>
  );
}

export default QuestionMark;
