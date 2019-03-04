package com.erez8.chatpic;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.navigation.Navigation;
import de.hdodenhof.circleimageview.CircleImageView;


public class SingUpFragment extends Fragment {

    private Button signup_btn;
    private EditText email, password, name;
    private CircleImageView selectPhotoImage;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;


    private  final  int REQ_CODE = 101;


    public SingUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sing_up, container, false);

        signup_btn = v.findViewById(R.id.SignUpbtn);
        email = v.findViewById(R.id.signUpEmail);
        password = v.findViewById(R.id.SignUpPassword);
        name = v.findViewById(R.id.username1);

        selectPhotoImage = v.findViewById(R.id.selecttPhoto);

        auth = FirebaseAuth.getInstance();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    //TODO: replace fragment with camera2
                } else {
                    Log.d("fragment login", "user is not registered ");
                }
            }
        };
        selectPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, REQ_CODE);
            }
        });

        return v;

    }
//

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String mEmail = email.getText().toString();
                final String mPassword = password.getText().toString();
                final String mName = name.getText().toString();
                auth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("faild", "sign in error ");
                        } else {

                            uploadImageToFirebase(mEmail,mPassword,mName);
                            Navigation.findNavController(v).navigate(R.id.toChatFragment);
                        }
                    }
                });
            }
        });



    }

    private void uploadImageToFirebase(final String mEmail, final String mPassword, final String mName) {

        final StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString());

        if (afterResultPhoto != null) {
            firebaseStorage.putFile(afterResultPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    firebaseStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String userID = auth.getCurrentUser().getUid();
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            Map userInfo = new HashMap<>();
                            userInfo.put("email", mEmail);
                            userInfo.put("name", mName);
                            userInfo.put("password", mPassword);
                            userInfo.put("uid", userID);
                                userInfo.put("userImage", uri.toString());
                                db.updateChildren(userInfo);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
        String userID = auth.getCurrentUser().getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        Map userInfo = new HashMap<>();
        userInfo.put("email", mEmail);
        userInfo.put("name", mName);
        userInfo.put("password", mPassword);
        userInfo.put("uid", userID);


        userInfo.put("userImage", "default");
        db.updateChildren(userInfo);
    }


    private Uri afterResultPhoto = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            afterResultPhoto= data.getData();

            Picasso.get().load(afterResultPhoto).into(selectPhotoImage);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        auth.removeAuthStateListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
