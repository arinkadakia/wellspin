import { Button, Typography } from "antd";
import { Test } from "../types";

type Props = {
  test: Test;
};

export const TestCard = ({ test }: Props) => {
  console.log(test);

  return (
    <div className="sorryCard-style">
      <Typography.Title level={3}>{test.name}</Typography.Title>
      <div>{test.description}</div>
      <div>
        <Typography.Title level={4}>Test Result</Typography.Title>
        <div>{test.testresulttext}</div>
      </div>
      <Button id="button-id" className="websiteButton-style" type="primary" href={test.url} target="_blank">
        Visit Website
      </Button>
    </div>
  );
};
