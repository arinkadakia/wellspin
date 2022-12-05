import { Button, Card, Typography } from "antd";
import { Program } from "../types";

type Props = {
  program: Program;
};

export const ProgramCard = ({ program }: Props) => {
  console.log(program);

  return (
    <div className="sorryCard-style">
      <Typography.Title level={3}>{program.name}</Typography.Title>
      <div>{program.description}</div>
      <Button id="button-id" className="websiteButton-style" type="primary" href={program.url} target="_blank">
        Visit Website
      </Button>
    </div>
  );
};
