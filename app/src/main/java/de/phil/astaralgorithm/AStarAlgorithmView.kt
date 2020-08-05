package de.phil.astaralgorithm

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.roundToInt


class AStarAlgorithmView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        private val TAG = "AStarAlgorithmView"
        private const val BASE_HORIZONTAL_OFFSET = 10f
        private const val BASE_VERTICAL_OFFSET = 40f
        private const val DEBUG_TEXT_PADDING = 16f
    }

    private var numTiles = 10
    private var mShowDebugInformation = false
    private var obstacleTileColor = Color.BLACK
    private var pathTileColor = Color.GREEN
    private var emptyTileColor = Color.GRAY
    private var debugTextColor = Color.RED

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var lastTouchPositionX = 0f
    private var lastTouchPositionY = 0f

    var showDebugInfo: Boolean
        get() = mShowDebugInformation
        set(value) {
            mShowDebugInformation = value
            invalidate()
        }

    init {
        initCustomViewAttributes(context, attrs)
        paint.style = Paint.Style.FILL
        paint.color = emptyTileColor
        paint.textSize = convertDpToPixel(16f)
    }

    private fun convertDpToPixel(dp: Float) = dp * (context.resources
        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

    private fun initCustomViewAttributes(context: Context, attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AStarAlgorithmView,
            0, 0
        ).apply {

            try {
                numTiles = getInteger(R.styleable.AStarAlgorithmView_numTiles, 10)
                mShowDebugInformation =
                    getBoolean(R.styleable.AStarAlgorithmView_showDebugInformation, false)
                obstacleTileColor =
                    getColor(R.styleable.AStarAlgorithmView_obstacleTileColor, Color.BLACK)
                pathTileColor = getColor(R.styleable.AStarAlgorithmView_pathTileColor, Color.GREEN)
                emptyTileColor =
                    getColor(R.styleable.AStarAlgorithmView_emptyTileColor, Color.GRAY)
                debugTextColor = getColor(R.styleable.AStarAlgorithmView_debugTextColor, Color.RED)
            } finally {
                recycle()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) {

            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    lastTouchPositionX = event.x
                    lastTouchPositionY = event.y
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {

        if (canvas == null)
            return

        drawGridBackground(canvas)
        drawGridTiles(canvas)
        drawGridLines(canvas)
        drawDebugInfo(canvas)

        super.onDraw(canvas)
    }

    private fun drawGridTiles(canvas: Canvas) {
        drawGridTile(canvas, 4, 9)
        drawGridTile(canvas, 5, 4)
        drawGridTile(canvas, 0, 0)
    }

    private fun drawGridTile(canvas: Canvas, indexX: Int, indexY: Int) {
        val tileSize = width / numTiles.toFloat()

        paint.color = pathTileColor

        val xOffset = indexX * tileSize
        val yOffset = indexY * tileSize

        canvas.drawRect(xOffset, yOffset, xOffset + tileSize, yOffset + tileSize, paint)
    }

    private fun drawGridLines(canvas: Canvas) {
        paint.color = Color.BLACK
        val tileSize = width / numTiles.toFloat()

        for (i in 1..numTiles) {
            val lineOffset = i * tileSize
            canvas.drawLine(0f, lineOffset, width.toFloat(), lineOffset, paint)
            canvas.drawLine(lineOffset, 0f, lineOffset, width.toFloat(), paint)
        }
    }

    private fun drawGridBackground(canvas: Canvas) {
        paint.color = emptyTileColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawDebugInfo(canvas: Canvas) {
        if (mShowDebugInformation) {
            val touchText = "touch: x=${lastTouchPositionX.roundToInt()}, y=${lastTouchPositionY.roundToInt()}"
            val numTilesText = "num tiles: $numTiles"
            val pathColorText = "path tile color: ${String.format("#%06X", 0xFFFFFF and pathTileColor)}"
            val obstacleColorText = "obstacle tile color: ${String.format("#%06X", 0xFFFFFF and obstacleTileColor)}"
            val emptyColorText = "empty tile color: ${String.format("#%06X", 0xFFFFFF and emptyTileColor)}"

            val longestTextLength = measureLongestTextLength(touchText, numTilesText, pathColorText, obstacleColorText, emptyColorText)
            drawTransparentDebugBackground(canvas, longestTextLength)

            drawDebugInfoTexts(canvas, touchText, numTilesText, pathColorText, obstacleColorText, emptyColorText)
        }
    }

    private fun measureLongestTextLength(vararg texts: String): Float {
        val longestText = texts.maxBy { paint.measureText(it) }
        return paint.measureText(longestText)
    }

    private fun drawDebugInfoTexts(
        canvas: Canvas,
        touchText: String,
        numTilesText: String,
        pathColorText: String,
        obstacleColorText: String,
        emptyColorText: String
    ) {
        paint.color = debugTextColor
        canvas.drawText(touchText, BASE_HORIZONTAL_OFFSET, BASE_VERTICAL_OFFSET, paint)
        canvas.drawText(
            numTilesText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * 1,
            paint
        )
        paint.color = pathTileColor
        canvas.drawText(
            pathColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * 2,
            paint
        )
        paint.color = obstacleTileColor
        canvas.drawText(
            obstacleColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * 3,
            paint
        )
        paint.color = emptyTileColor
        canvas.drawText(
            emptyColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * 4,
            paint
        )
    }

    private fun drawTransparentDebugBackground(canvas: Canvas, longestTextLength: Float) {
        paint.color = Color.parseColor("#77000000")
        canvas.drawRect(
            0f,
            0f,
            longestTextLength + DEBUG_TEXT_PADDING,
            BASE_VERTICAL_OFFSET + paint.textSize * 4 + DEBUG_TEXT_PADDING,
            paint
        )
    }


}