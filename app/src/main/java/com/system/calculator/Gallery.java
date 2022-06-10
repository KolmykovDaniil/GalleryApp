package com.system.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import android.net.Uri;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Gallery extends AppCompatActivity {

    private ArrayList<File> imageFiles;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter adapter;
    String dataPath = "/sdcard/Android/data/com.android.system";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //загрузка изображений из директории
        File myImgDir = new File(dataPath);
        File[] files = myImgDir.listFiles();
        imageFiles = new ArrayList<>(Arrays.asList(files));
        //подготовка элемента RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //создание LayoutManager типа Grid для отображения изображений,
        //в данном случае отображение происходит в 2 столбца
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        //подключение адаптера для обработки информации
        adapter = new MyAdapter(getApplicationContext(), imageFiles);
        recyclerView.setAdapter(adapter);
    }

    public static final int PICK_IMAGE_TO_ADD = 1;
    public static final int PICK_IMAGE_TO_REMOVE = 2;

    public void chooseFileToAdd(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_TO_ADD);
    }

    public void chooseFileToRemove(View view) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_TO_REMOVE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_TO_ADD) {
            Uri currentUri = data.getData();
            //конвертируем URI в путь к файлу
            String filePath= null;
            try {
                filePath = PathUtil.getPath(getApplicationContext(),currentUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            //создаем новый файл из пути
            File addedFile = new File(filePath);
            //Перемещаем наш выбранный файл
            Log.i("NEW PATH", addedFile.getParent());
            moveFile(addedFile.getParent(), addedFile.getName(), dataPath);
            addedFile = new File(dataPath + "/" + addedFile.getName());
            //переименовываем в рандом
            File newName = new File(dataPath + "/" + generateRandomStr(10));
            addedFile.renameTo(newName);
            //обновляем путь до изображения
            addedFile = new File (dataPath + "/" + newName.getName());
            //добавляем файл в список
            imageFiles.add(addedFile);
            //обновляем внешний вид
            adapter.notifyDataSetChanged();
        }
        else if (requestCode == PICK_IMAGE_TO_REMOVE){
            Uri currentUri = data.getData();
            //конвертируем URI в путь к файлу
            String filePath= null;
            try {
                filePath = PathUtil.getPath(getApplicationContext(),currentUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Log.i("DELETED", filePath);
            File toRemove = new File (filePath);
            //удаляем файл
            toRemove.delete();
            //обновляем активити полностью
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + "/" + inputFile);
            out = new FileOutputStream(outputPath + "/" + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath, inputFile).delete();
        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    public String generateRandomStr(int len) {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = len;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
}