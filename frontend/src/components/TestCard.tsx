import { Button, Typography } from "antd";
import { Test } from "../types";

type Props = {
  test: Test;
};

export const TestCard = ({ test }: Props) => {
  console.log(test);

  return (
    <div className="card-style">
      <Typography.Title level={4}>{test.name}</Typography.Title>
      <div className="test-description-style" dangerouslySetInnerHTML={{ __html: test.description }} />
      <div className="test-result-title-style">
        <Typography.Title level={4}>Test Result</Typography.Title>
        <div className="test-result-text" dangerouslySetInnerHTML={{ __html: test.testresulttext }} />
      </div>
      <Button id="button-style-id" className="button-style" href={test.url} target="_blank">
        Visit Website
      </Button>
    </div>
  );
};
