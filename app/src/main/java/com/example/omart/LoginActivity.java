package com.example.omart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omart.Model.Users;
import com.example.omart.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhoneNumber, inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private String parentDBname="Users";
    private CheckBox chkBoxRememberMe;
    private TextView adminLink, notAdminLink, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        inputPassword = (EditText) findViewById(R.id.login_password_input);
        inputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        forgotPassword = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(LoginActivity.this, "Feature coming soon...", Toast.LENGTH_SHORT).show();
            }
        });

        adminLink=findViewById(R.id.admin_panel_link);
        notAdminLink=findViewById(R.id.not_admin_panel_link);

        chkBoxRememberMe=findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        //if adminLink/notAdminLink is clicked make following changes
        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Admin Login");
                adminLink.setVisibility(View.INVISIBLE);
                chkBoxRememberMe.setVisibility(View.GONE);
                notAdminLink.setVisibility(View.VISIBLE);
                forgotPassword.setVisibility(View.INVISIBLE);
                parentDBname="Admins";
            }
        });
        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                forgotPassword.setVisibility(View.VISIBLE);
                chkBoxRememberMe.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDBname="Users";
            }
        });
    }


    private void loginUser() {

        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone) || phone.length()<10)
        {
            inputPhoneNumber.setError("atleast 10 digits");
        }
        else if (TextUtils.isEmpty(password) || password.length()<6)
        {
            inputPassword.setError("atleast 5 alphanumeric characters");
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(phone, password);
        }
    }

    private void allowAccessToAccount(final String phone, final String password) {

        //storing via prevalent class(inside phone memory) in order to make available for checkbox
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.userPhoneKey, phone);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.child(parentDBname).child(phone).exists())
                {

                    Users usersData = dataSnapshot.child(parentDBname).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDBname.equals("Admins"))
                            {
                                String name=usersData.getName();
                                Toast.makeText(LoginActivity.this, "Welcome Admin "+ name +", Logged in successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDBname.equals("Users"))
                              {
                                String name=usersData.getName();
                                Toast.makeText(LoginActivity.this, "Welcome "+ name +", Logged in successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser=usersData;  //to remember current online user data for checkbox functioning
                                startActivity(intent);
                            }
                        }
                        else
                            {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect...", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                    {
                    Toast.makeText(LoginActivity.this, "Account with this "+ phone+ " number do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    //Toast.makeText(LoginActivity.this, "You need to create a new account.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
