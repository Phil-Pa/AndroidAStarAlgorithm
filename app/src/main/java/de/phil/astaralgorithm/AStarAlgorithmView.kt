package de.phil.astaralgorithm

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class AStarAlgorithmView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var showDebugInformation = false

    private var obstacleTileColor = Color.BLACK
    private var pathTileColor = Color.YELLOW
    private var emptyTileColor = Color.WHITE


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AStarAlgorithmView,
            0, 0).apply {

            try {
//                mShowText = getBoolean(R.styleable.ASta, false)
//                textPos = getInteger(R.styleable.PieChart_labelPosition, 0)
            } finally {
                recycle()
            }
        }
    }


}