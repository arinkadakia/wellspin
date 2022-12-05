export type Question = {
  id: number;
  name: string;
  hint: string;
  type: "text_box" | "radio_button" | "drop_down" | "check_box" | "button" | "input_form" | "show_results_page";
  format: "number";
  answer: string[];
  nextid: number[];
  text: string;
};

export type QuestionAndAnswer = {
  question: Question;
  answer: string[];
};

export type Program = {
  id: number;
  name: string;
  description: string;
  url: string;
  eligibility: string;
  type: string;
  locationid: number
};
