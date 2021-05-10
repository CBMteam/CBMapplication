package com.cbm.cbmapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

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
import java.util.concurrent.ExecutionException;

public class  FriendRegisterPage extends AppCompatActivity {


    private ImageButton btn_searchid;
    private EditText et_searchfriend;
    private String IP_ADDRESS = "223.194.46.209";

    DialogGroup dialogGroup = new DialogGroup();
    ImageButton btn_friend_goback;
    private ArrayAdapter adapter;
    private ListView lv_friendlist;

    FriendCustomAdapter fcAdapter;

    ArrayList<String> friend_list = new ArrayList<>();
    ArrayList<Integer> friendicon_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friendregister);
        lv_friendlist=(ListView)findViewById(R.id.lv_friendlist);

        btn_friend_goback = (ImageButton) findViewById(R.id.btn_friend_goback);

        btn_searchid = (ImageButton) findViewById(R.id.btn_searchid);
        et_searchfriend = (EditText) findViewById(R.id.et_searchfriend);

        FriendRegisterPage.getFriendListTask getFriendTask = new FriendRegisterPage.getFriendListTask(FriendRegisterPage.this);

        try {
            String getFriendResult = getFriendTask.execute("http://" + IP_ADDRESS + "/getfriendlist.php", "1").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btn_friend_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuChoice.class);
                startActivity(intent);
            }
        });

        btn_searchid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchfriend_email = et_searchfriend.getText().toString();


                if (searchfriend_email.length()==0){
                    dialogGroup.dialogNotCompleteForm(FriendRegisterPage.this);
                    return;
                }

                FriendRegisterPage.CheckFriendTask task = new FriendRegisterPage.CheckFriendTask(FriendRegisterPage.this);
                try {

                    String result = task.execute("http://" + IP_ADDRESS + "/checkuser.php", "1", searchfriend_email).get();

                    if (result.equals("success")){
                        dialogGroup.friendRegisterComplete(FriendRegisterPage.this);
                    }
                    else if (result.equals("noSuchEmail")){
                        dialogGroup.findFriendEmailError(FriendRegisterPage.this);
                    }
                    else if (result.equals("duplicateEmail")){
                        dialogGroup.duplicateFriendError(FriendRegisterPage.this);
                    }
                    else{
                        dialogGroup.SomethingError(FriendRegisterPage.this);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });
//
//        btn_join.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), JoinPage.class);
//                startActivity(intent);
//            }
//        });

    }



    public class CheckFriendTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "InsertTask";

        public Context mContext;

        public CheckFriendTask(Context mContext) {
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
            String friend_email = (String) params[2];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "user_email=" + user_email + "&friend_email=" + friend_email;


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



    public class getFriendListTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "getFriendListTask";

        public Context mContext;

        public getFriendListTask(Context mContext) {
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

//        if (friend_list.isEmpty() == false) {
//            friend_list.clear();
//            adapter.boards.clear();
//        }


        String TAG_JSON = "friend_list"; //어레이 이름
        /*php 파일에서 넘긴 json 이름들*/
        String TAG_FRIEND_EMAIL = "friend_email";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            //JSON으로 푸시한 전체 데이터 가져오기
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i); //하나 가져오기

                String friend_email = item.getString(TAG_FRIEND_EMAIL);

                friend_list.add(friend_email);
                friendicon_list.add(R.drawable.ic_friendeach);
                Log.e("TAG", "showResult: "+friend_email);



                //Log.e("abc", "showResult: "+friend_email);


            }
        } catch (JSONException e) {
            //Log.d(TAG, "showResult : ", e);
        }

        String[] friend_list_convert = friend_list.toArray(new String[0]);
        Integer[] friendicon_list_convert = friendicon_list.toArray(new Integer[0]);

        fcAdapter = new FriendCustomAdapter(getApplicationContext(), friend_list_convert, friendicon_list_convert);
        lv_friendlist.setAdapter(fcAdapter);
        //adapter.getItemList(board_list);

    }

}