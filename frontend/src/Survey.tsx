import { Row, Spin } from "antd";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { surveyClient } from "./client/surveyClient";
import { PreviousQuestion } from "./components/PreviousQuestion";
import { QuestionCard } from "./components/QuestionCard";
import "./Survey.css";
import { Question, QuestionAndAnswer } from "./types";
import { None } from "./utils/None";
import { Some } from "./utils/Some";

export function Survey() {
  console.log("Rendering Survey()");

  // Let's ensure we have a valid sessionId as a query string parameter, and if not, probably just go to the home page
  const navigate = useNavigate();
  const sessionId = new URLSearchParams(window.location.search).get("sessionId");
  const [surveyId, setSurveyId] = useState<number>(0);

  if (None(sessionId) || sessionId === "") {
    navigate("/");
  }

  // I'm not sure how we're going to store the dynamic structure of previous questions & answers o.O
  // This is just a first guess
  const [previousQuestions, setPreviousQuestions] = useState<QuestionAndAnswer[]>([]);

  // This state will be used to hold the currently displayed question
  // I wasn't expecting this to be a dynamic one passed from the server, we'll have to figure out how to tell what validation
  // to use for each question, and how to render the input type
  const [currentQuestion, setCurrentQuestion] = useState<Question>();

  // We want to get the next question to display, and synchronize it to the previousQuestions.length state
  // That way, if we add (or remove) questions from previousQuestions array, we will call this effect again that
  // should make sure we're always displaying the right question on screen
  useEffect(() => {
    console.log("previousQuestions.length: ", previousQuestions.length);

    const getNextQuestion = async () => {
      // TODO: call "addToSession"
      // TODO: Update eligible programs count

      // Depending on how many questions are in the previousQuestions we need to behave differently
      if (previousQuestions.length === 0) {
        const question = await surveyClient.getFirstQuestion();
        setCurrentQuestion(question);
      }

      if (previousQuestions.length === 1) {
        if (Some(sessionId)) {
          const surveyId = await surveyClient.getSurveyIdFromZip(sessionId, previousQuestions[0].answer[0]);
          setSurveyId(surveyId);

          const question = await surveyClient.getSecondQuestion(
            sessionId,
            previousQuestions[previousQuestions.length - 1].question.id,
            previousQuestions[0].answer[0]
          );

          setCurrentQuestion(question);
        } else {
          throw new Error("Somehow attempting to getSurveyIdFromZip but sessionId is null");
        }
      }

      if (previousQuestions.length > 1) {
        const { question: lastQuestion, answer: lastAnswer } = previousQuestions[previousQuestions.length - 1];

        // All other questions need us to pass a `lastAnswerIds`. In the case of free text strings, this is expected to be 0
        const getLastAnswerIds = () => {
          switch (lastQuestion.type) {
            case "text_box":
              return "0";

            case "drop_down":
            case "radio_button":
              // TODO: this method searches the possible answer[] provided by backend, and looks for the first result
              // This is because a drop_down (Select) can only ever pick 1 result
              // How will we deal with checkboxes, where multiple indices could be selected? Let's deal with that when we get to it!
              return lastQuestion.answer.findIndex((_) => _ === lastAnswer[0]).toString();

            case "check_box":
              return lastAnswer.map((ans) => lastQuestion.answer.findIndex((_) => _ === ans).toString()).join(",");

            case "button":
              throw new Error("Not implemented");
          }
        };
        const getLastAnswerInputs = () => {
          switch (lastQuestion.type) {
            case "text_box":
            case "drop_down":
            case "radio_button":
              // TODO: this method searches the possible answer[] provided by backend, and looks for the first result
              // This is because a drop_down (Select) can only ever pick 1 result
              // How will we deal with checkboxes, where multiple indices could be selected? Let's deal with that when we get to it!
              return lastAnswer[0];

            case "check_box":
              return lastAnswer.join(",");

            case "button":
              throw new Error("Not implemented");
          }
        };
        if (Some(sessionId)) {
          const question = await surveyClient.getNextQuestion(
            sessionId,
            previousQuestions[previousQuestions.length - 1].question.id,
            surveyId,
            getLastAnswerIds(),
            getLastAnswerInputs()
          );
          setCurrentQuestion(question);
        }
      }

      // TODO: implement what happens when previousQuestions.length > 0
      // It might need to call a differeny surveyClient function, passing in some parameters
      // It must still update currentQuestion

      // TODO: how do we determine if we've finished the current questions, and what happens then?
    };

    getNextQuestion();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [previousQuestions.length]);

  return (
    <>
      {/* This will be the "Previous questions" bit from the design */}
      {previousQuestions.map((q) => (
        <PreviousQuestion key={q.question.id} questionAndAnswer={q} />
      ))}

      {/* Here, we will display the currentQuestion (if any loaded up yet) */}
      {Some(currentQuestion) ? (
        <QuestionCard
          question={currentQuestion}
          onNext={(answer) => {
            // Store the previous question away with it's answer
            setPreviousQuestions((prev) => [
              ...prev,
              {
                question: currentQuestion,
                answer,
              },
            ]);

            // And clear the current question
            // NOTE: we'll get a new one from the API automatically thanks to the useEffect watching the length of previousQuestions array
            setCurrentQuestion(undefined);
          }}
        />
      ) : (
        <Row justify="center" align="middle" className="mt1 mb1">
          <Spin size="large" />
        </Row>
      )}
    </>
  );
}
