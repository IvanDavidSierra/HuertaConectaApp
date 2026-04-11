package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class PublicacionDao {

    private final DatabaseHelper dbHelper;

    public PublicacionDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(String tituloPost, String contenidoPost,
                         String fechaPost, int idUsuariosHuertas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITULO_POST, tituloPost);
        values.put(DatabaseHelper.COL_CONTENIDO_POST, contenidoPost);
        values.put(DatabaseHelper.COL_FECHA_POST, fechaPost);
        values.put(DatabaseHelper.COL_ID_USUARIOS_HUERTAS, idUsuariosHuertas);
        return db.insert(DatabaseHelper.TABLE_PUBLICACIONES, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_PUBLICACIONES,
            null, null, null, null, null,
            DatabaseHelper.COL_FECHA_POST + " DESC"
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_PUBLICACIONES,
            null,
            DatabaseHelper.COL_ID_PUBLICACION + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public Cursor obtenerPorUsuarioHuerta(int idUsuariosHuertas) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_PUBLICACIONES,
            null,
            DatabaseHelper.COL_ID_USUARIOS_HUERTAS + " = ?",
            new String[]{String.valueOf(idUsuariosHuertas)},
            null, null,
            DatabaseHelper.COL_FECHA_POST + " DESC"
        );
    }

    public int actualizar(int id, String tituloPost, String contenidoPost) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITULO_POST, tituloPost);
        values.put(DatabaseHelper.COL_CONTENIDO_POST, contenidoPost);
        return db.update(
            DatabaseHelper.TABLE_PUBLICACIONES,
            values,
            DatabaseHelper.COL_ID_PUBLICACION + " = ?",
            new String[]{String.valueOf(id)}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_PUBLICACIONES,
            DatabaseHelper.COL_ID_PUBLICACION + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
