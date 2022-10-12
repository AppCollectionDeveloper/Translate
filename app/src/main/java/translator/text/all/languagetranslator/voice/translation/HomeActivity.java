package translator.text.all.languagetranslator.voice.translation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.exifinterface.media.ExifInterface;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import translator.text.all.languagetranslator.voice.translation.ad.AdBanner;
import translator.text.all.languagetranslator.voice.translation.ad.ManageAds;


public class HomeActivity extends AppCompatActivity {
    LinearLayout adsize;
    String imageFilePath;
    File photoFile;
    ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    float selectedStar = 1;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.home);

        ImageView layAds1 = findViewById(R.id.layAds1);
        ImageView layAds2 = findViewById(R.id.layAds2);
        ImageView layAds3 = findViewById(R.id.layAds3);

        if (ProPreference.isPurchase(this)) {
            layAds1.setVisibility(View.GONE);
            layAds2.setVisibility(View.GONE);
            layAds3.setVisibility(View.GONE);
        }

        findViewById(R.id.layGoPreminum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PremiumActivity.class));
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[5];

        drawerItem[0] = new DataModel(R.drawable.ic_feedback, getString(R.string.menu_feedback));
        drawerItem[1] = new DataModel(R.drawable.ic_rateus_menu, getString(R.string.menu_rate_us));
        drawerItem[2] = new DataModel(R.drawable.ic_share_menu, getString(R.string.menu_share));
        drawerItem[3] = new DataModel(R.drawable.ic_policy, getString(R.string.menu_policy));
        drawerItem[4] = new DataModel(R.drawable.ic_restore, getString(R.string.menu_restore));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        View footerView = getLayoutInflater().inflate(R.layout.nav_header_main_activity_drawer, null);
        footerView.findViewById(R.id.imgCloseDrawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
        footerView.findViewById(R.id.layGoPreminum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PremiumActivity.class));
            }
        });
        mDrawerList.addHeaderView(footerView);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerToggle();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        FirebaseApp.initializeApp(this);

        this.adsize = (LinearLayout) findViewById(R.id.adsize);
        if (isOnline() && !ProPreference.isPurchase(this)) {
            this.adsize.setVisibility(View.VISIBLE);
            AdBanner adBanner = findViewById(R.id.ad_banner);
            adBanner.loadAd();

            ManageAds.loadAdmobNative(this, findViewById(R.id.layNativeAds));
            loadRewardVideo();
        } else {
            this.adsize.setVisibility(View.GONE);
        }

        findViewById(R.id.layLangTranslator).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomeActivity.this.askPermission("1");
                } else {
                    HomeActivity.this.goTranslate();
                }
            }
        });
        findViewById(R.id.layHistory).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomeActivity.this.askPermission(ExifInterface.GPS_MEASUREMENT_2D);
                } else {
                    HomeActivity.this.goHistory();
                }
            }
        });
        findViewById(R.id.layChatTranslator).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomeActivity.this.askPermission(ExifInterface.GPS_MEASUREMENT_3D);
                } else {
                    HomeActivity.this.goVideoChat();
                }
            }
        });

        findViewById(R.id.layCameraTransator).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomeActivity.this.askPermission("5");
                } else {
                    HomeActivity.this.openCameraIntent();
                }
            }
        });
        findViewById(R.id.layGalleryTransator).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    HomeActivity.this.askPermission("6");
                } else {
                    HomeActivity.this.goRead();
                }
            }
        });

        findViewById(R.id.imgDrawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerList);

            }
        });

        toolbar.setTitle("");
    }

    void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name2, R.string.app_name2);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer, getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);
        mDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position - 1);
        }
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));
                break;
            case 1:
                final AlertDialog dialog1;
                AlertDialog.Builder builder1;
                builder1 = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();

                View view = inflater.inflate(R.layout.dialog_rate_us, (ViewGroup) null);
                builder1.setView(view);
                dialog1 = builder1.create();
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));


                TextView tvNotNow = view.findViewById(R.id.tvNotNow);
                TextView tvCancel = view.findViewById(R.id.tvCancel);
                LinearLayout layYes = view.findViewById(R.id.layYes);
                ScaleRatingBar simpleRatingBar = view.findViewById(R.id.simpleRatingBar);
                simpleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {

                        selectedStar = rating;
                    }
                });

                tvNotNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                layYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog1.dismiss();


                        if (selectedStar > 3) {
                            final String appPackageName = getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + appPackageName)));
                            } catch (ActivityNotFoundException anfe) {
                                startActivity(new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id="
                                                + appPackageName)));
                            }
                        } else {
                            startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
                        }

                    }
                });
                dialog1.show();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 2:
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("text/plain");
                shareIntent.putExtra("android.intent.extra.SUBJECT", getString(R.string.home_app_info, getString(R.string.app_name)));
                shareIntent.putExtra("android.intent.extra.TEXT", " - https://play.google.com/store/apps/details?id=" + getPackageName() + " \n\n");
                startActivity(Intent.createChooser(shareIntent, getString(R.string.home_share)));
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 3:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.privacyPolicyUrl)));
                startActivity(browserIntent);
                mDrawerLayout.closeDrawer(mDrawerList);
                return;
            case 4:
                startActivity(new Intent(HomeActivity.this, PremiumActivity.class));
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
        }
    }

    public void goTranslate() {
        startActivity(new Intent(this, TextActivity.class));

        ManageAds.displayInit(this);
    }


    public void goHistory() {
        startActivity(new Intent(this, HistoryActivity.class));

        ManageAds.displayInit(this);

    }


    public void goVideoChat() {
        if (ManageAds.isRewardVideoLoaded()) {
            ManageAds.showRewardVideo(this, new Intent(this, Voice_Chat_Activity.class), 0);
        } else {
            startActivity(new Intent(HomeActivity.this, Voice_Chat_Activity.class));
            loadRewardVideo();
        }
    }

    public void goRead() {
        if (ManageAds.isRewardVideoLoaded()) {
            ManageAds.showRewardVideo(this, new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101);
        } else {
            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101);
            loadRewardVideo();
        }
    }

    public void askPermission(final String str) {
        Dexter.withActivity(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO").withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (!multiplePermissionsReport.areAllPermissionsGranted()) {
                    return;
                }
                switch (str) {
                    case "1":
                        HomeActivity.this.goTranslate();
                        break;
                    case ExifInterface.GPS_MEASUREMENT_2D:
                        HomeActivity.this.goHistory();
                        break;
                    case ExifInterface.GPS_MEASUREMENT_3D:
                        HomeActivity.this.goVideoChat();
                        break;
                    case "4":
                        break;
                    case "5":
                        HomeActivity.this.openCameraIntent();
                        break;
                    case "6":
                        HomeActivity.this.goRead();
                        break;
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public void openCameraIntent() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                this.photoFile = createImageFile();
            } catch (IOException unused) {
                Toast.makeText(getApplicationContext(), getString(R.string.home_open_camera_error), Toast.LENGTH_SHORT).show();
            }
            File file = this.photoFile;
            if (file != null) {
                intent.putExtra("output", FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file));
            }
        }

        if (ManageAds.isRewardVideoLoaded()) {
            ManageAds.showRewardVideo(this, intent, 100);
        } else {
            startActivityForResult(intent, 100);
            loadRewardVideo();
        }
    }

    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File createTempFile = File.createTempFile("IMG_" + format + "_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        this.imageFilePath = createTempFile.getAbsolutePath();
        return createTempFile;
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 203) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
            if (i2 == -1) {
                try {
                    runTextRec(MediaStore.Images.Media.getBitmap(getContentResolver(), activityResult.getUri()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (i2 == 204) {
                Toast.makeText(getApplicationContext(), getString(R.string.home_error), Toast.LENGTH_SHORT).show();
            }
        }
        if (i == 100) {
            Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File file = new File(this.imageFilePath);
            Uri fromFile = Uri.fromFile(file);
            intent2.setData(fromFile);
            sendBroadcast(intent2);
            if (file.length() > 0) {
                CropImage.activity(fromFile).start(this);
            }
        } else if (i == 101) {
            try {
                String[] strArr = {"_data"};
                Cursor query = getContentResolver().query(intent.getData(), strArr, (String) null, (String[]) null, (String) null);
                query.moveToFirst();
                String string = query.getString(query.getColumnIndex(strArr[0]));
                query.close();
                File file2 = new File(string);
                Uri fromFile2 = Uri.fromFile(file2);
                if (file2.length() > 0) {
                    CropImage.activity(fromFile2).start(this);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    private void runTextRec(Bitmap bitmap) {
        FirebaseVision.getInstance().getOnDeviceTextRecognizer().processImage(FirebaseVisionImage.fromBitmap(bitmap)).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                if (firebaseVisionText.getText().length() <= 0) {
                    Toast.makeText(HomeActivity.this.getApplicationContext(), getString(R.string.home_empty_text_image_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(HomeActivity.this, TextActivity.class);
                intent.putExtra("text", firebaseVisionText.getText());
                HomeActivity.this.startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exc) {
                Toast.makeText(HomeActivity.this.getApplicationContext(), getString(R.string.home_text_image_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        openExitAppDialog();
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void openExitAppDialog() {
        final Dialog dialog = new Dialog(this, R.style.ExitDialogStyle);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_app_exit);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.layCancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.layOk).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                HomeActivity.this.finish();
            }
        });
        dialog.show();
    }

    public void loadRewardVideo() {
        if (!ProPreference.isPurchase(HomeActivity.this)) {
            ManageAds.loadRewardVideo(this);
        }
    }
}
