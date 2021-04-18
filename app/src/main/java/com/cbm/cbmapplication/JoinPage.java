package com.cbm.cbmapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class JoinPage  extends AppCompatActivity {

    Button btn_back;
    Button btn_join;

    private EditText et_email;
    private EditText et_passwd;
    private DatePicker et_birth;
    private EditText et_weight;
    private String IP_ADDRESS = "223.194.46.209";

    DialogGroup dialogGroup = new DialogGroup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        btn_back = findViewById(R.id.Cancel);
        btn_join = findViewById(R.id.joinBtn2);
        et_email = findViewById(R.id.emailEt2);
        et_birth = findViewById(R.id.birth);
        et_passwd = findViewById(R.id.pwdEt2);
        et_weight = findViewById(R.id.weight);

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginJoinSelectPage.class);
                startActivity(intent);
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString();
                String birth = et_birth.getYear() + "-" + (et_birth.getMonth()+1) + "-" + et_birth.getDayOfMonth();
                String passwd = et_passwd.getText().toString();
                String weight = et_weight.getText().toString();

                //fcm 기기의 토큰값과 이메일 값 전달
                String token= FirebaseInstanceId.getInstance().getToken();
                Log.d("fcmtoken",token);

                if (email.length() == 0 || birth.length() == 0 || passwd.length() == 0 || weight.length() == 0){
                    dialogGroup.dialogNotCompleteForm(JoinPage.this);
                    return;
                }

                JoinPage.JoinTask task = new JoinPage.JoinTask(JoinPage.this);

                try {
                    String result = task.execute("http://" + IP_ADDRESS + "/join.php", email,passwd, birth, weight, token).get();

                    if (result.equals("failure")){
                        dialogGroup.dialogJoinDuplicateId(JoinPage.this);
                        return;
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("Token", token)
                        .add("email",email)
                        .build();

                //request
                Request request = new Request.Builder()
                        .url("http://" + IP_ADDRESS + "/login.php")
                        .post(body)
                        .build();

                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                Intent intent = new Intent(getApplicationContext(), LoginJoinSelectPage.class);
                startActivity(intent);
            }

        });

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

            // TODO : 아래 형식처럼 원하는 key과 value를 계속 추가시킬수있다.
            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String user_email = (String) params[1];
            String user_pw = (String) params[2];
            String user_birth = (String) params[3];
            String user_weight= (String) params[4];
            String user_token=(String) params[5];


            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "email=" + user_email + "&passwd=" + user_pw + "&birth="+ user_birth + "&weight=" + user_weight +"&token=" + user_token ;

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
