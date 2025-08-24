"use client";

import Image from "next/image";
import NotFoundImage from "@/assets/images/404.png";
import { useRouter } from "next/navigation";
import AppButton from "@/components/app-button";
const NotFound = () => {
  const router = useRouter();
  return (
    <section className="min-h-screen">
      <div className="min-h-screen w-full flex items-center justify-center flex-col gap-2">
        <Image src={NotFoundImage} alt="NotFoundImage" />
        <h2 className="text-2xl font-bold">Something went wrong!</h2>
        <AppButton type="primary" onClick={() => router.push("/")}>
          Go to home
        </AppButton>
      </div>
    </section>
  );
};
export default NotFound;
