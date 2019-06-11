package com.example.ikigaiapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class CriarDoodleView extends View {

    //a tela principal
    private Bitmap bitmap;
    //para desenhar no bitmap
    private Canvas bitmapCanvas;
    //para desenhar na tela
    private final Paint paintScreen;

    public CriarDoodleView(Context context, AttributeSet attrs) {
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
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);

        drawIkigai();

    }


    public void drawIkigai() {

        Paint p = new Paint();

        p.setColor(new Color().rgb(230,30,0));
        p.setAlpha(80);
        bitmapCanvas.drawCircle(550,500,300,p);

        p.setColor(new Color().rgb(0,230,230));
        p.setAlpha(80);
        bitmapCanvas.drawCircle(350,700,300,p);

        p.setColor(new Color().rgb(50,30,50));
        p.setAlpha(80);
        bitmapCanvas.drawCircle(750,700,300,p);

        p.setColor(new Color().rgb(30,30,230));
        p.setAlpha(80);
        bitmapCanvas.drawCircle(550,900,300,p);

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
