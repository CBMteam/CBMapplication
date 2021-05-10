package com.cbm.cbmapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    public void dialogJoinDuplicateId(Context context){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setMessage("이미 있는 아이디입니다.\n다른 아이디를 사용해주세요.")
                .setTitle("아이디 중복")
                .setNeutralButton("확인", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

    public void friendRegisterComplete(final Context context){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setMessage("정상적으로 친구 등록이 되었습니다.")
                .setTitle("등록 완료")
                .setNeutralButton("확인", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

    public void findFriendEmailError(Context context){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setMessage("해당 이메일은 존재하지 않습니다.")
                .setTitle("이메일 오류")
                .setNeutralButton("확인", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }


    public void duplicateFriendError(Context context){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setMessage("이미 등록된 친구입니다.")
                .setTitle("등록 오류")
                .setNeutralButton("확인", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

    public void SomethingError(Context context){
        AlertDialog.Builder oDialog = new AlertDialog.Builder(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setMessage("알 수 없는 오류가 발생했습니다.")
                .setTitle("오류")
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
