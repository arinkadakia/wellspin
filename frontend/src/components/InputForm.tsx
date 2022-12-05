import { Button, Form, Input, Card, Space } from 'antd';
import { useState } from "react";

export function InputFormCard() {
    const [form] = Form.useForm();
    const [inputTxt, setInputTxt] = useState<string>();
    const inputTxtIsValid = inputTxt !== undefined && inputTxt.length <= 5;

    const skip = () => {
        form.setFieldsValue({
        email: 'https://taobao.com/',
        });
    };

    return (
        <div>
            <div className="site-card-border-less-wrapper">
                <Card title="Input Box" bordered={false} style={{ width: 300 }}>
                    <Form
                    form={form}
                    layout="vertical"
                    autoComplete="off"
                    >
                        <Form.Item
                            name="email"
                            label="Email"
                            rules={[{ required: true }, { type: 'email', warningOnly: true }, { type: 'string', min: 6 }]}
                        >
                            <Input 
                                placeholder="amysmith@gmail.com" 
                                value={inputTxt} 
                                min={0} 
                                onChange={(e: React.FormEvent<HTMLInputElement>) => setInputTxt(e.currentTarget.value)}
                            />
                        </Form.Item>
                        <Form.Item>
                            <Space>
                            <Button 
                                type="primary" 
                                htmlType="submit" 
                                onClick={async () => {
                                    if (!inputTxtIsValid) {
                                      return;
                                    }
                                  }}
                            >
                                Save email
                            </Button>
                            <Button htmlType="button" onClick={skip}>
                                No, continue
                            </Button>
                            </Space>
                        </Form.Item>
                        <Form.Item>
                            <>{inputTxtIsValid === true && <div>Please enter 5 characters</div>}</>
                        </Form.Item>
                    </Form>
                </Card>
            </div>
        </div>
    );
}