package ui.anwesome.com.kotlinpietricircleview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.pietricircleview.PieTriCircleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PieTriCircleView.create(this)
    }
}
