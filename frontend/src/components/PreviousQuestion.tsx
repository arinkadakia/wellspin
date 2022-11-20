import { Card } from "antd";
import { QuestionAndAnswer } from "../types";

type Props = {
  questionAndAnswer: QuestionAndAnswer;
};

export const PreviousQuestion = ({ questionAndAnswer: { question, answer } }: Props) => {
  // TODO: make this pretty, and maybe clickable (if person wants to go back to edit previous questions)
  return (
    <Card className="mb2">
      <h2>{question.text}</h2>
      <span>{answer.join(", ")}</span>
    </Card>
  );
};
