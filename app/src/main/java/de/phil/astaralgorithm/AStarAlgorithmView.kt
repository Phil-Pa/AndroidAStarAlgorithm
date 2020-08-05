package de.phil.astaralgorithm

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class AStarAlgorithmView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var numTiles = 10

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
                numTiles = getInteger(R.styleable.AStarAlgorithmView_numTiles, 10)
                showDebugInformation = getBoolean(R.styleable.AStarAlgorithmView_showDebugInformation, false)
                obstacleTileColor = getColor(R.styleable.AStarAlgorithmView_obstacleTileColor, Color.BLACK)
                pathTileColor = getColor(R.styleable.AStarAlgorithmView_pathTileColor, Color.YELLOW)
                emptyTileColor = getColor(R.styleable.AStarAlgorithmView_emptyTileColor, Color.WHITE)
            } finally {
                recycle()
            }
        }
    }


}