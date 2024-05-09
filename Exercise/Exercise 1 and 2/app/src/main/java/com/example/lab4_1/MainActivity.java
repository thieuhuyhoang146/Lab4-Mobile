package com.example.lab4_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button readData;

    Button readData2;

    Button writeData2;

    EditText text;

    EditText text2;
    Boolean open=Boolean.FALSE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readData = (Button) findViewById(R.id.readData);
        text = (EditText) findViewById(R.id.text);

        readData2 = (Button) findViewById(R.id.readData2);
        readData2.setOnClickListener(this);
        text2 = (EditText) findViewById(R.id.text2);
        writeData2 = (Button) findViewById(R.id.writeData2);
        writeData2.setOnClickListener(this);



        readData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (open==Boolean.TRUE) {
                    text.setText("");
                    open=Boolean.FALSE;
                    return;
                }
                open = Boolean.TRUE;
                String data;
                InputStream inputStream = getResources().openRawResource(R.raw.lorem);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                if (inputStream!=null) {
                    try {
                        while((data=bufferedReader.readLine())!=null) {
                            builder.append(data);
                            builder.append("\n");
                        }
                        inputStream.close();
                        text.setText(builder.toString());
                    } catch (IOException err) {
                        Log.e("ERROR", err.getMessage());
                    }
                }
            }
        });


    }

    protected void setReadData() {
        try {
            FileInputStream fileInputStream = openFileInput("test.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String data;
            StringBuilder stringBuilder = new StringBuilder();
            while ((data=bufferedReader.readLine()) != null)
            {
                stringBuilder.append(data);
                stringBuilder.append("\n");
            }
            fileInputStream.close();
            text2.setText(stringBuilder.toString());
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        } catch (IOException err) {
            err.printStackTrace();
        }

    }

    protected void setWriteData() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("test.txt", 0);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(text2.getText().toString());
            outputStreamWriter.close();
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.readData2) {
            setReadData();
        }
        if (view.getId() == R.id.writeData2) {
            setWriteData();
        }
    }
}