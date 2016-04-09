package com.juanmrivero.wearshoot.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.juanmrivero.wearshoot.game.World;
import com.juanmrivero.wearshoot.ui.drawers.WorldDrawer;

public class GameView extends View {

    private Paint paint;
    private float toPixelsScale;
    private float toWorldScale;

    private World world;
    private WorldDrawer worldDrawer;

    public GameView(Context context) {
        super(context);
        initialize();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        paint = new Paint();
        toPixelsScale = 1f;
    }

    public void setGameWorld(World world) {
        this.world = world;
        worldDrawer = new WorldDrawer(this.world);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateScales(w);
        world.updateHeight(h * toWorldScale);
        worldDrawer.setToPixelsScale(toPixelsScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        worldDrawer.onDraw(canvas, world);
    }

    private void drawBackground(Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private void calculateScales(int width) {
        if (world != null && width > 0) {
            toPixelsScale = width / world.width;
            toWorldScale = world.width / width;
        }
    }


}
