package io.realworld.android.vaccineslotalert.Activies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import io.realworld.android.vaccineslotalert.R;

/**
 * Splash Screen
 */
public class SplashScreenActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imageDesign, imageLogo;
    TextView logo, tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom);

        imageDesign =findViewById(R.id.design);
        imageLogo = findViewById(R.id.logo);
        logo=findViewById(R.id.logo_name);
        tag=findViewById(R.id.quote);

        imageDesign.setAnimation(topAnim);
        imageLogo.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        tag.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        },2000);
    }
}