package dragosholban.com.androidpuzzlegame;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "";
    String mCurrentPhotoPath;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 3;
    static final int REQUEST_IMAGE_GALLERY = 4;
    int nivelgame = 1;

    Button play_pause, btn_repetir;
    public MediaPlayer mp;
    int repetir = 2, posicion = 0;
    MediaPlayer vectormp[] = new MediaPlayer[3];

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    public StorageReference mStorageRef;
    public StorageReference mStorageRef2;
    public StorageReference mStorageRef3;
    public String ruta_jaws;
    public String ruta_empires;
    public String ruta_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("20930305024-h2fb6et223n3kemm6f7k5tlhmpk1nnci.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("/jaws_firebase.jpg");
        mStorageRef2 = FirebaseStorage.getInstance().getReference().child("/empire_firebase.jpg");
        mStorageRef3 = FirebaseStorage.getInstance().getReference().child("/back_firebase.jpg");
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        ;
        play_pause = (Button) findViewById(R.id.btn_play);
        btn_repetir = (Button) findViewById(R.id.btn_repetir);

        vectormp[0] = MediaPlayer.create(this, R.raw.long_song);
        vectormp[1] = MediaPlayer.create(this, R.raw.c2c);
        vectormp[2] = MediaPlayer.create(this, R.raw.mind);

        AssetManager am = getAssets();


        mp = MediaPlayer.create(this, R.raw.long_song);
        mp.start();
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPh, PhoneStateListener.LISTEN_CALL_STATE);
        AudioManager mAudioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        AudioFocusChangeListenerImpl mAudioFocusChange= new AudioFocusChangeListenerImpl();
        int result = mAudioManager.requestAudioFocus(mAudioFocusChange, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        try {
            final String[] files = am.list("img");

            GridView grid = findViewById(R.id.grid);
            grid.setAdapter(new ImageAdapter(this));
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            PuzzleActivity.nivel = 1;
                            break;
                        case 1:
                            PuzzleActivity.nivel = 2;
                            break;
                        case 2:
                            PuzzleActivity.nivel = 3;
                            break;
                        case 3:
                            PuzzleActivity.nivel = 4;
                            break;
                        case 4:
                            PuzzleActivity.nivel = 4;
                            break;
                    }
                    Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);

                    intent.putExtra("assetName", files[i % files.length]);
                    startActivity(intent);
                }

            });
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        }
        nivelgame = nivelgame + 1;
    }
    public void puzzle_jaws(View view){

        PuzzleActivity.nivel = 3;
        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra("mCurrentPhotoUri", ruta_jaws);
        startActivity(intent);
    }
    public void puzzle_back(View view){

        PuzzleActivity.nivel = 3;
        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra("mCurrentPhotoUri", ruta_back);
        startActivity(intent);
    }
    public void puzzle_empire(View view){

        PuzzleActivity.nivel = 3;
        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra("mCurrentPhotoUri", ruta_empires);
        startActivity(intent);
    }
    public void onhelpbutton(View view) {
        Intent intent = new Intent(this, Web.class);

        startActivity(intent);
    }

    public void onImageFromCameraClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted, initiate request
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        } else {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            mCurrentPhotoPath = image.getAbsolutePath(); // save this to use in the intent

            return image;
        }

        return null;
    }

    public void loagImagesFirebase_jaws(){

        try {
            final File localFile = File.createTempFile("jaws_firebase", "jpg");
            ruta_jaws= localFile.getAbsolutePath();
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.imagejaws)).setImageBitmap(bitmap);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( MainActivity.this, "Foto No Cargada "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loagImagesFirebase_empires(){

        try {
            final File localFile = File.createTempFile("empire_firebase", "jpg");
            ruta_empires= localFile.getAbsolutePath();
            mStorageRef2.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.imageempire)).setImageBitmap(bitmap);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( MainActivity.this, "Foto No Cargada "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loagImagesFirebase_back(){

        try {
            final File localFile = File.createTempFile("back_firebase", "jpg");
            ruta_back= localFile.getAbsolutePath();
            mStorageRef3.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.imageback)).setImageBitmap(bitmap);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( MainActivity.this, "Foto No Cargada "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onImageFromCameraClick(new View(this));
                }

                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(getApplicationContext(),"Google sign in",Toast.LENGTH_LONG).show();
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, PuzzleActivity.class);
            PuzzleActivity.nivel = 3;
            intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
            startActivity(intent);
        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            PuzzleActivity.nivel = 3;
            Intent intent = new Intent(this, PuzzleActivity.class);
            intent.putExtra("mCurrentPhotoUri", uri.toString());
            startActivity(intent);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"signInWithCredential:failure",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        loagImagesFirebase_empires();
        loagImagesFirebase_jaws();
        loagImagesFirebase_back();
    }

    public void onImageFromGalleryClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        }
    }
    //funciones para la música en el juego

    public void Volver(View view) {
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
    }

    public void PlayPause(View view) {
        mp.stop();
        if (vectormp[posicion].isPlaying()) {
            vectormp[posicion].pause();
            play_pause.setBackgroundResource(R.drawable.reproducir);
            Toast.makeText(this, "Pausa", Toast.LENGTH_SHORT).show();
        } else {
            vectormp[posicion].start();
            play_pause.setBackgroundResource(R.drawable.pausa);
            Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();
        }
    }

    public void Stop(View view) {
        mp.stop();
        if (vectormp[posicion] != null) {
            vectormp[posicion].stop();

            vectormp[0] = MediaPlayer.create(this, R.raw.long_song);
            vectormp[1] = MediaPlayer.create(this, R.raw.c2c);
            vectormp[2] = MediaPlayer.create(this, R.raw.mind);
            posicion = 0;
            play_pause.setBackgroundResource(R.drawable.reproducir);
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();

        }
    }

    public void Repetir(View view) {
        if (repetir == 1) {
            btn_repetir.setBackgroundResource(R.drawable.no_repetir);
            Toast.makeText(this, "No repetir", Toast.LENGTH_SHORT).show();
            vectormp[posicion].setLooping(false);
            repetir = 2;
        } else {
            btn_repetir.setBackgroundResource(R.drawable.repetir);
            Toast.makeText(this, "Repetir", Toast.LENGTH_SHORT).show();
            vectormp[posicion].setLooping(true);
            repetir = 1;
        }
    }

    public void Siguiente(View view) {
        if (posicion < vectormp.length - 1) {
            if (vectormp[posicion].isPlaying()) {
                vectormp[posicion].stop();
                posicion++;
                vectormp[posicion].start();
            } else {
                posicion++;
            }

        } else {
            Toast.makeText(this, "No hay mas canciones", Toast.LENGTH_SHORT).show();
        }
    }

    public void Anterior(View view) {
        if (posicion >= 1) {
            if (vectormp[posicion].isPlaying()) {
                vectormp[posicion].stop();
                vectormp[0] = MediaPlayer.create(this, R.raw.long_song);
                vectormp[1] = MediaPlayer.create(this, R.raw.c2c);
                vectormp[2] = MediaPlayer.create(this, R.raw.mind);
                posicion--;
                vectormp[posicion].start();
            } else {
                posicion--;
            }

        } else {
            Toast.makeText(this, "No hay mas canciones", Toast.LENGTH_SHORT).show();
        }

    }

    private final PhoneStateListener mPh = new PhoneStateListener() {
        // @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        vectormp[posicion].pause();
                        mp.pause();
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        vectormp[posicion].start();
                        mp.start();
                        break;
                    default:
                }
            } catch (Exception e) {


            }
        }
    };
    
    private class AudioFocusChangeListenerImpl implements AudioManager.OnAudioFocusChangeListener{
        @Override
        public void onAudioFocusChange(int focusChange){
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    vectormp[posicion].start();
                    mp.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    vectormp[posicion].pause();
                    mp.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    vectormp[posicion].pause();
                    mp.pause();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    vectormp[posicion].pause();
                    mp.pause();
                    break;
            }
        }
    }

}
