package com.example.cameratest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 55;
    final int CAMERA_REQUEST_CODE = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tryCamera(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Camera Permission").setMessage("I need Camera permission to spy on you, please");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] permissions = new String[]{Manifest.permission.CAMERA};
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, CAMERA_REQUEST_CODE);

                    }
                });
                builder.setNegativeButton("cancel", null);
                builder.create().show();
            } else {

                String[] permissions = new String[]{Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(this, permissions, CAMERA_REQUEST_CODE);
            }

        }
        // check permission
        //if exists --> takephoto()
        // if not --> requestpermission()

        //takephoto() :
        // make intent for camera
        // startactivity --> get result
        // display result.

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You have granted permission for " + permissions[0], Toast.LENGTH_SHORT).show();
                takePhoto();
            } else {
                Toast.makeText(this, "The app will not function without  " + permissions[0], Toast.LENGTH_SHORT).show();

            }
        }


    }

    void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // make intent
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            //Show error message
            Toast.makeText(this, "You don't have a camera app !!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap image = (Bitmap) bundle.get("data");
                showImage(image);
            } else {
                Toast.makeText(this, "you have canceled the intent", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void showImage(Bitmap bitmap) {
        ImageView imageView = findViewById(R.id.imageview);
        imageView.setImageBitmap(bitmap);
    }

}
