import { Card } from "antd";
import { QuestionAndAnswer } from "../types";

type Props = {
  questionAndAnswer: QuestionAndAnswer;
};

export const PreviousQuestion = ({ questionAndAnswer: { question, answer } }: Props) => {
  // TODO: make this pretty, and maybe clickable (if person wants to go back to edit previous questions)
  return (
    <div className="previousCard-style">
      <h3>{question.text}</h3>
      <span>{answer.join(", ")}</span>
    </div>
  );
};
