package firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserIdentifier;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class FirebaseService {

    public static FirebaseApp firebaseApp;
    private static Firestore firebaseDB;

    public static Firestore getFirebaseDBInstance() {
        if (firebaseDB == null) {
            setFirestore();
        }
        return firebaseDB;
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

}
