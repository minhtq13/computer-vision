import React from 'react';
import { Form, Input, Button, Space } from 'antd';
import {DeleteOutlined, PlusOutlined} from '@ant-design/icons';
import './EmailForm.scss';

const EmailForm = ({onFinish}) => {
    const [form] = Form.useForm();

    return (
        <Form form={form} name="dynamic-form-nest-item" onFinish={onFinish} autoComplete="off">
            <Form.List name="emails">
                {(fields, { add, remove }) => (
                    <>
                        {fields.map(({ key, name, fieldKey, ...restField }) => (
                            <Space key={key} style={{ display: 'flex', marginBottom: 8}} align="baseline">
                                <Form.Item
                                    style={{width: '30vw'}}
                                    {...restField}
                                    name={[name, 'email']}
                                    fieldKey={[fieldKey, 'email']}
                                    rules={[{required: true, message: 'Email rỗng'}, { type: 'email', message: 'Email không hợp lệ' }]}
                                >
                                    <Input placeholder="abc@example.com"/>
                                </Form.Item>
                                <DeleteOutlined onClick={() => {
                                    remove(name);
                                }} />
                            </Space>
                        ))}
                        <div className="button-block">
                            <Form.Item>
                                <Button type="dashed" onClick={() => {
                                    add();
                                }} block icon={<PlusOutlined />}>
                                    Thêm Email
                                </Button>
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" htmlType="submit">
                                    Áp dụng
                                </Button>
                            </Form.Item>
                        </div>
                    </>
                )}
            </Form.List>
        </Form>
    );
};

export default EmailForm;