package saci.development.sacilink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import saci.development.sacilink.network.NewUser;
import saci.development.sacilink.transactions.User;

public class CadastroActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtSenha;
    EditText editRepeteSenha;

    Button goLogin;
    Button buttonCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        edtEmail = findViewById( R.id.editEmail );
        edtSenha = findViewById( R.id.editSenha );
        editRepeteSenha = findViewById( R.id.editRepeteSenha);

        goLogin = (Button)findViewById(R.id.goLogin);
        buttonCadastro = (Button)findViewById(R.id.buttonCadastro);

        final Intent goLoginIntent = new Intent(this, logInActivity.class);

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(goLoginIntent);
            }
        });

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String repeteSenha = editRepeteSenha.getText().toString();

                if(!senha.equals(repeteSenha)) {
                    Toast.makeText(CadastroActivity.this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
                } else {
                    User novoUsuario = new User(email, senha);

                    NewUser newUser = new NewUser(novoUsuario, CadastroActivity.this);
                    newUser.start();
                }
            }
        });

    }

    public void cadastrado(){
        Intent login = new Intent(this, logInActivity.class);
        startActivity(login);
    }
}
