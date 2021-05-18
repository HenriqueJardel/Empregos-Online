package com.henrique.empregosonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.henrique.empregosonline.model.Emprego;
import com.henrique.empregosonline.model.Pessoa;

import java.util.ArrayList;
import java.util.HashMap;

public class CadastroPessoa extends AppCompatActivity {

    private TextView textTitle;
    private EditText edtNome;
    private EditText edtCpf;
    private EditText edtEmail;
    private EditText edtTelefone;
    private Button button;
    private Spinner spinner;

    private BDhelper bDhelper;

    private Pessoa pessoa = new Pessoa();
    private Pessoa alterarPessoa;

    private ArrayList<Emprego> empregos = new ArrayList<Emprego>();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoa);

        this.textTitle = findViewById(R.id.textTitle);
        this.edtNome = findViewById(R.id.edtDescricao);
        this.edtCpf = findViewById(R.id.edtCPF);
        this.edtEmail = findViewById(R.id.edtEmail);
        this.edtTelefone = findViewById(R.id.edtTelefone);
        this.button = findViewById(R.id.btnSalvar);
        this.spinner = findViewById(R.id.spinner);

        this.bDhelper = new BDhelper(this);
        this.empregos = bDhelper.selectAllEmpregos();
        this.bDhelper.close();

        Intent intent = getIntent();
        this.alterarPessoa = (Pessoa) intent.getSerializableExtra("chave_pessoa");

        if (alterarPessoa != null) {
            this.textTitle.setText("Alterar Dados");
            this.button.setText("Alterar");
            this.edtNome.setText(alterarPessoa.getNome());
            this.edtCpf.setText(alterarPessoa.getCpf());
            this.edtEmail.setText(alterarPessoa.getEmail());
            this.edtTelefone.setText(alterarPessoa.getTelefone());
        }

        else {
            this.textTitle.setText("Cadastrar Pessoa");
            this.button.setText("Salvar");
        }

        HashMap<String, Integer> vagas = new HashMap<String, Integer>();
        String descricao [] = new String [this.empregos.size()];
        for (int i = 0; i < this.empregos.size(); i++) {
            descricao[i] = this.empregos.get(i).getDescricao();
            vagas.put(this.empregos.get(i).getDescricao(), this.empregos.get(i).getVagaid());
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, descricao);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(arrayAdapter);

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() != null)
                    id = vagas.get(spinner.getSelectedItem().toString());
                else
                    id = vagas.get(descricao[0]);

                if (alterarPessoa == null) {
                    pessoa.setNome(edtNome.getText().toString());
                    pessoa.setVaga_id(id);
                    pessoa.setCpf(edtCpf.getText().toString());
                    pessoa.setEmail(edtEmail.getText().toString());
                    pessoa.setTelefone(edtTelefone.getText().toString());
                    long retorno = bDhelper.insert(pessoa);
                    bDhelper.close();
                    alert(retorno == -1 ? "Erro ao cadastrar" : "Cadastrado com sucesso!");
                }
                else {
                    alterarPessoa.setNome(edtNome.getText().toString());
                    alterarPessoa.setVaga_id(id);
                    alterarPessoa.setCpf(edtCpf.getText().toString());
                    alterarPessoa.setEmail(edtEmail.getText().toString());
                    alterarPessoa.setTelefone(edtTelefone.getText().toString());
                    long retorno = bDhelper.update(alterarPessoa);
                    bDhelper.close();
                    alert(retorno == -1 ? "Erro ao alterar dados" : "Dados alterados com sucesso!");
                }
                finish();
            }
        });

    }

    private void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        this.alterarPessoa = null;
        super.onDestroy();
    }


}