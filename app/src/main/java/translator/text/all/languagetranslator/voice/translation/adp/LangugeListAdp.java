package translator.text.all.languagetranslator.voice.translation.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import translator.text.all.languagetranslator.voice.translation.R;
import translator.text.all.languagetranslator.voice.translation.data.ItemLangSelect;

import java.util.ArrayList;

public class LangugeListAdp extends RecyclerView.Adapter<LangugeListAdp.MyViewHolder> {

    public ArrayList<ItemLangSelect> dataList;
    Context mContext;
    OnClickLang onClickLang;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLangSelected;
        public TextView tvLangName;
        LinearLayout laySelected;

        public MyViewHolder(View view) {
            super(view);
            this.tvLangName = (TextView) view.findViewById(R.id.tvLangName);
            this.imgLangSelected = (ImageView) view.findViewById(R.id.imgLangSelected);
            this.laySelected = view.findViewById(R.id.laySelected);

        }
    }

    public LangugeListAdp(Context context, ArrayList<ItemLangSelect> list, OnClickLang onClickLang1) {
        this.mContext = context;
        this.dataList = list;
        onClickLang = onClickLang1;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_select_lang, viewGroup, false));
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final ItemLangSelect historyItem = this.dataList.get(i);
        myViewHolder.tvLangName.setText(historyItem.getLan1());
        myViewHolder.laySelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickLang.onitemClick(myViewHolder.tvLangName.getText().toString());
            }
        });
    }

    public int getItemCount() {
        return this.dataList.size();
    }

    public interface OnClickLang {
        void onitemClick(String s);
    }
}
