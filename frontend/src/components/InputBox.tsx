import { Button, Card, InputNumber, Row, Col } from "antd";
import { useState } from "react";

export function InputBoxCard() {
  const [q1, setQ1] = useState<number>();
  const q1IsValid = q1 !== undefined && q1.toString().length <= 5;

  return (
    <div>
      <div className="site-card-border-less-wrapper">
        <Card title="Input Box" bordered={false} style={{ width: 300 }}>
          <Row style={{ marginBottom: 20 }}>
            <Col>
              <InputNumber value={q1} min={0} onChange={(value) => setQ1(value)} />
            </Col>
          </Row>
          <Row>
            <>{q1IsValid === true && <div>Please enter 5 characters</div>}</>
          </Row>
        </Card>
      </div>

      <Button
        type="primary"
        onClick={async () => {
          if (!q1IsValid) {
            return;
          }
        }}
      >
        Next
      </Button>
    </div>
  );
}
