package tk11.jphacks.titech.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import tk11.jphacks.titech.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hdl = new Handler();
        hdl.postDelayed(new splashHandler(), 500);
    }
    class splashHandler implements Runnable {
        public void run() {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
