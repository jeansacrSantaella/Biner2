package com.example.santaella.biner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class creaUsuario extends AppCompatActivity implements OnClickListener{
EditText nombreCL,apellidoPCL,apellidoMCL,nuCL,telefonoCL,contraseñaCL;
Button aceptar,cancelar;
String DEBUG_TAG;
private int creacion=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_usuario);
        nombreCL=(EditText)findViewById(R.id.nombreCL);
        apellidoPCL=(EditText)findViewById(R.id.apellidoPCL);
        apellidoMCL=(EditText)findViewById(R.id.apellidoMCL);
        nuCL=(EditText)findViewById(R.id.nuCL);
        telefonoCL=(EditText)findViewById(R.id.telefonoCL);
        contraseñaCL=(EditText)findViewById(R.id.contraseñaCL);
        aceptar=(Button)findViewById(R.id.aceptarCL);
        cancelar=(Button)findViewById(R.id.cancelarCL);
        aceptar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelarCL:
                finish();
                Intent nueva=new Intent(creaUsuario.this,logueo.class);
                startActivity(nueva);
                break;
            case R.id.aceptarCL:
                if(nombreCL.getText().toString().equals("") || apellidoPCL.getText().toString().equals("") || apellidoMCL.getText().toString().equals("")
                        || nuCL.getText().toString().equals("") || telefonoCL.getText().toString().equals("") || contraseñaCL.getText().toString().equals(""))
                {
                    Toast.makeText(this,"Faltan datos por llenar",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    aceptar.setEnabled(false);
                    buscando_Usario(nuCL.getText().toString());
                    break;
                }
        }
    }
    //***************************************************************************************************************************************************
    //***************************************************************************************************************************************************
    public void buscando_Usario(String usuario){
        Toast.makeText(this,"Procesando...",Toast.LENGTH_SHORT).show();
        DEBUG_TAG= "https://pruebaand.000webhostapp.com/consultaUsuarios.php?usuario="+usuario;
        new DownloadWebpageTask().execute(DEBUG_TAG);

    }
    public void crear_Usuario(){
        DEBUG_TAG= "https://pruebaand.000webhostapp.com/crearUsuarios.php?" +
                "nombre="+nombreCL.getText().toString()+"&apellidoP="+apellidoPCL.getText().toString()+"&apellidoM="+apellidoMCL.getText().toString()
                +"&nombreUsuario="+nuCL.getText().toString()+"&telefono="+telefonoCL.getText().toString()
                +"&contraseña="+contraseñaCL.getText().toString()+"&tipoUsuario="+1;
        new DownloadWebpageTask1().execute(DEBUG_TAG);

    }
    //***************************************************************************************************************************************************
    public void imprime(String aux) {
        SystemClock.sleep(1000);
        String[] parts = aux.split(";");
        if (!parts[0].equals("")) {
                Toast.makeText(this,"Usuario ya ocupado",Toast.LENGTH_SHORT).show();
            aceptar.setEnabled(true);
            return;
        }else{
            crear_Usuario();
        }
    }
    public void imprime1(String aux) {
        String[] parts = aux.split(";");
            Toast.makeText(this,"Creando Usuario",Toast.LENGTH_SHORT).show();
        SystemClock.sleep(1500);
        aceptar.setEnabled(true);
        Toast.makeText(this,"Usuario Creado",Toast.LENGTH_SHORT).show();
        finish();
        Intent nuevo=new Intent(creaUsuario.this,logueo.class);
        startActivity(nuevo);
            return;
    }
    //*******************************************************************************************************************************************************
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "No se puede recuperar la página web. URL puede no ser válida.";
            }
        }

        // onPostExecute muestra el resultado de AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            imprime(result);
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milisegundos */);
            conn.setConnectTimeout(15000 /* milisegundos */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Inicia la consulta
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "La respuesta es: " + response);
            is = conn.getInputStream();
            //Para descargar la página web completa
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String webPage = "";
            String data = "";
            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
            return webPage;
            // Se asegura de que el InputStream se cierra después de la aplicación deja de usarla.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    /*----------------------------------------------------------------------------------------------*/
    private class DownloadWebpageTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "No se puede recuperar la página web. URL puede no ser válida.";
            }
        }

        // onPostExecute muestra el resultado de AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            imprime1(result);
        }
    }

    private String downloadUrl1(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milisegundos */);
            conn.setConnectTimeout(15000 /* milisegundos */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Inicia la consulta
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "La respuesta es: " + response);
            is = conn.getInputStream();
            //Para descargar la página web completa
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String webPage = "";
            String data = "";
            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
            return webPage;
            // Se asegura de que el InputStream se cierra después de la aplicación deja de usarla.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
