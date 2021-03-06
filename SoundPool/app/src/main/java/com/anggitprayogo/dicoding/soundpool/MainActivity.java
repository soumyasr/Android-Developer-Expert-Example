package com.anggitprayogo.dicoding.soundpool;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSound, btnMediaPlayer, btnMediaPlayerStop;
    private int soundId, streamId;
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private boolean isSoundLoaded = false;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSound = findViewById(R.id.btn_soundpool);
        btnMediaPlayer = findViewById(R.id.btn_mediaplayer);
        btnMediaPlayerStop = findViewById(R.id.btn_mediaplayer_stop);

        btnSound.setOnClickListener(this);
        btnMediaPlayerStop.setOnClickListener(this);
        btnMediaPlayer.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        }else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isSoundLoaded = true;
            }
        });

        soundId = soundPool.load(this, R.raw.music, 1);

        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        intent = new Intent(this, MediaService.class);
        intent.setAction(MediaService.ACTION_CREATE);
        intent.setPackage(MediaService.ACTION_PACKAGE);
        startService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_soundpool:
                if (isSoundLoaded){
                    streamId = soundPool.play(soundId, 1, 1 ,0, 0, 1);
                    isSoundLoaded = false;
                }else{
                    soundPool.stop(streamId);
                    isSoundLoaded = true;
                }
                break;
            case R.id.btn_mediaplayer:
                intent.setAction(MediaService.ACTION_PLAY);
                intent.setPackage(MediaService.ACTION_PACKAGE);
                startService(intent);
                break;
            case R.id.btn_mediaplayer_stop:
                intent.setAction(MediaService.ACTION_STOP);
                intent.setPackage(MediaService.ACTION_PACKAGE);
                startService(intent);
                break;
        }
    }
}
