package com.wissen.mesut.j6_3xmlwebservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wissen.mesut.j6_3xmlwebservice.async.DovizServiceAsyncTask;
import com.wissen.mesut.j6_3xmlwebservice.entities.Doviz;

import java.util.ArrayList;

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
            dovizler = task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Doviz seciliDoviz = dovizler.get(position);
                Toast.makeText(MainActivity.this, seciliDoviz.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DovizActivity.class);
                //intent.putExtra("deneme", "bir ki üç");
                intent.putExtra("doviz", seciliDoviz);
                startActivity(intent);
            }
        });
    }

}
