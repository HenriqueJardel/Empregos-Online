package com.henrique.empregosonline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.henrique.empregosonline.model.Emprego;
import com.henrique.empregosonline.model.Pessoa;

import java.util.ArrayList;

public class BDhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "EmpregosOnline";

    // Tabelas
    private static final String TABLE_PESSOA = "PESSOA";
    private static final String TABLE_EMPREGO = "EMPREGO";

    //Atributos de pessoa
    private static final String KEY_PESSOA_ID = "id";
    private static final String KEY_PESSOA_VAGA_ID_FK = "vaga_id";
    private static final String KEY_PESSOA_NOME = "nome";
    private static final String KEY_PESSOA_CPF= "cpf";
    private static final String KEY_PESSOA_EMAIL = "email";
    private static final String KEY_PESSOA_TELEFONE = "telefone";
    private static final String KEY_PESSOA_VAGA_ESCOLHIDA = "vaga_escolhida";

    //Atributos de emprego
    private static final String KEY_EMPREGO_ID = "id";
    private static final String KEY_EMPREGO_DESCRICAO = "descricao";
    private static final String KEY_EMPREGO_HORAS_SEMANA = "horas_semana";
    private static final String KEY_EMPREGO_VALOR = "valor";

    private SQLiteDatabase bd;

    public BDhelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null ,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
       String CREATE_PESSOA_TABLE = "CREATE TABLE PESSOA (" +
               "id INTEGER PRIMARY KEY," +
               "vaga_id INTEGER REFERENCES EMPREGO," +
               "nome TEXT, " +
               "cpf TEXT," +
               "email TEXT," +
               "telefone TEXT);";

       String CREATE_EMPREGO_TABLE = "CREATE TABLE EMPREGO (" +
               "id INTEGER PRIMARY KEY," +
               "descricao TEXT," +
               "horas_semana INTEGER," +
               "valor REAL);";

       bd.execSQL(CREATE_PESSOA_TABLE);
       bd.execSQL(CREATE_EMPREGO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            bd.execSQL("DROP TABLE IF EXISTS " + TABLE_PESSOA);
            bd.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPREGO);
            onCreate(bd);
        }
    }

    public long insert(Pessoa pessoa) {
        this.bd = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PESSOA_VAGA_ID_FK,pessoa.getVaga_id());
        values.put(KEY_PESSOA_NOME, pessoa.getNome());
        values.put(KEY_PESSOA_CPF, pessoa.getCpf());
        values.put(KEY_PESSOA_EMAIL, pessoa.getEmail());
        values.put(KEY_PESSOA_TELEFONE, pessoa.getTelefone());
        long returnBD = bd.insert(TABLE_PESSOA,null, values);
        String res = Long.toString(returnBD);
        Log.i("BDHelper: ",res);
        this.bd.close();
        return returnBD;
    }

    public long update(Pessoa pessoa) {
        this.bd = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PESSOA_VAGA_ID_FK,pessoa.getVaga_id());
        values.put(KEY_PESSOA_NOME, pessoa.getNome());
        values.put(KEY_PESSOA_CPF, pessoa.getCpf());
        values.put(KEY_PESSOA_EMAIL, pessoa.getEmail());
        values.put(KEY_PESSOA_TELEFONE, pessoa.getTelefone());
        String [] args = {String.valueOf(pessoa.getId())};
        long returnBD = this.bd.update(TABLE_PESSOA,values,"id=?",args);
        this.bd.close();
        return returnBD;
    }

    public long delete(Pessoa pessoa) {
        this.bd = this.getWritableDatabase();
        String [] args = {String.valueOf(pessoa.getId())};
        long returnBD = this.bd.delete(TABLE_PESSOA, KEY_PESSOA_ID + "=?", args);
        this.bd.close();
        return returnBD;
    }

    public ArrayList<Pessoa> selectAllPessoas() {
        String [] colunas = {KEY_PESSOA_ID, KEY_PESSOA_VAGA_ID_FK, KEY_PESSOA_NOME,
                KEY_PESSOA_CPF, KEY_PESSOA_EMAIL, KEY_PESSOA_TELEFONE};
        Cursor cursor = getWritableDatabase().query(TABLE_PESSOA, colunas, null,null,null,
                null,null ,null);

        ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
        while (cursor.moveToNext()) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(cursor.getInt(0));
            pessoa.setVaga_id(cursor.getInt(1));
            pessoa.setNome(cursor.getString(2));
            pessoa.setCpf(cursor.getString(3));
            pessoa.setEmail(cursor.getString(4));
            pessoa.setTelefone(cursor.getString(5));
            pessoas.add(pessoa);
        }
        return pessoas;
    }

    public long insert(Emprego emprego) {
        this.bd = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMPREGO_DESCRICAO, emprego.getDescricao());
        values.put(KEY_EMPREGO_HORAS_SEMANA, emprego.getHorasSemana());
        values.put(KEY_EMPREGO_VALOR, emprego.getValor());
        long returnBD = bd.insert(TABLE_EMPREGO,null, values);
        String res = Long.toString(returnBD);
        Log.i("BDHelper: ",res);
        this.bd.close();
        return returnBD;
    }

    public long update(Emprego emprego) {
        this.bd = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMPREGO_DESCRICAO, emprego.getDescricao());
        values.put(KEY_EMPREGO_HORAS_SEMANA, emprego.getHorasSemana());
        values.put(KEY_EMPREGO_VALOR, emprego.getValor());
        String [] args = {String.valueOf(emprego.getVagaid())};
        long returnBD = this.bd.update(TABLE_EMPREGO,values,"id=?",args);
        this.bd.close();
        return returnBD;
    }

    public long delete(Emprego emprego) {
        this.bd = this.getWritableDatabase();
        String [] args = {String.valueOf(emprego.getVagaid())};
        long returnBD = this.bd.delete(TABLE_EMPREGO, KEY_PESSOA_ID + "=?", args);
        this.bd.close();
        return returnBD;
    }

    public ArrayList<Emprego> selectAllEmpregos() {
        String [] colunas = {KEY_EMPREGO_ID, KEY_EMPREGO_DESCRICAO, KEY_EMPREGO_HORAS_SEMANA, KEY_EMPREGO_VALOR};
        Cursor cursor = getWritableDatabase().query(TABLE_EMPREGO, colunas, null,null,null,
                null,null ,null);

        ArrayList<Emprego> empregos = new ArrayList<Emprego>();
        while(cursor.moveToNext()) {
            Emprego emprego = new Emprego();
            emprego.setVagaid(cursor.getInt(0));
            emprego.setDescricao(cursor.getString(1));
            emprego.setHorasSemana(cursor.getInt(2));
            emprego.setValor(cursor.getDouble(3));
            empregos.add(emprego);
        }
        return empregos;
    }




}
