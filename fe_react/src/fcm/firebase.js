import {initializeApp} from "firebase/app";
import {getMessaging, getToken, onMessage} from "firebase/messaging"
import {registerFCMTokenService} from "../services/accountServices";

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: process.env.REACT_APP_FIREBASE_APIKEY,
    authDomain: process.env.REACT_APP_FIREBASE_AUTH_DOMAIN,
    projectId: process.env.REACT_APP_FIREBASE_PROJECT_ID,
    storageBucket: process.env.REACT_APP_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.REACT_APP_FIREBASE_MESSAGING_SENDER_ID,
    appId: process.env.REACT_APP_FIREBASE_APP_ID
};
// Initialize Firebase
const app = (process.env.REACT_APP_ENV === "production" || process.env.REACT_APP_ENV === "development") ? initializeApp(firebaseConfig) : undefined;
const messaging = app !== undefined ? getMessaging(app) : undefined;

export const requestPermission = () => {
    if (messaging !== undefined) {
        console.log("Requesting User Permission......");
        Notification.requestPermission().then((permission) => {
            if (permission === "granted") {
                console.log("Notification User Permission Granted.");
                return getToken(messaging, {vapidKey: process.env.REACT_APP_FCM_NOTIFICATION_KEY_PAIR})
                    .then((currentToken) => {
                        if (currentToken) {
                            console.log('Received client token successfully! [**********************]')
                            // console.log('Client Token: ', currentToken);
                        } else {
                            console.log('Failed to generate the app registration token.');
                        }
                        registerFCMTokenService({fcmToken: currentToken}).then(() => {});
                    })
                    .catch((err) => {
                        console.log('An error occurred when requesting to receive the token.', err);
                    });
            } else {
                console.log("User Permission Denied.");
            }
        });
    }
}

// onMessageListener
export const onMessageListener = () => new Promise((resolve) => {
    if (messaging !== undefined) {
        onMessage(messaging, (payload) => {
            resolve(payload);
        });
    }
});

