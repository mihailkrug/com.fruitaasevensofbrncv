package com.fruitaasevensofbrncv;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.BuildConfig;
import io.paperdb.Paper;

public class MainActivity  extends AppCompatActivity {


    @BindView(R.id.spin)
    TextView spinBtn;
    @BindView(R.id.ib_share)
    ImageButton img_share;
    @BindView(R.id.resultText)
    TextView resultTv;

    @BindView(R.id.koleso)
    ImageView koleso;

    private static final String[] sectorMoney = {"2000", "60", "995",
            "500", "10000", "900","50", "100", "150", "200", "1000", "500", "550", "600", "650", "700", "10", "800"};
    private static int bablo = 0;

    private static final Random RANDOM = new Random();
    private int degree = 0, degreeOld = 0;

    private static final float HALF_SECTOR = 360f / 18f / 2f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ButterKnife.bind(this);
        Paper.init(this);

        bablo = Paper.book().read("money", 0);
        String conc = "  " + bablo;
        resultTv.setText(conc);

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
    }

    @OnClick(R.id.spin)
    public void spin360(View v) {
        degreeOld = degree % 360;

        degree = RANDOM.nextInt(360) + 720;

        RotateAnimation rotateAnim = new RotateAnimation(degreeOld, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(3600);
        rotateAnim.setFillAfter(true);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bablo = bablo + Integer.parseInt(getWinSect(360 - (degree % 360)));
                resultTv.setText(String.valueOf(bablo));
                Paper.book().write("money", bablo);

                spinBtn.setBackgroundResource(R.drawable.btn_inactive);

                spinBtn.setClickable(false);
                new CountDownTimer(5000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        String time = new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished));
                        spinBtn.setText(time);
                    }

                    public void onFinish() {
                        spinBtn.setBackgroundResource(R.drawable.btn_inactive);
                        spinBtn.setText("Play");

                        spinBtn.setClickable(true);
                    }

                }.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        koleso.startAnimation(rotateAnim);
    }

    private String getWinSect(int angle) {
        int i = 0;
        String text = null;

        do {
            float start = HALF_SECTOR * (i * 2 + 1);
            float end = HALF_SECTOR * (i * 2 + 3);

            if (angle >= start && angle < end) {
                text = sectorMoney[i];
            }

            i++;


        } while (text == null && i < sectorMoney.length);

        return text;
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hello, it's my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


}
