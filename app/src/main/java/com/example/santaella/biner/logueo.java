package com.example.santaella.biner;


import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class logueo extends AppCompatActivity implements View.OnClickListener{
Button reg,ac,cusuario;
EditText usucel,contraseña;
String DEBUG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo);
        reg=(Button)findViewById(R.id.cancelar);
        ac=(Button)findViewById(R.id.aceptar);
        cusuario=(Button)findViewById(R.id.cusuario);
        ac.setOnClickListener(this);
        reg.setOnClickListener(this);
        cusuario.setOnClickListener(this);
        usucel=(EditText)findViewById(R.id.usucel);
        contraseña=(EditText)findViewById(R.id.contraseñaL);

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.cancelar:
            finish();
            Intent nueva=new Intent(logueo.this,principal.class);
            startActivity(nueva);
            break;
        case R.id.aceptar:
            ac.setEnabled(false);
            if(evaluaText()){
                buscando_Usario(usucel.getText().toString());
            }
            break;
        case R.id.cusuario:
            Intent nueva2=new Intent(logueo.this,creaUsuario.class);
            startActivity(nueva2);
            break;
    }
    }
    public boolean evaluaText(){
        if(!usucel.getText().toString().equals("")){
            if(!contraseña.getText().toString().equals("")){
                Toast.makeText(this,"Conectando...",Toast.LENGTH_SHORT).show();
                ac.setEnabled(true);
                return true;
            }else{
                Toast.makeText(this,"Falta contraseña",Toast.LENGTH_SHORT).show();
                ac.setEnabled(true);
                return false;
            }
        }else{
            Toast.makeText(this,"Falta usuario",Toast.LENGTH_SHORT).show();
            ac.setEnabled(true);
            return false;
        }

    }
    //***************************************************************************************************************************************************
    public void buscando_Usario(String usuario){
        DEBUG_TAG= "https://pruebaand.000webhostapp.com/consultaUsuarios.php?usuario="+usuario;
        new DownloadWebpageTask().execute(DEBUG_TAG);

    }
    //***************************************************************************************************************************************************
    public void imprime(String aux) {
        String[] parts = aux.split(";");
        if (!parts[0].equals("")) {
            if(parts[6].equals(contraseña.getText().toString())){
                finish();
                Intent entrar=new Intent(logueo.this,MenuLogueado.class);
                entrar.putExtra("idUsuario",parts[0]);
                entrar.putExtra("usuario",parts[4]);
                startActivity(entrar);
                finish();
            }else{Toast.makeText(this,"Contraseña erronea",Toast.LENGTH_SHORT).show();}
        }else {
            ac.setEnabled(true);
            Toast.makeText(this,"Se desconoce usuario",Toast.LENGTH_SHORT).show();
        }
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
}
