package ui.anwesome.com.pietricircleview

/**
 * Created by anweshmishra on 10/04/18.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.*

class PieTriCircleView (ctx : Context) : View(ctx) {
    override fun onDraw(canvas : Canvas) {

    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                
            }
        }
        return true
    }
}