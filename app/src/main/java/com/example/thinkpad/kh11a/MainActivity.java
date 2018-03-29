package com.example.thinkpad.kh11a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btn1;
    private Button btn2;

    private String urlAddress="http://booktest.16mb.com/players.json";
    private ListView listView;
    private TextView tv_name;
    private TextView tv_age;
    private static String mName;
    private static int mAge;
    private TextView t1;
    private TextView t2;
    private TextView t3;

    private List<Map<String,Object>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initListener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //得到数据
                getInfo();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInfo();
            }
        });
    }

    private void setInfo() {
        t1.setText("Name: ");
        t2.setText("Age: ");
        t3.setText("PlayerList: ");
        Log.d(TAG, "mName为 " + mName);
        Log.d(TAG, "mAge为 " + mAge);
        tv_name.setText(mName);
        tv_age.setText(mAge+"");

        String []from={"name","pass","sex","1","2","3"};
        int []to={R.id.item_tv1, R.id.item_tv2, R.id.item_tv3,R.id.item_1,R.id.item_2,R.id.item_3};
        SimpleAdapter sa=new SimpleAdapter(this,list,R.layout.list_item,from,to);
        listView.setAdapter(sa);
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        listView = (ListView)findViewById(R.id.list_view);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_age = (TextView)findViewById(R.id.tv_age);
        t1 = (TextView) findViewById(R.id.title1_name);
        t2 = (TextView) findViewById(R.id.title2_age);
        t3 = (TextView) findViewById(R.id.title3_platerlist);
    }

    //getInfo
    private void getInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAddress);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(inputStream,"GBK"));
                        StringBuffer stringBuffer = new StringBuffer();
                        String readline = "";
                        while ((readline=bufferedReader.readLine()) != null) {
                            stringBuffer.append(readline);
                        }
                        inputStream.close();
                        bufferedReader.close();
                        httpURLConnection.disconnect();
                        String result=stringBuffer.toString();
                        Log.d(TAG, "resultBuffer== "+result);
                        parseData(result); //调用函数解析数据
                    } else {Log.d(TAG, "getResponse fail....////////////////////");}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //解析数据
    private void parseData(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            mName = jsonObject.getString("Name");
            mAge = jsonObject.getInt("Age");
            JSONArray jsonArray = jsonObject.getJSONArray("PlayerList");
            list=new ArrayList<Map<String,Object>>();
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Map map=new HashMap<String,Object>();
                map.put("name", object.getString("username"));
                map.put("pass", object.getString("userpass"));
                map.put("sex", object.getString("sex"));
                map.put("1", "username: ");
                map.put("2", "userpass: ");
                map.put("3","sex: ");
                list.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "得到的mName为 " + mName);
        Log.d(TAG, "得到的mAge为 " + mAge);
    }
}




