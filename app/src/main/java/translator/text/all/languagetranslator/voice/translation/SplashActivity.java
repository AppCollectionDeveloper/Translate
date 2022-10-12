package translator.text.all.languagetranslator.voice.translation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import translator.text.all.languagetranslator.voice.translation.ad.AppOpenManager;
import translator.text.all.languagetranslator.voice.translation.ad.ManageAds;

public class SplashActivity extends AppCompatActivity {

    String f212gm;
    int f213i = 0;
    ProgressBar progressBar;
    private final Handler progressBarHandler = new Handler();
    private int progressBarStatus = 0;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_splash);
        setStoreToken();
        loadAdmobAd();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(100);
        int color = getResources().getColor(R.color.white);
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressBarStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBarStatus < 100) {
                    progressBarStatus = progressBarStatus + 20;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBarHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                try {
                    Thread.sleep(100);
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    private void setStoreToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), 0);
        String string = sharedPreferences.getString("gm", "");
        this.f212gm = string;
        if (this.f213i == 0 && string.equals("")) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("gm", "0");
            edit.commit();
            this.f212gm = sharedPreferences.getString("gm", "");
        }
    }

    public void loadAdmobAd() {
        if (!ProPreference.isPurchase(this)) {
            ManageAds.loadInit(this);
            new AppOpenManager(getApplication());
        }
    }
}
