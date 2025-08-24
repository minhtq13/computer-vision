"use client";

import { Skeleton } from "antd";
import { useEffect, useState } from "react";

export default function ClientProvider({
  children,
  skeleton,
}: Readonly<{
  children: React.ReactNode | any;
  skeleton?: React.ReactNode | any;
}>) {
  const [isClient, setIsClient] = useState(false);
  useEffect(() => {
    setIsClient(true);
  }, []);
  if (!isClient) {
    if (skeleton) {
      return skeleton;
    }
    return (
      <div>
        <Skeleton.Button
          active={true}
          size={"default"}
          shape="square"
          block={true}
        />
      </div>
    );
  }
  return children;
}
