"use client";

import { ArrowLeft, ShieldAlert } from "lucide-react";
import { useRouter } from "next/navigation";
import AppButton from "../app-button";

const AccessDenied = () => {
  const router = useRouter();

  const handleBack = () => {
    router.back();
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-200px)] bg-gray-50 text-center p-6">
      <ShieldAlert className="w-24 h-24 text-red-500 mb-6" />
      <h1 className="text-4xl font-bold text-gray-800 mb-2">Access Denied</h1>
      <p className="text-lg text-gray-600 mb-8">Sorry, you do not have the necessary permissions to access this page.</p>
      <AppButton onClick={handleBack} type="primary">
        <div className="flex items-center">
          <ArrowLeft className="w-4 h-4 mr-2" />
          Go Back
        </div>
      </AppButton>
    </div>
  );
};

export default AccessDenied;
