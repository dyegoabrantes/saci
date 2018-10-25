package saci.development.sacilink;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import saci.development.sacilink.network.LogIn;

public class logInActivity extends AppCompatActivity {

    EditText email;
    EditText senha;
    LogIn login;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();

        email = findViewById( R.id.Uemail );
        senha = findViewById( R.id.Usenha );

        loginButton = findViewById(R.id.LogginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email != null && senha != null){
                    login = new LogIn( email.getText().toString(), senha.getText().toString() );
                    login.start();
                }
            }
        });

    }

    public void logado(){

        Intent home = new Intent(this, logInActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

}