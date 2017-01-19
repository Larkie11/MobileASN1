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

    private SoundPool Sounds;
    private AudioAttributes audioAttributes;

    private int SFX1;
    private int SFX2;

    public Soundmanager(Context context)
    {
        BGM = MediaPlayer.create(context,R.raw.background_music);
        audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        Sounds = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(2).build();

        SFX1 = Sounds.load(context,R.raw.correct,1);
        SFX2 = Sounds.load(context, R.raw.incorrect,1);
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
    public void StopBGM()
    {
        BGM.stop();
    }
    public void PlayBGM()
    {
        BGM.setLooping(true);
        BGM.start();
    }
    public void SFX1(float SFX1Left, float SFX1Right)
    {
        Sounds.play(SFX1, SFX1Left,SFX1Right,-1,0,1);
    }
    public void SFX2(float SFX2Left, float SFX2Right)
    {
        Sounds.play(SFX2, SFX2Left,SFX2Right,-1,0,1);
    }
}
