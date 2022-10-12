package translator.text.all.languagetranslator.voice.translation.adp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import translator.text.all.languagetranslator.voice.translation.R;
import translator.text.all.languagetranslator.voice.translation.data.ItemVoiceChat;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ItemVoiceChat> mMessageList;

    public int getItemViewType(int i) {
        return i;
    }

    public MsgAdapter(List<ItemVoiceChat> list) {
        this.mMessageList = list;
    }

    public int getItemCount() {
        return this.mMessageList.size();
    }

    public void add(ItemVoiceChat voiceChatItem) {
        this.mMessageList.add(voiceChatItem);
        notifyDataSetChanged();
    }

    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ReceivedMessageHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_received, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ReceivedMessageHolder) viewHolder).bind(this.mMessageList.get(i));
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        LinearLayout leyout_left;
        LinearLayout leyout_right;
        LinearLayout layNativeAds;
        TextView messageText;
        TextView messageText1;
        TextView timeText;
        TextView timeText1;

        ReceivedMessageHolder(View view) {
            super(view);
            this.timeText = (TextView) view.findViewById(R.id.text_message_name);
            this.messageText = (TextView) view.findViewById(R.id.text_message_body);
            this.timeText1 = (TextView) view.findViewById(R.id.text_message_name1);
            this.messageText1 = (TextView) view.findViewById(R.id.text_message_body1);
            this.leyout_left = (LinearLayout) view.findViewById(R.id.leyout_left);
            this.leyout_right = (LinearLayout) view.findViewById(R.id.leyout_right);
            this.layNativeAds = (LinearLayout) view.findViewById(R.id.layNativeAds);
        }

        public void bind(ItemVoiceChat voiceChatItem) {
            if (voiceChatItem.getType().equals("sender")) {
                this.leyout_right.setVisibility(View.VISIBLE);
                this.leyout_left.setVisibility(View.INVISIBLE);
                this.timeText1.setText(voiceChatItem.getLan2());
                this.messageText1.setText(voiceChatItem.getStr2());
                return;
            }
            this.leyout_right.setVisibility(View.INVISIBLE);
            this.leyout_left.setVisibility(View.VISIBLE);
            this.timeText.setText(voiceChatItem.getLan1());
            this.messageText.setText(voiceChatItem.getStr1());
        }
    }
}
