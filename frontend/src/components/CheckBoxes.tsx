import { Checkbox, Row, Card } from "antd";
import type { CheckboxValueType } from "antd/es/checkbox/Group";

type Props = {
  data: string[];
  onChange: (checkedValues: CheckboxValueType[]) => void;
};

export const CheckBoxCard = ({ data, onChange }: Props) => {
  const options = [
    { label: data[0], value: 0 },
    { label: data[1], value: 1 },
    { label: data[2], value: 2 },
  ];

  return (
    <div>
      <div className="site-card-border-less-wrapper">
        <Card title="Check Boxes" bordered={false} style={{ width: 300 }}>
          <Row>
            <Checkbox.Group options={options} defaultValue={[0]} onChange={onChange} />
          </Row>

          <Checkbox.Group defaultValue={[0]} onChange={onChange} />
        </Card>
      </div>
    </div>
  );
};
