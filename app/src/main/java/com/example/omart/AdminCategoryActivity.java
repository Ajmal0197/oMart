package com.example.omart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminCategoryActivity extends AppCompatActivity {

    private TextView general_ailments, heart_care, stomach_care, skin_care;
    private TextView blood_care, brain_care,  oral_care, hair_care;
    private TextView cough_care, chest_care, liver_care, joint_care;

    private Button logoutBtn, checkOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        general_ailments=findViewById(R.id.general_ailments);
        heart_care=findViewById(R.id.heart_care);
        stomach_care=findViewById(R.id.stomach_care);
        skin_care=findViewById(R.id.skin_care);

        blood_care=findViewById(R.id.blood_care);
        brain_care=findViewById(R.id.brain_care);
        oral_care=findViewById(R.id.oral_care);
        cough_care=findViewById(R.id.cough_care);

        chest_care=findViewById(R.id.chest_care);
        liver_care=findViewById(R.id.liver_care);
        joint_care=findViewById(R.id.joint_care);
        hair_care=findViewById(R.id.hair_care);

        logoutBtn=findViewById(R.id.admin_logout_btn);
        checkOrderBtn=findViewById(R.id.check_orders_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(), AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        //sending with Extra to the image selected
        general_ailments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "General Ailment");
                startActivity(intent);
            }
        });

        heart_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Heart Care");
                startActivity(intent);
            }
        });

        stomach_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Stomach Care");
                startActivity(intent);
            }
        });

        skin_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Skin Care");
                startActivity(intent);
            }
        });

        blood_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Blood Care");
                startActivity(intent);
            }
        });

        brain_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Brain Care");
                startActivity(intent);
            }
        });

        oral_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Oral Care");
                startActivity(intent);
            }
        });

        cough_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Cough Care");
                startActivity(intent);
            }
        });

        chest_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Chest Care");
                startActivity(intent);
            }
        });

        liver_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Liver Care");
                startActivity(intent);
            }
        });

        joint_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Joint Care");
                startActivity(intent);
            }
        });

        hair_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AdminAddNewProductActivity.class));
                Intent intent=new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
                intent.putExtra("category", "Hair Care");
                startActivity(intent);
            }
        });
    }
}
