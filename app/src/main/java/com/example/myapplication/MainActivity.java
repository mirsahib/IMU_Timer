package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private String currentDateandTime;
    private SimpleDateFormat sdf;
    private String timeString;
    private TextView timeView;
    private TextView lView;
    private EditText fileName;
    private Button startExBtn;
    private Button startMovBtn;
    private Button stopMovBtn;
    private Button stopExBtn;
    private Button chooseLabelBtn;
    private Button saveBtn;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.timeView);
        timeView.setMovementMethod(new ScrollingMovementMethod());
        fileName = findViewById(R.id.fileName);
        sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        timeString = "";
        startExBtn = findViewById(R.id.startExBtn);
        startMovBtn = findViewById(R.id.startMovBtn);
        stopMovBtn = findViewById(R.id.stopMovBtn);
        stopExBtn = findViewById(R.id.stopExBtn);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setEnabled(false);
        startExBtn.setEnabled(false);
        startMovBtn.setEnabled(false);
        stopMovBtn.setEnabled(false);
        stopExBtn.setEnabled(false);
        chooseLabelBtn = findViewById(R.id.choose_labelBtn);
        fileName.setEnabled(false);
        chooseLabelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        lView = findViewById(R.id.labelView);
        try{
            Bundle bundle = getIntent().getExtras();
            String message = bundle.getString("message");
            if (message!=null){
                lView.setText(message);
                startExBtn.setEnabled(true);
            }
            //Log.i("label",message);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }
    private void launchActivity() {
        startActivity (new Intent(this, Label.class));
    }

    public void startEx(View view) {
        //Log.i("Btn Msg:","Start Exercise");
        currentDateandTime = sdf.format(new Date());
        timeString +=currentDateandTime+",";
        Log.i("Start Ex:",timeString);
        timeView.setText(timeString);
        if(view.getId()== R.id.startExBtn){
            startExBtn.setEnabled(false);
        }
        if(!startMovBtn.isEnabled()){
            startMovBtn.setEnabled(true);
        }
    }
    public void startMov(View view) {
        //Log.i("Btn Msg:","Start Movement");
        currentDateandTime = sdf.format(new Date());
        timeString +=currentDateandTime+",\n";
        Log.i("Start Mov:",timeString);
        timeView.setText(timeString);
        if(view.getId()== R.id.startMovBtn){
            startMovBtn.setEnabled(false);
        }
        if(!stopMovBtn.isEnabled()){
            stopMovBtn.setEnabled(true);
        }
        if(!stopExBtn.isEnabled()){
            stopExBtn.setEnabled(true);
        }

    }

    public void stopMov(View view) {
        //Log.i("Btn Msg:","Stop Movement");
        currentDateandTime = sdf.format(new Date());
        timeString +=currentDateandTime+",";
        Log.i("Stop Mov:",timeString);
        timeView.setText(timeString);
        if(view.getId()== R.id.stopMovBtn){
            stopMovBtn.setEnabled(false);
        }
        if(!startMovBtn.isEnabled()){
            startMovBtn.setEnabled(true);
        }


    }

    public void stopEx(View view) {
        //Log.i("Btn Msg:","Stop Exercise");
        currentDateandTime = sdf.format(new Date());
        timeString +=currentDateandTime+",";
        Log.i("Stop Ex:",timeString);
        timeView.setText(timeString);
        if(view.getId()==R.id.stopExBtn){

            startMovBtn.setEnabled(false);
            stopMovBtn.setEnabled(false);
            stopExBtn.setEnabled(false);
            chooseLabelBtn.setEnabled(true);
            saveBtn.setEnabled(true);
            fileName.setEnabled(true);
        }


    }
    public boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.i("State","Yes it is writable");
            return true;
        }else{
            Log.i("State","No it is not writable");
            return false;
        }
    }

    public void saveToFile(View view) {
        String participantName = fileName.getText().toString();
        String label = lView.getText().toString();
        String fileNameStr = label.substring(0,1).toUpperCase()+label.substring(1)+"_"+participantName.substring(0,1).toUpperCase()+participantName.substring(1);

        if(fileNameStr.contains(".")){
            //write to sd card
            //Log.i("msg",fileNameStr);
            writeFile(fileNameStr);
        }else{
            // re edit file name
            fileNameStr+=".csv";
            //Log.i("msg",fileNameStr);
            writeFile(fileNameStr);
        }
    }
    public void writeFile(String fName){
        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            File myFile = new File(Environment.getExternalStorageDirectory(),fName);
            if(!myFile.exists()){
                try{
                    FileOutputStream fos = new FileOutputStream(myFile);
                    fos.write(timeString.getBytes());
                    fos.close();
                    //fos.write(myText);
                    Toast.makeText(this,fName+" Saved",Toast.LENGTH_SHORT).show();

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this,"File Name Exits",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"Cannot write to external storage",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this,permission);
        return(check== PackageManager.PERMISSION_GRANTED);
    }

    public void clearTextView(View view) {

        timeView.setText("");
        timeString = "";
        fileName.setText("");
        fileName.setEnabled(false);
        startExBtn.setEnabled(false);
        startMovBtn.setEnabled(false);
        stopMovBtn.setEnabled(false);
        stopExBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        lView.setText("");

    }
}

//test branch