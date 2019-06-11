package com.example.ikigaiapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;


public class Circulo extends DialogFragment {

    RadioButton amaB;
    RadioButton precisaB;
    RadioButton bomB;
    RadioButton pagoB;

    //seekbar para alpha
    private SeekBar alphaSeekBar;
    //seekbar para red
    private SeekBar redSeekBar;
    //seekbar para green
    private SeekBar greenSeekBar;
    //seekbar para blue
    private SeekBar blueSeekBar;
    //campo de amostra de cor
    private View colorView;
    //cor atual
    private int color;

    private MontarIkigaiFragment getDoodleFragment(){
        return (MontarIkigaiFragment)
                getFragmentManager().
                        findFragmentById(R.id.mainFragment);
    }

    private final SeekBar.OnSeekBarChangeListener colorChangedListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean
                        fromUser) {
                    alphaSeekBar.setProgress(70);
                    color = Color.argb(
                            alphaSeekBar.getProgress(),
                            redSeekBar.getProgress(),
                            greenSeekBar.getProgress(),
                            blueSeekBar.getProgress()
                    );
                    colorView.setBackgroundColor(color);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //builder para o diálogo
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        //infla a view do fragment
        final View circleDialogView =
                getActivity().
                        getLayoutInflater().
                        inflate(R.layout.fragment_cicle, null);
        builder.setView(circleDialogView);
        builder.setTitle(R.string.title_color_dialog);
        //busca os componentes visuais na árvore
        alphaSeekBar =
                circleDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar =
                circleDialogView.findViewById(R.id.redSeekBar);
        greenSeekBar =
                circleDialogView.findViewById(R.id.greenSeekBar);
        blueSeekBar =
                circleDialogView.findViewById(R.id.blueSeekBar);
        colorView = circleDialogView.findViewById(R.id.colorView);

        amaB = circleDialogView.findViewById(R.id.amaB);
        precisaB = circleDialogView.findViewById(R.id.precisaB);
        bomB = circleDialogView.findViewById(R.id.bomB);
        pagoB = circleDialogView.findViewById(R.id.pagoB);

        amaB.setEnabled( true );
        precisaB.setEnabled( true );
        bomB.setEnabled( true );
        pagoB.setEnabled( true );

        //registra o observador em todas as SeekBars
        alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        //pega o DoodleView para pegar a cor atual e configurar
        //o progresso das barras
        final MontarDoodleView doodleView =
                getDoodleFragment().getDoodleView();

        //configura o progresso de cada barra
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        greenSeekBar.setProgress(Color.green(color));
        blueSeekBar.setProgress(Color.blue(color));


        builder.setPositiveButton(R.string.button_set_color, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog amaAlert;
                        AlertDialog precisaAlert;
                        AlertDialog bomAlert;
                        AlertDialog pagoAlert;
                        RadioGroup radioGroup = circleDialogView.findViewById(R.id.radioA);

                        int bla = radioGroup.getCheckedRadioButtonId();

                        switch (bla) {
                            case R.id.amaB:
                                AlertDialog.Builder builderAma = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflaterAma = getLayoutInflater();
                                builderAma.setView(inflaterAma.inflate(R.layout.dialog_ama_layout, null));
                                builderAma.setPositiveButton( "Pronto", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        EditText amaTextView = ((Dialog) arg0).findViewById( R.id.caixaAma );
                                        String s = amaTextView.getText().toString();
                                        doodleView.drawCircleAma(color);
                                    }
                                });
                                amaAlert = builderAma.create();
                                amaAlert.show();
                                    break;
                            case R.id.bomB:
                                AlertDialog.Builder builderBom = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflaterBom = getLayoutInflater();
                                builderBom.setView(inflaterBom.inflate(R.layout.dialog_bom_layout, null));
                                builderBom.setPositiveButton( "Pronto", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        EditText bomTextView = ((Dialog) arg0).findViewById( R.id.caixaBom );
                                        String s = bomTextView.getText().toString();
                                        doodleView.drawCircleBom(color);
                                    }
                                });
                                bomAlert = builderBom.create();
                                bomAlert.show();
                                    break;
                            case R.id.precisaB:
                                AlertDialog.Builder builderPre = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflaterPre = getLayoutInflater();
                                builderPre.setView(inflaterPre.inflate(R.layout.dialog_precisa_layout, null));
                                builderPre.setPositiveButton( "Pronto", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        EditText preTextView = ((Dialog) arg0).findViewById( R.id.caixaPre );
                                        String s = preTextView.getText().toString();
                                        doodleView.drawCirclePrecisa(color);
                                    }
                                });
                                precisaAlert = builderPre.create();
                                precisaAlert.show();
                                    break;
                            case R.id.pagoB:
                                AlertDialog.Builder builderPago = new AlertDialog.Builder(getActivity());
                                LayoutInflater inflaterPago = getLayoutInflater();
                                builderPago.setView(inflaterPago.inflate(R.layout.dialog_pago_layout, null));
                                builderPago.setPositiveButton( "Pronto", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        EditText pagoTextView = ((Dialog) arg0).findViewById( R.id.caixaPago );
                                        String s = pagoTextView.getText().toString();
                                        doodleView.drawCirclePago(color);
                                    }
                                });
                                pagoAlert = builderPago.create();
                                pagoAlert.show();
                                    break;
                        }


                    }

                });
        AlertDialog dialog = builder.create();
        return dialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MontarIkigaiFragment fragment = getDoodleFragment();
        if (fragment != null){
            //o diálogo está sendo exibido
            fragment.setDialogOnScreen(true);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        MontarIkigaiFragment fragment = getDoodleFragment();
        if (fragment != null)
            //o diálogo não está mais na tela
            fragment.setDialogOnScreen(false);
    }

}
