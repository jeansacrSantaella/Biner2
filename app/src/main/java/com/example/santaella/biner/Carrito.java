package com.example.santaella.biner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Carrito extends AppCompatActivity {
    private ArrayList<Pedido> pedido;
    //----------------------------------------------------------------------------------------------
    private ArrayList<Pedido> pedidos;
    private ListView listaPedidos;
    private PedidoAdapter adapterPedido;
    private TextView pedidoNombre,pedidoCantidad,pedidoTotal;
    private Button enviarCarrtio,cancelarCarrito;
    String DEBUG_TAG;
    Double totalSumado;
    Bundle datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito);
        pedidos=new ArrayList<Pedido>();
        rellenarArray();
        pedidoNombre=(TextView)findViewById(R.id.pedidoNombre);
        pedidoCantidad=(TextView)findViewById(R.id.pedidoCantidad);
        pedidoTotal=(TextView)findViewById(R.id.pedidoTotal);

    }
//***************************************************************************************************************************************************
    public void crearListaPedidos(){
        listaPedidos=(ListView)findViewById(R.id.listaPedidos);
        adapterPedido= new PedidoAdapter(this,pedidos);
        listaPedidos.setAdapter(adapterPedido);

        /*FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.footerview,null);
        enviarCarrtio = (Button) footerLayout.findViewById(R.id.enviarCarrtio);
        cancelarCarrito=(Button) footerLayout.findViewById(R.id.cancelarCarrito);
        listaPedidos.addFooterView(footerLayout);*/

    }
    //***************************************************************************************************************************************************
    public void rellenarArray(){
        datos = this.getIntent().getExtras();
        String id=datos.getString("idUsuario");
        DEBUG_TAG= "https://pruebaand.000webhostapp.com/consultaPedidos.php?opcion=2&usuario="+id;
        new DownloadWebpageTask().execute(DEBUG_TAG);
    }
    //***************************************************************************************************************************************************
    public void imprime(String aux) {
        totalSumado=0.0;
        int totalCantidad=0;
        String[] parts = aux.split("-");
        if (!parts[0].equals("")) {
            for(int i=0;i<parts.length-1;i=i+3){
                    pedidos.add(new Pedido((parts[i]),Integer.parseInt(parts[i+1]),Double.parseDouble(parts[i+2])));
                    totalCantidad=totalCantidad+Integer.parseInt(parts[i+1]);
                    totalSumado=totalSumado+Double.parseDouble(parts[i+2]);
            }
            pedidos.add(new Pedido("Total a pagar\n Cantidad Pedida Total",totalCantidad,totalSumado));
            crearListaPedidos();
        }else {
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


