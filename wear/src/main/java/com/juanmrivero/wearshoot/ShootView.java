package com.juanmrivero.wearshoot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.juanmrivero.wearshoot.com.juanmrivero.graphics.CircleF;
import com.juanmrivero.wearshoot.com.juanmrivero.graphics.LineF;


public class ShootView extends View {

    private static final float AIM_RADIO_RATIO = 0.35f;

    private Paint paint;

    private CircleF aimBorder;
    private LineF aimLineH;
    private LineF aimLineV;

    private float aimBorderStroke;
    private float aimCrossStroke;

    private int aimColor;
    private int aimColorNormal;
    private int aimColorPress;

    public ShootView(Context context) {
        super(context);
        initialize();
    }

    public ShootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ShootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        aimBorder = new CircleF();
        aimLineH = new LineF();
        aimLineV = new LineF();


        aimColorNormal = getResources().getColor(R.color.aim_normal);
        aimColorPress = getResources().getColor(R.color.aim_press);
        aimColor = aimColorNormal;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            aimColor = aimColorPress;
            invalidate();
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            aimColor = aimColorNormal;
            invalidate();
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        aimBorderStroke = width * 0.05f;
        aimCrossStroke = aimBorderStroke / 2;

        paint.setStrokeWidth(width * 0.05f);
        aimBorder.set(width * 0.5f, height * 0.5f, Math.min(width, height) * AIM_RADIO_RATIO);
        aimLineH.setHorizontal(aimBorder.x - aimBorder.radius, aimBorder.x + aimBorder.radius, aimBorder.y);
        aimLineV.setVertical(aimBorder.y - aimBorder.radius, aimBorder.y + aimBorder.radius, aimBorder.x);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(aimColor);


        paint.setStrokeWidth(aimBorderStroke);
        aimBorder.draw(canvas, paint);

        paint.setStrokeWidth(aimCrossStroke);
        aimLineV.draw(canvas, paint);
        aimLineH.draw(canvas, paint);
    }
}
