package helpludogoretz.hackbulgaria.antoan.helpludogoretz;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import java.io.File;


public class MyActivity extends Activity {

    public static final int SEEK_DURATION = 3000;

    ImageButton prev;
    ImageButton next;
    ImageButton play;
    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);
        play = (ImageButton) findViewById(R.id.play);
        video = (VideoView) findViewById(R.id.video);

        File clip = new File(Environment.getExternalStorageDirectory() + "/HackBulgaria", "HelpLudogoretz.mp4");
        video.setVideoURI(Uri.fromFile(clip));

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(video.isPlaying()) {
                    video.pause();
                    play.setImageResource(R.drawable.play);
                }
                else {
                    video.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video.pause();
                long currentPos = video.getCurrentPosition();
                long back = (currentPos > SEEK_DURATION ? currentPos - SEEK_DURATION : 0);
                video.seekTo((int) back);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video.pause();
                long currentPos = video.getCurrentPosition();
                long forward = (video.getDuration() - currentPos > SEEK_DURATION ? currentPos + SEEK_DURATION : video.getDuration());
                video.seekTo((int) forward);
            }
        });
    }
}
