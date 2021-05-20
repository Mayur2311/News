package com.example.mainactivity;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.mainactivity.firebasecallbacks.FirestoreWrite;
import com.example.mainactivity.firebasecallbacks.LoginCallBacks;
import com.example.mainactivity.firebasecallbacks.ReadCallBacks;
import com.example.mainactivity.firebasecallbacks.RegisterCallBacks;
import com.example.mainactivity.models.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseComponents {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Activity activity;
    Person person;

    public FirebaseComponents() {
    }

    public FirebaseComponents(FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
    }

    public FirebaseComponents(FirebaseAuth firebaseAuth, Activity activity) {
        this.firebaseAuth = firebaseAuth;
        this.activity = activity;
    }

    public FirebaseComponents(FirebaseFirestore firebaseFirestore, FirebaseAuth firebaseAuth, Activity activity) {
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseAuth = firebaseAuth;
        this.activity = activity;
    }

    public void loginUser(String email, String password, LoginCallBacks loginCallbacks)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {

            if(task.isSuccessful())
            {
                loginCallbacks.loginCallBack(true);
            }else{
                loginCallbacks.loginCallBack(false);
            }

        });
    }

    public void readFireStore(ReadCallBacks readCallbacks)
    {
        DocumentReference docRef =  firebaseFirestore.collection("User").document(firebaseUser.getUid());

        docRef.get().addOnCompleteListener(task -> {

            if (task.isSuccessful())
            {
                DocumentSnapshot doc = task.getResult();

//                Log.d("FirebaseFirestore", doc.getString("Name"));

                person = new Person(doc.getString("Email"), doc.getString("First Name"),doc.getString("Last Name"),doc.getString("Phone"), doc.getString("Location"));

                readCallbacks.onCallbacks(person);
            }else{
                Log.d("FirebaseComponents", task.getException().toString());
            }
        });
    }

    public void createUser(Person person, RegisterCallBacks registerCallbacks)
    {
        firebaseAuth.createUserWithEmailAndPassword(person.getEmail(), person.getPassword())
                .addOnCompleteListener(activity, task -> {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        writeFireStore(person, firebaseUser, new FirestoreWrite() {
                            @Override
                            public void fireStoreWrite(boolean success) {
                                registerCallbacks.registerSuccess(success);
                            }
                        });
                    }else{
                        Toast.makeText(activity.getApplicationContext(), "Registration Error!!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void writeFireStore(Person person,  FirebaseUser firebaseUser, FirestoreWrite firestoreWrite)
    {

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("First Name", person.getFname());
        userMap.put("Last Name", person.getLname());
        userMap.put("Email", person.getEmail());
        userMap.put("Phone", person.getPhone());
        userMap.put("Location", person.getLocation());

        firebaseFirestore.collection("User").document(firebaseUser.getUid())
                .set(userMap).addOnCompleteListener(activity, task -> {

            if (task.isSuccessful())
            {
                Toast.makeText(activity.getApplicationContext(), "Registration Success!", Toast.LENGTH_SHORT).show();
                firestoreWrite.fireStoreWrite(true);
            }else{
                Toast.makeText(activity.getApplicationContext(), "Firestore Error!!!", Toast.LENGTH_SHORT).show();
                firestoreWrite.fireStoreWrite(false);
            }
        });
    }


}
