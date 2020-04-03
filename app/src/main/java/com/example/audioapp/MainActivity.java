package com.example.audioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static Button mRecordButton;
    private static Button mStopRecordButton;
    private static Button mPlayButton;
    private static Button mStopPlayButton;
    private static TextView mTextView;

    private static MediaRecorder mRecorder;
    private static MediaPlayer mPlayer;
    private static String mAudioFileName;

    private static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecordButton = (Button) findViewById(R.id.btnRecord);
        mStopRecordButton = (Button) findViewById(R.id.btnStopRecord);
        mPlayButton = (Button) findViewById(R.id.btnPlay);
        mStopPlayButton = (Button) findViewById(R.id.btnStopPlay);
        mTextView = (TextView) findViewById(R.id.debugTextView);


        PackageManager myManager = this.getPackageManager();
        if(myManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
            mRecordButton.setEnabled(true);
        else
            mRecordButton.setEnabled(false);

        mStopRecordButton.setEnabled(false);
        mPlayButton.setEnabled(false);
        mStopPlayButton.setEnabled(false);
        mAudioFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/myAudio.3gp";


        mTextView.setText("file: " + mAudioFileName +
                "\nstate: "+ Environment.getExternalStorageState());


        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

// If we don't have permissions, ask user for permissions
        if (permission != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS_STORAGE = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            };
            int REQUEST_EXTERNAL_STORAGE = 1;


            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );
        }

//        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED) {
//            int YOUR_REQUEST_CODE = 200;
//            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, YOUR_REQUEST_CODE} );
//
//        }



    }

    public void onRecordClick(View v) throws IOException{
        mRecordButton.setEnabled(false);
        mStopRecordButton.setEnabled(true);

        try {
            mRecorder = new MediaRecorder();
//            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            mRecorder.setOutputFile(mAudioFileName);
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mRecorder.setOutputFile(mAudioFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.prepare();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        mRecorder.start();
    }

    public void onStopRecordClick(View v)
    {
        mStopRecordButton.setEnabled(false);
        mPlayButton.setEnabled(true);

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

    }

    public void onPlayClick(View v)
    {
        mPlayButton.setEnabled(false);
        mStopPlayButton.setEnabled(true);

        try{
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mAudioFileName);
            mPlayer.prepare();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        mPlayer.start();
    }

    public void onStopPlayClick(View v){
        mStopPlayButton.setEnabled(false);
        mRecordButton.setEnabled(true);
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }
}
