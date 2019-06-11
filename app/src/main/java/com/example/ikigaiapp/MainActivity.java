package com.example.ikigaiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void btnIniciarOnClick(View v){
        alerta_montar();
        Toast.makeText(MainActivity.this, R.string.alert_select, Toast.LENGTH_SHORT).show();

    }
    private AlertDialog alertaMontar;
    private AlertDialog alertaAjuda;
    private AlertDialog alertaSobre;

    private int button;

    private void alerta_montar() {
        final Intent intent = new Intent(this, CriarActivity.class);
        final Intent intentMontar = new Intent(this, MontarIkigaiActivity.class);
        final CharSequence[] charSequences = new CharSequence[]{getString( R.string.aut_mon), getString( R.string.man_mon)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle( R.string.como_gostaria);
        builder.setSingleChoiceItems(charSequences, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                button = which;
                //Toast.makeText(MainActivity.this, String.valueOf(button), Toast.LENGTH_LONG).show();

            }
        });

        builder.setPositiveButton( R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (button == 0) {
                    startActivity(intent);
                }
                if (button == 1) {
                    startActivity(intentMontar);
                }
            }
        });
        alertaMontar = builder.create();
        alertaMontar.show();
    }

    public void btnAjudaInicial(View v) {
        alerta_ajuda_inicial();
    }
    private void alerta_ajuda_inicial() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle( R.string.welcome);
        builder.setMessage( R.string.initial_help);

        builder.setPositiveButton( R.string.entendi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {}
        });
        alertaAjuda = builder.create();
        alertaAjuda.show();
    }



    public void btnSobreoApp (View v) {
        alerta_sobre();
    }

    private void alerta_sobre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.sobre_o_app, null));
        alertaSobre = builder.create();
        alertaSobre.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide(); para tirar a barra padrao DO ANDROID
        setContentView(R.layout.activity_main);
    }


}
