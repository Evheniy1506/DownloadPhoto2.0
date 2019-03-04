package com.adr.starnet.downloadphoto;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    ImageView imageView;

    final static String PHOTO_KEY = "PHOTO_KEY";
    String photoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);

        findViewById(R.id.b_find).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                System.out.println("1111111");

                Callback callback = new Callback() {
                    @Override
                    public void onSuccess() {
                        Time time = new Time();
                        time.setToNow();
                        File fn;
                        photoPath = "/storage/emulated/0/"
                                + Integer.toString(time.year)
                                + Integer.toString(time.month)
                                + Integer.toString(time.monthDay)
                                + Integer.toString(time.hour)
                                + Integer.toString(time.minute)
                                + Integer.toString(time.second)
                                + ".jpg";
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        try {  // Try to Save #1
                            fn = new File(photoPath);
                            fn.createNewFile();
                            FileOutputStream out = new FileOutputStream(fn);
                            Toast.makeText(getApplicationContext(), "In Save",
                                    Toast.LENGTH_LONG).show();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                            out.flush();
                            out.close();

                            Toast.makeText(getApplicationContext(),
                                    "File is Saved in  " + fn, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getApplicationContext(), "ERROR",
                                Toast.LENGTH_SHORT).show();
                    }
                };

                Picasso.with(getApplicationContext())
                        .load(editText.getText().toString())
                        .into(imageView,callback);

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PHOTO_KEY, photoPath);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        photoPath = savedInstanceState.getString(PHOTO_KEY);
        System.out.println(photoPath);
        File imagePath = new File(photoPath);//путь к изображению
        Picasso.with(getApplicationContext()) //передаем контекст приложения
                .load(imagePath)
                .into(imageView);

    }
}
