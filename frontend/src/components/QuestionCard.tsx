import { Button, Card, Col, Input, Select, Row, Typography, Radio, Space, Checkbox, Form } from "antd";
import { CheckboxValueType } from "antd/lib/checkbox/Group";
import { useState } from "react";
import { Question } from "../types";
import { Some } from "../utils/Some";
import "../Survey.css";

type Props = {
  question: Question;

  // TODO: will we need to modify this from "string" or does string support all possible input types
  // number, checkbox, dropdown etc...
  onNext: (answer: string[]) => void;
};

export const QuestionCard = ({ question, onNext }: Props) => {
  const [answer, setAnswer] = useState<string[]>([]);
  const [validationError, setValidationError] = useState<string>();
  const [form] = Form.useForm();
  console.log(question);

  if (question.type === "input_form") {
    return (
      <div className="card-style">
        <div className="question-title-style">{question.text}</div>

        <Form form={form} layout="vertical" autoComplete="off" onFinish={() => onNext(answer)}>
          <Form.Item name="email" rules={[{ type: "email" }]}>
            <Input
              className="input-box-style"
              placeholder="Enter a valid email address"
              value={answer}
              onChange={(e) => setAnswer([e.target.value])}
            />
          </Form.Item>

          <Form.Item>
            <Space>
              <Button className="button-style" htmlType="submit">
                Save email
              </Button>

              <Button className="dontSaveButton" type="default" onClick={() => onNext([])}>
                No, continue
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </div>
    );
  }

  return (
    <div className="card-style">
      <div className="question-title-style">{question.text}</div>

      <form
        onSubmit={(event) => {
          // This cancels the HTML default action, when you submit a form it will reload the page
          event.preventDefault();

          if (answer.length === 0 || answer[0] === "") {
            setValidationError("Please enter a valid answer");
          } else {
            // If validation passes, we tell the parent component what the answer is
            // Parent component will pop the answer into previousQuestions, and then ask the API for the next question
            // And maybe some other stuff too
            onNext(answer);
          }
        }}
      >
        <Row className="card-actions-style">
          <Col span={24}>
            {/* TODO: support all question.types possible */}
            {question.type === "text_box" && (
              <Input
                className="input-box-style"
                placeholder="Enter a number"
                value={answer}
                onChange={(e) => setAnswer([e.target.value])}
              />
            )}

            {question.type === "radio_button" && (
              <Radio.Group style={{ width: "100%" }} onChange={(e) => setAnswer([e.target.value])}>
                {question.answer
                  .filter((answer) => answer !== "")
                  .map((answer) => (
                    <Space direction="vertical" key={answer}>
                      <Radio value={answer}>{answer}</Radio>
                    </Space>
                  ))}
              </Radio.Group>
            )}

            {question.type === "drop_down" && (
              <Select style={{ width: "100%" }} onChange={(answer: string) => setAnswer([answer])}>
                {question.answer
                  .filter((answer) => answer !== "")
                  .map((answer) => (
                    <Select.Option key={answer} value={answer}>
                      {answer}
                    </Select.Option>
                  ))}
              </Select>
            )}

            {question.type === "check_box" && (
              <Checkbox.Group
                style={{ width: "100%" }}
                onChange={(checkedValues: CheckboxValueType[]) =>
                  setAnswer(checkedValues.map((checkboxValue) => checkboxValue.toString()))
                }
              >
                {question.answer
                  .filter((answer) => answer !== "")
                  .map((answer) => (
                    <div key={answer} style={{ textAlign: "left" }}>
                      <Checkbox value={answer}>{answer}</Checkbox>
                    </div>
                  ))}
              </Checkbox.Group>
            )}

            {/* Handle "Checkbox" by using Antd Checkbox.Group */}

            {Some(validationError) && <Typography.Text type="danger">{validationError}</Typography.Text>}
          </Col>
        </Row>

        <Button htmlType="submit" className="button-style">
          Next
        </Button>
      </form>
    </div>
  );
};
