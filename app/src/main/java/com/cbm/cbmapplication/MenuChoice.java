package com.cbm.cbmapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class MenuChoice extends AppCompatActivity {

    private LineChart chart;
    TextView ml_tteresult;
    Intent intent = getIntent();
    Context mContext = this;

    private String IP_ADDRESS = "223.194.46.209";

    // String name = intent.getStringExtra("name");

    String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuchoice);

        user_email = PreferenceManager.getString(mContext, "user_email");


        // draw chart

        chart = findViewById(R.id.cgmchart);

        MenuChoice.getBloodSugarTask task = new MenuChoice.getBloodSugarTask(MenuChoice.this);
        task.execute("http://"+IP_ADDRESS+"/getbloodsugarlist.php",user_email);


        ml_tteresult = (TextView) findViewById(R.id.ml_tteresult);
        ml_tteresult.setText((int)(Math.random()*100)+" 분 뒤");
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


    }



    public class getBloodSugarTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "getBloodSugarTask";

        public Context mContext;

        public getBloodSugarTask(Context mContext) {
            super();

            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /* http 연결 진행이 끝나고 난 후에 사용되는곳 (result -> http 연결 후 결과) */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "response - " + result); //로그로 결과 우선 확인
            if (result == null) {
                Log.d(TAG, "result is null - ");
            } else {
                showResult(result); //JSON 형태의 결과를 처리
            }
        }


        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // TODO : 아래 형식처럼 원하는 key과 value를 계속 추가시킬수있다.
            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String user_email = (String) params[1];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "user_email="+user_email;

            Log.d(TAG, postParameters);

            StringBuilder jsonHtml = new StringBuilder();

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);//5초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000);//5초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST");//요청 방식을 POST로 합니다.
                httpURLConnection.connect(); //전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));//전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.
                outputStream.flush();
                outputStream.close();

                // 3. 응답을 읽습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) // 정상적인 응답 데이터
                {
                    inputStream = httpURLConnection.getInputStream();
                } else {  // 에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }

                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                String data = sb.toString().trim();

                return data;

            } catch (Exception e) {

                Log.d(TAG, "getPets: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }


    }


    private void showResult(String mJsonString) {


        String TAG_JSON = "bloodsugar_list"; //어레이 이름
        /*php 파일에서 넘긴 json 이름들*/
        String BLOODSUGAR = "bloodsugar";
        String FDATE = "fDate";

        ArrayList<Entry> cgm_value_list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            //JSON으로 푸시한 전체 데이터 가져오기
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i); //하나 가져오기

                String bloodsugar =  item.getString(BLOODSUGAR);
                String fDate =  item.getString(FDATE);

                float val = Integer.parseInt(bloodsugar);
                cgm_value_list.add(new Entry((jsonArray.length()*5 - i*5)*(-1)+5, val));

            }
        } catch (JSONException e) {
            //Log.d(TAG, "showResult : ", e);
        }

        LineDataSet set1;
        set1 = new LineDataSet(cgm_value_list, "혈당값");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);


        chart.notifyDataSetChanged();
        chart.invalidate();
        //adapter.getItemList(board_list);

    }
}
