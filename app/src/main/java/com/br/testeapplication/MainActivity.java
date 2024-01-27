package com.br.testeapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados; // objeto que é o responsável por manipular os dados
    public ListView listViewDados;

    public Button botao;

    public ArrayList<Integer> arrayIds;

    public Integer idSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = (ListView) findViewById(R.id.listViewDados);
        botao = (Button) findViewById(R.id.buttonTelaCadastro);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaCadastro();
            }


        });
        listViewDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                confirmaExcluir();
                return true;// faz com que o comando click somente responda para o click longo, e não ao click unico, mundando de false para true

            }

        });
        listViewDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long j) {
                idSelecionado = arrayIds.get(i);
                abrirTelaAlterar();
            }
        });
        criarBancoDados();
        //inserirDadosTemp();
        listarDados();
    }




    public void criarBancoDados() {


        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS coisa(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR)");
            bancoDados.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void listarDados() {

        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id,nome FROM  coisa", null);// responsável por guardar as informações da consulta
            ArrayList<String> linhas = new ArrayList<>();
            ArrayAdapter meuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1, linhas);
            listViewDados.setAdapter(meuAdapter);
            meuCursor.moveToFirst();
            while (meuCursor != null) {
                linhas.add(meuCursor.getString(1));
                arrayIds.add(meuCursor.getInt(0));
                meuCursor.moveToNext();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void inserirDadosTemp() {

        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "INSERT INTO coisa (nome) VALUES (?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);


            stmt.bindString(1, "mauricio");
            stmt.executeInsert();
            stmt.bindString(1, "sabrina");
            stmt.executeInsert();
            stmt.bindString(1, "willian");
            stmt.executeInsert();
            stmt.bindString(1, "jose persias");
            stmt.executeInsert();

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void abrirTelaCadastro() {//responsável por ao clicar no botão, abrir a tela de cadastro
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {//responsável por após inserir os dados pela tela de cadastro, volta a tela inicial, e atualizar a tabela
        super.onResume();
        listarDados();
    }

    private void excluir() {
        //Toast.makeText(this, i.toString(), Toast.LENGTH_SHORT).show();

        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "DELETE FROM coisa  WHERE id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, idSelecionado);
            stmt.executeUpdateDelete();
            listarDados();

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void confirmaExcluir() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("deseja excluir o cadastro ?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                excluir();
                listarDados();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                listarDados();
            }
        });
        msgBox.show();
    }

    private void abrirTelaAlterar() {
        Intent intent =  new Intent(this,AlterarActivity.class);
        intent.putExtra("id",idSelecionado);
        startActivity(intent);

    }

}