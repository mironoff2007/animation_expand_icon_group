package com.mironov.animationexpandiconsgroup

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import androidx.core.view.children

class ExpandableLayout(context: Context?, attrs: AttributeSet?) : TableLayout(context, attrs) {

    fun touch(){
        this.startAnimation(AnimationUtils.loadAnimation(this.context,R.anim.touch_impact_animation))
    }

}