package translator.text.all.languagetranslator.voice.translation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import translator.text.all.languagetranslator.voice.translation.billing.AppBillingClient;

public class PremiumActivity extends AppCompatActivity implements AppBillingClient.AppBillingListener {

    private AppBillingClient billingClientForPurchase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        billingClientForPurchase = new AppBillingClient();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.premium_progress_dialog));
        progressDialog.setCancelable(false);

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        findViewById(R.id.layBuyNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                startBecomePro();
            }
        });
        findViewById(R.id.tvRestorePurchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                startBecomePro();
            }
        });

        if (ProPreference.isPurchase(this)) {
            ((TextView) findViewById(R.id.btnPro)).setText(getString(R.string.premium_purchased));
            findViewById(R.id.tvRestorePurchase).setVisibility(View.GONE);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBillingEnd() {
        progressDialog.dismiss();
    }

    @Override
    public void onBillingSuccess() {
        ProPreference.setPurchase(this);
        startActivity(new Intent(PremiumActivity.this, SplashActivity.class));
        finishAffinity();
    }

    void startBecomePro() {
        billingClientForPurchase.start(this);
    }
}