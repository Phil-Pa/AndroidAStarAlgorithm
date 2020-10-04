package de.phil.astaralgorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
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

        button_solve.setOnClickListener {
            astar_view.solve()
        }

        seekbar_numTiles.max = resources.displayMetrics.widthPixels / maxPixelsPerTile
        astar_view.pixelsPerTile = maxPixelsPerTile
        seekbar_numTiles.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                astar_view.numTiles = progress
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        seekbar_maxPixelsPerTile.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                maxPixelsPerTile = progress
                astar_view.pixelsPerTile = progress
                seekbar_numTiles.max = resources.displayMetrics.widthPixels / maxPixelsPerTile
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        button_create_grid.setOnClickListener {
            astar_view.createRandomGrid(seekbar_percentWalls.progress)
        }
    }
}