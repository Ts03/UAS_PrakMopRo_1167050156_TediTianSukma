package com.restaurant.teditiansukma;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.teditiansukma.data.Constans;
import com.restaurant.teditiansukma.data.Session;
import com.restaurant.teditiansukma.model.RestaurantAction;
import com.restaurant.teditiansukma.utils.DialogUtils;

public class RestaurantActivity extends AppCompatActivity {

    Session session;
    EditText resto_name, resto_category, resto_imgurl, resto_address;
    Button create_restaurant;
    ProgressDialog progressDialog;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        userId = getIntent().getStringExtra("userId");
        initBinding();
        initClick();
    }

    private void initBinding() {
        resto_name = findViewById(R.id.resto_name);
        resto_category = findViewById(R.id.resto_category);
        resto_imgurl = findViewById(R.id.resto_imgurl);
        resto_address = findViewById(R.id.resto_address);
        create_restaurant = findViewById(R.id.btn_create_restaurant);
    }

    private void initClick() {
        create_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resto_name.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Namanya belum keisi Bos", Toast.LENGTH_SHORT).show();
                }else if(resto_category.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Kategori belum keisi Bos", Toast.LENGTH_SHORT).show();
                }else if(resto_imgurl.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Url Gambar belum keisi Bos", Toast.LENGTH_SHORT).show();
                }else if(resto_address.getText().toString().isEmpty()){
                    Toast.makeText(RestaurantActivity.this, "Alamat belum keisi Bos", Toast.LENGTH_SHORT).show();
                } else {
                    createRestaurant();
                }
            }
        });
    }
    public void createRestaurant() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.CREATE_RESTAURANT)
                .addBodyParameter("userid", userId)
                .addBodyParameter("namarm", resto_name.getText().toString())
                .addBodyParameter("kategori", resto_category.getText().toString())
                .addBodyParameter("link_foto", resto_imgurl.getText().toString())
                .addBodyParameter("alamat", resto_address.getText().toString())
                .build()
                .getAsObject(RestaurantAction.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RestaurantAction) {
                            RestaurantAction res = (RestaurantAction) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(RestaurantActivity.this,"Berhasil menambah restauran", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RestaurantActivity.this,"Gagal menambah restauran", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RestaurantActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RestaurantActivity.this, "Terjadi kesalahan : "+anError.getCause().toString(), Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }

}
