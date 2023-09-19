/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

// const {onRequest} = require("firebase-functions/v2/https");
// const logger = require("firebase-functions/logger");

const functions = require("firebase-functions");

const admin = require("firebase-admin");
admin.initializeApp();

exports.notifyNewMessage = functions.firestore
    .document("chatChannels/{channel}/messages/{message}")
    .onCreate((docSnapshot, context) => {
      const message = docSnapshot.data();
      const recipientId = message["recipientId"];
      const senderName = message["senderName"];

      return admin.firestore().doc("users/" + recipientId).get()
          .then((userDoc) => {
            const fcmToken = userDoc.get("fcmToken");

            const notificationBody = message["text"];

            const payload = {
              notification: {
                title: senderName + "sent you a message",
                body: notificationBody,
                clickAction: "RentOutActivity",
              },
              data: {
                USER_NAME: senderName,
                USER_ID: message["senderId"],
              },
            };
            return admin.messaging()
                .sendToDevice(fcmToken, payload).then((response) => {
                  const stillRegisteredToken = fcmToken;

                  // response.results.forEach((result, index) => {
                  //   const error = result.error;
                  //   if (error) {
                  //     const failedFCMToken = fcmToken[index];
                  //     console.error("blah", failedFCMToken, error);
                  //     if (error.code ===
                  //       "messaging/invalid-registration-token" ||
                  //         error.code ===
                  //           "messaging/registration-token-not-registered") {
                  //       const failedIndex = stillRegisteredToken
                  //           .indexOf(failedFCMToken);
                  //       if (failedIndex > -1) {
                  //         stillRegisteredToken.splice(failedFCMToken, 1);
                  //       }
                  //     }
                  //   }
                  // });
                  return admin.firestore().doc("users/" + recipientId).update({
                    fcmToken: stillRegisteredToken,
                  });
                });
          });
    });
