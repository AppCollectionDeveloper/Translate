package translator.text.all.languagetranslator.voice.translation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.Key;

import translator.text.all.languagetranslator.voice.translation.ad.AdBanner;
import translator.text.all.languagetranslator.voice.translation.ad.ManageAds;
import translator.text.all.languagetranslator.voice.translation.adp.MsgAdapter;
import translator.text.all.languagetranslator.voice.translation.data.ItemVoiceChat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import static android.speech.SpeechRecognizer.ERROR_AUDIO;
import static android.speech.SpeechRecognizer.ERROR_CLIENT;
import static android.speech.SpeechRecognizer.ERROR_NETWORK;
import static android.speech.SpeechRecognizer.ERROR_NO_MATCH;
import static android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY;
import static android.speech.SpeechRecognizer.ERROR_SERVER;
import static android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT;

public class Voice_Chat_Activity extends AppCompatActivity implements View.OnClickListener {

    AlertDialog.Builder builder1;
    ItemVoiceChat chatItem = new ItemVoiceChat();
    AlertDialog dialog1;
    Spinner fspinner, fspinner2;
    int fSelectedPos = 0, tSelectedPos = 0;
    LayoutInflater inflater;
    String input1, input2;
    LinearLayout lang1, lang2;
    MsgAdapter mMessageAdapter;

    public ProgressDialog mProgressDialog;
    List<ItemVoiceChat> messageList;
    RecyclerView reyclerview_message_list;
    LinearLayout layEmpty;
    TextView tvLangTo, tvLangFrom;
    int trans;
    TextView tvSpinner, tvFspinner2;
    private ArrayList<String> arrayList;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_voice_chat);


        if (!ProPreference.isPurchase(this)) {
            AdBanner mAdView = findViewById(R.id.ad_banner);
            mAdView.loadAd();
        }


        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        findViewById(R.id.layGoPreminum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Voice_Chat_Activity.this, PremiumActivity.class));
            }
        });


        this.layEmpty = findViewById(R.id.layEmpty);
        tvSpinner = findViewById(R.id.tvSpinner);
        tvFspinner2 = findViewById(R.id.tvFspinner2);

        tvSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Voice_Chat_Activity.this, SelectLangActivity.class), 555);
            }
        });
        tvFspinner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Voice_Chat_Activity.this, SelectLangActivity.class), 556);
            }
        });


        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        ImageView imgDeleteAll = (ImageView) findViewById(R.id.imgDeleteAll);
        imgBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Voice_Chat_Activity.this.onBackPressed();
                ManageAds.displayInit(Voice_Chat_Activity.this);


            }
        });
        this.reyclerview_message_list = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        imgDeleteAll.setOnClickListener(this);
        this.lang1 = (LinearLayout) findViewById(R.id.lang1);
        this.lang2 = (LinearLayout) findViewById(R.id.lang2);
        LinearLayout laySpeak = (LinearLayout) findViewById(R.id.laySpeak);
        laySpeak.setOnClickListener(this);
        LinearLayout laySpeak2 = findViewById(R.id.laySpeak2);
        laySpeak2.setOnClickListener(this);
        this.fspinner = (Spinner) findViewById(R.id.fspinner);
        this.fspinner2 = (Spinner) findViewById(R.id.fspinner2);
        tvLangTo = findViewById(R.id.tvLangTo);
        tvLangFrom = findViewById(R.id.tvLangFrom);
        arrayList = new ArrayList<>();
        Collections.addAll(arrayList, Languages.getSpeakLang(this));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_display, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        this.fspinner.setAdapter(arrayAdapter);

        int fromCode = ProPreference.getSelectedLanguage(this, "from", 14);
        int tocode = ProPreference.getSelectedLanguage(this, "to", 23);

        this.fspinner.setSelection(fromCode);
        this.fspinner2.setAdapter(arrayAdapter);
        this.fspinner2.setSelection(tocode);
        tvFspinner2.setText(arrayList.get(tocode));
        tvLangTo.setText(arrayList.get(tocode));
        tvLangFrom.setText(arrayList.get(fromCode));
        tvSpinner.setText(arrayList.get(fromCode));

        this.fspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Voice_Chat_Activity.this.getResources().getColor(R.color.spinnerText));
            }
        });
        this.fspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Voice_Chat_Activity.this.getResources().getColor(R.color.spinnerText));
            }
        });
        this.builder1 = new AlertDialog.Builder(this);
        this.inflater = getLayoutInflater();
        this.messageList = new ArrayList<>();
        this.reyclerview_message_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.reyclerview_message_list.setItemAnimator(new DefaultItemAnimator());
        MsgAdapter messageListAdapter = new MsgAdapter(this.messageList);
        this.mMessageAdapter = messageListAdapter;
        this.reyclerview_message_list.setAdapter(messageListAdapter);
        if (this.messageList.size() == 0) {
            this.layEmpty.setVisibility(View.VISIBLE);
            this.reyclerview_message_list.setVisibility(View.GONE);
            return;
        }
        this.layEmpty.setVisibility(View.GONE);
        this.reyclerview_message_list.setVisibility(View.VISIBLE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDeleteAll:
                final Dialog dialog = new Dialog(this, R.style.ExitDialogStyle);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.delete_dialog);

                LinearLayout layNo = dialog.findViewById(R.id.layNo);
                LinearLayout layYes = dialog.findViewById(R.id.layYes);
                layNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                layYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Voice_Chat_Activity.this.messageList.clear();
                        Voice_Chat_Activity.this.mMessageAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
                dialog.show();


                if (this.messageList.size() == 0) {
                    this.layEmpty.setVisibility(View.VISIBLE);
                    this.reyclerview_message_list.setVisibility(View.GONE);
                    break;
                }
                this.layEmpty.setVisibility(View.GONE);
                this.reyclerview_message_list.setVisibility(View.VISIBLE);
                break;
            case R.id.laySpeak:
                this.trans = 0;
                trans();
                break;
            case R.id.laySpeak2:
                this.trans = 1;
                trans2();
                break;
        }
    }

    private void trans() {
        SpeechRecognizer createSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Languages.getSpeakLangCode(this.fspinner.getSelectedItemPosition()));
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{Languages.getSpeakLangCode(this.fspinner.getSelectedItemPosition())});
        intent.putExtra("android.speech.extra.MAX_RESULTS", 5);
        intent.putExtra("calling_package", getPackageName());
        createSpeechRecognizer.startListening(intent);
        createSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            public void onBeginningOfSpeech() {
            }

            public void onBufferReceived(byte[] bArr) {
            }

            public void onEndOfSpeech() {
            }

            public void onEvent(int i, Bundle bundle) {
            }

            public void onPartialResults(Bundle bundle) {
            }

            public void onRmsChanged(float f) {
            }

            public void onReadyForSpeech(Bundle bundle) {
                Voice_Chat_Activity.this.builder1.setView(Voice_Chat_Activity.this.inflater.inflate(R.layout.speakdialog, (ViewGroup) null));
                Voice_Chat_Activity voice_Chat_Activity = Voice_Chat_Activity.this;
                voice_Chat_Activity.dialog1 = voice_Chat_Activity.builder1.create();
                Voice_Chat_Activity.this.dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                Voice_Chat_Activity.this.dialog1.show();
                ((TextView) Voice_Chat_Activity.this.dialog1.findViewById(R.id.dlang)).setText(Languages.getSpeakLang(Voice_Chat_Activity.this)[Voice_Chat_Activity.this.fspinner.getSelectedItemPosition()]);
            }

            public void onError(int i) {
                int strId;
                switch (i) {
                    case ERROR_NETWORK:
                        strId = R.string.recognize_network_error;
                        break;
                    case ERROR_AUDIO:
                        strId = R.string.recognize_audio_error;
                        break;
                    case ERROR_SERVER:
                        strId = R.string.recognize_server_error;
                        break;
                    case ERROR_CLIENT:
                        strId = R.string.recognize_client_error;
                        break;
                    case ERROR_SPEECH_TIMEOUT:
                        strId = R.string.recognize_timeout_error;
                        break;
                    case ERROR_NO_MATCH:
                        strId = R.string.recognize_match_error;
                        break;
                    case ERROR_RECOGNIZER_BUSY:
                        strId = R.string.recognize_busy_error;
                        break;
                    default:
                        strId = R.string.recognize_error;
                        break;
                }
                Toast.makeText(Voice_Chat_Activity.this.getApplicationContext(), getString(strId), Toast.LENGTH_SHORT).show();
                if (Voice_Chat_Activity.this.dialog1 != null) {
                    Voice_Chat_Activity.this.dialog1.dismiss();
                }
            }

            public void onResults(Bundle bundle) {
                ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                Voice_Chat_Activity.this.dialog1.dismiss();

                Voice_Chat_Activity.this.translate(stringArrayList.get(0).trim());
            }
        });
    }


    public void translate(String str) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getString(R.string.translate_progress_dialog));
        this.mProgressDialog.setProgressStyle(0);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
        this.input1 = str;
        try {
            String encode = URLEncoder.encode(str, Key.STRING_CHARSET_NAME);
            ReadJSONFeedTask readJSONFeedTask = new ReadJSONFeedTask();
            readJSONFeedTask.execute(new String[]{"https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + Languages.getsptrCode(this.fspinner.getSelectedItemPosition()) + "&tl=" + Languages.getsptrCode(this.fspinner2.getSelectedItemPosition()) + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + encode});


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void trans2() {
        SpeechRecognizer createSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Languages.getSpeakLangCode(this.fspinner2.getSelectedItemPosition()));
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{Languages.getSpeakLangCode(this.fspinner2.getSelectedItemPosition())});
        intent.putExtra("android.speech.extra.MAX_RESULTS", 5);
        intent.putExtra("calling_package", getPackageName());
        createSpeechRecognizer.startListening(intent);
        createSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            public void onBeginningOfSpeech() {
            }

            public void onBufferReceived(byte[] bArr) {
            }

            public void onEndOfSpeech() {
            }

            public void onEvent(int i, Bundle bundle) {
            }

            public void onPartialResults(Bundle bundle) {
            }

            public void onRmsChanged(float f) {
            }

            public void onReadyForSpeech(Bundle bundle) {
                Voice_Chat_Activity.this.builder1.setView(Voice_Chat_Activity.this.inflater.inflate(R.layout.speakdialog, (ViewGroup) null));
                Voice_Chat_Activity voice_Chat_Activity = Voice_Chat_Activity.this;
                voice_Chat_Activity.dialog1 = voice_Chat_Activity.builder1.create();
                Voice_Chat_Activity.this.dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                Voice_Chat_Activity.this.dialog1.show();
                ((TextView) Voice_Chat_Activity.this.dialog1.findViewById(R.id.dlang)).setText(Languages.getSpeakLang(Voice_Chat_Activity.this)[Voice_Chat_Activity.this.fspinner2.getSelectedItemPosition()]);
            }

            public void onError(int i) {
                int strId;
                switch (i) {
                    case ERROR_NETWORK:
                        strId = R.string.recognize_network_error;
                        break;
                    case ERROR_AUDIO:
                        strId = R.string.recognize_audio_error;
                        break;
                    case ERROR_SERVER:
                        strId = R.string.recognize_server_error;
                        break;
                    case ERROR_CLIENT:
                        strId = R.string.recognize_client_error;
                        break;
                    case ERROR_SPEECH_TIMEOUT:
                        strId = R.string.recognize_timeout_error;
                        break;
                    case ERROR_NO_MATCH:
                        strId = R.string.recognize_match_error;
                        break;
                    case ERROR_RECOGNIZER_BUSY:
                        strId = R.string.recognize_busy_error;
                        break;
                    default:
                        strId = R.string.recognize_error;
                        break;
                }
                Toast.makeText(Voice_Chat_Activity.this.getApplicationContext(), getString(strId), Toast.LENGTH_SHORT).show();
                if (Voice_Chat_Activity.this.dialog1 != null) {
                    Voice_Chat_Activity.this.dialog1.dismiss();
                }
            }

            public void onResults(Bundle bundle) {
                ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                Voice_Chat_Activity.this.dialog1.dismiss();

                Voice_Chat_Activity.this.translate2(stringArrayList.get(0).trim());
            }
        });
    }

    public void translate2(String str) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getString(R.string.translate_progress_dialog));
        this.mProgressDialog.setProgressStyle(0);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
        this.input2 = str;
        try {
            String encode = URLEncoder.encode(str, Key.STRING_CHARSET_NAME);
            ReadJSONFeedTask readJSONFeedTask = new ReadJSONFeedTask();
            readJSONFeedTask.execute(new String[]{"https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + Languages.getsptrCode(this.fspinner2.getSelectedItemPosition()) + "&tl=" + Languages.getsptrCode(this.fspinner.getSelectedItemPosition()) + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + encode});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

        private ReadJSONFeedTask() {
        }

        public void onPreExecute() {
            super.onPreExecute();
        }

        public String doInBackground(String... strArr) {
            return Voice_Chat_Activity.this.readJSONFeed(strArr[0]);
        }

        public void onPostExecute(String str) {
            Voice_Chat_Activity.this.mProgressDialog.dismiss();
            if (str.equals("[\"ERROR\"]")) {
                Toast.makeText(Voice_Chat_Activity.this, getString(R.string.translate_load_error), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jSONArray = new JSONArray(str);
                String str2 = "";
                for (int i = 0; i < jSONArray.getJSONArray(0).length(); i++) {
                    str2 = str2 + jSONArray.getJSONArray(0).getJSONArray(i).getString(0);
                }
                if (Voice_Chat_Activity.this.trans == 0) {
                    Voice_Chat_Activity.this.chatItem.setLan1(Voice_Chat_Activity.this.input1);
                    Voice_Chat_Activity.this.chatItem.setStr1(str2);
                    Voice_Chat_Activity.this.chatItem.setType("receiver");
                    Voice_Chat_Activity.this.messageList.add(Voice_Chat_Activity.this.chatItem);
                    Voice_Chat_Activity.this.mMessageAdapter.notifyItemInserted(Voice_Chat_Activity.this.messageList.size() - 1);
                    if (Voice_Chat_Activity.this.messageList.size() == 0) {
                        Voice_Chat_Activity.this.layEmpty.setVisibility(View.VISIBLE);
                        Voice_Chat_Activity.this.reyclerview_message_list.setVisibility(View.GONE);
                    } else {
                        Voice_Chat_Activity.this.layEmpty.setVisibility(View.GONE);
                        Voice_Chat_Activity.this.reyclerview_message_list.setVisibility(View.VISIBLE);
                    }
                } else if (Voice_Chat_Activity.this.trans == 1) {
                    Voice_Chat_Activity.this.chatItem.setLan2(Voice_Chat_Activity.this.input2);
                    Voice_Chat_Activity.this.chatItem.setStr2(str2);
                    Voice_Chat_Activity.this.chatItem.setType("sender");
                    Voice_Chat_Activity.this.messageList.add(Voice_Chat_Activity.this.chatItem);
                    Voice_Chat_Activity.this.mMessageAdapter.notifyItemInserted(Voice_Chat_Activity.this.messageList.size() - 1);
                    if (Voice_Chat_Activity.this.messageList.size() == 0) {
                        Voice_Chat_Activity.this.layEmpty.setVisibility(View.VISIBLE);
                        Voice_Chat_Activity.this.reyclerview_message_list.setVisibility(View.GONE);
                    } else {
                        Voice_Chat_Activity.this.layEmpty.setVisibility(View.GONE);
                        Voice_Chat_Activity.this.reyclerview_message_list.setVisibility(View.VISIBLE);
                    }
                }
                Voice_Chat_Activity.this.mProgressDialog.dismiss();
            } catch (Exception ignore) {
            }
        }
    }

    public String readJSONFeed(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpResponse execute = new DefaultHttpClient().execute(new HttpGet(str));
            if (execute.getStatusLine().getStatusCode() == 200) {
                InputStream content = execute.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                }
                content.close();
            }
        } catch (Exception e) {
            sb.append("[\"ERROR\"]");
        }
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 555 && resultCode == -1) {
            String message = data.getStringExtra("MESSAGE");
            tvSpinner.setText(message);
            tvLangFrom.setText(message);

            fSelectedPos = arrayList.indexOf(message);
            fspinner.setSelection(fSelectedPos);
            ProPreference.setSelectedLanguage(this, "from", arrayList.indexOf(message));

        } else if (requestCode == 556 && resultCode == -1) {
            String message = data.getStringExtra("MESSAGE");
            tvFspinner2.setText(message);
            tvLangTo.setText(message);

            tSelectedPos = arrayList.indexOf(message);
            fspinner2.setSelection(tSelectedPos);

            ProPreference.setSelectedLanguage(this, "to", arrayList.indexOf(message));
        }
    }
}
