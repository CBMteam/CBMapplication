package com.cbm.cbmapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MenuChoice extends AppCompatActivity {

    String URL = "https://ad4d33a7ca5f.ngrok.io";

    private LineChart chart;
    TextView ml_tteresult;
    Intent intent = getIntent();
    Context mContext = this;

    // String name = intent.getStringExtra("name");

    String user_email;

    DialogGroup dialogGroup = new DialogGroup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuchoice);

        String tte = "";

        MenuChoice.JoinTask task = new MenuChoice.JoinTask(MenuChoice.this);
        System.out.println("tte 받으러 가는중");
        try {
            String result = task.execute(URL+"/tte").get();
            System.out.println("시간 : " + result);
            tte = Integer.toString(Integer.parseInt(result)*5);

            if (result.equals("failure")){
                dialogGroup.dialogJoinDuplicateId(MenuChoice.this);
                return;
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ml_tteresult = (TextView) findViewById(R.id.ml_tteresult);
        ml_tteresult.setText(tte+" 분 뒤");
        ImageButton btn_logout = (ImageButton) findViewById(R.id.logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginJoinSelectPage.class);
                startActivity(intent);
            }
        });

        ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapAPItest.class);
                startActivity(intent);
            }
        });


        ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent);
            }
        });

        ImageButton btn_registfriend = (ImageButton) findViewById(R.id.btn_registfriend);
        btn_registfriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FriendRegisterPage.class);
                startActivity(intent);
            }
        });


        // txtView.setText(name);


        // draw chart

        chart = findViewById(R.id.cgmchart);

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);

    }

    public class JoinTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "InsertTask";

        public Context mContext;

        public JoinTask(Context mContext) {
            super();

            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];



            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;;

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                java.net.URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();

                outputStream.flush();
                outputStream.close();


                // 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {

                    // 에러 발생

                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }



}
