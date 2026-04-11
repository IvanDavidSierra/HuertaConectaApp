package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class CultivoDao {

    private final DatabaseHelper dbHelper;

    public CultivoDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(String tituloCultivo, String descripcionCultivo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITULO_CULTIVO, tituloCultivo);
        values.put(DatabaseHelper.COL_DESCRIPCION_CULTIVO, descripcionCultivo);
        return db.insert(DatabaseHelper.TABLE_CULTIVOS, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_CULTIVOS,
            null, null, null, null, null,
            DatabaseHelper.COL_TITULO_CULTIVO + " ASC"
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_CULTIVOS,
            null,
            DatabaseHelper.COL_ID_CULTIVO + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public int actualizar(int id, String tituloCultivo, String descripcionCultivo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITULO_CULTIVO, tituloCultivo);
        values.put(DatabaseHelper.COL_DESCRIPCION_CULTIVO, descripcionCultivo);
        return db.update(
            DatabaseHelper.TABLE_CULTIVOS,
            values,
            DatabaseHelper.COL_ID_CULTIVO + " = ?",
            new String[]{String.valueOf(id)}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_CULTIVOS,
            DatabaseHelper.COL_ID_CULTIVO + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
