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
import de.phil.astaralgorithm.datastructure.Array2D
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis


class AStarAlgorithmView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        private const val BASE_HORIZONTAL_OFFSET = 10f
        private const val BASE_VERTICAL_OFFSET = 40f
        private const val DEBUG_TEXT_PADDING = 12f
    }

    private var mNumTiles = 10
    var numTiles: Int
        get() = mNumTiles
        set(value) {
            mNumTiles = value
            grid = Array2D(numTiles, numTiles)
            initGrid()
            invalidate()
        }

    private var mShowDebugInformation = false
    private var startTileColor = Color.CYAN
    private var endTileColor = Color.MAGENTA
    private var wallTileColor = Color.BLACK
    private var pathTileColor = Color.GREEN
    private var emptyTileColor = Color.GRAY
    private var debugTextColor = Color.RED

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tileWidth: Int
            get() = width / numTiles
    private val tileHeight: Int
            get() = height / numTiles

    private lateinit var grid: Array2D<Tile>

    var tileTypePaint = TileType.WALL
    var animationsPerSecond = 2
    var showAnimation = false
    var diagonal = false

    private var lastTouchPositionX = 0f
    private var lastTouchPositionY = 0f
    private var lastSolveTimeMillis = 0L

    var showDebugInfo: Boolean
        get() = mShowDebugInformation
        set(value) {
            mShowDebugInformation = value
            invalidate()
        }

    init {
        initCustomViewAttributes(context, attrs)
        initPaint()
        initGrid()
    }

    private fun initPaint() {
        paint.style = Paint.Style.FILL
        paint.color = emptyTileColor
        paint.textSize = convertDpToPixel(DEBUG_TEXT_PADDING)
    }

    private fun initGrid() {
        grid = Array2D(numTiles, numTiles)
        for (x in 0 until numTiles) {
            for (y in 0 until numTiles) {
                grid.set(x, y, Tile(TileType.WALKABLE, x, y))
            }
        }
    }

    fun solve() {
        lastSolveTimeMillis = measureTimeMillis {
            val algorithm = AStarAlgorithm(grid)
            algorithm.findPath(diagonal)
        }
        invalidate()
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
                startTileColor = getColor(R.styleable.AStarAlgorithmView_startTileColor, Color.CYAN)
                endTileColor = getColor(R.styleable.AStarAlgorithmView_endTileColor, Color.MAGENTA)
                wallTileColor =
                    getColor(R.styleable.AStarAlgorithmView_wallTileColor, Color.BLACK)
                pathTileColor = getColor(R.styleable.AStarAlgorithmView_pathTileColor, Color.GREEN)

                emptyTileColor =
                    getColor(R.styleable.AStarAlgorithmView_emptyTileColor, Color.GRAY)
                debugTextColor = getColor(R.styleable.AStarAlgorithmView_debugTextColor, Color.RED)
                showAnimation = getBoolean(R.styleable.AStarAlgorithmView_showAnimation, false)
                animationsPerSecond = getInteger(R.styleable.AStarAlgorithmView_animationsPerSecond, 2)
                diagonal = getBoolean(R.styleable.AStarAlgorithmView_diagonal, false)
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

                    val (gridX, gridY) = getGridCoordinates(lastTouchPositionX, lastTouchPositionY)
                    if (gridX != -1 && gridY != -1)
                        grid.get(gridX, gridY).type = tileTypePaint

                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getGridCoordinates(
        lastTouchPositionX: Float,
        lastTouchPositionY: Float
    ): Pair<Int, Int> {
        for (x in 0 until numTiles) {
            for (y in 0 until numTiles) {
                val lowXBound = x * tileWidth
                val highXBound = (x + 1) * tileWidth
                val lowYBound = y * tileHeight
                val highYBound = (y + 1) * tileHeight

                if (lastTouchPositionX > lowXBound && lastTouchPositionX < highXBound
                    && lastTouchPositionY > lowYBound && lastTouchPositionY < highYBound) {
                    return Pair(x, y)
                }
            }
        }

        return Pair(-1, -1)
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
        for (x in 0 until numTiles) {
            for (y in 0 until numTiles) {
                drawGridTile(canvas, x, y)
            }
        }
    }



    private fun drawGridTile(canvas: Canvas, indexX: Int, indexY: Int) {

        val tile = grid.get(indexX, indexY)

        if (tile.type == TileType.WALKABLE)
            return

        when (tile.type) {
            TileType.WALL -> paint.color = wallTileColor
            TileType.WALKABLE -> paint.color = emptyTileColor
            TileType.PATH -> paint.color = pathTileColor
            TileType.START -> paint.color = startTileColor
            TileType.END -> paint.color = endTileColor
        }

        val tileSize = width / numTiles.toFloat()

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
            val wallColorText = "wall tile color: ${String.format("#%06X", 0xFFFFFF and wallTileColor)}"
            val emptyColorText = "empty tile color: ${String.format("#%06X", 0xFFFFFF and emptyTileColor)}"
            val startColorText = "start tile color: ${String.format("#%06X", 0xFFFFF and startTileColor)}"
            val endColorText = "end tile color: ${String.format("#%06X", 0xFFFFF and endTileColor)}"
            val solveTimeMillisText = "solve duration: $lastSolveTimeMillis milliseconds"

            val longestTextLength = measureLongestTextLength(touchText, numTilesText, pathColorText, wallColorText, emptyColorText,
                startColorText, endColorText, solveTimeMillisText)
            drawTransparentDebugBackground(canvas, longestTextLength)

            drawDebugInfoTexts(
                canvas,
                touchText,
                numTilesText,
                solveTimeMillisText,
                pathColorText,
                wallColorText,
                emptyColorText,
                startColorText,
                endColorText
            )
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
        solveTimeMillisText: String,
        pathColorText: String,
        obstacleColorText: String,
        emptyColorText: String,
        startColorText: String,
        endColorText: String
    ) {
        var step = 1
        paint.color = debugTextColor
        canvas.drawText(touchText, BASE_HORIZONTAL_OFFSET, BASE_VERTICAL_OFFSET, paint)
        canvas.drawText(
            numTilesText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * step++,
            paint
        )
        canvas.drawText(
            solveTimeMillisText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * step++,
            paint
        )
        paint.color = pathTileColor
        canvas.drawText(
            pathColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * step++,
            paint
        )
        paint.color = wallTileColor
        canvas.drawText(
            obstacleColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * step++,
            paint
        )
        paint.color = emptyTileColor
        canvas.drawText(
            emptyColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * step++,
            paint
        )
        paint.color = startTileColor
        canvas.drawText(
            startColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * step++,
            paint
        )
        paint.color = endTileColor
        canvas.drawText(
            endColorText,
            BASE_HORIZONTAL_OFFSET,
            BASE_VERTICAL_OFFSET + paint.textSize * step,
            paint
        )
    }

    private fun drawTransparentDebugBackground(canvas: Canvas, longestTextLength: Float) {
        paint.color = Color.parseColor("#AA000000")
        canvas.drawRect(
            0f,
            0f,
            longestTextLength + DEBUG_TEXT_PADDING,
            BASE_VERTICAL_OFFSET + paint.textSize * 8 + DEBUG_TEXT_PADDING,
            paint
        )
    }

    fun createRandomGrid(percentWalls: Int) {
        grid = AStarAlgorithm.createGrid(numTiles, percentWalls)
        invalidate()
    }


}