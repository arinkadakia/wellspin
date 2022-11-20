export type Question = {
  id: number;
  name: string;
  hint: string;
  type: "text_box" | "radio_button" | "drop_down" | "check_box" | "button";
  format: "number";
  answer: string[];
  nextid: number[];
  text: string;
};

export type QuestionAndAnswer = {
  question: Question;
  answer: string[];
};
