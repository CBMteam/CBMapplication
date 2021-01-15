package com.cbm.cbmapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar extends AppCompatActivity {

    CalendarView calendarView;
    int selectYear,selectMonth,selectDay;
    Button btnadd,btndel;
    TextView calcontent;
    String send;
    String content;
    private int REQUEST=1;

    //값 잘 들어갔나 확인용(DB 쓰면 지우세용)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST) {
            if (resultCode == RESULT_OK) {
                content=data.getStringExtra("content");
                Toast.makeText(getApplicationContext(),"일정이 등록되었습니다.",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"오류",Toast.LENGTH_LONG).show();
            }
        }
    }
    //여긱까지~

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        btnadd=findViewById(R.id.btnadd);//일정 추가 버튼
        btndel=findViewById(R.id.btndel); //일정 삭제 버튼
        calendarView=findViewById(R.id.calendarView);//캘린더
        calcontent=findViewById(R.id.calcontent);//일정 출력

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

                if(content!=null)
                {calcontent.setText(content);}

            }
        });

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



    }

}
