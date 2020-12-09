package com.example.neostore2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ExampleViewHolder> {
    public List<DataCart> mExampleList;
    Activity context;
    String[] items = new String[] {"Select","1", "2", "3","4","5","6","7","8"};
    String token;
    Boolean initialDisplay = true;
    TextView tool;




    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView cimage;
        public TextView cname;
        public TextView cprice;
        public TextView ccategory;
        public Spinner spinner;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            cimage = itemView.findViewById(R.id.ivCImage);
            cname = itemView.findViewById(R.id.tvCName);
            cprice = itemView.findViewById(R.id.tvCPrice);
            ccategory = itemView.findViewById(R.id.tvCcategory);
            spinner = itemView.findViewById(R.id.cartSpinner);
        }
    }


    public AdapterCart(List<DataCart> exampleList, Activity context, String atoken, TextView totalbox) {
        mExampleList = exampleList;
        this.context = context;
        this.token = atoken;
        this.tool= totalbox;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_cart, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        final DataCartProduct currentItem = mExampleList.get(position).getProduct();

        holder.cname.setText(currentItem.getName());
        String val = currentItem.getCategory();
        holder.ccategory.setText(val);
        String s = mExampleList.get(position).getQuantity();
        int q = Integer.parseInt(s);

        int first = Integer.valueOf(currentItem.getTotal());
        int base = first / q;

        int cost = Integer.valueOf(currentItem.getTotal());



        holder.cprice.setText("Rs. "+format(cost));
        Glide.with(context).load(currentItem.getProductImage()).into(holder.cimage);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(q);


        initialDisplay = false;
        holder.spinner.setOnTouchListener((v,me) -> {initialDisplay = true; v.performClick(); return false;});

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(initialDisplay){
                    int done = base * position;


                    holder.cprice.setText("Rs. "+format(done));

                    int itemPosition = holder.spinner.getSelectedItemPosition();
                    editcart(token, currentItem.getId(), itemPosition);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void editcart(String token, String id, int itemPosition) {




                Call<ResponseEditCart> call =  RetrofitClient.getInstance().getApi()
                .editCart(token, id, String.valueOf(itemPosition));


        call.enqueue(new Callback<ResponseEditCart>() {
            @Override
            public void onResponse(Call<ResponseEditCart> call, Response<ResponseEditCart> response) {




            }

            @Override
            public void onFailure(Call<ResponseEditCart> call, Throwable t) {

            }
        });

        Call<ResponseCart> call1 = RetrofitClient.getInstance().getApi()
                .listcart(token);
        call1.enqueue(new Callback<ResponseCart>() {
            @Override
            public void onResponse(Call<ResponseCart> call, Response<ResponseCart> response) {

                String total = format(Integer.valueOf(response.body().getTotal()));

                tool.setText("Rs. " + total);
            }

            @Override
            public void onFailure(Call<ResponseCart> call, Throwable t) {

            }
        });
    }


    private String format(int amount){
        return NumberFormat.getNumberInstance(new Locale("en", "in")).format(amount);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}