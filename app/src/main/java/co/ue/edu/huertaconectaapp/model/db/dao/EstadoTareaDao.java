package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class EstadoTareaDao {

    private final DatabaseHelper dbHelper;

    public EstadoTareaDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(String descripcionEstado) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DESCRIPCION_ESTADO_TAREA, descripcionEstado);
        return db.insert(DatabaseHelper.TABLE_ESTADOS_TAREAS, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_ESTADOS_TAREAS,
            null, null, null, null, null,
            DatabaseHelper.COL_ID_ESTADO_TAREA + " ASC"
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_ESTADOS_TAREAS,
            null,
            DatabaseHelper.COL_ID_ESTADO_TAREA + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public int actualizar(int id, String descripcionEstado) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_DESCRIPCION_ESTADO_TAREA, descripcionEstado);
        return db.update(
            DatabaseHelper.TABLE_ESTADOS_TAREAS,
            values,
            DatabaseHelper.COL_ID_ESTADO_TAREA + " = ?",
            new String[]{String.valueOf(id)}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_ESTADOS_TAREAS,
            DatabaseHelper.COL_ID_ESTADO_TAREA + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
