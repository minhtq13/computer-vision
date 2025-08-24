export interface SocketInterface {
  isConnected: boolean;
  addEvent: (
    eventName: string,
    handleEventCallback: (data: any) => void
  ) => void;
  removeEvent: (eventName: string, listener?: (data: any) => void) => void;
  connect: () => void;
  disconnect: () => void;
}
