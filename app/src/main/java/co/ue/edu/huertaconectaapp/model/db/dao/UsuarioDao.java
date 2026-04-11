package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class UsuarioDao {

    private final DatabaseHelper dbHelper;

    public UsuarioDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Fila mínima para satisfacer la FK de usuarios_huertas (el usuario real vive en el servidor).
     */
    public void asegurarUsuarioDeSesion(int idUsuario) {
        if (idUsuario <= 0) {
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(
            DatabaseHelper.TABLE_USUARIOS,
            new String[]{DatabaseHelper.COL_ID_USUARIO},
            DatabaseHelper.COL_ID_USUARIO + " = ?",
            new String[]{String.valueOf(idUsuario)},
            null, null, null
        )) {
            if (c.moveToFirst()) {
                return;
            }
        }
        db = dbHelper.getWritableDatabase();
        // Correo único por id (evita UNIQUE con otras filas / cuentas en el mismo SQLite)
        String correo = "sync-" + idUsuario + "@huertaconecta.local";
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_ID_USUARIO, idUsuario);
        cv.put(DatabaseHelper.COL_NOMBRE, "—");
        cv.put(DatabaseHelper.COL_APELLIDO, "—");
        cv.put(DatabaseHelper.COL_CORREO, correo);
        cv.put(DatabaseHelper.COL_CONTRASENA, "-");
        cv.put(DatabaseHelper.COL_FECHA_CREACION, fecha);
        db.insert(DatabaseHelper.TABLE_USUARIOS, null, cv);
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
