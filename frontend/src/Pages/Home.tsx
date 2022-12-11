import { Row, Col, Button, Image, Typography } from "antd";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { surveyClient } from "../client/surveyClient";
import { Some } from "../utils/Some";

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
    <Row justify="center">
      <Col span={8}>
        <Row justify="center" className="mb4">
          <Col>
            {/* TODO: replace this with the image from the design. Don't forget to update the alt text! */}
            <Image width={157} src="https://placekitten.com/157/157" preview={false} alt="A cute kitten" />
          </Col>
        </Row>

        <Row className="mb2">
          <Col>
            <Typography.Title>Find what services you are eligible for in less than 5 minutes</Typography.Title>
          </Col>
        </Row>

        <Row justify="center">
          <Col>
            <Button type="primary" size="large" shape="round" onClick={startSurvey} loading={isLoading}>
              Start
            </Button>
          </Col>
        </Row>
      </Col>
    </Row>
  );
}
