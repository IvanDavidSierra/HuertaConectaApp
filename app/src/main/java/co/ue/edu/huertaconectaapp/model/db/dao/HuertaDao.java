package co.ue.edu.huertaconectaapp.model.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.ue.edu.huertaconectaapp.model.Huerta;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;

public class HuertaDao {

    private final DatabaseHelper dbHelper;

    public HuertaDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Garantiza una fila en {@code huertas} con el id del API (requerido por FK de usuarios_huertas).
     */
    public void asegurarHuertaDesdeApi(Huerta h) {
        if (h == null || h.getIdHuerta() <= 0) {
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_ID_HUERTA, h.getIdHuerta());
        String nombre = h.getNombreHuerta();
        cv.put(DatabaseHelper.COL_NOMBRE_HUERTA,
            (nombre != null && !nombre.isEmpty()) ? nombre : "Huerta");
        cv.put(DatabaseHelper.COL_DIRECCION_HUERTA, h.getDireccionHuerta());
        cv.put(DatabaseHelper.COL_DESCRIPCION, h.getDescripcion());
        cv.put(DatabaseHelper.COL_FECHA_CREACION, h.getFechaCreacion());
        db.insertWithOnConflict(
            DatabaseHelper.TABLE_HUERTAS,
            null,
            cv,
            SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public long insertar(String nombreHuerta, String direccionHuerta,
                         String descripcion, String fechaCreacion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NOMBRE_HUERTA, nombreHuerta);
        values.put(DatabaseHelper.COL_DIRECCION_HUERTA, direccionHuerta);
        values.put(DatabaseHelper.COL_DESCRIPCION, descripcion);
        values.put(DatabaseHelper.COL_FECHA_CREACION, fechaCreacion);
        return db.insert(DatabaseHelper.TABLE_HUERTAS, null, values);
    }

    public Cursor obtenerTodos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_HUERTAS,
            null, null, null, null, null,
            DatabaseHelper.COL_NOMBRE_HUERTA + " ASC"
        );
    }

    public Cursor obtenerPorId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
            DatabaseHelper.TABLE_HUERTAS,
            null,
            DatabaseHelper.COL_ID_HUERTA + " = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
    }

    public int actualizar(int id, String nombreHuerta, String direccionHuerta, String descripcion) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NOMBRE_HUERTA, nombreHuerta);
        values.put(DatabaseHelper.COL_DIRECCION_HUERTA, direccionHuerta);
        values.put(DatabaseHelper.COL_DESCRIPCION, descripcion);
        return db.update(
            DatabaseHelper.TABLE_HUERTAS,
            values,
            DatabaseHelper.COL_ID_HUERTA + " = ?",
            new String[]{String.valueOf(id)}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
            DatabaseHelper.TABLE_HUERTAS,
            DatabaseHelper.COL_ID_HUERTA + " = ?",
            new String[]{String.valueOf(id)}
        );
    }
}
