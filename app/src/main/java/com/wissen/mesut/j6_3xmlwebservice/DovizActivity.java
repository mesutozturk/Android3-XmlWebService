package com.wissen.mesut.j6_3xmlwebservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wissen.mesut.j6_3xmlwebservice.entities.Doviz;

/**
 * Created by Mesut on 23.08.2017.
 */

public class DovizActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doviz);
        textView = (TextView) findViewById(R.id.detay);
        Intent intent = getIntent();
        //String mesaj = intent.getStringExtra("deneme");
        Doviz gelenDoviz = (Doviz) intent.getSerializableExtra("doviz");
        textView.setText(gelenDoviz.toString());
    }
}
