package com.wissen.mesut.j6_3xmlwebservice.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wissen.mesut.j6_3xmlwebservice.entities.Doviz;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Mesut on 23.08.2017.
 */public class DovizServiceAsyncTask extends AsyncTask<String, String, ArrayList<Doviz>> {

    private Context context;
    private ListView listView;
    private ProgressDialog progressDialog;

    public DovizServiceAsyncTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        //UI hread içerisinde yürütülür
        //İlk çalışan metotdur
        //task çalışmadan önce yapılacak hazırlık işlemleri burada hazırlanabilir
        progressDialog = progressDialog.show(context, "Lütfen bekleyiniz", "İşlem yürütülüyor", true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressDialog.setMessage("Veri okuma işlemi tamamlandı");
            }
        });
    }

    @Override
    protected ArrayList<Doviz> doInBackground(String... params) {
        //override edilmesi zorunu
        //1. parametre buraya geliyor
        //execute metoduna verilen arguman ile çağırılır. arkaplan işlemleri burada yapılır.
        //geriye dönen değer 3. parametre tipinde ve onpostexecute metoduna arguman olarak veriliyor.
        //UI thread içinde değil yardımcı thread içerisinde çalışır..
        //burada arayüz güncellenemez..
        //publisProgress metodu ile onProgressUpdate metoduna bilgi gönderiliyor
        ArrayList<Doviz> dovizler = new ArrayList<>();
        HttpURLConnection baglanti = null;
        try {
            URL url = new URL(params[0]);
            baglanti = (HttpURLConnection) url.openConnection();
            if (baglanti.getResponseCode() == HttpURLConnection.HTTP_OK) {
                publishProgress("Döviz kurları okunuyor");
                BufferedInputStream stream = new BufferedInputStream(baglanti.getInputStream());
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document document = documentBuilder.parse(stream);

                NodeList dovizNodeList = document.getElementsByTagName("Currency");
                for (int i = 0; i < dovizNodeList.getLength()-1; i++) {
                    Element element = (Element) dovizNodeList.item(i);
                    NodeList nlbirim = element.getElementsByTagName("Unit");
                    NodeList nlisim = element.getElementsByTagName("Isim");
                    NodeList nlalis = element.getElementsByTagName("ForexBuying");
                    NodeList nlsatis = element.getElementsByTagName("ForexSelling");

                    Doviz doviz = new Doviz();
                    doviz.setIsim(nlisim.item(0).getFirstChild().getNodeValue());
                    doviz.setBirim(nlbirim.item(0).getFirstChild().getNodeValue());
                    doviz.setAlis(Double.valueOf(nlalis.item(0).getFirstChild().getNodeValue()));
                    doviz.setSatis(Double.valueOf(nlsatis.item(0).getFirstChild().getNodeValue()));

                    dovizler.add(doviz);
                    publishProgress("Liste güncelleniyor");
                }
            }else{
                publishProgress("İnternet bağlantısı bulunamadı");
            }
            return dovizler;
        } catch (Exception ex) {
            Toast.makeText(context, "XML Okuma Hatası", Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            if (baglanti != null)
                baglanti.disconnect();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //UI thread içerisinde yürütülür..
        //2. parametre tipinde değer buraya armuman olarak geliyor
        //ProgressDialog güncelleniyor..
        //doInBackgraund içerisinden publishProgress metodu ile arguman gönderiliyor
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Doviz> dovizs) {
        //UI Thread içerisinden yürütülür
        //Parametre olarak doInBackgraund metodunun sonucunu alır

        Toast.makeText(context, "İşlem Tamamlandı", Toast.LENGTH_LONG).show();
        if(dovizs!=null){
            ArrayAdapter<Doviz> adapter= new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,dovizs);
            listView.setAdapter(adapter);
            progressDialog.cancel();
        }
    }

    @Override
    protected void onCancelled(ArrayList<Doviz> dovizs) {
        super.onCancelled(dovizs);
        //UI Thread içerisinden yürütülür
        //Herhangi bir sebepten dolayı AsyncTask iptal edilirse bu metod uyarılır.
        //Burada kullandığınız kaynakları temizleyebilirsiniz.
    }
}
