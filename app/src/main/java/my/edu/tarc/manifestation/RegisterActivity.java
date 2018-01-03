package my.edu.tarc.manifestation;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    ImageButton imageButtonCancel, imageButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setTitle("Register");

        imageButtonCancel = (ImageButton) findViewById(R.id.imageButtonCancel);
        imageButtonRegister = (ImageButton) findViewById(R.id.imageButtonRegister);

        imageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCancel(v);
            }
        });

        imageButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegister(v);
            }
        });

    }

    public void onClickCancel(View v){
        Intent intentCancel = new Intent(this, LoginActivity.class);
        startActivity(intentCancel);
    }

    public void onClickRegister(View v){
        Intent intentRegister = new Intent(this, LoginActivity.class);
        startActivity(intentRegister);
    }
}
