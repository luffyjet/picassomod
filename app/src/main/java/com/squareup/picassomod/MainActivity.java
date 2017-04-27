package com.squareup.picassomod;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.imageview);
        Picasso.with(this).load("https://cloud.githubusercontent.com/assets/1963460/9204584/5c2b1e7a-405e-11e5-8ac2-3d95376b16b9.jpg").centerCrop().fit().into(imageView);
    }
}
