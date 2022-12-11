import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { surveyClient } from "../client/surveyClient";
import { TestCard } from "../components/TestCard";
import { Test } from "../types";
import { None } from "../utils/None";
import "../Survey.css";

export function EvaluateTest() {
  const [evaluateTest, setEvaluateTest] = useState<Test>();

  // We'll need to navigate to the survey page
  const navigate = useNavigate();

  const sessionId = new URLSearchParams(window.location.search).get("sessionId");
  if (None(sessionId) || sessionId === "") {
    navigate("/");
  }

  useEffect(() => {
    const evaluateTest = async () => {
      if (sessionId != null) {
        const test = await surveyClient.evaluateTest(sessionId);
        setEvaluateTest(test);
      } else {
        navigate("/");
      }
    };
    evaluateTest();
  }, []);

  return (
    <>
      {evaluateTest != null && (
        <div>
          <TestCard key={evaluateTest.id} test={evaluateTest} />
        </div>
      )}
    </>
  );
}
