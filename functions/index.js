const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();
const kostRef = db.collection('Kost').doc();
const id = kostRef.id;


exports.createNewUser = functions.https.onCall((data, context) =>{
	var jsonParams = JSON.parse(data.jsonParams);

	return new Promise(async (resolve, reject) => {

		admin.auth().createUser({
			email: jsonParams.userEmail,
			password: jsonParams.userPassword
		})
		.then(function(userRecord) {
			console.log(JSON.stringify(userRecord));
			db.collection("User").doc(userRecord.uid).set({
				userId:userRecord.uid,
				userName: jsonParams.userName,
				userEmail: jsonParams.userEmail,
				userPassword : jsonParams.userPassword,
				userKtp: jsonParams.userKtp,
				userType: jsonParams.userType
			})
			console.log(userRecord.uid);
			console.log(JSON.stringify(test));
			return resolve("Success");
		})
		.catch(function(error) {
			console.log("Error creating new user:", error);
			return reject(error);
		});
	})
});


exports.addKost = functions.https.onCall(async(data, context) => {
    var jsonParams = JSON.parse(data.jsonParams);

    admin.auth().createUser({
        email: jsonParams.kostOwner.userEmail,
        password: jsonParams.kostOwner.userPassword
    })
    .then(function(userRecord) {
        console.log(JSON.stringify(userRecord));
        db.collection("User").doc(userRecord.uid).set({
            userId:userRecord.uid,
            userName: jsonParams.userName,
            userEmail: jsonParams.userEmail,
            userPassword : jsonParams.userPassword,
            userKtp: jsonParams.userKtp,
            userType: jsonParams.userType
        })
        db.collection("Kost").doc(id).set({
            kostId:id,
            kostName: jsonParams.kostName,
            kost

        })
        console.log(userRecord.uid);
        console.log(JSON.stringify(test));
        return resolve("Success");
    })
    .catch(function(error) {
        console.log("Error creating new user:", error);
        return reject(error);
    });

	// const kost = data.kost;
    // const addKost = await db.collection("Kost").doc(id).set(kost);
    

    // return addKost;
    

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
                  index ++;
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