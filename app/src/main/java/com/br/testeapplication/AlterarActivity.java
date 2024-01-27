package com.br.testeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AlterarActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private Button buttonoAlterar;

    private EditText editTextNome;

    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);


        buttonoAlterar = (Button) findViewById(R.id.buttonAlterar);
        editTextNome = (EditText) findViewById(R.id.editTextNome);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        carregarDados();

        buttonoAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterar();
            }


        });

    }

    private void carregarDados() {

        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,nome FROM coisa WHERE id=" +
                    id.toString() ,null);
            cursor.moveToFirst();
            editTextNome.setText(cursor.getString(1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void alterar() {
        String valueNome;
        valueNome = editTextNome.getText().toString();
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE coisa SET  nome=? WHERE id=?";
            SQLiteStatement stms = bancoDados.compileStatement(sql);
            stms.bindString(1, valueNome);
            stms.bindLong(2,id);
            stms.executeUpdateDelete();



        }catch (Exception e){
            e.printStackTrace();
        }
        finish();

    }
}