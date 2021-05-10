package com.cbm.cbmapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Calendar extends AppCompatActivity {

    CalendarView calendarView;
    int selectYear,selectMonth,selectDay;
    Button btnadd,btndel;
    private ListView callist;
    private ListViewAdpater adapter;
    String send;
    String content;
    private int REQUEST=1;
    ImageButton btn_cal_goback;
    String user_email;

    private String IP_ADDRESS="223.194.46.209";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        //Adapter 생성
        adapter=new ListViewAdpater();


        user_email = PreferenceManager.getString(getApplicationContext(), "user_email");

        btnadd=findViewById(R.id.btnadd);//일정 추가 버튼
        btndel=findViewById(R.id.btndel); //일정 삭제 버튼
        calendarView=findViewById(R.id.calendarView);//캘린더

        //리스트뷰 참조 및 Adapter 달기
        callist=(ListView)findViewById(R.id.callist);
        callist.setAdapter(adapter);

        //달력을 클릭하지 않을 경우 오늘 날짜를 가져옴
        long now=System.currentTimeMillis();
        Date CurDate=new Date(now);
        SimpleDateFormat Year=new SimpleDateFormat("yyyy");
        SimpleDateFormat Month=new SimpleDateFormat("MM");
        SimpleDateFormat Day=new SimpleDateFormat("dd");

        selectYear=Integer.parseInt(Year.format(CurDate));
        selectMonth=Integer.parseInt(Month.format(CurDate));
        selectDay=Integer.parseInt(Day.format(CurDate));

        //날짜 선택할 경우
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                selectYear=year;
                selectMonth=month+1;
                selectDay=dayOfMonth;

            }
        });

        String month=Integer.toString(selectMonth);
        String day=Integer.toString(selectDay);
        if(Integer.toString(selectMonth).length()==1){
            month="0"+selectMonth;
        }
        if(Integer.toString(selectDay).length()==1){
            day="0"+selectDay;
        }
        String date=selectYear+"-"+month+"-"+day;
        Log.d("datecheck",date);

        String email="guswl9_9@naver.com";

        getdata task=new getdata(Calendar.this);
        task.execute("http://"+IP_ADDRESS+"/getcarb.php",email,date);

        btnadd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(Calendar.this);
                dlg.setTitle("항목 선택"); //제목
                final String[] versionArray = new String[] {"인슐린","식사량","기타"};
                send=versionArray[2];

                dlg.setSingleChoiceItems(versionArray, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        send=versionArray[which];
                    }
                });
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent intent=new Intent(getApplicationContext(),else_record.class);
                        intent.putExtra("Radio",send);
                        intent.putExtra("Year",selectYear);
                        intent.putExtra("Month",selectMonth);
                        intent.putExtra("Day",selectDay);
                       startActivityForResult(intent,REQUEST);
                    }
                });
                dlg.show();
            }
        });

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //디비에 있는 일정 내용 삭제 추가 필요
                AlertDialog.Builder dlg2 = new AlertDialog.Builder(Calendar.this);
                dlg2.setTitle("항목 삭제")
                        .setMessage("항목을 지우시겠습니까?")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //일정 삭제 구현
                                Toast.makeText(getApplicationContext(),"일정이 삭제되었습니다",Toast.LENGTH_SHORT).show();
                            }
                        });

                dlg2.show();
            }
        });


        btn_cal_goback = (ImageButton) findViewById(R.id.btn_cal_goback);

        btn_cal_goback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuChoice.class);
                startActivity(intent);
            }
        });


    }

    public class getdata extends AsyncTask<String, Void, String> {

        private static final String TAG = "getdata";

        public Context mContext;

        public getdata(Context mContext) {
            super();

            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // TODO : 아래 형식처럼 원하는 key과 value를 계속 추가시킬수있다.
            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String user_email = (String) params[1];
            String user_date = (String) params[2];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "email=" + user_email + "&date=" + user_date;

            Log.d(TAG, postParameters);

            StringBuilder jsonHtml = new StringBuilder();

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "InsertData: Error ", e);
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("results");
                String zz = "";
                zz += "Status : " + jObject.get("status");
                zz += "\n";
                zz += "Number of results : " + jObject.get("num_result");
                zz += "\n";
                zz += "Results : \n";

                String calcontent;

                for ( int i = 0; i < results.length(); ++i ) {
                    JSONObject temp = results.getJSONObject(i);
                    calcontent = temp.get("carb").toString();
                    adapter.addItem("식사량",calcontent);
                    zz += "\temail : " + temp.get("email");
                    zz += "\tdate : " + temp.get("date");
                }
                Log.d(TAG,zz);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}

