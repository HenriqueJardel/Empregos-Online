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
import com.henrique.empregosonline.CadastroEmprego;
import com.henrique.empregosonline.model.Pessoa;
import com.henrique.empregosonline.model.Emprego;
import com.henrique.empregosonline.R;

import java.util.ArrayList;


public class EmpregoFragment extends Fragment {

    private ListView listView;
    private Button button;
    private BDhelper bdHelper;
    private Intent intent;
    private int id1, id2;

    private ArrayList<Emprego> empregos = new ArrayList<Emprego>();
    private ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
    private Emprego emprego;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empregofragment_layout, container, false);

        this.button = view.findViewById(R.id.cadastrar);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), CadastroEmprego.class);
                startActivity(intent);
            }
        });

        this.listView = view.findViewById(R.id.ListView);
        registerForContextMenu(this.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Emprego emprego = (Emprego) listView.getAdapter().getItem(position);
                intent = new Intent(getContext(), CadastroEmprego.class);
                intent.putExtra("chave_emprego", emprego);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 emprego = (Emprego) listView.getAdapter().getItem(position);
                return false;
            }
        });

        this.bdHelper = new BDhelper(getContext());

        return view;
    }


    public void preencheLista() {
        this.empregos= this.bdHelper.selectAllEmpregos();
        this.bdHelper.close();

        if (listView != null) {
            listView .setAdapter(new ArrayAdapter<Emprego>(getContext(), android.R.layout.simple_list_item_1, empregos));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu , View view, ContextMenu.ContextMenuInfo info) {
        MenuItem deletar = menu.add(Menu.NONE, this.id1, 1, "Deletar");
        MenuItem cancelar = menu.add(Menu.NONE, this.id2, 2, "Cancelar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (podeDeletar(emprego)) {
                    long retorno = bdHelper.delete(emprego);
                    alert(retorno == -1 ? "Erro na exclusão" : "Excluido com sucesso!");
                    preencheLista();

                }
                else {
                    alert("Não é possivel excluir emprego que possui pessoas cadastradas!");
                }
                return false;
            }
        });
        super.onCreateContextMenu(menu, view ,info);

    }

    public boolean podeDeletar(Emprego emprego) {
        this.pessoas = bdHelper.selectAllPessoas();
        this.bdHelper.close();

        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getVaga_id() == emprego.getVagaid()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        preencheLista();
        super.onResume();
    }

    private void alert(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
