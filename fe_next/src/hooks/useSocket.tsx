/* eslint-disable react-hooks/exhaustive-deps */
"use client";
import { SocketContext } from "@/libs/socket/context";
import { useContext } from "react";

const useSocket = () => useContext(SocketContext);

export default useSocket;
