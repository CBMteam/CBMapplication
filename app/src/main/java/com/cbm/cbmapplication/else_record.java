package com.cbm.cbmapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class else_record extends AppCompatActivity {

    EditText addedit;//내용 입력
    TimePicker tPicker;//타임 피커
    Button btnend;
    TextView caltitle;
    String contetnt,choice,kind;
    int selectYear,selectMonth,selectDay,selectHour,selectmin;

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

                //일정 내용 받아오기
                contetnt="내용: "+addedit.getText().toString()+"종류: "+kind+"날짜: "
                        +Integer.toString(selectYear)+"/"+Integer.toString(selectMonth)+"/"+Integer.toString(selectDay)
                +" "+Integer.toString(selectHour)+":"+Integer.toString(selectmin);

                intent2.putExtra("content",contetnt);

                //디비에 일정 넣는 부분 추가 필요(Calendar.java로 값 넘기지 말고 여기서 db에 저장, Calendar에서 textView에 출력하는건 DB 이용하기)

                //사용자가 날짜를 선택하지 않은 경우는 현재 이기때문에 오늘날짜로 설정해줌 안그러면 년/일/월에 0들어감(추후수정)

                //일정 추가 완료시 원래 화면으로
                setResult(RESULT_OK,intent2);
                finish();

            }
        });




    }
}
