package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class UsuarioDao {

    private final DatabaseHelper dbHelper;

    public UsuarioDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(String nombre, String apellido, String correo,
                         String contrasena, int idTipoUsuario, String fechaCreacion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NOMBRE, nombre);
        values.put(DatabaseHelper.COL_APELLIDO, apellido);
        values.put(DatabaseHelper.COL_CORREO, correo);
        values.put(DatabaseHelper.COL_CONTRASENA, contrasena);
        values.put(DatabaseHelper.COL_ID_TIPO_USUARIO, idTipoUsuario);
        values.put(DatabaseHelper.COL_FECHA_CREACION, fechaCreacion);
        return db.insert(DatabaseHelper.TABLE_USUARIOS, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_USUARIOS,
            null, null, null, null, null,
            DatabaseHelper.COL_ID_USUARIO + " ASC"
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_USUARIOS,
            null,
            DatabaseHelper.COL_ID_USUARIO + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public Cursor obtenerPorCorreo(String correo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_USUARIOS,
            null,
            DatabaseHelper.COL_CORREO + " = ?",
            new String[]{correo},
            null, null, null
        );
    }

    public int actualizar(int id, String nombre, String apellido,
                          String correo, String contrasena, int idTipoUsuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NOMBRE, nombre);
        values.put(DatabaseHelper.COL_APELLIDO, apellido);
        values.put(DatabaseHelper.COL_CORREO, correo);
        values.put(DatabaseHelper.COL_CONTRASENA, contrasena);
        values.put(DatabaseHelper.COL_ID_TIPO_USUARIO, idTipoUsuario);
        return db.update(
            DatabaseHelper.TABLE_USUARIOS,
            values,
            DatabaseHelper.COL_ID_USUARIO + " = ?",
            new String[]{String.valueOf(id)}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_USUARIOS,
            DatabaseHelper.COL_ID_USUARIO + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
