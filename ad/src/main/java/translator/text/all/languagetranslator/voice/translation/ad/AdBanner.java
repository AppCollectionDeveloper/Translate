package translator.text.all.languagetranslator.voice.translation.ad;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdBanner extends FrameLayout {

    AdView adView;

    public AdBanner(Context context) {
        super(context);
        init(context, null);
    }
    public AdBanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public AdBanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @TargetApi(21)
    public AdBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setAdSize(@NonNull AdSize adSize) {
        adView.setAdSize(adSize);
    }

    public void loadAd() {
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.ad_banner, this);

        adView = findViewById(R.id.ad_view);
    }
}
