package com.cbm.cbmapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class else_record extends AppCompatActivity {

    EditText addedit;//내용 입력
    TimePicker tPicker;//타임 피커
    Button btnend;
    TextView caltitle;
    String choice,kind;
    ImageButton btn_record_goback;
    int selectYear,selectMonth,selectDay,selectHour,selectmin;
    String user_email;

    //DB
    String IP_ADDRESS="223.194.46.209";//안드에서는 localhost 쓰려면 127.0.0.1 이 아니고 10.0.2.2

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(else_record.this);
        builder.setMessage("이전 페이지로 이동하시겠습니까?");
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                else_record.super.onBackPressed();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.else_record);

        addedit=findViewById(R.id.addedit);//내용 입력
        tPicker=findViewById(R.id.timePicker1);//타임 피커
        btnend=findViewById(R.id.btnend);
        caltitle=findViewById(R.id.caltitle);

        user_email = PreferenceManager.getString(getApplicationContext(), "user_email");

        Intent intent1=getIntent();

        choice=intent1.getStringExtra("Radio");
        selectYear=intent1.getIntExtra("Year",-1);
        selectMonth=intent1.getIntExtra("Month",-1);
        selectDay=intent1.getIntExtra("Day",-1);

        if(selectYear==-1||selectMonth==-1||selectDay==-1)
        {Toast.makeText(getApplicationContext(),"값을 받아오던 중 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();}

        if(choice.equals("인슐린")){
            caltitle.setText("인슐린 값을 입력하세요.");
            addedit.setHint("(인슐린 단위)");
            kind="insulin";
        }
        else if(choice.equals("식사량")){
            caltitle.setText("식사량을 입력하세요");
            addedit.setHint("(식사량 단위)");
            kind="food";
        }
        else{
            caltitle.setText("일정을 입력하세요.");
            kind="else";
        }

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent();

                //시간 설정
                selectHour=tPicker.getHour();
                selectmin=tPicker.getMinute();
                String date=Integer.toString(selectYear)+"-"+Integer.toString(selectMonth)+"-"
                        +Integer.toString(selectDay)+" "+Integer.toString(selectHour)+":"+Integer.toString(selectmin)+":"+"00";

                //일정 내용 받아오기
                String carb=addedit.getText().toString();
                String email="guswl9_9@naver.com";

                else_record.ElseTask task=new else_record.ElseTask(else_record.this);

               // if(choice.equals("인슐린")){
                task.execute("http://"+IP_ADDRESS+"/carbinsert.php",email,carb,date);
               // }
               // else if(choice.equals("식사량")){

               // }
               // else{
                //    caltitle.setText("일정을 입력하세요.");
                 //   kind="else";
               // }

                //일정 추가 완료시 원래 화면으로
                setResult(RESULT_OK,intent2);
                finish();

            }
        });

        btn_record_goback = (ImageButton) findViewById(R.id.btn_record_goback);

        btn_record_goback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent);
            }
        });

    }

    public class ElseTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "carbinsertTask";

        public Context mContext;

        public ElseTask(Context mContext) {
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

            Log.d(TAG, "POST response(2)  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // TODO : 아래 형식처럼 원하는 key과 value를 계속 추가시킬수있다.
            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String user_email = (String) params[1];
            String user_carb = (String) params[2];
            String user_date = (String) params[3];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "email=" + user_email + "&carb=" + user_carb + "&date=" + user_date;

            Log.d(TAG, postParameters);

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다.

                outputStream.flush();
                outputStream.close();


                // 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code(1) - " + responseStatusCode);

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
