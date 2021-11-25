package application.dak.DAK.firebase;

import application.dak.DAK.backend.common.dto.Log;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.UUID;

import static application.dak.DAK.backend.utils.Constants.LOG_COLLECTION;

public class FirestoreService {

    public static FirebaseApp firebaseApp;
    private static Firestore firebaseDB;
    private static FirestoreService firestoreService;

    private FirestoreService(){
        setFirestore();
    }

    public static FirestoreService getInstance(){
        if(firestoreService == null)
            firestoreService = new FirestoreService();
        return firestoreService;
    }

    public static void setFirestore(){
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("./src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
            firebaseDB = FirestoreClient.getFirestore();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static FirebaseApp getFirebaseApp(){
        return firebaseApp;
    }

    public void log(String tag, String event){
        String logID = UUID.randomUUID().toString();
        HashMap<String, Log> log = new HashMap<>();
        log.put(logID, new Log(tag, event));
        firebaseDB.collection(LOG_COLLECTION).document(logID).set(log);
    }

}
