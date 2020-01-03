package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.R;

public class CadastroActivity extends AppCompatActivity {

    EditText editTextNome, editTextEmail, editTextSenha;
    Switch switchTipo;

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

        }

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
