package ui.anwesome.com.pietricircleview

/**
 * Created by anweshmishra on 10/04/18.
 */

import android.app.Activity
import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.*

class PieTriCircleView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating (startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class PieTriCircle (var i : Int, val state : State = State()) {
        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            canvas.save()
            canvas.translate(w/2, h/2)
            paint.color = Color.WHITE
            paint.strokeWidth = Math.min(w,h) / 60
            paint.strokeCap = Paint.Cap.ROUND
            paint.style = Paint.Style.STROKE
            val r : Float = Math.min(w,h)/10
            val path : Path = Path()
            for (i in 0..(360 * state.scales[0]).toInt()) {
                val x : Float = r * Math.cos(i * Math.PI/180).toFloat()
                val y : Float = r * Math.sin(i * Math.PI/180).toFloat()
                if (i == 0) {
                    path.moveTo(x, y)
                }
                else {
                    path.lineTo(x, y)
                }
            }
            canvas.drawPath(path, paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f, 0f, r * this.state.scales[1], paint)
            paint.style = Paint.Style.STROKE
            paint.color = Color.parseColor("#212121")
            for (i in 0..1) {
                canvas.save()
                canvas.rotate(30f * (1 - 2 * i) * state.scales[3])
                canvas.drawLine(0f, 0f, 0f, r * state.scales[2], paint)
                canvas.restore()
            }
            val x_gap = r / 4
            var r_gap = x_gap
            for (i in 0..3) {
                canvas.save()
                val x1 : Float = r_gap * Math.cos(Math.PI/6).toFloat()
                val y1 : Float = r_gap * Math.sin(Math.PI/6).toFloat()
                canvas.drawLine(x1 * this.state.scales[4], y1, -x1 * this.state.scales[4] , y1, paint)
                canvas.restore()
                r_gap += x_gap
            }
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class Renderer(private val view : PieTriCircleView) {
        private val animator : Animator = Animator(view)
        private val pieTriCircle : PieTriCircle = PieTriCircle(0)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            pieTriCircle.draw(canvas, paint)
            animator.animate {
                pieTriCircle.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            pieTriCircle.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : PieTriCircleView {
            val view : PieTriCircleView = PieTriCircleView(activity)
            activity.setContentView(view)
            return view
        }
    }
}