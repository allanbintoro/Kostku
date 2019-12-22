var functions = require('firebase-functions');
var admin = require('firebase-admin');

admin.initializeApp({
  credential: admin.credential.applicationDefault(),
  databaseURL: "https://kostku-d9d46.firebaseio.com"
});

const db = admin.firestore();
var FieldValue = require("firebase-admin").firestore.FieldValue;
const kostRef = db.collection('Kost').doc();
const id = kostRef.id;

var sevenDays = 604800;


exports.createNewUser = functions.https.onCall((data, context) => {
  var jsonParams = JSON.parse(data.jsonParams);

  return new Promise(async (resolve, reject) => {
    admin.auth().createUser({
      email: jsonParams.userEmail,
      password: jsonParams.userPassword
    })
    .then(async(userRecord)=> {
      const test= await db.collection("User").doc(userRecord.uid).set({
        userId: userRecord.uid,
        userName: jsonParams.userName,
        userEmail: jsonParams.userEmail,
        userPassword: jsonParams.userPassword,
        userKtp: jsonParams.userKtp,
        userType: jsonParams.userType
      });
      console.log(JSON.stringify(userRecord));
      console.log(JSON.stringify(jsonParams));
      await db.collection("User").doc(userRecord.uid).set({
        userId: userRecord.uid,
        userName: jsonParams.userName,
        userEmail: jsonParams.userEmail,
        userPassword: jsonParams.userPassword,
        userKtp: jsonParams.userKtp,
        userType: jsonParams.userType
      })
      if (jsonParams.userType === "3") {
        await db.collection("Kost").doc(jsonParams.kostId).collection("Room").doc(jsonParams.roomId).set({
          userId: userRecord.uid,
          roomStatus: false,
          dateIn: Date.now(),
          dueDate: Date.now() + (1000 * 60 * 60 * 24 * 30)
        }, { merge: true });
      }
      console.log(test);
      console.log(Date.now())
      return resolve("Success");
    })
      .catch((error)=> {
        console.log("Error creating new user:", error);
        return reject(error);
      });
  })
});

exports.loadKost = functions.https.onCall((data, context) => {

  return new Promise((resolve, reject) => {

    var kosts = {};
    var index = 0;

    db.collection('Kost')
      .get()
      .then(snapshot => {

        snapshot.forEach(doc => {

          var kost = doc.data();
          //color['uid'] = key;

          kosts[index] = kost;
          index++;
        });

        var kostsStr = JSON.stringify(kosts, null, '\t');
        console.log('kosts callback result : ' + kostsStr);

        resolve(kostsStr);
        return null;
      })
      .catch(reason => {
        console.log('db.collection("Kost").get gets err, reason: ' + reason);
        reject(reason);
      });
  });

});

exports.midtransCallBack = functions.https.onRequest(async (req, res) => {

  var gross_amount = req.body.gross_amount;
  var order_id = req.body.order_id;
  var payment_type = req.body.payment_type;
  var status_code = req.body.status_code;
  var transaction_id = req.body.transaction_id;
  var transaction_status = req.body.transaction_status;
  var transaction_time = req.body.transaction_time;
  var settlement_time = req.body.settlement_time;
  var transaction_type = "income"

  if (status_code === "200") {
    console.log("transaction success");
    const finishTransaction = {
      status_code: status_code,
      transaction_status: transaction_status,
      settlement_time: Date.now(),
      transaction_type: transaction_type
    }

    await db.collection('Transaction').doc(order_id).set(finishTransaction, { merge: true });
    // await db.collectionGroup('Transaction').doc(order_id).set(newTransaction, {merge: true});

  } else {
    console.log("transaction Unfinished");
    const newTransaction = {
      gross_amount: gross_amount,
      order_id: order_id,
      payment_type: payment_type,
      transaction_id: transaction_id,
      transaction_status: transaction_status,
      transaction_time: transaction_time,
      status_code: status_code
    }

    await db.collection('Transaction').doc(order_id).set(newTransaction, { merge: true });

  }

  res.end();
});


// exports.onTransactionFinish = functions.firestore.document('Transaction/{transactionId}').onWrite((change, context)=>{
//     const newValue = change.after.data;
//     const previousData = change.before.data();

//     if (data.transaction_status === previousData.transaction_status) return null;
//     if(data.transaction_status === newValue.transaction_status)

// })

