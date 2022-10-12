package translator.text.all.languagetranslator.voice.translation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import translator.text.all.languagetranslator.voice.translation.adp.LangugeListAdp;
import translator.text.all.languagetranslator.voice.translation.data.ItemLangSelect;

import java.util.ArrayList;

public class SelectLangActivity extends AppCompatActivity {

    RecyclerView recyclerLang;
    public LangugeListAdp mAdapter;
    EditText etSearch;
    private ArrayList<ItemLangSelect> arrayList;
    private ArrayList<ItemLangSelect> arrayList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        findViewById(R.id.layGoPreminum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectLangActivity.this, PremiumActivity.class));
            }
        });

        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        for (String s : Languages.getSpeakLang(this)) {
            ItemLangSelect itemlist = new ItemLangSelect();
            itemlist.setLan1(s);
            arrayList.add(itemlist);
            arrayList2.add(itemlist);

        }

        etSearch = findViewById(R.id.etSearch);
        recyclerLang = findViewById(R.id.recyclerLang);
        recyclerLang.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerLang.setItemAnimator(new DefaultItemAnimator());

        setAdp();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String etString = etSearch.getText().toString();
                arrayList.clear();
                if (!etString.equalsIgnoreCase("")) {
                    for (ItemLangSelect s1 : arrayList2) {
                        if (s1.getLan1().toLowerCase().contains(etString.toLowerCase()))
                            arrayList.add(s1);
                    }
                } else {
                    arrayList.addAll(arrayList2);
                }

                setAdp();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void setAdp() {
        mAdapter = new LangugeListAdp(this, arrayList, new LangugeListAdp.OnClickLang() {
            @Override
            public void onitemClick(String s) {

                Intent intent = new Intent();
                intent.putExtra("MESSAGE", s);
                setResult(-1, intent);
                finish();
            }
        });
        recyclerLang.setAdapter(mAdapter);
    }
}