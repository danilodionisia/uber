package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.example.myapplication.config.ConfiguracaoFirebase;
import com.example.myapplication.helper.UsuarioFirebase;
import com.example.myapplication.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    EditText editTextNome, editTextEmail, editTextSenha;
    Switch switchTipo;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = findViewById(R.id.editTextCadastroNome);
        editTextEmail = findViewById(R.id.editTextCadastroEmail);
        editTextSenha = findViewById(R.id.editTextCadastroSenha);
        switchTipo = findViewById(R.id.switchCadastroTipo);

        editTextNome.requestFocus();

    }

    public void cadastraUsuario(View view){

        if (validaCamposCadastro()){

            Usuario usuario = new Usuario();

            usuario.setEmail(editTextEmail.getText().toString());
            usuario.setNome(editTextNome.getText().toString());
            usuario.setSenha(editTextSenha.getText().toString());
            usuario.setTipo(checaTipoUsuario());

            cadastrarUsuarioBanco(usuario);


        }

    }

    private void cadastrarUsuarioBanco(final Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    try {

                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId(idUsuario);
                        usuario.salvar();

                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                        if(checaTipoUsuario() == "P"){
                            startActivity(new Intent(CadastroActivity.this, MapsActivity.class));
                            finish();
                            Toast.makeText(getApplicationContext(), "Sucesso ao cadastrar o passageiro", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(CadastroActivity.this, RequisicoesActivity.class));
                            finish();
                            Toast.makeText(getApplicationContext(), "Sucesso ao cadastrar o motorista", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{

                    String excessao = "";

                    try{
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excessao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excessao = "Por favor, digite um e-mail válido!";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excessao = "Esta conta já foi cadastrada!";
                    }catch (Exception e){
                        excessao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excessao, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public String checaTipoUsuario(){

        return switchTipo.isChecked() ? "M" : "P" ;

    }

    public boolean validaCamposCadastro(){

        if (!editTextNome.getText().toString().isEmpty()){

            if(!editTextEmail.getText().toString().isEmpty()){

                if (!editTextSenha.getText().toString().isEmpty() && editTextSenha.getText().toString().length() >= 6){

                    return true;

                }else {
                    Toast.makeText(getApplicationContext(), "O campo senha não pode ser vazio nem ter menos que 6 dígitos", Toast.LENGTH_SHORT).show();
                    editTextSenha.requestFocus();
                    return false;
                }

            }else {
                Toast.makeText(getApplicationContext(), "Preencha o campo E-mail", Toast.LENGTH_SHORT).show();
                editTextEmail.requestFocus();
                return false;
            }

        }else {
            Toast.makeText(getApplicationContext(), "Preencha o campo nome!", Toast.LENGTH_SHORT).show();
            editTextNome.requestFocus();
            return false;
        }
    }
}
