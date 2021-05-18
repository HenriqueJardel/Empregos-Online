package com.henrique.empregosonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.henrique.empregosonline.model.Emprego;

public class CadastroEmprego extends AppCompatActivity {

    private TextView textTitle;
    private EditText edtDescricao;
    private EditText edtHoras;
    private EditText edtValor;
    private Button button;

    private Intent intent;
    private BDhelper bDhelper;

    Emprego emprego, alterarEmprego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_emprego);

        this.textTitle = findViewById(R.id.textTitle);
        this.edtDescricao = findViewById(R.id.edtDescricao);
        this.edtHoras = findViewById(R.id.edtHoras);
        this.edtValor = findViewById(R.id.edtValor);
        this.button = findViewById(R.id.btnSalvar);

        this.bDhelper = new BDhelper(this);

        this.intent = getIntent();
        this.alterarEmprego = (Emprego) this.intent.getSerializableExtra("chave_emprego");

        if (this.alterarEmprego != null) {
            this.textTitle.setText("Alterar Dados");
            this.edtDescricao.setText(this.alterarEmprego.getDescricao());
            this.edtHoras.setText("" + this.alterarEmprego.getHorasSemana());
            this.edtValor.setText("" + this.alterarEmprego.getValor());
            this.button.setText("Alterar");
        }
        else {
            this.textTitle.setText("Cadastrar Emprego");
            this.button.setText("Salvar");
        }

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alterarEmprego == null) {
                    emprego = new Emprego();
                    emprego.setDescricao(edtDescricao.getText().toString());
                    emprego.setHorasSemana(Integer.parseInt(edtHoras.getText().toString()));
                    emprego.setValor(Double.parseDouble(edtValor.getText().toString()));
                    long retorno = bDhelper.insert(emprego);
                    alert(retorno == -1 ? "Erro ao cadastrar" : "Cadastrado com sucesso!");
                }
                else {
                    alterarEmprego.setDescricao(edtDescricao.getText().toString());
                    alterarEmprego.setHorasSemana(Integer.parseInt(edtHoras.getText().toString()));
                    alterarEmprego.setValor(Double.parseDouble(edtValor.getText().toString()));
                    long retorno = bDhelper.update(alterarEmprego);
                    alert(retorno == -1 ? "Erro ao alterar" : "Alterado com sucesso!");
                }
                finish();
            }
        });

    }

    private void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        this.alterarEmprego = null;
        super.onDestroy();
    }
}