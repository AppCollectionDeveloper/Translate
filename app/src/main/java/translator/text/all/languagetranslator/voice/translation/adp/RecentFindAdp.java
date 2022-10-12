package translator.text.all.languagetranslator.voice.translation.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import translator.text.all.languagetranslator.voice.translation.ad.ManageAds;
import translator.text.all.languagetranslator.voice.translation.R;
import translator.text.all.languagetranslator.voice.translation.DbHelper;
import translator.text.all.languagetranslator.voice.translation.data.ItemHistory;
import java.util.List;

public class RecentFindAdp extends RecyclerView.Adapter<RecentFindAdp.MyViewHolder> {

    public List<ItemHistory> dataList;
    DbHelper dbHelper;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        public TextView lang1;
        public TextView lang2;
        public TextView str1;
        public TextView str2;
        public LinearLayout layNativeAds;

        public MyViewHolder(View view) {
            super(view);
            this.lang1 = (TextView) view.findViewById(R.id.lang1);
            this.str1 = (TextView) view.findViewById(R.id.str1);
            this.lang2 = (TextView) view.findViewById(R.id.lang2);
            this.str2 = (TextView) view.findViewById(R.id.str2);
            this.delete = (ImageView) view.findViewById(R.id.delete);
            this.layNativeAds = view.findViewById(R.id.layNativeAds);
        }
    }

    public RecentFindAdp(Context context, List<ItemHistory> list) {
        this.mContext = context;
        this.dataList = list;
        DbHelper databaseHelper = new DbHelper(context);
        this.dbHelper = databaseHelper;
        databaseHelper.openDataBase();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recentsearch_list_row, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final ItemHistory historyItem = this.dataList.get(i);
        myViewHolder.lang1.setText(historyItem.getLan1());
        myViewHolder.str1.setText(historyItem.getStr1());
        myViewHolder.lang2.setText(historyItem.getLan2());
        myViewHolder.str2.setText(historyItem.getStr2());
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (RecentFindAdp.this.dbHelper.Delete_History_id(historyItem.getId())) {
                    RecentFindAdp.this.dataList.remove(i);
                    RecentFindAdp.this.notifyItemRemoved(i);
                    RecentFindAdp recentSearchAdapter = RecentFindAdp.this;
                    recentSearchAdapter.notifyItemRangeChanged(i, recentSearchAdapter.dataList.size());
                    return;
                }
                Toast.makeText(RecentFindAdp.this.mContext, mContext.getString(R.string.history_image_delete_error), Toast.LENGTH_SHORT).show();
            }
        });

        if (i % 3 == 0) {
            ManageAds.loadAdmobNative(mContext, myViewHolder.layNativeAds);
            myViewHolder.layNativeAds.setVisibility(View.VISIBLE);

        } else
            myViewHolder.layNativeAds.setVisibility(View.GONE);
    }

    public int getItemCount() {
        return this.dataList.size();
    }
}
