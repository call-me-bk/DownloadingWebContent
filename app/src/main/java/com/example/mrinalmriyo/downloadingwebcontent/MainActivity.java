package com.example.mrinalmriyo.downloadingwebcontent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> list;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try
            {
                url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();

                while(data!=-1)
                {
                    char current=(char)data;
                    result=result+current;
                    data=reader.read();
                }
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String jsonString=jsonObject.getString("Sheet1");
                JSONArray  jsonArray=new JSONArray(jsonString);

                for(int i=0;i<jsonArray.length();i++) {

                    JSONObject details=jsonArray.getJSONObject(i);
                    String itemName=details.getString("Item");
                    String quantity=details.getString("Quantity");

                    list.add(itemName+":"+quantity);

                }

                arrayAdapter=new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        list);
                listView.setAdapter(arrayAdapter);

            }
            catch (Exception e)
            {

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listView);
        list=new ArrayList<String>();

        DownloadTask task=new DownloadTask();
        task.execute("https://script.google.com/macros/s/" +
                "AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/" +
                "exec?id=1537eUPEsA76XxMM0xo5AqhHmeKh3pJtXkQcSpa4RMXg&sheet=Sheet1");
    }
}
