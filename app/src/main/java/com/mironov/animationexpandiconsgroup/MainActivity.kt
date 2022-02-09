package com.mironov.animationexpandiconsgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mironov.animationexpandiconsgroup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    private lateinit var array: Array<ExpandableLayout>

    private var expandedView: ExpandableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        array =
            arrayOf(binding.container1, binding.container2, binding.container3, binding.container4)

        binding.container1.setOnClickListener {
            expand(it as ExpandableLayout)
        }

        binding.container2.setOnClickListener {
            expand(it as ExpandableLayout)
        }

        binding.container3.setOnClickListener {
            expand(it as ExpandableLayout)
        }

        binding.container4.setOnClickListener {
            expand(it as ExpandableLayout)
        }
    }

    fun expand(it: ExpandableLayout) {
        expandedView = it
        it.touch()
        fadeOthers(it)
    }

    private fun fadeOthers(view: View) {
        array.forEach {
            if (view != it) {
                it.fade()
            }
        }
    }

    private fun showOthers(view: View) {
        array.forEach {
            if (view != it) {
                it.show()
            }
        }
    }

    override fun onBackPressed() {
        if (expandedView != null) {
            expandedView!!.shrink()
            showOthers(expandedView!!)
            expandedView = null
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}