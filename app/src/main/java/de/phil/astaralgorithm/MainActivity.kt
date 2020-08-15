package de.phil.astaralgorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
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
    }
}