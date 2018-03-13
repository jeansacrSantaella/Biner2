package com.example.santaella.biner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class vistaProducto extends AppCompatActivity  implements View.OnClickListener{
    ImageView imagenProducto;
    EditText dproducto;
    TextView nproducto;
    Spinner spinner;
    Button aceptarP,cancelarP;
    Bundle datos;
    String DEBUG_TAG;
    int cantidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_producto);
         datos = this.getIntent().getExtras();
        /*-----------------------------------------------------------------------------------------*/

        nproducto=(TextView)findViewById(R.id.nProducto);
        nproducto.setText(datos.getString("Nombre"));
        imagenProducto=(ImageView)findViewById(R.id.imgProducto);
        imagenProducto.setImageResource(getImagen(datos.getInt("imagen")));
        dproducto=(EditText)findViewById(R.id.dProducto);
        dproducto.setText(datos.getString("descripcion"));

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                //Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });

        aceptarP=(Button)findViewById(R.id.aceptarP);
        aceptarP.setOnClickListener(this);
        cancelarP=(Button)findViewById(R.id.cancelarP);
        cancelarP.setOnClickListener(this);
        if (datos.getString("nombreU")==null) {
            aceptarP.setEnabled(false);
            Toast.makeText(this, "Necesita Logueo \npara realizar pedidos", Toast.LENGTH_SHORT).show();
        }

    }
    public Integer getImagen(int valorId){
        int[] imgid= {
                R.drawable.platillo1,
                R.drawable.platillo2,
                R.drawable.platillo3,
                R.drawable.platillo4,
                R.drawable.platillo5,
                R.drawable.platillo6,
        };
        return imgid[valorId];
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.cancelarP:
            finish();
            break;
        case R.id.aceptarP:
                crear_Pedido();
            break;
    }
    }
    //***************************************************************************************************************************************************
    public void crear_Pedido(){
        Date horaActual=new Date();
        String hora=""+horaActual.getHours()+":"+horaActual.getMinutes();
        Toast.makeText(this, "Consultando disponibilidad", Toast.LENGTH_SHORT).show();
        https://pruebaand.000webhostapp.com/creaPedido.php?idP=1&idU=1&hora=10:00&latitud=0.232323&longitud=1.121&cantidad=10&estado=1
        DEBUG_TAG= "https://pruebaand.000webhostapp.com/creaPedido.php?nombreProducto="+datos.getString("Nombre")+
                                                                "&idU="+Integer.parseInt(datos.getString("idUsuario"))+
                                                                "&hora="+hora+"&latitud=0.0000&longitud=0.0000&cantidad="+Integer.parseInt(spinner.getSelectedItem().toString())+
                                                                "&estado=1";
        Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
        new DownloadWebpageTask().execute(DEBUG_TAG);

    }
    //***************************************************************************************************************************************************
    public void imprime(String aux) {
        Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();

        String[] parts = aux.split(";");
        if (!parts[0].equals("")) {
            Toast.makeText(this, "..", Toast.LENGTH_SHORT).show();

            Toast.makeText(this,parts[0],Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Error en el servidor",Toast.LENGTH_SHORT).show();

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
