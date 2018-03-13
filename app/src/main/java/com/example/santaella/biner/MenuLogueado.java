package com.example.santaella.biner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
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
import java.util.Date;

public class MenuLogueado extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String DEBUG_TAG;
    Bundle datos;
    private ArrayList<Pedido> pedido;
    //----------------------------------------------------------------------------------------------
    private ArrayList<Platillo> platillos;
    private ListView lista;
    private PlatilloAdapter adapter;
    private TextView textoPrincipa,avisoSesion;
    String nombredeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_logueado);

        platillos=new ArrayList<Platillo>();
        rellenarArray();
        Toast.makeText(this,"Cargando..",Toast.LENGTH_SHORT).show();
        textoPrincipa=(TextView) findViewById(R.id.texto_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        avisoSesion=(TextView)hView.findViewById(R.id.avisoSesion);
        datos = this.getIntent().getExtras();
        nombredeUsuario=datos.getString("usuario");
        avisoSesion.setText("Bienvenido "+nombredeUsuario);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logueado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                crearListaPlatillos();
                break;
            case R.id.carrito:
                    /*Date horaActual=new Date();
                    String curDateTime=""+horaActual.getHours();
                    String dia=""+horaActual.getDay();*/
                    Toast.makeText(this,"Da Clic para eliminar pedido",Toast.LENGTH_LONG).show();
                    Intent carrito=new Intent(MenuLogueado.this,Carrito.class);
                     carrito.putExtra("idUsuario",datos.getString("idUsuario"));
                    startActivity(carrito);
                break;
            case R.id.hservicio:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*----------------------------------------------------------------------------------------------------*/
    public void crearListaPlatillos(){
        lista=(ListView)findViewById(R.id.mi_lista);
        adapter= new PlatilloAdapter(this,platillos);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date horaActual=new Date();
                String curDateTime=""+horaActual.getHours();
                String idUsuario=datos.getString("idUsuario");
                int hora=horaActual.getHours();
                if(hora<10){
                    Toast.makeText(getApplicationContext(),"Recuerda el servicio inicia a las 10", Toast.LENGTH_SHORT).show();
                    //}else if (hora>16) {
                    //  Toast.makeText(getApplicationContext(), "Recuerda el servicio inicia a las 10", Toast.LENGTH_SHORT).show();
                }else{
                    Intent descripcion=new Intent(MenuLogueado.this,vistaProducto.class);
                    descripcion.putExtra("idUsuario",datos.getString("idUsuario"));//id del usuario);
                    descripcion.putExtra("nombreU",nombredeUsuario);
                    descripcion.putExtra("Nombre",platillos.get(position).getNombre());
                    descripcion.putExtra("imagen",position);
                    descripcion.putExtra("descripcion",platillos.get(position).getDescripcion());
                    startActivity(descripcion);
                }
            }
        });

    }
    //***************************************************************************************************************************************************
    public void rellenarArray(){
        Date horaActual=new Date();
        int dia=horaActual.getDay();
        DEBUG_TAG= "https://pruebaand.000webhostapp.com/consultaProductos.php?dia="+dia;
        new DownloadWebpageTask().execute(DEBUG_TAG);
    }
    //***************************************************************************************************************************************************
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
    //***************************************************************************************************************************************************
    public void imprime(String aux) {
        String[] parts = aux.split("-");
        if (!parts[0].equals("")) {
            int contador=1,auxImagen=0;
            for(int i=0;i<parts.length-1;i++){
                if(contador==i){
                    platillos.add(new Platillo((parts[i]),getImagen(auxImagen),parts[i+1]));
                    contador=contador+11;
                    auxImagen++;
                }
            }
            crearListaPlatillos();
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
