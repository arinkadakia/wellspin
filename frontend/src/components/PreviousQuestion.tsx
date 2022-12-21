import { QuestionAndAnswer } from "../types";

type Props = {
  questionAndAnswer: QuestionAndAnswer;
};

export const PreviousQuestion = ({ questionAndAnswer: { question, answer } }: Props) => {
  // TODO: make this pretty, and maybe clickable (if person wants to go back to edit previous questions)
  return (
    <div className="previousCard-style">
      <div className="previous-question-title-style" dangerouslySetInnerHTML={{ __html: question.text }} />
      <div className="card-actions-style">{answer.join(", ")}</div>
    </div>
  );
};
