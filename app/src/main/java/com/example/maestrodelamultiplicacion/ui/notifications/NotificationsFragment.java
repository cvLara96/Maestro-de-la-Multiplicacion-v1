package com.example.maestrodelamultiplicacion.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.maestrodelamultiplicacion.R;
import com.example.maestrodelamultiplicacion.databinding.FragmentNotificationsBinding;
import com.example.maestrodelamultiplicacion.ui.dashboard.Multiplicacion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    TextView textFecha;
    TextView textEstadisticas;
    Button btnEnviar;


    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*-- INICIO asigncacion fecha*/
         textFecha = (TextView) binding.textFecha;
         textFecha.setText(recuperarFecha());
        /*-- FIN asigncacion fecha*/

        /*-- INICIO ArrayList de operaciones erroneas*/
        ArrayList<Multiplicacion> errores = recuperarErroneas();
        /*-- FIN ArrayList de operaciones erroneas*/

        /*-- INICIO asignacion textView*/
        textEstadisticas = (TextView) binding.estadisticas;
        textEstadisticas.setText("ESTADISTICAS:\n\n" +
                "- Tabla del " + recuperarFactor() + " completada" +
                "\n- Dificultad seleccionada: " + recuperarDificultad() + "\n"+
                "- Nº de aciertos: " + recuperarAciertos() + "\n" +
                "- Nº de fallos: " + recuperarFallos() + "\n" +
                getOperaciones(errores) +
                "\n\nPorcentaje de aciertos del " + promedio(recuperarAciertos()) + "%");
        cargarInsignia(recuperarAciertos(), recuperarAvatar());
        /*-- FIN asignacion textView*/

        /*-- Asignamos el metodo enviarMail al boton enviar*/
        btnEnviar = (Button) binding.btnEnviar;
        btnEnviar.setOnClickListener(this::enviarMail);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Metodo para enviar el correo
    public void enviarMail (View v){

        //En primer lugar, se crea el objeto Intent:
        Intent i = new Intent();

        //Creamos un chooser y lo igualamos a null:
        //El chooser es el mecanismo por el cual Android permite al usuario elegir una aplicación de entre las
        //posibles candidatas a tratar la petición que envía el intent
        Intent chooser = null;

        //A continuacion creamos el editText donde escribimos la direccion de correo
        EditText mail = (EditText) binding.editTextEmail;
        //Asignamos la accion al intent implicito
            //ACTION_SEND -> (enviar)
        i.setAction(Intent.ACTION_SEND);
        //Le indicamos el valor de lo que debera buscar, al tratarse de un
        //email, indicaremos mailto: en el setData() y luego mediante .putExtra()
        //indicaremos el resto de campos:
        i.setData(Uri.parse("mailto:"));
        //Creamos el "para" del mail
        String para [] = {mail.getText().toString()};
        //Creamos el asunto
        i.putExtra(Intent.EXTRA_EMAIL, para);
        i.putExtra(Intent.EXTRA_SUBJECT, "Estadísticas maestro de la multiplicación"); //Indicamos un asunto por defecto
        i.putExtra(Intent.EXTRA_TEXT, textEstadisticas.getText().toString() + "\nFecha de realización: " + recuperarFecha()); //Indicamos un mensaje por defecto
        //Para enviar un email hay
        //que indicar que el tipo corresponde al MIME especificado en la RFC 822 --> ("message/rfc822")
        i.setType("message/rfc822");
        //Creamos el chooser
        chooser = Intent.createChooser(i, "Enviar email");
        //Iniciamos la actividad, al no esperar un resultado, sera con startActivity()
        startActivity(i);
        //Lanzamos una tostada a modo de informacion:
        Toast.makeText(getContext(),"Envia el email!", Toast.LENGTH_LONG).show();
    }

    //Metodo para calcular el promedio de aciertos
    private double promedio (int aciertos){

        double promedio = 0;

        promedio = (aciertos * 100)/11;

        return promedio;
    }

    //Metodo para cargar el avatar completo en caso de 11 aciertos:
    private void cargarInsignia(int aciertos, String avatar){

        ImageView imagenInsignia = (ImageView) binding.imageInsignia;
        TextView textoInsignia = (TextView) binding.textInsignia;

        if(aciertos == 11){
            switch(avatar){
                case "Black Panther":
                    imagenInsignia.setVisibility(View.VISIBLE);
                    imagenInsignia.setImageResource(R.drawable.icon_black);
                    textoInsignia.setVisibility(View.VISIBLE);
                    textoInsignia.setText("¡INSIGNIA DE BLACK PANTHER CONSEGUIDA!");
                    break;
                case "Capitan America":
                    imagenInsignia.setVisibility(View.VISIBLE);
                    imagenInsignia.setImageResource(R.drawable.icon_capi);
                    textoInsignia.setVisibility(View.VISIBLE);
                    textoInsignia.setText("¡INSIGNIA DEL CAPITÁN AMÉRICA CONSEGUIDA!");
                    break;
                case "Hulk":
                    imagenInsignia.setVisibility(View.VISIBLE);
                    imagenInsignia.setImageResource(R.drawable.icon_hulk);
                    textoInsignia.setVisibility(View.VISIBLE);
                    textoInsignia.setText("¡INSIGNIA DE HULK CONSEGUIDA!");
                    break;
                case "Iron-Man":
                    imagenInsignia.setVisibility(View.VISIBLE);
                    imagenInsignia.setImageResource(R.drawable.icon_iron);
                    textoInsignia.setVisibility(View.VISIBLE);
                    textoInsignia.setText("¡INSIGNIA DE IRON-MAN CONSEGUIDA!");
                    break;
                case "Spiderman":
                    imagenInsignia.setVisibility(View.VISIBLE);
                    imagenInsignia.setImageResource(R.drawable.icon_spider);
                    textoInsignia.setVisibility(View.VISIBLE);
                    textoInsignia.setText("¡INSIGNIA DE SPIDER-MAN CONSEGUIDA!");
                    break;
            }
            } else {
            imagenInsignia.setVisibility(View.INVISIBLE);
            textoInsignia.setVisibility(View.INVISIBLE);
        }

    }



    //Metodo para recuperar el numero seleccionado en el fragment principal
    private int recuperarFactor(){
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // El segundo parámetro es el valor predeterminado en caso de que no se encuentre la clave
        return preferences.getInt("factor1", 0);
    }

    //Metodo para recuperar la fecha seleccionada en el fragment principal
    private String recuperarFecha(){
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // El segundo parámetro es el valor predeterminado en caso de que no se encuentre la clave
        return preferences.getString("fecha_seleccionada", "sin_seleccion");
    }

    //Metodo para recuperar el avatar seleccionado en el fragment principal
    private String recuperarDificultad(){
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // El segundo parámetro es el valor predeterminado en caso de que no se encuentre la clave
        return preferences.getString("dificultad_seleccionada", "");
    }

    //Metodo para recuperar el numero de aciertos:
    private int recuperarAciertos(){
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // El segundo parámetro es el valor predeterminado en caso de que no se encuentre la clave
        return preferences.getInt("num_aciertos", 0);
    }

    //Metodo para recuperar el numero de fallos:
    private int recuperarFallos(){
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // El segundo parámetro es el valor predeterminado en caso de que no se encuentre la clave
        return preferences.getInt("num_fallos", 0);
    }

    //Metodo para recuperar las falladas:
    private ArrayList<Multiplicacion> recuperarErroneas(){
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // El segundo parámetro es el valor predeterminado en caso de que no se encuentre la clave
        String multiplicaciones = preferences.getString("multiplicaciones_fallidas", "");
        // Si la cadena no está vacía, la convertimos de nuevo a la lista de objetos
        if (!TextUtils.isEmpty(multiplicaciones)) {
            Gson gson = new Gson();
            //Obtenemos el tipo de lista que debe recuperar
            Type tipoListaMultiplicaciones = new TypeToken<ArrayList<Multiplicacion>>(){}.getType();
            return gson.fromJson(multiplicaciones, tipoListaMultiplicaciones);

        } else {
            // Si la cadena está vacía, devuelve una lista vacía
            return new ArrayList<>();
        }
    }

    //Metodo para recuperar el avatar seleccionado en el fragment principal
    private String recuperarAvatar(){
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // El segundo parámetro es el valor predeterminado en caso de que no se encuentre la clave
        return preferences.getString("avatar_seleccionado", "sin_seleccion");
    }

    //Metodo que devuelve las operaciones
    public String getOperaciones (ArrayList<Multiplicacion> m ){
        String lista = "";
        if(!m.isEmpty()){
            for (Multiplicacion multiplicacion : m){
                lista = lista +"\n"+ multiplicacion.getFactor1() + " x " + multiplicacion.getFactor2() + " = " + multiplicacion.getResultado();
            }
        }else{
            lista = "-- No hay multiplicaciones erróneas --";
        }
        return lista;
    }

}