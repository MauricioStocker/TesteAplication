package com.br.testeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {

	SQLiteDatabase bancoDados;
	EditText editeTextNome;
	Button botao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);
		editeTextNome = (EditText) findViewById(R.id.editTextNome);
		botao = (Button) findViewById(R.id.buttonAlterar);

		botao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cadastrar();
			}

			private void cadastrar() {
				if (!TextUtils.isEmpty(editeTextNome.getText().toString())) {
					try {
						bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);

						String sql = "INSERT INTO coisa (nome) VALUES (?)";
						SQLiteStatement stmt = bancoDados.compileStatement(sql);
						stmt.bindString(1,editeTextNome.getText().toString());
						stmt.executeInsert();
						finish();
						bancoDados.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

	}
}