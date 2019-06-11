package com.example.ikigaiapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class CriarIkigai extends Fragment {
    //para desenhar e lidar com os eventos de toque na tela
    private CriarDoodleView doodleView;

    //para lidar com os eventos do acelerômetro
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    //para evitar que mais de um diálogo seja exibido
    private boolean dialogOnScreen = false;
    //limiar que deve ser superado para considerar
    //que o usuário balançou o dispositivo
    private static final int ACCELERATION_THRESHOLD = 1000000;
    //código que identifica o pedido de permissão
    //para acesso ao armazenamento externo
    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //infla a view desse fragment
        View view = inflater.inflate(R.layout.criar_fragment,container, false);
        //indica que esse fragment tem itens de menu a serem adicionados à toolbar
        setHasOptionsMenu(true);
        doodleView = view.findViewById(R.id.doodleViewCriar);
        //aceleração atual
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        //última conhecida
        lastAcceleration = SensorManager.GRAVITY_EARTH;
        //será atualizada quando uma nova aceleração for detectada
        acceleration = 0.00f;
        return view;


    }


    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //verifica se um diálogo já não está sendo exibido
            if (!dialogOnScreen){
                //pega a aceleração nos três eixos
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values [2];
                //última aceleração se torna a atual
                //faz sentido depois da primeira vez
                lastAcceleration = currentAcceleration;
                //atualiza a aceleração atual
                currentAcceleration = x * x + y * y + z * z;
                //qual a diferença entre a aceleração que já
                //existia e a nova detectada?
                acceleration = currentAcceleration *
                        (currentAcceleration - lastAcceleration);
                //passou do limiar?
                if (acceleration > ACCELERATION_THRESHOLD){
                    confirmErase();
                }
            }

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void enableAccelerometerListening() {
        //obtém o serviço
        SensorManager sensorManager =
                (SensorManager) getActivity().
                        getSystemService(Context.SENSOR_SERVICE);
        //registra o observer
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void disableAccelerometerListening(){
        SensorManager sensorManager =
                (SensorManager) getActivity().
                        getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(
                sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void onResume() {
        super.onResume();
        enableAccelerometerListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableAccelerometerListening();
    }

    private void confirmErase (){
        ApagarCriaImageDialogFragment eraseFragment = new ApagarCriaImageDialogFragment();
        eraseFragment.show(getFragmentManager(),"erase dialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_ikigai_automatico, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_drawing:
                confirmErase();
                return true;
            case R.id.save:
                saveImage();
                return true;
            case R.id.print:
                doodleView.printImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImage (){
        if (getContext().
                checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            //permissão ainda não concedida
            //usuário já disse não uma vez? se sim, explicar
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )){
                //constroi um (builder de) diálogo
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.permission_explanation);
                builder.setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //pede a permissão
                                requestPermissions(new String []{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        SAVE_IMAGE_PERMISSION_REQUEST_CODE);
                            }
                        });

                builder.create().show();
            }
            else{
                requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        SAVE_IMAGE_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            doodleView.saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case SAVE_IMAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    doodleView.saveImage();

                }
        }
    }

    public CriarDoodleView getDoodleView(){
        return this.doodleView;
    }

    public void setDialogOnScreen(boolean dialogOnScreen) {
        this.dialogOnScreen = dialogOnScreen;
    }



}
