package co.ue.edu.huertaconectaapp.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "huerta_conecta.db";
    private static final int DATABASE_VERSION = 4;

    private static DatabaseHelper instance;

    // ── Tabla tipos_usuarios ──────────────────────────────────────────────────
    public static final String TABLE_TIPOS_USUARIOS = "tipos_usuarios";
    public static final String COL_ID_TIPO_USUARIO = "id_tipo_usuario";
    public static final String COL_DESCRIPCION_TIPO_USUARIO = "descripcion_tipo_usuario";

    // ── Tabla usuarios ────────────────────────────────────────────────────────
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COL_ID_USUARIO = "id_usuario";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_APELLIDO = "apellido";
    public static final String COL_CORREO = "correo";
    public static final String COL_CONTRASENA = "contrasena";
    public static final String COL_FECHA_CREACION = "fecha_creacion";

    // ── Tabla huertas ─────────────────────────────────────────────────────────
    public static final String TABLE_HUERTAS = "huertas";
    public static final String COL_ID_HUERTA = "id_huerta";
    public static final String COL_NOMBRE_HUERTA = "nombre_huerta";
    public static final String COL_DIRECCION_HUERTA = "direccion_huerta";
    public static final String COL_DESCRIPCION = "descripcion";

    // ── Tabla usuarios_huertas ────────────────────────────────────────────────
    public static final String TABLE_USUARIOS_HUERTAS = "usuarios_huertas";
    public static final String COL_ID_USUARIOS_HUERTAS = "id_usuarios_huertas";
    public static final String COL_FECHA_VINCULACION = "fecha_vinculacion";

    // ── Tabla cultivos ────────────────────────────────────────────────────────
    public static final String TABLE_CULTIVOS = "cultivos";
    public static final String COL_ID_CULTIVO = "id_cultivo";
    public static final String COL_TITULO_CULTIVO = "titulo_cultivo";
    public static final String COL_DESCRIPCION_CULTIVO = "descripcion_cultivo";

    // ── Tabla estados_tareas ──────────────────────────────────────────────────
    public static final String TABLE_ESTADOS_TAREAS = "estados_tareas";
    public static final String COL_ID_ESTADO_TAREA = "id_estado_tarea";
    public static final String COL_DESCRIPCION_ESTADO_TAREA = "descripcion_estado_tarea";

    // ── Tabla tareas ──────────────────────────────────────────────────────────
    public static final String TABLE_TAREAS = "tareas";
    public static final String COL_ID_TAREA = "id_tarea";
    public static final String COL_TITULO_TAREA = "titulo_tarea";
    public static final String COL_DESCRIPCION_TAREA = "descripcion_tarea";
    public static final String COL_FECHA_INICIO_TAREA = "fecha_inicio_tarea";
    public static final String COL_FECHA_FIN_TAREA = "fecha_fin_tarea";

    // ── Tabla publicaciones ───────────────────────────────────────────────────
    public static final String TABLE_PUBLICACIONES = "publicaciones";
    public static final String COL_ID_PUBLICACION = "id_publicacion";
    public static final String COL_TITULO_POST = "titulo_post";
    public static final String COL_CONTENIDO_POST = "contenido_post";
    public static final String COL_FECHA_POST = "fecha_post";
    public static final String COL_IMAGEN_BLOB = "imagen_blob";
    public static final String COL_AUTOR_CORREO = "autor_correo";

    // ── Tabla sesion ──────────────────────────────────────────────────────────
    public static final String TABLE_SESION = "sesion";
    public static final String COL_ID_SESION = "id_sesion";
    public static final String COL_TOKEN = "token";
    public static final String COL_CORREO_SESION = "correo";
    public static final String COL_FECHA_LOGIN = "fecha_login";
    public static final String COL_ID_USUARIO_SESION = "id_usuario_sesion";

    // ── CREATE statements ─────────────────────────────────────────────────────

    private static final String CREATE_TIPOS_USUARIOS =
        "CREATE TABLE " + TABLE_TIPOS_USUARIOS + " (" +
            COL_ID_TIPO_USUARIO   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DESCRIPCION_TIPO_USUARIO + " TEXT NOT NULL" +
        ");";

    private static final String CREATE_USUARIOS =
        "CREATE TABLE " + TABLE_USUARIOS + " (" +
            COL_ID_USUARIO   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOMBRE       + " TEXT NOT NULL, " +
            COL_APELLIDO     + " TEXT NOT NULL, " +
            COL_CORREO       + " TEXT NOT NULL UNIQUE, " +
            COL_CONTRASENA   + " TEXT NOT NULL, " +
            COL_ID_TIPO_USUARIO + " INTEGER, " +
            COL_FECHA_CREACION  + " TEXT, " +
            "FOREIGN KEY (" + COL_ID_TIPO_USUARIO + ") REFERENCES " +
                TABLE_TIPOS_USUARIOS + "(" + COL_ID_TIPO_USUARIO + ") ON DELETE SET NULL" +
        ");";

    private static final String CREATE_HUERTAS =
        "CREATE TABLE " + TABLE_HUERTAS + " (" +
            COL_ID_HUERTA        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOMBRE_HUERTA    + " TEXT NOT NULL, " +
            COL_DIRECCION_HUERTA + " TEXT, " +
            COL_DESCRIPCION      + " TEXT, " +
            COL_FECHA_CREACION   + " TEXT" +
        ");";

    private static final String CREATE_USUARIOS_HUERTAS =
        "CREATE TABLE " + TABLE_USUARIOS_HUERTAS + " (" +
            COL_ID_USUARIOS_HUERTAS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ID_USUARIO          + " INTEGER NOT NULL, " +
            COL_ID_HUERTA           + " INTEGER NOT NULL, " +
            COL_FECHA_VINCULACION   + " TEXT, " +
            "FOREIGN KEY (" + COL_ID_USUARIO + ") REFERENCES " +
                TABLE_USUARIOS + "(" + COL_ID_USUARIO + ") ON DELETE CASCADE, " +
            "FOREIGN KEY (" + COL_ID_HUERTA + ") REFERENCES " +
                TABLE_HUERTAS + "(" + COL_ID_HUERTA + ") ON DELETE CASCADE" +
        ");";

    private static final String CREATE_CULTIVOS =
        "CREATE TABLE " + TABLE_CULTIVOS + " (" +
            COL_ID_CULTIVO          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TITULO_CULTIVO      + " TEXT NOT NULL, " +
            COL_DESCRIPCION_CULTIVO + " TEXT" +
        ");";

    private static final String CREATE_ESTADOS_TAREAS =
        "CREATE TABLE " + TABLE_ESTADOS_TAREAS + " (" +
            COL_ID_ESTADO_TAREA          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DESCRIPCION_ESTADO_TAREA + " TEXT NOT NULL" +
        ");";

    private static final String CREATE_TAREAS =
        "CREATE TABLE " + TABLE_TAREAS + " (" +
            COL_ID_TAREA            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TITULO_TAREA        + " TEXT NOT NULL, " +
            COL_DESCRIPCION_TAREA   + " TEXT, " +
            COL_FECHA_INICIO_TAREA  + " TEXT, " +
            COL_FECHA_FIN_TAREA     + " TEXT, " +
            COL_ID_ESTADO_TAREA     + " INTEGER, " +
            COL_ID_CULTIVO          + " INTEGER, " +
            COL_FECHA_CREACION      + " TEXT, " +
            COL_ID_USUARIOS_HUERTAS + " INTEGER, " +
            "FOREIGN KEY (" + COL_ID_ESTADO_TAREA + ") REFERENCES " +
                TABLE_ESTADOS_TAREAS + "(" + COL_ID_ESTADO_TAREA + ") ON DELETE SET NULL, " +
            "FOREIGN KEY (" + COL_ID_CULTIVO + ") REFERENCES " +
                TABLE_CULTIVOS + "(" + COL_ID_CULTIVO + ") ON DELETE SET NULL, " +
            "FOREIGN KEY (" + COL_ID_USUARIOS_HUERTAS + ") REFERENCES " +
                TABLE_USUARIOS_HUERTAS + "(" + COL_ID_USUARIOS_HUERTAS + ") ON DELETE CASCADE" +
        ");";

    private static final String CREATE_PUBLICACIONES =
        "CREATE TABLE " + TABLE_PUBLICACIONES + " (" +
            COL_ID_PUBLICACION      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TITULO_POST         + " TEXT NOT NULL, " +
            COL_CONTENIDO_POST      + " TEXT, " +
            COL_FECHA_POST          + " TEXT, " +
            COL_ID_USUARIOS_HUERTAS + " INTEGER, " +
            COL_IMAGEN_BLOB         + " BLOB, " +
            COL_AUTOR_CORREO        + " TEXT, " +
            "FOREIGN KEY (" + COL_ID_USUARIOS_HUERTAS + ") REFERENCES " +
                TABLE_USUARIOS_HUERTAS + "(" + COL_ID_USUARIOS_HUERTAS + ") ON DELETE CASCADE" +
        ");";

    private static final String CREATE_SESION =
        "CREATE TABLE " + TABLE_SESION + " (" +
            COL_ID_SESION        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TOKEN            + " TEXT NOT NULL, " +
            COL_CORREO_SESION    + " TEXT NOT NULL, " +
            COL_FECHA_LOGIN      + " TEXT NOT NULL, " +
            COL_ID_USUARIO_SESION + " INTEGER NOT NULL DEFAULT 0" +
        ");";

    // ── Singleton ─────────────────────────────────────────────────────────────

    private DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(CREATE_TIPOS_USUARIOS);
        db.execSQL(CREATE_USUARIOS);
        db.execSQL(CREATE_HUERTAS);
        db.execSQL(CREATE_USUARIOS_HUERTAS);
        db.execSQL(CREATE_CULTIVOS);
        db.execSQL(CREATE_ESTADOS_TAREAS);
        db.execSQL(CREATE_TAREAS);
        db.execSQL(CREATE_PUBLICACIONES);
        db.execSQL(CREATE_SESION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLICACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTADOS_TAREAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CULTIVOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS_HUERTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HUERTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_USUARIOS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }
}
