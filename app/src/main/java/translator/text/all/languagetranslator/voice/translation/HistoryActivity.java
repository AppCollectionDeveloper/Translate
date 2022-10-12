package translator.text.all.languagetranslator.voice.translation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import translator.text.all.languagetranslator.voice.translation.adp.RecentFindAdp;
import translator.text.all.languagetranslator.voice.translation.data.ItemHistory;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ImageView imgDeleteAll;
    DbHelper dbHelper;
    RecyclerView history;
    public List<ItemHistory> historylist;
    public RecentFindAdp mAdapter;
    ProgressBar progress;
    LinearLayout layEmpty;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_history);
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.layGoPreminum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, PremiumActivity.class));
            }
        });

        imgDeleteAll = (ImageView) findViewById(R.id.imgDeleteAll);

        DbHelper databaseHelper = new DbHelper(this);
        this.dbHelper = databaseHelper;
        databaseHelper.openDataBase();
        this.history = (RecyclerView) findViewById(R.id.recycler_view);
        this.progress = (ProgressBar) findViewById(R.id.progress);
        this.layEmpty = findViewById(R.id.layEmpty);
        this.history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.history.setItemAnimator(new DefaultItemAnimator());
        new getSearchHistory().execute();

        imgDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.Delete_History_All()) {
                    startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));
                    finish();
                } else {
                    Toast.makeText(HistoryActivity.this, getString(R.string.history_image_delete_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private class getSearchHistory extends AsyncTask<Void, Void, Void> {

        public void onPreExecute() {
            HistoryActivity.this.historylist = new ArrayList<>();
            HistoryActivity.this.progress.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(Void... voidArr) {
            try {
                HistoryActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        HistoryActivity.this.historylist = HistoryActivity.this.dbHelper.getSearchHistory();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                HistoryActivity.this.progress.setVisibility(View.GONE);
            }
            try {
                Thread.sleep(1000);
                return null;
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            HistoryActivity.this.progress.setVisibility(View.GONE);
            if (HistoryActivity.this.historylist.size() == 0) {
                HistoryActivity.this.layEmpty.setVisibility(View.VISIBLE);
                return;
            }
            HistoryActivity.this.layEmpty.setVisibility(View.GONE);
            mAdapter = new RecentFindAdp(HistoryActivity.this, historylist);
            HistoryActivity.this.history.setAdapter(HistoryActivity.this.mAdapter);
        }
    }
}
