package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.AdapterView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Label extends AppCompatActivity {
    private ArrayList<String> listItem = new ArrayList<String>();
    private ArrayList<String> labelItem;
    private ArrayAdapter<String> adapter;
    private EditText labelName;
    private Button LabelBtn;
    private RadioGroup radio_group;
    private ListView lv;
    private Button sBtn;
    private String selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItem);
        lv = findViewById(R.id.lv);
        labelName = findViewById(R.id.label_name);
        LabelBtn = findViewById(R.id.addLabelBtn);
        lv.setAdapter(adapter);
        labelName.setEnabled(false);
        LabelBtn.setEnabled(false);
        sBtn = findViewById(R.id.selectBtn);
        radio_group = findViewById(R.id.radioGroup);
        radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                int selectedId = radio_group.getCheckedRadioButtonId();
                if(selectedId==R.id.radioButton4){
                    labelName.setEnabled(true);
                    LabelBtn.setEnabled(true);
                }else {
                    labelName.setEnabled(false);
                    LabelBtn.setEnabled(false);
                }
            }
        });
        readFile();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedItem = labelItem.get(position);
                Toast.makeText(Label.this,selectedItem+" label is selected",Toast.LENGTH_SHORT).show();            }
        });
    }

    public void addItems(View view) {
        String label_name = labelName.getText().toString();
        listItem.add(label_name);
        adapter.notifyDataSetChanged();
        writeToFile(label_name);
        labelName.setText("");
    }
    public void writeToFile(String labelName){
        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            File myFile = new File(Environment.getExternalStorageDirectory(),"list.txt");
            FileWriter writer;
            if(!myFile.exists()){
                try{
                    writer = new FileWriter(myFile);
                    writer.write(labelName);
                    writer.close();
                    //fos.write(myText);
                    Toast.makeText(this,"File Saved",Toast.LENGTH_SHORT).show();

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //if file already exist
                try{
                    writer = new FileWriter(myFile);
                    labelItem.add(labelName);
                    for(int i=0;i<labelItem.size();i++){
                        writer.write(labelItem.get(i)+"\n");
                    };
                    writer.close();
//                    //fos.write(myText);
                    Toast.makeText(this,"File Updated",Toast.LENGTH_SHORT).show();

                }catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }else {
            Toast.makeText(this,"Cannot write to external storage",Toast.LENGTH_SHORT).show();
        }
    }


    public void readFile(){

        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (!dir.exists())
            {
                if(!dir.mkdirs()){
                    Log.e("TAG","could not create the directories");
                }
            }
            final File myFile = new File(dir, "list" + ".txt");

            FileInputStream fis = new FileInputStream(myFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            labelItem = new ArrayList<String>();

            String strLine;
            while ((strLine = br.readLine()) != null) {
                listItem.add(strLine);
                adapter.notifyDataSetChanged();
                labelItem.add(strLine);
            }
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
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
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this,permission);
        return(check== PackageManager.PERMISSION_GRANTED);
    }

    public void selectBtnAction(View view){
        if(selectedItem!=null){
            Intent intent = new Intent(Label.this, MainActivity.class);
            intent.putExtra("message", selectedItem);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Please select a label",Toast.LENGTH_SHORT).show();
        }
    }

}
//label branch