package com.prieto.william.sustentacionsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by willo on 02/05/2016.
 */
public class UsuariosSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME="UsuariosBD";
    private static final int DATA_VERSION=1;
    //public static final String QUOTES_TABLE_NAME = "Usuarios";

    //Sentencia SQL Para crear una tabla
    String sqlCreate = "CREATE TABLE Usuarios(codigo INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT ,cantidad INTEGER,valor INTEGER)";
    String sql_tabla_defec = "INSERT INTO Usuarios (nombre, cantidad,valor)"
                          + " VALUES('Iron_Man','10','15000'),('Viuda_Negra','10','12000')," +
                            "('Capitan_America','10','15000'),('Hulk','10','12000')," +
                            "('Bruja_Escarlata','10','15000'),('SpiderMan','10','10000')";

    String sqlCreate_gain = "CREATE TABLE Ganancias(codigogain INTEGER PRIMARY KEY AUTOINCREMENT,gananciatotal INTEGER)";
    String sql_tabla_gain = "INSERT INTO Ganancias (gananciatotal)"
                             + " VALUES('0')";
//    db.execSQL("INSERT INTO Usuarios(nombre,edad)"
  //                      + "VALUES('" + nombre + "','" + edad + "')");

    public UsuariosSQLiteHelper(Context contexto) {
        super(contexto, DATA_BASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
        //Insertar registros iniciales
        db.execSQL(sql_tabla_defec);
///////////////////

        db.execSQL(sqlCreate_gain);
        db.execSQL(sql_tabla_gain);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate_gain);
    }
}
