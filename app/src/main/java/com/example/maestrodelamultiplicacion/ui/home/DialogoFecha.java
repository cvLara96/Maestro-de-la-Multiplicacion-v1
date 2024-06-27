package com.example.maestrodelamultiplicacion.ui.home;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

//Indicaremos que esta clase hereda de DialogFragment y tambien
//implementaremos la interfaz DatePickerDialog.OnDateSetListener
public class DialogoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //Ahora en HomeFragment en el metodo onClickFecha ya podremos hacer lo
    //siguiente:
    //DialogoFecha df = new DialogoFecha();
    //df.show(getSupportFragmentManager(), "Mi dialogo fecha");
    //es decir, invocar al metodo .show() que mostrara lo que definamos en el Dialogo

    //------------------------------------

    //Necesitaremos el metodo onAttach para volver a la actividad principal:
    //Crearemos una referencia a la interfaz onFechaSeleccionada
    onFechaSeleccionada actividad;

    @Override
    public void onAttach(Context context) {
        //Con esto conseguimos una referencia para despues poder hacer
        //el callback en el metodo onDateSet()
        try {
            actividad = (onFechaSeleccionada) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " debe implementar onFechaSeleccionada");
        }

        super.onAttach(context);
    }

    //------------------------------------
    //Sobreescribimos el metodo onCreateDialog

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Crearemos un dialogo y lo retornaremos a la funcion que lo
        //llama, que en este caso sera .show()

        //Queremos que nuestro dialogo muestre la fecha actual, para ello
        //crearemos un objeto de tipo Calendar
        Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR); //Extrae el año del objeto Calendar
        int mes = c.get(Calendar.MONTH); //Extrae el mes del objeto Calendar
        int dia = c.get(Calendar.DAY_OF_MONTH); //Extrae el dia del objeto Calendar

        //-- Creacion del dialogo
        //  - parametro contexto: usamos getActivity() que obtiene una referencia
        //      a la actividad dueña del fragmento
        //  - interfaz de callback, utilizamos this, para que esto funcione deberemos
        //      haber implementado la interfaz DatePickerDialog.OnDateSetListener
        //  - año,mes y dia: pasaremos anio, mes y dia creados anteriormente
        DatePickerDialog d = new DatePickerDialog(getActivity(), this, anio, mes, dia);

        //retornamos el dialogo creado
        return d;
    }

    //-------------------------------
    //Para poder asignarle una funcion a onDateSet debe ser a traves de una interfaz
    //la cual tendra un metodo que recibe como parametro un calendario Gregoriano
    //Esta interfaz deberemos implementarla en MAINACTIVITY, NO EN HOMEFRAGMENT
    public interface onFechaSeleccionada{
        void onResultadoFecha(GregorianCalendar c);
    }

    //--------------------------------
    //Cuando el usuario haya seleccionado una fecha:
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Construiremos un calendario gregoriano con los parametros que recibe
        //este metodo:
        GregorianCalendar c = new GregorianCalendar(year, month, dayOfMonth);
        //Ahora debemos retornar el objeto GregorianCalendar a la actividad principal
        //Para ello utilizaremos la actividad referenciada en el metodo onAttach
        //pasandole como parametro el objeto GregorianCalendar
        actividad.onResultadoFecha(c);

    }
}
