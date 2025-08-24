import { createContext } from "react";
import { SocketInterface } from "./types";

export const SocketContext = createContext<SocketInterface>({
  isConnected: false,
  addEvent: () => {},
  removeEvent: () => {},
  connect: () => {},
  disconnect: () => {},
});
