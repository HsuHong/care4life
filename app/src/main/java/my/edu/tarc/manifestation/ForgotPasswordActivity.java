package my.edu.tarc.manifestation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageButton imageButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        this.setTitle("Forgot Password");

        imageButtonSubmit = (ImageButton) findViewById(R.id.imageButtonSubmit);

        imageButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmit(v);
            }
        });
    }

    public void onClickSubmit(View v){
        Intent intentCancel = new Intent(this, LoginActivity.class);
        startActivity(intentCancel);
    }
}
