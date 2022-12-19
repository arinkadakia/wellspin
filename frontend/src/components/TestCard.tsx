import { Button, Typography } from "antd";
import { Test } from "../types";

type Props = {
  test: Test;
};

export const TestCard = ({ test }: Props) => {
  console.log(test);

  return (
    <div className="card-style">
      <div className="question-title-style">{test.name}</div>
      <div>{test.description}</div>
      <div>
        <Typography.Title level={4}>Test Result</Typography.Title>
        <div>{test.testresulttext}</div>
      </div>
      <Button className="button-style" href={test.url} target="_blank">
        Visit Website
      </Button>
    </div>
  );
};
