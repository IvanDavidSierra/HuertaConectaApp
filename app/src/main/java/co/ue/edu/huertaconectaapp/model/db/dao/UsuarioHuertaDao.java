package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.Huerta;
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

    /**
     * Inserta vínculo tras crear filas mínimas en {@code usuarios} y {@code huertas} (FK).
     */
    public long insertarConReferencias(int idUsuario, int idHuerta, String fechaVinculacion,
                                       Huerta huertaModelo) {
        if (idUsuario <= 0 || idHuerta <= 0 || huertaModelo == null) {
            return -1;
        }
        HuertaDao huertaDao = new HuertaDao(dbHelper);
        UsuarioDao usuarioDao = new UsuarioDao(dbHelper);
        huertaDao.asegurarHuertaDesdeApi(huertaModelo);
        usuarioDao.asegurarUsuarioDeSesion(idUsuario);
        return insertar(idUsuario, idHuerta, fechaVinculacion);
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

    /** @return id_usuarios_huertas o -1 si no existe la vinculación local */
    public int obtenerIdUsuariosHuertas(int idUsuario, int idHuerta) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(
            DatabaseHelper.TABLE_USUARIOS_HUERTAS,
            new String[]{DatabaseHelper.COL_ID_USUARIOS_HUERTAS},
            DatabaseHelper.COL_ID_USUARIO + " = ? AND " + DatabaseHelper.COL_ID_HUERTA + " = ?",
            new String[]{String.valueOf(idUsuario), String.valueOf(idHuerta)},
            null, null, null
        )) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return -1;
    }

    /**
     * Primera fila de vínculo para una huerta (mismo dispositivo).
     * Útil si id_usuario en sesión no coincide con la fila creada al unirse.
     */
    public int obtenerPrimerIdUsuariosHuertasPorHuerta(int idHuerta) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(
            DatabaseHelper.TABLE_USUARIOS_HUERTAS,
            new String[]{DatabaseHelper.COL_ID_USUARIOS_HUERTAS},
            DatabaseHelper.COL_ID_HUERTA + " = ?",
            new String[]{String.valueOf(idHuerta)},
            null, null,
            DatabaseHelper.COL_ID_USUARIOS_HUERTAS + " ASC",
            "1"
        )) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return -1;
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
