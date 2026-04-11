package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class SesionDao {

    private final DatabaseHelper dbHelper;

    public SesionDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void guardarSesion(String token, String correo, String fechaLogin, int idUsuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Solo puede haber una sesión activa a la vez
        db.delete(DatabaseHelper.TABLE_SESION, null, null);
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TOKEN, token);
        values.put(DatabaseHelper.COL_CORREO_SESION, correo);
        values.put(DatabaseHelper.COL_FECHA_LOGIN, fechaLogin);
        values.put(DatabaseHelper.COL_ID_USUARIO_SESION, idUsuario);
        db.insert(DatabaseHelper.TABLE_SESION, null, values);
    }

    public Cursor obtenerSesionActiva() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_SESION,
            null, null, null, null, null, null,
            "1"
        );
    }

    public String obtenerToken() {
        Cursor cursor = obtenerSesionActiva();
        String token = null;
        if (cursor.moveToFirst()) {
            token = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOKEN));
        }
        cursor.close();
        return token;
    }

    public String obtenerCorreo() {
        Cursor cursor = obtenerSesionActiva();
        String correo = null;
        if (cursor.moveToFirst()) {
            correo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CORREO_SESION));
        }
        cursor.close();
        return correo;
    }

    public boolean haySesionActiva() {
        Cursor cursor = obtenerSesionActiva();
        boolean activa = cursor.moveToFirst();
        cursor.close();
        return activa;
    }

    public int obtenerIdUsuario() {
        Cursor cursor = obtenerSesionActiva();
        int idUsuario = 0;
        if (cursor.moveToFirst()) {
            idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID_USUARIO_SESION));
        }
        cursor.close();
        return idUsuario;
    }

    public void cerrarSesion() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_SESION, null, null);
    }
}
