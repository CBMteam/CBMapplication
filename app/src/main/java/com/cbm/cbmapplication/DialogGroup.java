package com.cbm.cbmapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class DialogGroup {

    public void dialogWrongLogin(Context context){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setMessage("아이디/비밀번호를 확인해주세요.")
                .setTitle("로그인 오류")
                .setNeutralButton("확인", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

    public void dialogNotCompleteForm(Context context){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setMessage("항목을 모두 채워주세요.")
                .setTitle("작성 오류")
                .setNeutralButton("확인", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

}
