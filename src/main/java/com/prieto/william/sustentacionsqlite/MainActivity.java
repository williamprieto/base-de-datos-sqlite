package com.prieto.william.sustentacionsqlite;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView Resultado,txcod,txcan,txvalor,txga,txpesos,titulo;

    private Button bventa, bganancia,bconfive;
    private EditText ecanven,eID;


    SQLiteDatabase db;
    SQLiteDatabase dbga;
    int x =0;
    int ganacia_actual=0;
    String ganan_actual_tabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resultado = (TextView) findViewById(R.id.txtResultado);
        txcod = (TextView) findViewById(R.id.txcod);
        txcan = (TextView) findViewById(R.id.txcan);
        bventa = (Button) findViewById(R.id.bventa);
        bganancia = (Button) findViewById(R.id.bganancia);
        bconfive = (Button) findViewById(R.id.bconfive);
        ecanven = (EditText) findViewById(R.id.ecanven);
        eID = (EditText) findViewById(R.id.eID);

        txvalor = (TextView) findViewById(R.id.txvalor);
        txga = (TextView) findViewById(R.id.txga);
        txpesos = (TextView) findViewById(R.id.txpesos);
        //Abrimos la base de datos 'UsuariosBD' en modo escritura
        UsuariosSQLiteHelper usuario = new UsuariosSQLiteHelper(this);
        UsuariosSQLiteHelper ganacias = new UsuariosSQLiteHelper(this);
        //UsuariosSQLiteHelper usuario = new UsuariosSQLiteHelper(this, "UsuariosBD", null, 1);


        db = usuario.getWritableDatabase();
        dbga = ganacias.getWritableDatabase();
        // SQLiteDatabase db = usdbh.getWritableDatabase();

        Ver_Tabla();

        ////////////////////////////////
        bventa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txcod.setVisibility(View.VISIBLE); eID.setVisibility(View.VISIBLE);
                txcan.setVisibility(View.VISIBLE);ecanven.setVisibility(View.VISIBLE);
                bconfive.setVisibility(View.VISIBLE);
                txga.setVisibility(View.INVISIBLE);
                txpesos.setVisibility(View.INVISIBLE);
                txvalor.setVisibility(View.INVISIBLE);
                eID.setText("");
                ecanven.setText("");
                bganancia.setText("Ganancias");
                x=0;

            }
        });

        bconfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   String codigo=eCodigo.getText().toString();
                String cod=eID.getText().toString();
                String cantidad=ecanven.getText().toString();
                if(cod.equals("")|| cantidad.equals("") ){
                    Toast.makeText(MainActivity.this,"Por favor llenar los campos ",Toast.LENGTH_SHORT).show();
                }else {
                realizar_venta(cod, cantidad);
                }
            }
        });
        bganancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txcod.setVisibility(View.INVISIBLE); eID.setVisibility(View.INVISIBLE);
                txcan.setVisibility(View.INVISIBLE);ecanven.setVisibility(View.INVISIBLE);
                bconfive.setVisibility(View.INVISIBLE);
                eID.setText("");
                ecanven.setText("");
                if (x==0) {
                bganancia.setText("Ocultar Gan.");
                txga.setVisibility(View.VISIBLE);
                txpesos.setVisibility(View.VISIBLE);
                txvalor.setVisibility(View.VISIBLE);
                    String codgain = "1";
                    String[] camposs = new String[]{"codigogain", "gananciatotal"};
                    String[] argss = new String[]{codgain};

                    Cursor cc = dbga.query("Ganancias", camposs, "codigogain=?", argss, null, null, null);

                    ///////////
                    if (cc.moveToFirst()) {
                        do {
                            String  aux_g = cc.getString(1);
                            txvalor.setText(aux_g );
                        } while (cc.moveToNext());
                    }
                    x=1;
                }else {
                    bganancia.setText("Ganancias");
                    txga.setVisibility(View.INVISIBLE);
                    txpesos.setVisibility(View.INVISIBLE);
                    txvalor.setVisibility(View.INVISIBLE);
                    x=0;
                }
                }

        });
    }



    protected void Ver_Tabla() {
        //PAra mostrar todos los campos de la tabla

        txcod.setVisibility(View.INVISIBLE); eID.setVisibility(View.INVISIBLE);
        txcan.setVisibility(View.INVISIBLE);ecanven.setVisibility(View.INVISIBLE);
        bconfive.setVisibility(View.INVISIBLE);
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


    //---------------BOTON DE MENU MAIN 2-------------------------
    //creacion el menú configurar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);//recurso
        return super.onCreateOptionsMenu(menu);
    }
    //dandolo la funcionalidad al menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.menu_Operaciones){
            // Toast.makeText(this, "Presionó configurar",Toast.LENGTH_SHORT).show();
            //EL INTENT PERMITE COMPARTIR INFORMACION ENTRE ACTIVIDADES
            Intent intent = new Intent(this,Formulario.class);

            //startActivity(intent);
            startActivityForResult(intent, 1234);
            limpiar ();
        }

        return super.onOptionsItemSelected(item);
    }
    //obteniendo los nuevos datos los cuales fueron modificados en el main 22
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String   codigo,nombre,cantidad,valor,band;
        int flag_me;

        if (requestCode==1234 && resultCode==RESULT_OK){

            band = data.getStringExtra("flag");
            flag_me=Integer.parseInt(band);
            codigo = data.getStringExtra("codN");
            nombre = data.getStringExtra("nameN");
            cantidad = data.getStringExtra("cantidaN");
            valor = data.getStringExtra("valorN");

            switch (flag_me){
                case 1:
                    //Toast.makeText(MainActivity.this, "caso 1", Toast.LENGTH_SHORT).show();
                    agregar(codigo, nombre, cantidad, valor);

                    break;
                case 2:
                   // Toast.makeText(MainActivity.this, "caso 2", Toast.LENGTH_SHORT).show();
                    actuali(nombre, cantidad, valor);

                    break;
                case 3:
                   // Toast.makeText(MainActivity.this, "caso 3", Toast.LENGTH_SHORT).show();
                    elimi(nombre);

                    break;
            }
        }
    }

    public void agregar (String codigo,String nombre,String cantidad,String valor){
                Toast.makeText(MainActivity.this, "Producto agregado", Toast.LENGTH_SHORT).show();
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("codigo", codigo);
                nuevoRegistro.put("nombre", nombre);
                nuevoRegistro.put("cantidad", cantidad);
                nuevoRegistro.put("valor", valor);
                db.insert("Usuarios", null, nuevoRegistro);
                Ver_Tabla();
    }

    public void actuali (String nombre,String cantidad,String valor){
            ContentValues nuevoValor = new ContentValues();
            //nuevoValor.put("nombre",nombre);
            nuevoValor.put("cantidad",cantidad);
            nuevoValor.put("valor", valor);
            String[] args = {nombre};
            db.update("Usuarios", nuevoValor, "nombre=?", args);

            Ver_Tabla();
    }
    //////////////////////////////////////////////////////////////
    public void actuliza_gain (int  ganacia_actual){
        //////////

        String codgain = "1";
        String[] camposs = new String[]{"codigogain", "gananciatotal"};
        String[] argss = new String[]{codgain};

        Cursor cc = dbga.query("Ganancias", camposs, "codigogain=?", argss, null, null, null);
        ///////////
        if (cc.moveToFirst()) {
            do {
                ganan_actual_tabla = cc.getString(1);

            } while (cc.moveToNext());
        }
        int  aux_gain_total = Integer.parseInt(ganan_actual_tabla)+ganacia_actual;

        ContentValues nuevoValorgain = new ContentValues();
        //nuevoValor.put("nombre",nombre);
        nuevoValorgain.put("gananciatotal",  aux_gain_total);
        String codigo = "1";
        dbga.update("Ganancias", nuevoValorgain, "codigogain=" + codigo, null);
       // txvalor.setVisibility(View.VISIBLE);
     //   txvalor.setText(String.valueOf(aux_gain_total));

    ////////////////////////////////////////////////////////
    }
    public void elimi (String nombre){
        //nuevoValor.put("nombre",nombre);
        String[]args={nombre};
        db.delete("Usuarios", "nombre=?", args);
        Ver_Tabla();
    }
    public void realizar_venta (String id,String cant_vent){

                String[] campos = new String[]{"codigo", "nombre", "cantidad", "valor"};
                String[] args = new String[]{id};
                Cursor c = db.query("Usuarios", campos, "codigo=?", args, null, null, null);

                //Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    //Resultado.setText("");

                    //Recorremos el cursor hasta que no encontrar mas registros
                    do {

                        //se obtienen los datos
                        String nombre = c.getString(1);
                        int cantidad_actual = c.getInt(2);
                        int valor = c.getInt(3);

                        int  vent = Integer.parseInt(cant_vent);

                        if (vent<=cantidad_actual){
                            int nueva_cantidad = cantidad_actual - vent;
                            //////////////////////////////////
                            if(nueva_cantidad<=5){
                                //Toast.makeText(MainActivity.this," poca existencia del producto",Toast.LENGTH_SHORT).show();
                                notificar(nombre,nueva_cantidad);
                            }
                            ganacia_actual=vent*valor;
                            ///////////////////////////////
                            String  val=String.valueOf(valor);
                            String  canti=String.valueOf(nueva_cantidad);

                            actuali(nombre, canti, val);
                            Toast.makeText(MainActivity.this," Venta exitosa ",Toast.LENGTH_SHORT).show();
                            actuliza_gain(ganacia_actual);
                        }else{
                            Toast.makeText(MainActivity.this," el producto no tiene esa cantidad  ",Toast.LENGTH_SHORT).show();
                        }

                        /////////////////////////////
                      //

                    } while (c.moveToNext());
                }else{
                    Toast.makeText(MainActivity.this," codigo del producto no existe ",Toast.LENGTH_SHORT).show();
                }

        }
    public void limpiar (){
        bganancia.setText("Ganancias");
        txga.setVisibility(View.INVISIBLE);
        txpesos.setVisibility(View.INVISIBLE);
        txvalor.setVisibility(View.INVISIBLE);
        txcod.setVisibility(View.INVISIBLE); eID.setVisibility(View.INVISIBLE);
        txcan.setVisibility(View.INVISIBLE);ecanven.setVisibility(View.INVISIBLE);
        bconfive.setVisibility(View.INVISIBLE);
        x=0;
    }
    public void notificar (String nombre,int cantidad){


        NotificationCompat.Builder builder = new NotificationCompat.
                Builder(getApplicationContext());

        builder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setLargeIcon((((BitmapDrawable) getResources()
                .getDrawable(R.mipmap.ic_launcher)).getBitmap()))
                .setContentTitle(" Alerta! Poca existencia.")
                .setContentText(nombre)
                .setContentInfo(String.valueOf(cantidad))
                .setTicker("Alerta!")
                .setAutoCancel(true);

        Intent noIntent = new Intent(MainActivity.this, MainActivity.class);

        //

        PendingIntent contIntent = PendingIntent.
                getActivity(MainActivity.this, 0, noIntent,0);

        builder.setContentIntent(contIntent);

        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        nm.notify(1, builder.build());

    }

    }










