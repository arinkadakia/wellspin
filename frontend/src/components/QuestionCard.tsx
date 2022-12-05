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

  return (
    <div className="card-style">
      <Typography.Title level={3}>{question.text}</Typography.Title>

      <Row className="mb1">
        <Col span={24}>
          {/* TODO: support all question.types possible */}
          {question.type === "text_box" && (
            <Input placeholder="12345" value={answer} onChange={(e) => setAnswer([e.target.value])} />
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
            <Select style={{ width: "400px" }} onChange={(answer: string) => setAnswer([answer])}>
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
                  <Checkbox key={answer} value={answer}>
                    {answer}
                  </Checkbox>
                ))}
            </Checkbox.Group>
          )}

          {question.type == "input_form" && (
            <Form form={form} layout="vertical" autoComplete="off">
              <Form.Item
                name="email"
                label="Email"
                rules={[
                  { type: "email", warningOnly: true },
                  { type: "string", min: 6 },
                ]}
              >
                <Input placeholder="amysmith@gmail.com" value={answer} onChange={(e) => setAnswer([e.target.value])} />
              </Form.Item>
              <Form.Item>
                <Space>
                  <Button
                    className="button-style"
                    htmlType="submit"
                    onClick={() => {
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
                    Save email
                  </Button>
                  <div className="dontSaveButton"> No, continue </div>
                </Space>
              </Form.Item>
            </Form>
          )}

          {/* Handle "Checkbox" by using Antd Checkbox.Group */}

          {Some(validationError) && <Typography.Text type="danger">{validationError}</Typography.Text>}
        </Col>
      </Row>

      {question.type != "input_form" && (
        <Button
          className="button-style"
          onClick={() => {
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
          Next
        </Button>
      )}
    </div>
  );
};
