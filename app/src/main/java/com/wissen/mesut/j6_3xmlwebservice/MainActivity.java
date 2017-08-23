package com.wissen.mesut.j6_3xmlwebservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.wissen.mesut.j6_3xmlwebservice.async.DovizServiceAsyncTask;
import com.wissen.mesut.j6_3xmlwebservice.entities.Doviz;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String xmlUrl = "http://www.tcmb.gov.tr/kurlar/today.xml";
    ListView listView;
    ArrayList<Doviz> dovizler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        try {
            DovizServiceAsyncTask task = new DovizServiceAsyncTask(this, listView);
            task.execute(xmlUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
