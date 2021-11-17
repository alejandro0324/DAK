import { initializeApp } from "firebase/app";
import { getAuth, onAuthStateChanged, signInWithEmailAndPassword, sendPasswordResetEmail, createUserWithEmailAndPassword} from 'firebase/auth';

var app;

window.ns = {
initApp: function(){
    const firebaseConfig = {
      apiKey: "AIzaSyBQ02Tx7m3X_RZnnk7u0IN-TQ8kufxjrFU",
      authDomain: "obligatorio-53b03.firebaseapp.com",
      projectId: "obligatorio-53b03",
      storageBucket: "obligatorio-53b03.appspot.com",
      messagingSenderId: "424607021006",
      appId: "1:424607021006:web:ebf1a49e85993fbeb0dd9f"
    };

    // Initialize Firebase
    app = initializeApp(firebaseConfig);

    console.log("Firebase has been initialized");

    return app;
}
}
window.lg = {
     login: function(email, password, view){
         const auth = getAuth(app);
         signInWithEmailAndPassword(auth, email, password).
               then((userCredential) => {
                 const user = userCredential.user;
                 view.$server.loginOk(email);
               })
               .catch((error) => {
                 view.$server.loginError();
               })
     },
 }
 window.fo = {
      forgot: function(email, view){
          const auth = getAuth(app);
          sendPasswordResetEmail(auth, email).
                then((userCredential) => {
                  view.$server.sentOk();
                })
                .catch((error) => {
                    alert("pepe");
                })
      },
  }
   window.cr = {
        create: function(email, password, view){
            const auth = getAuth(app);
            createUserWithEmailAndPassword(auth, email, password).
                  then((userCredential) => {
                    view.$server.createOk();
                  })
                  .catch((error) => {
                    view.$server.createFail();
                  })
        },
    }