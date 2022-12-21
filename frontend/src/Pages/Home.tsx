import { Row, Button, Image, Typography } from "antd";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { surveyClient } from "../client/surveyClient";

export function Home() {
  // This state will be used to tell if a new session start is underway
  const [isLoading, setIsLoading] = useState(false);

  // We'll need to navigate to the survey page
  const navigate = useNavigate();

  // This function will be called when we press Start Survey
  const startSurvey = async () => {
    // Using this isLoading state let's us make the button pretty & spin while we're waiting for the API call to finish
    // It also protects us from double-clicking the button, which would call the API more than once
    setIsLoading(true);

    const sessionId = await surveyClient.startNewSession();
    navigate(`/survey?sessionId=${sessionId}`);

    // Usually we might setIsLoading(false) but actually we don't care because the whole component is unmounting
    // when we navigate to the Survey page
  };

  return (
    <div>
      <Row justify="center" className="mb4">
        <Image width={250} src="image.png" preview={false} alt="WellSpin" />
      </Row>

      <Row className="mb2">
        <Typography.Title style={{ textAlign: "center" }}>Find your ADHD score in less than 5 minutes</Typography.Title>
      </Row>

      <Row justify="center" className="display-block">
        <Button className="button-style" onClick={startSurvey} loading={isLoading}>
          Start
        </Button>
      </Row>
    </div>
  );
}
