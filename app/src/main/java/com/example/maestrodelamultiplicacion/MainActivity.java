package com.example.maestrodelamultiplicacion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.maestrodelamultiplicacion.ui.home.DialogoFecha;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.maestrodelamultiplicacion.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements DialogoFecha.onFechaSeleccionada {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*--INICIO BottomNavigation*/
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        /*--FIN BottomNavigation*/

    }

    //Llamamos al metodo con el resultado de la fecha
    @Override
    public void onResultadoFecha(GregorianCalendar c) {
        //Mostramos en el editText de la fecha, la fecha seleccionada
        EditText editFecha = (EditText) findViewById(R.id.editFecha);
        editFecha.setText(c.get(Calendar.DAY_OF_MONTH) + "/" +
                (c.get(Calendar.MONTH)+1) + "/" +
                (c.get((Calendar.YEAR))));
        String fecha = c.get(Calendar.DAY_OF_MONTH) + "/" +
                (c.get(Calendar.MONTH)+1) + "/" +
                (c.get((Calendar.YEAR)));
        guardarFecha(fecha);
        //NOTA: LOS MESES VAN NUMERADOS DEL 0 AL 11, POR ESO SUMANOS 1 AL MES
    }

    //Metodo para guardar la fecha que seleccione el usuario
    private void guardarFecha (String fecha){
        //Obtenemos una referencia a SharedPreferences asociado con la actividad actual
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        //Creamos un editor para modificar los valores en SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        //Almacenamos la fecha seleccionado con la clave "fecha_seleccionada"
        editor.putString("fecha_seleccionada", fecha);
        //Aplicar los cambios al editor (guardar los datos en SharedPreferences)
        editor.apply();
    }




}

