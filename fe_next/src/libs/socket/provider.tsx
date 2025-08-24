"use client";
import Cookies from "js-cookie";
import React, { useCallback, useRef, useState } from "react";
import { Socket, io } from "socket.io-client";
import { CookieSessionKey } from "../session/constants";
import { SocketContext } from "./context";

const domain = process.env.NEXT_PUBLIC_API_ENDPOINT;

interface Props {
  children: React.ReactNode;
}

const SocketProvider = ({ children }: Props) => {
  const accessToken = Cookies.get(CookieSessionKey.user);
  const socket = useRef<Socket | null>(null);
  const [isConnected, setIsConnected] = useState<boolean>(false);

  // handle event
  const handleConnect = useCallback(() => {
    setIsConnected(true);
    console.log("Connect socket successfully!");
  }, []);
  const handleConnectError = useCallback(() => {
    setIsConnected(false);
    console.log("Connect socket failed");
  }, []);
  const handleError = useCallback((error: any) => {
    console.log("error", error);
  }, []);
  const handleMissingSocketInstance = useCallback(() => {
    console.log("Socket hasn't been initiated");
  }, []);

  const connect = useCallback(() => {
    if (!socket.current) {
      socket.current = io(domain || "", {
        autoConnect: false,
        transports: ["websocket", "polling"],
        query: {
          authorization: accessToken,
        },
      });

      socket.current.on("connect", handleConnect);
      socket.current.on("connect_error", handleConnectError);
      socket.current.io.on("error", handleError);
    }

    if (!isConnected && socket.current.disconnected) {
      socket.current.connect();
    }
  }, [accessToken, domain, handleConnect, handleConnectError, handleError, isConnected]);

  const disconnect = () => {
    if (socket.current) {
      socket.current.removeAllListeners();
      console.log("Remove all listeners and disconnect socket");
      socket.current.disconnect();
      socket.current.on("disconnect", (reason) => {
        console.log("Disconnect socket successfully!", reason);
      });
      socket.current = null;
      setIsConnected(false);
    }
  };

  const addEvent = useCallback((eventName: string, handleEventCallback: (data: any) => void) => {
    if (socket.current) {
      console.log(`Added event ${eventName}`);
      socket.current.on(eventName, handleEventCallback);
    } else {
      handleMissingSocketInstance();
    }
  }, []);
  const removeEvent = useCallback((eventName: string, listener?: (data: any) => void) => {
    if (socket.current) {
      if (listener) {
        socket.current.off(eventName, listener);
        console.log(`Removed listener of [${eventName}]`);
      } else {
        socket.current.off(eventName);
        console.log(`Removed all listeners of [${eventName}]`);
      }
    } else {
      handleMissingSocketInstance();
    }
  }, []);

  return <SocketContext.Provider value={{ isConnected, addEvent, removeEvent, connect, disconnect }}>{children}</SocketContext.Provider>;
};

export default SocketProvider;
