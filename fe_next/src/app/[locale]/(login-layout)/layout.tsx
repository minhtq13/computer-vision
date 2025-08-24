import LoginLayout from "@/components/layout/LoginLayout";

export default async function LayoutLogin({ children }: { children: React.ReactNode }) {
  return <LoginLayout>{children}</LoginLayout>;
}
