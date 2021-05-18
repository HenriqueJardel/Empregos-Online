package com.henrique.empregosonline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.henrique.empregosonline.BDhelper;
import com.henrique.empregosonline.CadastroPessoa;
import com.henrique.empregosonline.model.Pessoa;
import com.henrique.empregosonline.R;

import java.util.ArrayList;


public class PessoaFragment extends Fragment {

    private ListView listView;
    private Button button;
    private BDhelper bdHelper;
    private Intent intent;
    private int id1, id2;

    private ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
    private Pessoa pessoa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pessoafragment_layout, container, false);
        this.listView = (ListView) view.findViewById(R.id.ListView);
        this.button = (Button) view.findViewById(R.id.cadastrar);

        registerForContextMenu(listView);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), CadastroPessoa.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pessoa pessoa = (Pessoa) listView.getAdapter().getItem(position);
                intent = new Intent(getContext(), CadastroPessoa.class);
                intent.putExtra("chave_pessoa", pessoa);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pessoa = (Pessoa) listView.getAdapter().getItem(position);
                return false;
            }
        });


        return view;
    }

    public void preencheLista() {

        this.bdHelper = new BDhelper(getContext());
        this.pessoas = this.bdHelper.selectAllPessoas();
        this.bdHelper.close();

        if (listView != null) {
            listView.setAdapter(new ArrayAdapter<Pessoa>(getContext(), android.R.layout.simple_list_item_1, this.pessoas));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu , View view, ContextMenu.ContextMenuInfo info) {
        MenuItem deletar = menu.add(Menu.NONE, this.id1, 1, "Deletar");
        MenuItem cancelar = menu.add(Menu.NONE, this.id2, 2, "Cancelar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bdHelper = new BDhelper (getContext());
                long retorno = bdHelper.delete(pessoa);
                alert(retorno == -1 ? "Erro na exclusão" : "Excluido com sucesso!");
                preencheLista();
                return false;
            }
        });
        super.onCreateContextMenu(menu, view ,info);

    }

    private void alert(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        preencheLista();
        super.onResume();
    }

}
