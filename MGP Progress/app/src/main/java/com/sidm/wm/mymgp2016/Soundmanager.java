package com.sidm.wm.mymgp2016;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore;

/**
 * Created by WM on 12/12/2016.
 */

public class Soundmanager {
    private MediaPlayer BGM;
    private MediaPlayer MAIN;
    private MediaPlayer MAIN2;
    private MediaPlayer OTHERS;
    private MediaPlayer RANK;
    private MediaPlayer SETTING;

    private SoundPool Sounds;
    private AudioAttributes audioAttributes;

    private MediaPlayer SFX1;
    private MediaPlayer SFX2;
    private MediaPlayer SFX3;
    private MediaPlayer FAIL;

    public Soundmanager(Context context)
    {
        BGM = MediaPlayer.create(context,R.raw.background_music);
        MAIN = MediaPlayer.create(context,R.raw.main);
        MAIN2 = MediaPlayer.create(context,R.raw.bgm);
        OTHERS = MediaPlayer.create(context,R.raw.help2);
        RANK = MediaPlayer.create(context,R.raw.rank);
        SETTING = MediaPlayer.create(context,R.raw.setting);


        audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        Sounds = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(2).build();
        SFX1 = MediaPlayer.create(context,R.raw.correct);
        SFX2 = MediaPlayer.create(context,R.raw.win);
        SFX3 = MediaPlayer.create(context,R.raw.cashregistercheckout);
        FAIL = MediaPlayer.create(context,R.raw.fail);

    }
    public void SetBGMVolume(float LeftBGMVol, float RightBGMVol)
    {
        BGM.setVolume(LeftBGMVol,RightBGMVol);
    }
    public void PauseBGM()
    {
        BGM.pause();
    }
    public void ResumeBGM()
    {
        BGM.start();
    }
    public void PlayMAIN2()
    {
        MAIN2.setLooping(true);
        MAIN2.start();
    }
    public void StopMAIN2()
    {
        MAIN2.stop();
    }
    public void StopBGM()
    {
        BGM.stop();
    }
    public void StopMAIN()
    {
        MAIN.stop();
    }
    public void PlayBGM()
    {
        BGM.setLooping(true);
        BGM.start();
    }
    public void PlaySET()
    {
        SETTING.setLooping(true);
        SETTING.start();
    }
    public void StopSET()
    {
        SETTING.stop();
    }
    public void PlayOTHERS()
    {
        OTHERS.setLooping(true);
        OTHERS.start();
    }
    public void StopRANK()
    {
        RANK.stop();
    }
    public void PlayRANK()
    {
        RANK.setLooping(true);
        RANK.start();
    }
    public void StopOTHERS()
    {
        OTHERS.stop();
    }

    public void PlayMAIN()
    {
        MAIN.setLooping(true);
        MAIN.start();
    }
    public void SFX1(float SFX1Left, float SFX1Right)
    {
        SFX1.start();
    }
    public void Fail()
    {
        FAIL.start();
    }
    public void SFX2()
    {
        SFX2.start();
    }
    public void SFX3()
    {
        SFX3.start();
    }
}
