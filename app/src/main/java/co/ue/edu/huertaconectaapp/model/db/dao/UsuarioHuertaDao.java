package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class UsuarioHuertaDao {

    private final DatabaseHelper dbHelper;

    public UsuarioHuertaDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(int idUsuario, int idHuerta, String fechaVinculacion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ID_USUARIO, idUsuario);
        values.put(DatabaseHelper.COL_ID_HUERTA, idHuerta);
        values.put(DatabaseHelper.COL_FECHA_VINCULACION, fechaVinculacion);
        return db.insert(DatabaseHelper.TABLE_USUARIOS_HUERTAS, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_USUARIOS_HUERTAS,
            null, null, null, null, null, null
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_USUARIOS_HUERTAS,
            null,
            DatabaseHelper.COL_ID_USUARIOS_HUERTAS + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public Cursor obtenerHuertasPorUsuario(int idUsuario) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_USUARIOS_HUERTAS,
            null,
            DatabaseHelper.COL_ID_USUARIO + " = ?",
            new String[]{String.valueOf(idUsuario)},
            null, null, null
        );
    }

    public Cursor obtenerUsuariosPorHuerta(int idHuerta) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_USUARIOS_HUERTAS,
            null,
            DatabaseHelper.COL_ID_HUERTA + " = ?",
            new String[]{String.valueOf(idHuerta)},
            null, null, null
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_USUARIOS_HUERTAS,
            DatabaseHelper.COL_ID_USUARIOS_HUERTAS + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
