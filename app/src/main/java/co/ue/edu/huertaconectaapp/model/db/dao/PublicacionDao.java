package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.ue.edu.huertaconectaapp.model.Publicacion;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class PublicacionDao {

    private final DatabaseHelper dbHelper;

    public PublicacionDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(String tituloPost, String contenidoPost, String fechaPost,
                         int idUsuariosHuertas, byte[] imagenBlob, String autorCorreo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITULO_POST, tituloPost);
        values.put(DatabaseHelper.COL_CONTENIDO_POST, contenidoPost);
        values.put(DatabaseHelper.COL_FECHA_POST, fechaPost);
        values.put(DatabaseHelper.COL_ID_USUARIOS_HUERTAS, idUsuariosHuertas);
        if (imagenBlob != null && imagenBlob.length > 0) {
            values.put(DatabaseHelper.COL_IMAGEN_BLOB, imagenBlob);
        }
        values.put(DatabaseHelper.COL_AUTOR_CORREO, autorCorreo);
        return db.insert(DatabaseHelper.TABLE_PUBLICACIONES, null, values);
    }

    /**
     * Publicaciones de todos los miembros vinculados localmente a esta huerta.
     */
    public List<Publicacion> listarPorIdHuerta(int idHuerta) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql =
            "SELECT p." + DatabaseHelper.COL_ID_PUBLICACION +
            ", p." + DatabaseHelper.COL_TITULO_POST +
            ", p." + DatabaseHelper.COL_CONTENIDO_POST +
            ", p." + DatabaseHelper.COL_FECHA_POST +
            ", p." + DatabaseHelper.COL_ID_USUARIOS_HUERTAS +
            ", p." + DatabaseHelper.COL_IMAGEN_BLOB +
            ", p." + DatabaseHelper.COL_AUTOR_CORREO +
            " FROM " + DatabaseHelper.TABLE_PUBLICACIONES + " p" +
            " INNER JOIN " + DatabaseHelper.TABLE_USUARIOS_HUERTAS + " uh ON p." +
            DatabaseHelper.COL_ID_USUARIOS_HUERTAS + " = uh." + DatabaseHelper.COL_ID_USUARIOS_HUERTAS +
            " WHERE uh." + DatabaseHelper.COL_ID_HUERTA + " = ?" +
            " ORDER BY p." + DatabaseHelper.COL_FECHA_POST + " DESC";

        List<Publicacion> lista = new ArrayList<>();
        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(idHuerta)})) {
            int iId = c.getColumnIndexOrThrow(DatabaseHelper.COL_ID_PUBLICACION);
            int iTit = c.getColumnIndexOrThrow(DatabaseHelper.COL_TITULO_POST);
            int iCont = c.getColumnIndexOrThrow(DatabaseHelper.COL_CONTENIDO_POST);
            int iFecha = c.getColumnIndexOrThrow(DatabaseHelper.COL_FECHA_POST);
            int iUh = c.getColumnIndexOrThrow(DatabaseHelper.COL_ID_USUARIOS_HUERTAS);
            int iBlob = c.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGEN_BLOB);
            int iAutor = c.getColumnIndexOrThrow(DatabaseHelper.COL_AUTOR_CORREO);
            while (c.moveToNext()) {
                byte[] blob = c.isNull(iBlob) ? null : c.getBlob(iBlob);
                lista.add(new Publicacion(
                    c.getInt(iId),
                    c.getString(iTit),
                    c.getString(iCont),
                    c.getString(iFecha),
                    c.getInt(iUh),
                    blob,
                    c.isNull(iAutor) ? null : c.getString(iAutor)
                ));
            }
        }
        return lista;
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
