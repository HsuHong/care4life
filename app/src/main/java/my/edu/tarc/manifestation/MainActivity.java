package my.edu.tarc.manifestation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonViewProfile = (Button) findViewById(R.id.button4);

        buttonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickViewProfile(v);
            }
        });
    }

    private void onClickViewProfile(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register");
        builder.setMessage("Are you sure want to register a new account?");

        builder.setPositiveButton("AA", null);

        AlertDialog dialog = builder.create();
        dialog.show();


        Intent intentViewProfile = new Intent(this, ViewProfileActivity.class);
        startActivity(intentViewProfile);
    }


}
