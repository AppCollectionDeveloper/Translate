package translator.text.all.languagetranslator.voice.translation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FeedbackActivity extends AppCompatActivity {
    EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        etDescription = findViewById(R.id.etDescription);

        ((LinearLayout) findViewById(R.id.layCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_feedback)});
                Email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_feedback_subject));
                Email.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.email_feedback_text) + etDescription.getText().toString());
                startActivity(Intent.createChooser(Email, getResources().getString(R.string.email_feedback_choose)));
            }
        });

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.layGoPreminum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedbackActivity.this, PremiumActivity.class));
            }
        });
    }
}