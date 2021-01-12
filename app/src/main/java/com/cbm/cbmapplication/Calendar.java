package com.cbm.cbmapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Calendar extends AppCompatActivity {

    CalendarView calendarView;
    TimePicker tPicker;
    int selectYear,selectMonth,selectDay,selectHour,selectmin;
    EditText addedit;
    String contetnt;
    LinearLayout addView,basicview,addbtnview,basicbtnview;
    Button btnend,btnadd,btndel,btnback;
    TextView calcontent,caltitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        btnadd=findViewById(R.id.btnadd);//일정 추가 버튼
        btndel=findViewById(R.id.btndel); //일정 삭제 버튼
        btnend=findViewById(R.id.btnend); //일정 추가 완료 버튼
        calendarView=findViewById(R.id.calendarView);//캘린더
        addView=findViewById(R.id.addview);//일정 추가시 타임 피커를 보이도록
        basicview=findViewById(R.id.basicview);//원래 화면
        addbtnview=findViewById(R.id.addbtnview);//일정 추가시 버튼 화면
        basicbtnview=findViewById(R.id.basicbtnview);//원래 버튼 화면
        tPicker=findViewById(R.id.timePicker1);//타임 피커
        addedit=findViewById(R.id.addedit);//내용 입력
        calcontent=findViewById(R.id.calcontent);//일정 출력
        btnback=findViewById(R.id.btnback);
        caltitle=findViewById(R.id.caltitle);


        //날짜 선택
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                selectYear=year;
                selectMonth=month+1;
                selectDay=dayOfMonth;

            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(Calendar.this);
                dlg.setTitle("항목 선택"); //제목
                final String[] versionArray = new String[] {"인슐린","식사량","기타"};

                dlg.setSingleChoiceItems(versionArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(versionArray[which]=="인슐린")
                        {
                            caltitle.setText("인슐린을 투여하셨나요?");
                            addedit.setHint("(인슐린 투여량 단위)");
                        }
                        else if(versionArray[which]=="식사량")
                        {
                            caltitle.setText("식사를 하셨나요?");
                            addedit.setHint("탄수화물량(g)");
                        }
                    }
                });
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //토스트 메시지
                        Toast.makeText(Calendar.this,"확인을 눌르셨습니다.",Toast.LENGTH_SHORT).show();
                        setContentView(R.layout.else_record);
                    }
                });
                dlg.show();

                //시간 설정
                selectHour=tPicker.getHour();
                selectmin=tPicker.getMinute();

                //일정 내용 받아오기
                contetnt=addedit.getText().toString();

            }
        });

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //디비에 일정 넣는 부분 추가 필요

                //사용자가 날짜를 선택하지 않은 경우는 현재 이기때문에 오늘날짜로 설정해줌 안그러면 년/일/월에 0들어감(추후수정)

                //내용 잘 들어가는지 확인용 나중엔 디비에서 받아오는 것으로 변경
                calcontent.setText(Integer.toString(selectYear)+"년"+Integer.toString(selectMonth)
                                +"월"+Integer.toString(selectDay)+"일"+Integer.toString(selectHour)
                                +"시"+Integer.toString(selectmin)+"분"+"  "+contetnt);

                //일정 추가 완료시 원래 화면으로
                setContentView(R.layout.calendar);

            }
        });

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //디비에 있는 일정 내용 삭제 추가 필요
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setContentView(R.layout.calendar);
            }
        });
    }

}
