package dragosholban.com.androidpuzzlegame;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.DateTime;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static uoc.appdroid8.utilidades.Utilidades.CAMPO_ID;
import static uoc.appdroid8.utilidades.Utilidades.CAMPO_PUNTUACION;
import static uoc.appdroid8.utilidades.Utilidades.TABLA_PUNTUACIONES;

public class Puntuacion extends AppCompatActivity {

    ListView lv;
    ArrayList<String> puntuaciones;
    ArrayAdapter adaptador;
    private ListView listview;
    private ArrayList<String> names;
    private ArrayList<String> scores;
    TextView receiver_msg;
    private Button returnButton;
    private Button niveles;
    private Button calendario;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        calendario = findViewById(R.id.calendario);

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

        calendario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Integer punt = intent.getIntExtra("puntuacion_puzzle", 0);
                calendarEvent(punt);
            }
        });


        registrarPuntuacion(punt);
        addData(punt);
        //deleteTitle(9999);

        cargarDatos(punt);

        //calendarEvent(punt);



        String punt1 = Integer.toString(punt);

        receiver_msg = (TextView) findViewById(R.id.received_value_id);

        receiver_msg.setText(punt1);



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){

           CharSequence name = "¡New Record!";
           String description = "You earned a new record: puzle completed in 20 seconds";
           int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("record", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    private void calendarEvent(Integer punt){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Scores PuzzleDroid8");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Score: "+punt+" seconds");
        intent.putExtra("beginTime", calendar.getTimeInMillis());
                intent.putExtra("allDay", false);
                intent.putExtra("endTime", calendar.getTimeInMillis());

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            Toast.makeText(Puntuacion.this, "You don't have any calendar", Toast.LENGTH_SHORT).show();
        }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void cargarDatos(int punt) {
        String dd;
        uoc.appdroid8.entidades.ConexionSQLiteHelper conn = new uoc.appdroid8.entidades.ConexionSQLiteHelper(this, "bd_appdroid8", null, 1);
        SQLiteDatabase db2 = conn.getReadableDatabase();
        Cursor c = db2.rawQuery("select * from puntuaciones ORDER BY id ASC", null);
        int cantidad = c.getCount();
        int i = 0;
        listview = (ListView) findViewById(R.id.puntuaciones);

        names = new ArrayList<String>();
        scores = new ArrayList<String>();
        db.collection("Puntuacion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String value = document.getData().toString();
                                value = value.substring(0,value.length()-1);
                                String[] value2 = value.split("=");
                                scores.add(value2[1]+" sec");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Error getting documents.",Toast.LENGTH_SHORT).show();

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, scores);
                        listview.setAdapter(adapter);
                    }

                });

        if (c.moveToFirst()) {
            do {
                dd = c.getInt(0) + " Sec";
                names.add(dd);
            } while (c.moveToNext());

        }



        //Almacena la posición 0 del array
        int position = names.indexOf(punt+ " Seconds");

        //Comprueba que la posición es 0, es decir la máxima puntuación
        if (position==0) {
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "record")
                    .setSmallIcon(R.drawable.logobg)
                    .setContentTitle("¡New Record!")
                    .setContentText("You have achieved a new record: Puzzle completed in "+punt+" seconds")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(100, builder.build());
        }
    }


    //Registrar puntuación en base de datos
    public void onclick(View view){
        // registrarPuntuacion();
    }
    public void addData(Integer puntuacion){
        Map<String,Object> mapPuntuacion = new HashMap<>();
        mapPuntuacion.put("Puntuacion", puntuacion);


        db.collection("Puntuacion")
                .add(mapPuntuacion)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Score save in a server",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                });
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

        Toast.makeText(getApplicationContext(),"¡Puzzle Completed!",Toast.LENGTH_SHORT).show();

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