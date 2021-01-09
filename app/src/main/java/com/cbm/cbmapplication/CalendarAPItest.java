package com.cbm.cbmapplication;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class CalendarAPItest extends AppCompatActivity {

    CalendarView calendarView;
    TimePicker tPicker;
    int selectYear,selectMonth,selectDay,selectHour,selectmin;
    EditText addedit;
    String contetnt;
    LinearLayout addView,basicview,addbtnview,basicbtnview;
    Button btnend,btnadd,btndel;
    TextView calcontent;

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

        //화면 시작시 일정 추가 관련 항목이 보이지 않도록 설정
        addView.setVisibility(View.INVISIBLE);
        addbtnview.setVisibility(View.INVISIBLE);

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

                //일정 추가 화면 사용시 원래 화면과 버튼이 보이지 않도록 설정
                addView.setVisibility(View.VISIBLE);
                addbtnview.setVisibility(View.VISIBLE);
                basicview.setVisibility(View.INVISIBLE);
                basicbtnview.setVisibility(View.INVISIBLE);

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
                addView.setVisibility(View.INVISIBLE);
                addbtnview.setVisibility(View.INVISIBLE);
                basicview.setVisibility(View.VISIBLE);
                basicbtnview.setVisibility(View.VISIBLE);

            }
        });

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //디비에 있는 일정 내용 삭제 추가 필요
            }
        });
    }

}
