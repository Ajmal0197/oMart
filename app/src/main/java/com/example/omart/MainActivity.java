package com.example.omart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.omart.Model.Users;
import com.example.omart.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton, crashCheck;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton=findViewById(R.id.main_join_now_btn);
        loginButton=findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);  //initializing paper in order to be used

//        crashCheck= findViewById(R.id.crashcheckbtn);
//        crashCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Crashlytics.getInstance().crash(); // Force a crash
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //reading value ie stored via paper so that we can open home page without again and again logging in
        String userPhoneKey=Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey=Paper.book().read(Prevalent.userPasswordKey);

        if(userPhoneKey!="" && userPasswordKey!="") //if not null
        {
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey)){

                allowAccess(userPhoneKey, userPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void allowAccess(final String phone, final String password)     //checkbox for users only not for admin(remembering the credentials)
    {
        final DatabaseReference rootRef;								//in fb, key is always of String type, whereas value can b anything like Object type
        rootRef = FirebaseDatabase.getInstance().getReference();		//pointing to the root node for write/read if .getReference("string") then points to child node.
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {	//addListenerForSingleValueEvent is runs only one time whenever value of child is updated rest like aCEL, vEL runs two time; addChildEventListener is called whenever child's value is affected; valueEventListener is for root value          
		
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
				//called every time any event occurs
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);	//.getValue(Users.class) bcoz we are getting values from that class type, it can also be generic like String.class in other scenario 

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Logged in successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser=usersData;  //for checkbox functioning
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

//                else{
//                    Toast.makeText(MainActivity.this, "Account with this "+ phone+ " number do not exist", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                    Toast.makeText(LoginActivity.this, "You need to create a new account.", Toast.LENGTH_SHORT).show();   }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
				//error handling code here
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
