package saci.development.sacilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import saci.development.sacilink.network.NewUser;
import saci.development.sacilink.transactions.User;

public class CadastroActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtSenha;

    NewUser newUser;

    Button goLogin;
    Button goCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        edtEmail = findViewById( R.id.editEmail );
        edtSenha = findViewById( R.id.editSenha );

        goLogin = (Button)findViewById(R.id.goLogin);
        goCadastro = (Button)findViewById(R.id.buttonCadastro);

        final Intent goLoginIntent = new Intent(this, logInActivity.class);

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(goLoginIntent);
            }
        });

    }

    public void adicionar( View view ){

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        User novoUsuario = new User( email, senha );

        newUser = new NewUser( novoUsuario );
        newUser.start();

    }

    public void cadastrado(){

        Intent goLogin = new Intent(this, logInActivity.class);
        goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goLogin);
    }

}
