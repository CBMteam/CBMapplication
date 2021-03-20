package com.cbm.cbmapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Model extends AppCompatActivity {

    TextView result;
    private String IP_ADDRESS = "223.194.46.209";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model);

        result = (TextView)findViewById(R.id.machine_learning_result);

        

        //db에서 받아온 시퀀스 전처리
        //float[][] input = new float[311][4];
        //float[][] output = new float[311][1];

        float[][][] input = new float[1][311][4];
        float[][][] output = new float[1][311][1];

        System.out.println(Model.this);

        //모델 불러오기
        Interpreter tflite = getTfliteInterpreter("model.tflite");
        if (tflite == null) System.out.println("모델 못 가져옴");

        //input shape : [(none, 311, 4)]
        //output shape : [(none, 311, 1)]
        tflite.run(input, output);

        System.out.println(output);

        result.setText(output.toString());
    }

    //모델명을 입력 받아서 메모리로 가져옴
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    //모델 파일 이름 입력하면 loadModelFile로 로딩하고 Interpreter로 반환
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(Model.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
