package com.samansepahvand.qrcodetemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.samansepahvand.qrcodetemplate.scanner.Capture;

import pub.devrel.easypermissions.AppSettingsDialog;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {




    public static final String  TAG= "MainActivity";


    public static final int PERMISSION_REQUEST_CODE=10;
    private Button btnScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initView();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
    private void initView() {
        btnScan = findViewById(R.id.btnScan);

        btnScan.setOnClickListener(view -> {

            startInstalledAppDetailsActivity(this);

//
//            if (!checkPermission()) {
//                requestPermission();
//            }else {
//
//                Log.e(TAG, "initView: " + "hiiiii");
//                IntentIntegrator intentIntegrator = new IntentIntegrator(
//
//                        MainActivity.this
//                );
//
//                //set flash light
//                intentIntegrator.setPrompt("for flash use volume up");
//                //set beep
//                intentIntegrator.setBeepEnabled(true);
//                //locked orientation
//                intentIntegrator.setOrientationLocked(true);
//                //set capture activity
//                intentIntegrator.setCaptureActivity(Capture.class);
//                //init scan
//                intentIntegrator.initiateScan();
//
//            }
     });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //init  intent result
        IntentResult intentResult=IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        //check condition
        if (intentResult.getContents()!=null){
            //when result content is  not null
            //init alert dialog
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents());

            Log.e(TAG, "onActivityResult: IRC url"+intentResult.getContents() );
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

        }else{
            //when content result is null
            //display toast 
            Toast.makeText(this, "OOPS! .....You did not Scan anything .!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);



        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted) {

                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {

                                showMessageOKCancel("دسترسی تایید نشد! شما اجازه استفاده از سایر قسمت های سیستم را نداری  ",

                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/iran_sans.ttf");
        TextView content = new TextView(this);
        content.setText("برای استفاده از سایر امکانات  برنامه به دسترسی ها نیاز داریم!");
        content.setTypeface(face);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(this, R.style.alert));
        alertDialogBuilder.setTitle("درخواست دسترسی");
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setView(content);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                requestPermission();
            }
        });
        alertDialogBuilder.setNeutralButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void showMessage(String body, String type) {
        if (type.equals("s")) {
        } else if (type.equals("t")) {
            Toast.makeText(this, "" + body, Toast.LENGTH_SHORT).show();

        }
    }



}