import type { RadioChangeEvent } from "antd";
import { Radio, Row, Card } from "antd";
import { useState } from "react";

export function RadioButtonCard() {
  const [value, setValue] = useState(1);

  const onChange = (e: RadioChangeEvent) => {
    console.log("radio checked", e.target.value);
    setValue(e.target.value);
  };

  return (
    <>
      <Card title="Radio Button" style={{ width: 300 }}>
        <Radio.Group onChange={onChange} value={value}>
          <Row>
            <Radio value={0}>Option A</Radio>
          </Row>
          <Row>
            <Radio value={1}>Option B</Radio>
          </Row>
          <Row>
            <Radio value={2}>Option C</Radio>
          </Row>
        </Radio.Group>
      </Card>
    </>
  );
}
