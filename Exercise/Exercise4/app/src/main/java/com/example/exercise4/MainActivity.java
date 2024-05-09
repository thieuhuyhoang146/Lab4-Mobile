package com.example.exercise4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_DCIM_DIRECTORY = 123;

    Button btn_read, btn_write;
    EditText edt_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_read = findViewById(R.id.btnReadData);
        btn_write = findViewById(R.id.btnWriteData);
        edt_data = findViewById(R.id.editText);

        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your reading logic here
                // Ensure to use compatible APIs if accessing external storage
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(intent, REQUEST_CODE_DCIM_DIRECTORY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DCIM_DIRECTORY && resultCode == RESULT_OK && data != null) {
            Uri treeUri = data.getData();
            if (treeUri != null) {
                saveToFile(treeUri);
            }
        }
    }

    private void saveToFile(Uri treeUri) {
        ContentResolver resolver = getContentResolver();
        Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri,
                DocumentsContract.getTreeDocumentId(treeUri));
        Uri fileUri = null;
        try {
            fileUri = DocumentsContract.createDocument(resolver, documentUri, "text/plain", "childtext.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (OutputStream outputStream = resolver.openOutputStream(fileUri);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            writer.write(edt_data.getText().toString());
            writer.close();
            Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(getApplicationContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }
}
