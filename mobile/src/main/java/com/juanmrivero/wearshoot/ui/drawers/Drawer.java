package com.juanmrivero.wearshoot.ui.drawers;

import android.graphics.Canvas;

public interface Drawer<T> {

    void onDraw(Canvas canvas, T drawableObject);

}
