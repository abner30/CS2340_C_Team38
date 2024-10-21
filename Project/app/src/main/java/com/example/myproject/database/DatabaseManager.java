import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseReference databaseReference;
  
    private DatabaseManager() {
        // initializing firebase connection
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // additional firebase config here
        //
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    // not sure if this is needed or not
    public void closeConnection() {
        instance = null;
    }
}
