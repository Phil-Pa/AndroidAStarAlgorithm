package de.phil.astaralgorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var maxPixelsPerTile = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TileType.values().map { it.toString() })

        spinner_tile_types.adapter = spinnerAdapter
        spinner_tile_types.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                astar_view.tileTypePaint = TileType.fromInt(spinner_tile_types.selectedItemPosition)
            }

        }

        switch_debug.setOnCheckedChangeListener { compoundButton, debug ->
            astar_view.showDebugInfo = debug
        }
        astar_view.showDebugInfo = switch_debug.isChecked

        switch_diagonal.setOnCheckedChangeListener { compoundButton, diagonal ->
            astar_view.diagonal = diagonal
        }
        astar_view.diagonal = switch_diagonal.isChecked

        button_solve.setOnClickListener {
            astar_view.solve()
        }

        seekbar_numTiles.max = (resources.displayMetrics.widthPixels / maxPixelsPerTile).toFloat()
        astar_view.pixelsPerTile = maxPixelsPerTile

        seekbar_numTiles.setIndicatorTextFormat("\${Num tiles} %")
        seekbar_numTiles.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                if (seekParams != null)
                    astar_view.numTiles = seekParams.progress
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {

            }
        }

        seekbar_maxPixelsPerTile.setIndicatorTextFormat("\${Pixels per tile} %")
        seekbar_maxPixelsPerTile.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                if (seekParams != null) {
                    maxPixelsPerTile = seekParams.progress
                    astar_view.pixelsPerTile = seekParams.progress
                    seekbar_numTiles.max = (resources.displayMetrics.widthPixels / maxPixelsPerTile).toFloat()
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {

            }
        }

        seekbar_percentWalls.setIndicatorTextFormat("\${Percent walls} %")

        button_create_grid.setOnClickListener {
            astar_view.createRandomGrid(seekbar_percentWalls.progress)
        }

        button_resetZoom.setOnClickListener {
            zoom_layout.zoomTo(1.0f, true)
        }
    }
}