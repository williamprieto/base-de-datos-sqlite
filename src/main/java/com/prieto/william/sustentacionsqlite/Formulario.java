package com.prieto.william.sustentacionsqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Formulario extends AppCompatActivity {

    private Button bInsertar, bActualizar, bEliminar, bConsultar,binventario;
    private EditText eCodigo, eNombre, eCantidad,eValor;

    private TextView Resultado,tvis;
    int x =0;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        bInsertar = (Button) findViewById(R.id.bInsertar);
        bActualizar = (Button) findViewById(R.id.bActualizar);
        bEliminar = (Button) findViewById(R.id.bEliminar);
        bConsultar = (Button) findViewById(R.id.bConsultar);
        binventario = (Button) findViewById(R.id.binventario);
        eCodigo = (EditText) findViewById(R.id.eCod);
        eNombre = (EditText) findViewById(R.id.eNom);
        eCantidad = (EditText) findViewById(R.id.eCantidad);
        eValor = (EditText) findViewById(R.id.eValor);

        Resultado = (TextView) findViewById(R.id.txtResultado);
        tvis = (TextView) findViewById(R.id.tvis);

        //Abrimos la base de datos 'UsuariosBD' en modo escritura
        UsuariosSQLiteHelper usuario = new UsuariosSQLiteHelper(this);
        //UsuariosSQLiteHelper usuario = new UsuariosSQLiteHelper(this, "UsuariosBD", null, 1);

        db = usuario.getWritableDatabase();
        // SQLiteDatabase db = usdbh.getWritableDatabase();

        //Ver_Tabla();

        bInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codigo = eCodigo.getText().toString();
                String nombre = eNombre.getText().toString();
                String cantidad = eCantidad.getText().toString();
                String valor = eValor.getText().toString();
                binventario.setText("Visualizar inventario");
                Resultado.setText("");
                x=0;
                if(nombre.equals("")|| cantidad.equals("") || valor.equals("")){
                    Toast.makeText(Formulario.this,"Por favor llenar los campos del producto",Toast.LENGTH_SHORT).show();
                }else {
                    if (codigo.equals("")) {
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("nombre", nombre);
                        nuevoRegistro.put("cantidad", cantidad);
                        nuevoRegistro.put("valor", valor);
                        db.insert("Usuarios", null, nuevoRegistro);
                        comunicacion(1);
                    } else {

                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("codigo", codigo);
                        nuevoRegistro.put("nombre", nombre);
                        nuevoRegistro.put("cantidad", cantidad);
                        nuevoRegistro.put("valor", valor);
                        db.insert("Usuarios", null, nuevoRegistro);
                        comunicacion(1);
                    }
                    //Ver_Tabla();
                    Visualizar_producto(1);
                }
              eCodigo.setText("");
              eNombre.setText("");
              eCantidad.setText("");
              eValor.setText("");
            }
        });
        bActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   String codigo=eCodigo.getText().toString();
                String nombre=eNombre.getText().toString();
                String cantidad=eCantidad.getText().toString();
                String valor=eValor.getText().toString();
                binventario.setText("Visualizar inventario");
                Resultado.setText("");
                x=0;
                if(nombre.equals("")|| cantidad.equals("") || valor.equals("")){
                    Toast.makeText(Formulario.this,"Por favor llenar los campos del producto a actualizar",Toast.LENGTH_SHORT).show();
                }else {
                    ContentValues nuevoValor = new ContentValues();
                    //nuevoValor.put("nombre",nombre);
                    nuevoValor.put("cantidad", cantidad);
                    nuevoValor.put("valor", valor);
                    String[] args = {nombre};
                    db.update("Usuarios", nuevoValor, "nombre=?", args);
                    comunicacion(2);
                    Visualizar_producto(2);
                    // Ver_Tabla();
                }
                eCodigo.setText("");
                eNombre.setText("");
                eCantidad.setText("");
                eValor.setText("");
            }
        });


        bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String codigo=eCodigo.getText().toString();
                //    db.delete("Usuarios", "codigo="+codigo, null);
                binventario.setText("Visualizar inventario");
                Resultado.setText("");
                x=0;
                String nombre=eNombre.getText().toString();
                if(nombre.equals("")){
                    Toast.makeText(Formulario.this," llenar el campo del nombre a eliminar",Toast.LENGTH_SHORT).show();
                }else {
                    Visualizar_producto(3);
                    String[] args = {nombre};
                    db.delete("Usuarios", "nombre=?", args);
                    comunicacion(3);
                    //Ver_Tabla();

                }
                eCodigo.setText("");
                eNombre.setText("");
                eCantidad.setText("");
                eValor.setText("");
            }
        });

        bConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binventario.setText("Visualizar inventario");
                Resultado.setText("");
                x=0;
               // String id = eCodigo.getText().toString();
                String name = eNombre.getText().toString();
                if(name.equals("")){
                    Toast.makeText(Formulario.this," llenar el campo del nombre a consultar",Toast.LENGTH_SHORT).show();
                }else {
                    String[] campos = new String[]{"codigo", "nombre", "cantidad", "valor"};
                    String[] args = new String[]{name};

                    Cursor c = db.query("Usuarios", campos, "nombre=?", args, null, null, null);

                    //Nos aseguramos de que existe al menos un registro
                    if (c.moveToFirst()) {
                        Resultado.setText("");
                        //Recorremos el cursor hasta que no encontrar mas registros
                        do {
                            String codigo = c.getString(0);
                            String nombre = c.getString(1);
                            int cantidad = c.getInt(2);
                            int valor = c.getInt(3);
                            tvis.setText("Producto encontrado");
                            Resultado.append(" " + codigo + " - " + nombre + " - " + cantidad + " - " + valor + "\n");
                        } while (c.moveToNext());
                    }
                    else{
                        Toast.makeText(Formulario.this,"Producto no existe",Toast.LENGTH_SHORT).show();
                        Resultado.setText("");
                        tvis.setText("");
                    }
                }
            }
        });
        binventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvis.setText("");
                if (x==0) {
                    Ver_Tabla();

                    binventario.setText("Ocultar inventario");
                    x=1;
                }else
                {
                    binventario.setText("Visualizar inventario");
                    Resultado.setText("");
                    x=0;
                }

            }
        });
    }
    //////////////////////////
    protected void Visualizar_producto(int o) {

        String name = eNombre.getText().toString();
        String[] campos = new String[]{"codigo", "nombre", "cantidad", "valor"};
        String[] args = new String[]{name};

        Cursor c = db.query("Usuarios", campos, "nombre=?", args, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Resultado.setText("");
            //Recorremos el cursor hasta que no encontrar mas registros
            do {
                String codigo = c.getString(0);
                String nombre = c.getString(1);
                int cantidad = c.getInt(2);
                int valor = c.getInt(3);
                switch (o){
                    case 1:
                        tvis.setText("Producto Agregado");
                          break;
                    case 2:
                        tvis.setText("Producto Actualizado");
                        break;
                    case 3:
                        tvis.setText("Producto Eliminado");
                        //Toast.makeText(MainActivity.this, "caso 3", Toast.LENGTH_SHORT).show();
                        break;
                }

                Resultado.append(" " + codigo + " - " + nombre + " - " + cantidad +" - " + valor    + "\n");
            } while (c.moveToNext());
        }else{
            Toast.makeText(Formulario.this,"Producto no existe",Toast.LENGTH_SHORT).show();
            Resultado.setText("");
            tvis.setText("");
        }
    }






 //////////////////////////////////
    protected void Ver_Tabla() {

        //PAra mostrar todos los campos de la tabla
        Cursor c = db.rawQuery("SELECT codigo,nombre,cantidad,valor FROM Usuarios", null);

        Resultado.setText("");
        if (c.moveToFirst())
            do {
                String codigo = c.getString(0);
                String nombre = c.getString(1);
                int    cantidad = c.getInt(2);
                int    valor = c.getInt(3);

                Resultado.append(" " + codigo + " -- " + nombre + " -- " + cantidad +" -- " + valor  + "\n");

            }while (c.moveToNext());
    }
//funcion para realizar la comunicacion con la actividad principal
    protected void comunicacion(int op) {
        //Nuevo Intent con Extras
        Intent backData = new Intent();

        switch (op){
            case 1:
                backData.putExtra("flag", "1");
                backData.putExtra("codN", eCodigo.getText().toString());
                backData.putExtra("nameN", eNombre.getText().toString());
                backData.putExtra("cantidaN", eCantidad.getText().toString());
                backData.putExtra("valorN", eValor.getText().toString());
                //Enviar la informacion
                setResult(RESULT_OK, backData);
                break;
            case 2:
                backData.putExtra("flag", "2");
                backData.putExtra("nameN", eNombre.getText().toString());
                backData.putExtra("cantidaN", eCantidad.getText().toString());
                backData.putExtra("valorN", eValor.getText().toString());
                setResult(RESULT_OK, backData);
                break;
            case 3:
                backData.putExtra("flag", "3");
                backData.putExtra("nameN", eNombre.getText().toString());
                setResult(RESULT_OK, backData);
                break;


        }

    }


}
