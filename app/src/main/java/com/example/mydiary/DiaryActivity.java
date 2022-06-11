package com.example.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.Models.Diary;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;


import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000;
    EditText editText_title,editText_diary;
    ImageView imageView_save,pdf;
    Diary diary;

    boolean isOldNote = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        editText_diary = findViewById(R.id.editText_diary);
        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        diary = new Diary();
        pdf = findViewById(R.id.pdf);
        try {
            diary = (Diary) getIntent().getSerializableExtra("old_diary");
            editText_title.setText(diary.getTitle());
            editText_diary.setText(diary.getDiary());
            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText_title.getText().toString();
                String body = editText_diary.getText().toString();
                if(body.isEmpty()){
                    Toast.makeText(DiaryActivity.this, "You can not save empty message!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(title.isEmpty()){
                    Toast.makeText(DiaryActivity.this, "You can not save a message without title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();
                if(!isOldNote){
                    diary = new Diary();
                }
                diary.setTitle(title);
                diary.setDiary(body);
                diary.setDate(formatter.format(date));
                Toast.makeText(DiaryActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("diary", diary);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String body = editText_diary.getText().toString();
                if(body.isEmpty()){
                    Toast.makeText(DiaryActivity.this, "You can not save empty body as pdf!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,STORAGE_CODE);
                    }
                    else{
                        pdfKaydet();
                    }
                }else{
                    pdfKaydet();
                }
            }

        });
    }
    private void pdfKaydet() {
        com.itextpdf.text.pdf.PdfDocument doc = new PdfDocument();
        String title = editText_title.getText().toString();
        String filename = new String(title);
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+ filename+".pdf";
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filepath));
            doc.open();
            String text = editText_diary.getText().toString();
            doc.add(new Paragraph("text"));
            doc.close();
            Toast.makeText(DiaryActivity.this, "success", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    pdfKaydet();
                }
                else{
                    Toast.makeText(DiaryActivity.this, "Storage Access Permission is Needed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

