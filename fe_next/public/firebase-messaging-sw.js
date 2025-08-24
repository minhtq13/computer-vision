importScripts("https://www.gstatic.com/firebasejs/10.11.0/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/10.11.0/firebase-messaging-compat.js");

//the Firebase config object
const firebaseConfig = {
    apiKey: "AIzaSyD08WIGPF-SCcIBS06WvB06bW0uT2m0ZQA",
    authDomain: "elearningsupportsystem-55777.firebaseapp.com",
    projectId: "elearningsupportsystem-55777",
    storageBucket: "elearningsupportsystem-55777.appspot.com",
    messagingSenderId: "147611979267",
    appId: "1:147611979267:web:a7004e4246b0840090c836"
};

firebase.initializeApp(firebaseConfig)
const messaging = firebase.messaging();

// messaging.onBackgroundMessage(function(payload) {
//     console.log('Received background message ', payload);
//     const notificationTitle = payload.notification?.title;
//     const notificationOptions = {
//         body: payload.notification?.body,
//     };
//
//     self.registration.showNotification(notificationTitle,
//         notificationOptions).then(() => {});
// });