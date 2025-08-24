"use client";

import AppButton from "@/components/app-button";

export default function GlobalError({ error, reset }: { error: Error & { digest?: string }; reset: () => void }) {
  console.log(error);
  return (
    <div className="text-black">
      <h2>Something went wrong!</h2>
      <AppButton onClick={() => reset()}>Try again</AppButton>
    </div>
  );
}
