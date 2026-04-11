package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class TareaDao {

    private final DatabaseHelper dbHelper;

    public TareaDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertar(String titulTarea, String descripcionTarea, String fechaInicio,
                         String fechaFin, int idEstadoTarea, int idCultivo,
                         String fechaCreacion, int idUsuariosHuertas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITULO_TAREA, titulTarea);
        values.put(DatabaseHelper.COL_DESCRIPCION_TAREA, descripcionTarea);
        values.put(DatabaseHelper.COL_FECHA_INICIO_TAREA, fechaInicio);
        values.put(DatabaseHelper.COL_FECHA_FIN_TAREA, fechaFin);
        values.put(DatabaseHelper.COL_ID_ESTADO_TAREA, idEstadoTarea);
        values.put(DatabaseHelper.COL_ID_CULTIVO, idCultivo);
        values.put(DatabaseHelper.COL_FECHA_CREACION, fechaCreacion);
        values.put(DatabaseHelper.COL_ID_USUARIOS_HUERTAS, idUsuariosHuertas);
        return db.insert(DatabaseHelper.TABLE_TAREAS, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_TAREAS,
            null, null, null, null, null,
            DatabaseHelper.COL_FECHA_CREACION + " DESC"
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_TAREAS,
            null,
            DatabaseHelper.COL_ID_TAREA + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public Cursor obtenerPorUsuarioHuerta(int idUsuariosHuertas) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_TAREAS,
            null,
            DatabaseHelper.COL_ID_USUARIOS_HUERTAS + " = ?",
            new String[]{String.valueOf(idUsuariosHuertas)},
            null, null,
            DatabaseHelper.COL_FECHA_CREACION + " DESC"
        );
    }

    public Cursor obtenerPorEstado(int idEstadoTarea) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_TAREAS,
            null,
            DatabaseHelper.COL_ID_ESTADO_TAREA + " = ?",
            new String[]{String.valueOf(idEstadoTarea)},
            null, null, null
        );
    }

    public int actualizar(int id, String titulTarea, String descripcionTarea, String fechaInicio,
                          String fechaFin, int idEstadoTarea, int idCultivo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITULO_TAREA, titulTarea);
        values.put(DatabaseHelper.COL_DESCRIPCION_TAREA, descripcionTarea);
        values.put(DatabaseHelper.COL_FECHA_INICIO_TAREA, fechaInicio);
        values.put(DatabaseHelper.COL_FECHA_FIN_TAREA, fechaFin);
        values.put(DatabaseHelper.COL_ID_ESTADO_TAREA, idEstadoTarea);
        values.put(DatabaseHelper.COL_ID_CULTIVO, idCultivo);
        return db.update(
            DatabaseHelper.TABLE_TAREAS,
            values,
            DatabaseHelper.COL_ID_TAREA + " = ?",
            new String[]{String.valueOf(id)}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_TAREAS,
            DatabaseHelper.COL_ID_TAREA + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
