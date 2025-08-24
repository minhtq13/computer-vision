"use client";
// import { useAppSelector } from "@/libs/redux/store";
// import { getUserInfo } from "@/stores/user/selectors";
import { useEffect } from "react";
import useSocket from "./useSocket";

const useConnectSocket = () => {
  const { isConnected, connect, disconnect } = useSocket();
  // const user = useAppSelector(getUserInfo);

  useEffect(() => {
    if (true) {
      connect();
    } else {
      disconnect();
    }
  }, [isConnected]);
};

export default useConnectSocket;
