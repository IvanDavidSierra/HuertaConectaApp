package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class TipoUsuarioDao {

    private final DatabaseHelper dbHelper;

    public TipoUsuarioDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(String descripcion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DESCRIPCION_TIPO_USUARIO, descripcion);
        return db.insert(DatabaseHelper.TABLE_TIPOS_USUARIOS, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_TIPOS_USUARIOS,
            null, null, null, null, null,
            DatabaseHelper.COL_ID_TIPO_USUARIO + " ASC"
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_TIPOS_USUARIOS,
            null,
            DatabaseHelper.COL_ID_TIPO_USUARIO + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public int actualizar(int id, String descripcion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DESCRIPCION_TIPO_USUARIO, descripcion);
        return db.update(
            DatabaseHelper.TABLE_TIPOS_USUARIOS,
            values,
            DatabaseHelper.COL_ID_TIPO_USUARIO + " = ?",
            new String[]{String.valueOf(id)}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_TIPOS_USUARIOS,
            DatabaseHelper.COL_ID_TIPO_USUARIO + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
