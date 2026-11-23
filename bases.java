package com.example.tablas;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TableLayout vistita;
    int clave[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    String puesto[] = {"secretaria", "director", "programador", "capturista", "test", "carpintero", "electricista", "albañil", "azulejos", "pintor"};
    String depto[] = {"programacion", "programacion", "programacion", "programacion", "conservacion", "conservacion", "conservacion", "conservacion", "conservacion", "conservacion"};
    int sueldo[] = {10000, 50000, 30000, 12000, 15000, 5000, 5000, 5000, 5000, 5000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        vistita = findViewById(R.id.tablita);

        for (int i = 0; i < 10; i++) {
            TableRow fila = new TableRow(this);
            TextView txt1 = new TextView(this);
            TextView txt2 = new TextView(this);
            TextView txt3 = new TextView(this);
            TextView txt4 = new TextView(this);

            txt3.setOnClickListener(this);
            txt1.setText(String.valueOf(clave[i]));
            txt2.setText(puesto[i]);
            txt3.setText(depto[i]);
            txt4.setText(String.valueOf(sueldo[i]));

            fila.addView(txt1);
            fila.addView(txt2);
            fila.addView(txt3);
            fila.addView(txt4);
            vistita.addView(fila);
        }
    }

    @Override
    public void onClick(View v) {
        TextView vistitaaa = ((TextView) v);
        Toast.makeText(this,
                vistitaaa.getText(),
                Toast.LENGTH_SHORT).show();
    }
}

/*
// ==========================================================
// 1) CRUD SQLITE (proyecto basedatos)
// ==========================================================

// ---------- 1.1 Base.java COMPLETO ----------
package com.example.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Base extends SQLiteOpenHelper {
    public Base(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cinito(clave int primary key, descripcion text, precio int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}


// ---------- 1.2 MainActivity.java COMPLETO (CRUD) ----------
package com.example.basedatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etcodigo, etdescripcion, etprecio;
    Button botoncito, botoncitob, botoncitoc, botoncitoco;
    Base admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etcodigo = findViewById(R.id.ecodigo);
        etdescripcion = findViewById(R.id.edescripcion);
        etprecio = findViewById(R.id.eprecio);

        botoncito = findViewById(R.id.altas);
        botoncitob = findViewById(R.id.bajas);
        botoncitoc = findViewById(R.id.cambios);
        botoncitoco = findViewById(R.id.consultas);

        botoncito.setOnClickListener(this);
        botoncitob.setOnClickListener(this);
        botoncitoc.setOnClickListener(this);
        botoncitoco.setOnClickListener(this);

        admin = new Base(this, "administracion", null, 1);
    }

    @Override
    public void onClick(View v) {
        String cadenita = ((Button) v).getText().toString();
        switch (cadenita) {
            case "altas":
                restaurarBotones();
                limpiar();
                habilitar(true, true, true);
                botoncito.setText("agregar");
                break;
            case "bajas":
                restaurarBotones();
                limpiar();
                habilitar(true, false, false);
                botoncitob.setText("eliminar");
                break;
            case "cambios":
                restaurarBotones();
                limpiar();
                habilitar(true, true, true);
                botoncitoc.setText("guardar");
                break;
            case "consultas":
                restaurarBotones();
                limpiar();
                habilitar(true, false, false);
                botoncitoco.setText("buscar");
                break;
            case "agregar":
                agregar();
                break;
            case "eliminar":
                eliminar();
                break;
            case "guardar":
                guardar();
                break;
            case "buscar":
                buscar();
                break;
        }
    }

    private void agregar() {
        String codigo = etcodigo.getText().toString();
        String descripcion = etdescripcion.getText().toString();
        String precio = etprecio.getText().toString();

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("clave", Integer.parseInt(codigo));
        registro.put("descripcion", descripcion);
        registro.put("precio", Integer.parseInt(precio));

        long resultado = db.insert("cinito", null, registro);
        db.close();

        if (resultado == -1)
            Toast.makeText(this, "Ya existe esa clave", Toast.LENGTH_LONG).show();
        else
            terminar("Producto agregado");
    }

    private void eliminar() {
        String codigo = etcodigo.getText().toString();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Escribe la clave", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = admin.getWritableDatabase();
        int cant = db.delete("cinito", "clave=?", new String[]{codigo});
        db.close();

        if (cant == 1)
            terminar("Producto eliminado");
        else
            Toast.makeText(this, "No existe ese producto", Toast.LENGTH_LONG).show();
    }

    private void guardar() {
        String codigo = etcodigo.getText().toString();
        String descripcion = etdescripcion.getText().toString();
        String precio = etprecio.getText().toString();

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("descripcion", descripcion);
        registro.put("precio", Integer.parseInt(precio));

        int cant = db.update("cinito", registro, "clave=?", new String[]{codigo});
        db.close();

        if (cant == 1)
            terminar("Producto modificado");
        else
            Toast.makeText(this, "No existe ese producto", Toast.LENGTH_LONG).show();
    }

    private void buscar() {
        String codigo = etcodigo.getText().toString();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Escribe la clave", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor fila = db.rawQuery(
                "select descripcion, precio from cinito where clave=?",
                new String[]{codigo});

        if (fila.moveToFirst()) {
            etdescripcion.setText(fila.getString(0));
            etprecio.setText(fila.getString(1));
            habilitar(false, false, false);
            restaurarBotones();
        } else {
            Toast.makeText(this, "No existe ese producto", Toast.LENGTH_LONG).show();
        }
        fila.close();
        db.close();
    }

    private void habilitar(boolean codigo, boolean descripcion, boolean precio) {
        etcodigo.setEnabled(codigo);
        etdescripcion.setEnabled(descripcion);
        etprecio.setEnabled(precio);
    }

    private void limpiar() {
        etcodigo.setText("");
        etdescripcion.setText("");
        etprecio.setText("");
    }

    private void restaurarBotones() {
        botoncito.setText("altas");
        botoncitob.setText("bajas");
        botoncitoc.setText("cambios");
        botoncitoco.setText("consultas");
    }

    private void terminar(String mensaje) {
        limpiar();
        habilitar(false, false, false);
        restaurarBotones();
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}


// ---------- 1.3 activity_main.xml COMPLETO (CRUD) ----------
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/ecodigo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:enabled="false"
        android:hint="Clave"
        android:inputType="number" />

    <EditText
        android:id="@+id/edescripcion"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:enabled="false"
        android:hint="Descripción"
        android:inputType="text" />

    <EditText
        android:id="@+id/eprecio"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:enabled="false"
        android:hint="Precio"
        android:inputType="number" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:orientation="horizontal">

        <Button
            android:id="@+id/altas"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="altas"
            android:textSize="12sp" />

        <Button
            android:id="@+id/bajas"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="bajas"
            android:textSize="12sp" />

        <Button
            android:id="@+id/cambios"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="cambios"
            android:textSize="12sp" />

        <Button
            android:id="@+id/consultas"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="consultas"
            android:textSize="12sp" />
    </LinearLayout>

</LinearLayout>


// ---------- 1.4 FRAGMENTOS RÁPIDOS DEL CRUD ----------

// Abrir y cerrar la base
Base admin = new Base(this, "administracion", null, 1);
SQLiteDatabase db = admin.getWritableDatabase();
db.close();

// ALTAS
ContentValues registro = new ContentValues();
registro.put("clave", Integer.parseInt(codigo));
registro.put("descripcion", descripcion);
registro.put("precio", Integer.parseInt(precio));
long resultado = db.insert("cinito", null, registro);
if (resultado == -1) { }   // clave repetida

// BAJAS
int cant = db.delete("cinito", "clave=?", new String[]{codigo});
if (cant == 1) { } else { }   // eliminado / no existe

// CAMBIOS
ContentValues registro = new ContentValues();
registro.put("descripcion", descripcion);
registro.put("precio", Integer.parseInt(precio));
int cant = db.update("cinito", registro, "clave=?", new String[]{codigo});

// CONSULTAS
Cursor fila = db.rawQuery("select descripcion, precio from cinito where clave=?", new String[]{codigo});
if (fila.moveToFirst()) {
    etdescripcion.setText(fila.getString(0));
    etprecio.setText(fila.getString(1));
} else {
    Toast.makeText(this, "No existe ese producto", Toast.LENGTH_LONG).show();
}
fila.close();
db.close();

// CONSULTAR TODOS
Cursor fila = db.rawQuery("select * from cinito", null);
while (fila.moveToNext()) {
    int c = fila.getInt(0);
    String d = fila.getString(1);
    int p = fila.getInt(2);
}
fila.close();

// VALIDAR VACÍOS
if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
    Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_LONG).show();
    return;
}

// HABILITAR / DESHABILITAR / LIMPIAR
etcodigo.setEnabled(true);
etdescripcion.setEnabled(false);
etcodigo.setText("");
botoncito.setText("agregar");


// ==========================================================
// 2) TABLAS DINÁMICAS (proyecto tablas)
// ==========================================================

// ---------- 2.1 activity_main.xml COMPLETO (tablas) ----------
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TableLayout
        android:id="@+id/tablita"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:stretchColumns="*" />

</ScrollView>

// ---------- 2.2 Estructura ----------
vistita.addView(fila);
fila.addView(celda);

// ---------- 2.3 Crear filas con un for ----------
for (int i = 0; i < clave.length; i++) {
    TableRow fila = new TableRow(this);
    TextView celda = new TextView(this);
    celda.setText(String.valueOf(clave[i]));
    fila.addView(celda);
    vistita.addView(fila);
}

// ---------- 2.4 Número a texto ----------
txt1.setText(String.valueOf(clave[i]));
txt4.setText(sueldo[i] + "");

// ---------- 2.5 onClick con cast ----------
TextView t = (TextView) v;
Toast.makeText(this, t.getText(), Toast.LENGTH_SHORT).show();

// ---------- 2.6 Saber qué fila se tocó ----------
txt3.setTag(i);
int fila = (int) v.getTag();
Toast.makeText(this, puesto[fila], Toast.LENGTH_SHORT).show();

// ---------- 2.7 Encabezados ----------
TableRow encabezado = new TableRow(this);
String titulos[] = {"Clave", "Puesto", "Depto", "Sueldo"};
for (int j = 0; j < titulos.length; j++) {
    TextView th = new TextView(this);
    th.setText(titulos[j]);
    encabezado.addView(th);
}
vistita.addView(encabezado);

// ---------- 2.8 Estilo de celdas ----------
celda.setPadding(16, 8, 16, 8);
celda.setTextSize(16);
celda.setBackgroundColor(Color.LTGRAY);
celda.setGravity(Gravity.CENTER);


// ==========================================================
// 3) IMPORTS QUE SE SUELEN OLVIDAR
// ==========================================================
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.view.Gravity;
import androidx.activity.EdgeToEdge;
*/
