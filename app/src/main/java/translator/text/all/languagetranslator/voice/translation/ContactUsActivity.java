package translator.text.all.languagetranslator.voice.translation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ContactUsActivity extends AppCompatActivity {

    EditText etEmail, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.layGoPreminum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactUsActivity.this, PremiumActivity.class));
            }
        });

        etEmail = findViewById(R.id.etEmail);
        etDescription = findViewById(R.id.etDescription);

        ((LinearLayout) findViewById(R.id.laySend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"" + etEmail.getText().toString()});
                Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                Email.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text) + etDescription.getText().toString());
                startActivity(Intent.createChooser(Email, getString(R.string.email_choose)));
            }
        });
    }
}