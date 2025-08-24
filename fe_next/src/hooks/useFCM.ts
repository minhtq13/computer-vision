import { useRegisterFCMTokenMutation } from "@/stores/notification/api";
import { FirebaseApp, initializeApp } from "firebase/app";
import { getMessaging, getToken, Messaging, onMessage } from "firebase/messaging";
import { useCallback } from "react"; // Import useCallback

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: process.env.NEXT_PUBLIC_FIREBASE_APIKEY,
  authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
  projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
  storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
};

let app: FirebaseApp | undefined;
let messaging: Messaging | undefined;

// Initialize Firebase app and messaging only on the client side
if (typeof window !== "undefined") {
  if (process.env.NEXT_PUBLIC_ENV === "production" || process.env.NEXT_PUBLIC_ENV === "development") {
    try {
      app = initializeApp(firebaseConfig);
      messaging = getMessaging(app);
      console.log("Firebase Messaging initialized successfully.");
    } catch (error) {
      console.error("Failed to initialize Firebase Messaging:", error);
    }
  } else {
    console.log("Firebase Messaging not initialized due to NEXT_PUBLIC_ENV settings.");
  }
} else {
  console.log("Firebase Messaging not initialized (not in browser environment).");
}

const useFCM = () => {
  const [registerFCMToken] = useRegisterFCMTokenMutation();

  const requestPermission = useCallback(() => {
    if (messaging !== undefined) {
      console.log("Requesting User Permission......");
      Notification.requestPermission().then((permission) => {
        if (permission === "granted") {
          console.log("Notification User Permission Granted.");
          return getToken(messaging, { vapidKey: process.env.NEXT_PUBLIC_FCM_NOTIFICATION_KEY_PAIR })
            .then(async (currentToken: any) => {
              if (currentToken) {
                console.log("Received client token successfully!");
                await registerFCMToken({ fcmToken: currentToken }).unwrap();
              } else {
                console.log("Failed to generate the app registration token.");
              }
            })
            .catch((err: any) => {
              console.log("An error occurred when requesting to receive the token.", err);
            });
        } else {
          console.log("User Permission Denied.");
        }
      });
    } else {
      console.warn("Firebase Messaging is not available or not initialized. Cannot request permission.");
    }
  }, [registerFCMToken]); // Add registerFCMToken as a dependency

  // onMessageListener
  const onMessageListener = useCallback(
    () =>
      new Promise((resolve, reject) => {
        // Added reject for better error handling
        if (messaging !== undefined) {
          onMessage(messaging, (payload) => {
            console.log("Message received in foreground:", payload);
            resolve(payload);
          });
        } else {
          console.warn("Firebase Messaging is not available or not initialized. Cannot listen for messages.");
          reject(new Error("Firebase Messaging not initialized"));
        }
      }),
    []
  ); // messaging is module-scoped and stable after init, so empty dependency array is fine

  return { onMessageListener, requestPermission };
};

export default useFCM;
