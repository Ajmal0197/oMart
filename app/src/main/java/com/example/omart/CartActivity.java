package com.example.omart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omart.Model.Cart;
import com.example.omart.Prevalent.Prevalent;
import com.example.omart.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button nextProcessBtn;
    private TextView txtTotalAmount, txtMsgCongrats;
    private int overallTotalPrice = 0;  //total price of all products selected




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //setting to recycler view
        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn=findViewById(R.id.next_process_btn);
        txtTotalAmount=findViewById(R.id.total_price);
        txtMsgCongrats=findViewById(R.id.msgCongrats);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //txtTotalAmount.setText("Total Price = ₹ "+ String.valueOf(overallTotalPrice));

                Intent intent=new Intent(getApplicationContext(), ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderStatus(); //for purchase validation

        //referencing parent node on recycler view
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        //displaying on recycler view as per the data on Fdb
        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText("Quantity = "+ model.getQuantity());
                holder.txtProductPrice.setText("₹ " + model.getPrice());
                holder.txtProductName.setText(model.getPname());

                //prices of all products selected and then their overall price
                int oneTypeProdTotalPrice= ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overallTotalPrice = overallTotalPrice + oneTypeProdTotalPrice;

                txtTotalAmount.setText("Total Price =  ₹ "+ String.valueOf(overallTotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "Edit",         //index-> 0
                                        "Remove"        //index-> 1
                                };

                        AlertDialog.Builder builder=  new AlertDialog.Builder(CartActivity.this);

                        builder.setTitle("Cart Options: ");
                        builder.setItems(options, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    Intent intent= new Intent(getApplicationContext(), ProductsDetailActivity.class);
                                    intent.putExtra("pid", model.getPid());     //getting product of specific id
                                    startActivity(intent);

                                }
                                if(which==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(model.getPid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();

                                                Intent intent= new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder holder= new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrderStatus()
    {
        DatabaseReference ordersRef;
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingStatus = dataSnapshot.child("status").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if(shippingStatus.equals("shipped"))
                    {
                        txtTotalAmount.setText("Dear "+ userName + "\n order is shipped successfully.");

                        recyclerView.setVisibility(View.GONE);
                        txtMsgCongrats.setVisibility(View.VISIBLE);
                        txtMsgCongrats.setText("Congratulation, Your final order has been shipped successfully. Soon you will receive your order at your door step.");
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase more products once you receive your first order.", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingStatus.equals("not shipped"))
                    {
                        txtTotalAmount.setText("Shipping Status = Not Shipped");

                        recyclerView.setVisibility(View.GONE);
                        txtMsgCongrats.setVisibility(View.VISIBLE);
                        txtMsgCongrats.setText("Congratulation, Your final order has been placed successfully. Soon it will be verified. \n Thanks for your purchase :)");
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase more products once you receive your first order.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(CartActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
