import { Form, FormInstance } from "antd";
import React from "react";
import AppButton from "@/components/app-button";

interface SubmitButtonProps {
  form: FormInstance;
  children: React.ReactNode;
  loading?: boolean;
  customclass?: string;
  size?: "small" | "middle" | "large";
}

const SubmitButton: React.FC<SubmitButtonProps> = ({ form, children, loading, customclass, size = "large", ...props }) => {
  const [submittable, setSubmittable] = React.useState<boolean>(false);

  const values = Form.useWatch([], form);

  React.useEffect(() => {
    form
      .validateFields({ validateOnly: true })
      .then(() => setSubmittable(true))
      .catch(() => setSubmittable(false));
  }, [form, values]);

  return (
    <AppButton size={size} type="primary" htmlType="submit" disabled={!submittable} loading={loading} customclass={customclass} {...props}>
      {children}
    </AppButton>
  );
};
export default SubmitButton;
