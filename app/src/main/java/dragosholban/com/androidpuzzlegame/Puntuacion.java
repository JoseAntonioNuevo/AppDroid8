package dragosholban.com.androidpuzzlegame;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static uoc.appdroid8.utilidades.Utilidades.CAMPO_ID;
import static uoc.appdroid8.utilidades.Utilidades.CAMPO_PUNTUACION;
import static uoc.appdroid8.utilidades.Utilidades.TABLA_PUNTUACIONES;

public class Puntuacion extends AppCompatActivity {

    ListView lv;
    ArrayList<String> puntuaciones;
    ArrayAdapter adaptador;
    private ListView listview;
    private ArrayList<String> names;
    TextView receiver_msg;
    private Button returnButton;
    private Button niveles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

        AssetManager am = getAssets();
        try {
            final String[] files  = am.list("img");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        Integer punt = intent.getIntExtra("puntuacion_puzzle", 0);

        returnButton = findViewById(R.id.reintentar);
        niveles = findViewById(R.id.niveles);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openPuzzle();
            }
        });

        niveles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNiveles();
            }
        });

        registrarPuntuacion(punt);
        //deleteTitle(9999);
        cargarDatos();

        String punt1 = Integer.toString(punt);

        receiver_msg = (TextView) findViewById(R.id.received_value_id);

        receiver_msg.setText(punt1+" segundos");

    }

    public void openNiveles() {
       Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openPuzzle(){
        Intent intent = getIntent();
        String puzletip = intent.getStringExtra("puzletip");
        intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra("assetName", puzletip);
        startActivity(intent);
    }

    private void cargarDatos(){
        String dd;
        uoc.appdroid8.entidades.ConexionSQLiteHelper conn = new uoc.appdroid8.entidades.ConexionSQLiteHelper(this, "bd_appdroid8", null, 1);
        SQLiteDatabase db= conn.getReadableDatabase();
        Cursor c = db.rawQuery("select * from puntuaciones ORDER BY id ASC", null);
        int cantidad = c.getCount();
        int i = 0;
        listview = (ListView) findViewById(R.id.puntuaciones);

        names = new ArrayList<String>();

        if (c.moveToFirst()) {
            do{
                dd = c.getInt(0) + " Segundos";
                names.add(dd);
            }while(c.moveToNext());

        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);

        /*
        String[] arreglo = new String[cantidad];
        if (c.moveToFirst()) {
            do{
                String linea = c.getInt(0) + "" + c.getString(1);
            arreglo[i] = linea;
            }while(c.moveToNext());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arreglo);
        ListView lista = (ListView) findViewById(R.id.puntuaciones);
        lista.setAdapter(adapter);
*/
    }



    //Registrar puntuación en base de datos
    public void onclick(View view){
        // registrarPuntuacion();
    }

    private void registrarPuntuacion(Integer punt){
        uoc.appdroid8.entidades.ConexionSQLiteHelper conn = new uoc.appdroid8.entidades.ConexionSQLiteHelper(this, "bd_appdroid8", null, 1);
        SQLiteDatabase db= conn.getWritableDatabase();

        //db.execSQL("DROP TABLE IF EXISTS puntuaciones");
        //db.execSQL("CREATE TABLE "+TABLA_PUNTUACIONES+" ("+CAMPO_ID+" INTEGER, "+CAMPO_PUNTUACION+" INTEGER)");

        ContentValues values = new ContentValues();
        // values.put(Utilidades.CAMPO_ID,puntuacion.getText().toString());
        values.put(CAMPO_ID,punt);
        Long idResultante=db.insert(TABLA_PUNTUACIONES, CAMPO_PUNTUACION,values);

        Toast.makeText(getApplicationContext(),"¡Puzle Completado!",Toast.LENGTH_SHORT).show();

    }

    //Borrar datos
    public void deleteTitle(Integer rowID)
    {
        uoc.appdroid8.entidades.ConexionSQLiteHelper conn = new uoc.appdroid8.entidades.ConexionSQLiteHelper(this, "bd_appdroid8", null, 1);
        SQLiteDatabase db= conn.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS puntuaciones");
        //db.execSQL("CREATE TABLE "+TABLA_PUNTUACIONES+" ("+CAMPO_ID+" INTEGER, "+CAMPO_PUNTUACION+" INTEGER)");
        db.execSQL("DELETE FROM "+TABLA_PUNTUACIONES+" WHERE "+CAMPO_ID+" != "+rowID);
        //db.delete(Utilidades.TABLA_PUNTUACIONES, Utilidades.CAMPO_PUNTUACION + "!=" + rowID, null);
    }

}