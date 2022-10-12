package translator.text.all.languagetranslator.voice.translation.billing;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

import static com.android.billingclient.api.BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED;

public class AppBillingClient {

    String skuID = "bill_id_forever";

    private BillingClient m_billingClient;
    private AppBillingListener m_listener;

    public void start(@NonNull AppBillingListener listener) {
        BillingClient.Builder builder = BillingClient.newBuilder(listener.getActivity());
        builder.enablePendingPurchases();
        builder.setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                processPurchasesUpdateResult(billingResult, list);
            }
        });

        m_billingClient = builder.build();
        m_billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if (m_billingClient.isReady()) {

                        final ArrayList<String> skuList = new ArrayList<>();
                        skuList.add(skuID);
                        SkuDetailsParams.Builder skuDetailsParams = SkuDetailsParams.newBuilder();
                        skuDetailsParams.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

                        m_billingClient.querySkuDetailsAsync(skuDetailsParams.build(), new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    Activity activity = m_listener.getActivity();
                                    if (skuDetailsList.size() > 0) {

                                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                .setSkuDetails(skuDetailsList.get(0))
                                                .build();
                                        m_billingClient.launchBillingFlow(activity, billingFlowParams);
                                    } else {
                                        Toast.makeText(activity, activity.getString(R.string.billing_error), Toast.LENGTH_SHORT).show();
                                    }
                                    m_listener.onBillingEnd();
                                }
                            }
                        });
                    }
                } else {
                    m_listener.onBillingEnd();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                m_listener.onBillingEnd();
            }
        });
    }

    private void processPurchasesUpdateResult(@NonNull BillingResult result, @Nullable List<Purchase> purchaseList) {
        if (purchaseList != null && purchaseList.size() > 0) {
            Purchase pur = purchaseList.get(0);

            AcknowledgePurchaseParams acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(pur.getPurchaseToken())
                            .build();

            AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
                @Override
                public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                }
            };

            m_billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

            ConsumeParams consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(pur.getPurchaseToken())
                    .build();

            ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String purchaseToken) {
                }
            };
            m_billingClient.consumeAsync(consumeParams, consumeResponseListener);
            m_listener.onBillingSuccess();

        } else if (result.getResponseCode() == ITEM_ALREADY_OWNED) {
            m_listener.onBillingSuccess();
        }
    }

    public interface AppBillingListener {
        Activity getActivity();
        void onBillingEnd();
        void onBillingSuccess();
    }
}
