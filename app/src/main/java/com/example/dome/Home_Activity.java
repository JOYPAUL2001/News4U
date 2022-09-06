package com.example.dome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dome.Models.NewsApiResponse;
import com.example.dome.Models.NewsHeadlines;

import java.util.List;

public class Home_Activity extends AppCompatActivity implements SelectListener, View.OnClickListener{
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    ImageButton profile;
    Button one,two,three,four,five,six,seven;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news article of "+query);
                dialog.show();
                RequestManager manager = new RequestManager(Home_Activity.this);
                manager.getNewsHeadlines(listner,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        one = findViewById(R.id.btn_1);
        one.setOnClickListener(this);
        two = findViewById(R.id.btn_2);
        two.setOnClickListener(this);
        three = findViewById(R.id.btn_3);
        three.setOnClickListener(this);
        four = findViewById(R.id.btn_4);
        four.setOnClickListener(this);
        five = findViewById(R.id.btn_5);
        five.setOnClickListener(this);
        six = findViewById(R.id.btn_6);
        six.setOnClickListener(this);
        seven = findViewById(R.id.btn_7);
        seven.setOnClickListener(this);





        profile = findViewById(R.id.bt_home_Profile);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching new articles..");
        dialog.show();

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listner,"general",null);


        //function to go profile

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_Activity.this,Profile.class));
            }
        });

    }

    private final OnFetchDataListner<NewsApiResponse> listner = new OnFetchDataListner<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            
            showNews(list);
            dialog.dismiss();

        }
        @Override
        public void onError(String message) {
            Toast.makeText(Home_Activity.this, "An Error Occured!!!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter= new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClick(NewsHeadlines headlines) {
         startActivity(new Intent(Home_Activity.this,DetailsActivity.class)
                 .putExtra("data",headlines));

    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;

        String category = button.getText().toString();
        dialog.setTitle("Fetching news article of "+category);
        dialog.show();
        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listner,category,null);
    }
}