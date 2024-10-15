package com.example.gaana;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv, currentTimeTv, totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay, nextBtn, previousBtn, musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    ObjectAnimator rotationAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        musicIcon = findViewById(R.id.music_icon_big);
//        locationBtn = findViewById(R.id.locationbtn);
        titleTv.setSelected(true);

        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");




        Button locationButton = findViewById(R.id.locationbtn);
        locationButton.setBackgroundColor(Color.BLUE); // You can use any predefined color
        locationButton.setTextColor(Color.WHITE); // You can also use predefined colors or RGB values


        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation(view);
            }

        });






        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));
                    if(mediaPlayer.isPlaying()) {
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    } else {
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(mediaPlayer, "currentPosition", mediaPlayer.getCurrentPosition(), progress);
                    anim.setDuration(300); // smooth transition for 300ms
                    anim.setInterpolator(new LinearInterpolator());
                    anim.start();
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    void setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSong.getTitle());
        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

        // Initialize rotation animation for the music icon
        rotationAnimator = ObjectAnimator.ofFloat(musicIcon, "rotation", 0f, 360f);
        rotationAnimator.setDuration(10000); // 10 seconds for one full rotation
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        pausePlay.setOnClickListener(v -> pausePlay());
        nextBtn.setOnClickListener(v -> playNextSong());
        previousBtn.setOnClickListener(v -> playPreviousSong());

        playMusic();
    }

    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());

            // Start rotation animation when music starts
            if (!rotationAnimator.isRunning()) {
                rotationAnimator.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNextSong() {
        if(MyMediaPlayer.currentIndex == songsList.size() - 1) return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void playPreviousSong() {
        if(MyMediaPlayer.currentIndex == 0) return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay() {
        animateButton(pausePlay); // Trigger scaling animation
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            rotationAnimator.pause(); // Pause rotation when music pauses
        } else {
            mediaPlayer.start();
            rotationAnimator.resume(); // Resume rotation when music starts
        }
    }

    private void animateButton(ImageView button) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0.9f, 1f);
        AnimatorSet scaleAnimation = new AnimatorSet();
        scaleAnimation.playTogether(scaleX, scaleY);
        scaleAnimation.setDuration(300);
        scaleAnimation.start();
    }




    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public void getLocation(View view) {
        Toast.makeText(getApplicationContext(),"Hello Ujash Solanki",Toast.LENGTH_SHORT).show();

    }
}
