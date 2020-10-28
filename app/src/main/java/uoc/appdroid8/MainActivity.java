package uoc.appdroid8;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.widget.Toolbar;

import uoc.appdroid8.entidades.ConexionSQLiteHelper;

public class MainActivity extends AppCompatActivity  {
    WebView miVisorWeb;
    //URL provisional:
    String url = "https://www.puzzlepassion.com/top-10-trucos-y-consejos-para-montar-puzzles-con-exito";
    //String url = "https://sites.google.com/d/1ERbNSvMNOVMD2gLeGbo27BX8GOez_Av5/p/1kShnFwb7oE0mlawvpLsTgcLg1jBDn6lj/edit";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Conexión a a base de datos
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_appdroid8", null, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Botón para ir atrás en toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    //Abrir web
    public void helpView(View v){
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        final WebSettings ajustesVisorWeb = miVisorWeb.getSettings();
        ajustesVisorWeb.setJavaScriptEnabled(true);
        miVisorWeb.loadUrl(url);
    }

    //Impedir que el botón Atrás cierre la aplicación
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView miVisorWeb;
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (miVisorWeb.canGoBack()) {
                        miVisorWeb.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyActivity extends AppCompatActivity {
        // ...
    }

}