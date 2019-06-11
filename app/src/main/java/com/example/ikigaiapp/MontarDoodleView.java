package com.example.ikigaiapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MontarDoodleView extends View {

    //liminar para decidir se o usuário moveu o dedo suficiente
    private static final float TOUCH_TOLERANCE = 10;
    //a tela principal
    private Bitmap bitmap;
    //para desenhar no bitmap
    private Canvas bitmapCanvas;
    private Canvas circle;
    //para desenhar na tela
    private final Paint paintScreen;

    //mapa dos paths atualmente sendo desenhados
    private final Map<Integer, Path> pathMap = new HashMap<>();
    //mapa que armazena o último ponto em cada path
    private final Map<Integer, Point> previousPointMap = new HashMap<>();

    public MontarDoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintScreen = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        //o canvas desenha no bitmap
        bitmapCanvas = new Canvas (bitmap);
        bitmap.eraseColor(Color.WHITE);//apaga usando branco
    }

    public void clear (){
        pathMap.clear();
        previousPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    public View inflatext(LayoutInflater inflater, ViewGroup container){
        View view = inflater.inflate(R.layout.montar_fragment,container, false);
        return view;
    };


    //Os
    //métodos da Listagem 27.1, também da classe DoodleView, dão acesso de leitura e escrita
    //à cor e à espessura atualmente sendo utilizadas.

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);
    }

    public void drawCircleAma(int color) {
        Paint p = new Paint();
        p.setColor(color);
        bitmapCanvas.drawCircle(550,550,300, p);
    }

    public void drawCircleBom(int color) {
        Paint p = new Paint();
        p.setColor(color);
        bitmapCanvas.drawCircle(350,750,300,p);
    }

    public void drawCirclePrecisa(int color) {
        Paint p = new Paint();
        p.setColor(color);
        bitmapCanvas.drawCircle(750,750,300,p);
    }

    public void drawCirclePago(int color) {
        Paint p = new Paint();
        p.setColor(color);
        bitmapCanvas.drawCircle(550,950,300,p);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //tipo de evento, comparar com constantes de MotionEvent
        int action = event.getActionMasked();
        //id do dedo nesse (somente nesse) MotionEvent
        //esse id não persiste ao longo de diversos eventos MotionEvent
        //ele é o índice dentro desse MotionEvent que usamos
        //para encontrar o id único de cada dedo
        int actionIndex = event.getActionIndex();
        //usuário tocou quando não tinha dedo algum ou
        //tocou com um novo dedo?
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN){
        //trata o evento de toque iniciado
        //os parâmetros são as coordenadas onde o toque começou
        //e o id que persiste ao longo de diversos MotionEvents
            touchStarted (
                    event.getX(actionIndex),
                    event.getY(actionIndex),
                    event.getPointerId(actionIndex)
            );
        }
        //Usuário tirou um dedo?
        else if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_POINTER_UP){

            touchEnded (event.getPointerId(actionIndex));
        }
        //usuário só arrastou um dedo, sem tirar da tela
        else{

            touchMoved (event);
        }
        //indica que a view deve ser redesenhada
        invalidate();
        return true;
    }

    private void touchStarted (float x, float y, int lineID){
        Path path;
        Point point;
        if (pathMap.containsKey(lineID)){
            path = pathMap.get(lineID);
            //limpa todas as linhas desse path
            //não quer dizer que o que está na tela será apagado
            path.reset();
            point = previousPointMap.get(lineID);
        }
        else{
            path = new Path ();
            pathMap.put(lineID, path);
            point = new Point ();
            previousPointMap.put (lineID, point);
        }
        path.moveTo(x, y);
        point.x = (int)x;
        point.y = (int)y;
    }

    private void touchMoved (MotionEvent event){
        for (int i = 0; i < event.getPointerCount(); i++){
            int pointerID = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerID);
            if (pathMap.containsKey(pointerID)){
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);
                Path path = pathMap.get(pointerID);
                Point point = previousPointMap.get(pointerID);
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE){
                    path.quadTo(point.x,
                            point.y,
                            (newX + point.x) / 2,
                            (newY + point.y) / 2);
                    point.x = (int) newX;
                    point.y = (int) newY;}
            }
        }
    }

    private void touchEnded (int lineID){
        Path path = pathMap.get(lineID);
        path.reset();
    }


    public void saveImage(){
        final String name = "Doodlz-" + System.currentTimeMillis() + ".jpg";
        String location =
                MediaStore.
                        Images.Media.insertImage(
                        getContext().getContentResolver(),
                        bitmap,
                        name,
                        "Um desenho feito no Doodlz"
                );
        Toast message = null;
        if (location != null){
            message =
                    Toast.makeText(getContext(),
                            R.string.message_saved,
                            Toast.LENGTH_SHORT
                    );
        }
        else{
            message =
                    Toast.makeText(getContext(),
                            R.string.message_error_saving,
                            Toast.LENGTH_SHORT
                    );
        }
        message.setGravity(
                Gravity.CENTER,
                message.getXOffset() / 2,
                message.getYOffset() / 2);
        message.show();
    }

    public void printImage(){
        if (PrintHelper.systemSupportsPrint()){
            PrintHelper printHelper = new PrintHelper(getContext());
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            printHelper.printBitmap("Doodlz Image", bitmap);
        }
        else{
            Toast message =
                    Toast.makeText(getContext(),
                            R.string.message_error_printing,
                            Toast.LENGTH_SHORT
                    );
            message.setGravity(
                    Gravity.CENTER,
                    message.getXOffset() / 2,
                    message.getYOffset() / 2);
            message.show();
        }
    }


}
