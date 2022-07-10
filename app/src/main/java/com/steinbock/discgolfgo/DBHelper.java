package com.steinbock.discgolfgo;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    private static DBHelper db;
    private FirebaseFirestore firestore;

    public static DBHelper getInstance() {
        if (db == null)
            db = new DBHelper();
        return db;
    }

    private DBHelper() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void addDisc(DiscModel disc) {
        firestore.collection("discs")
                .add(disc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("Disc added: " + documentReference.getId());
                    }
                });
    }

    public void getDiscsByType(String type, DBCallback callback) {
        firestore.collection("discs")
                .whereEqualTo("type", type)
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DiscModel> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(DiscModel.class));
                        }
                        callback.onResult(list);
                    }
                });
    }

    interface DBCallback {
        void onResult(List<DiscModel> discs);
    }
}
