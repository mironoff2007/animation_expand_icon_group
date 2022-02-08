package com.mironov.animationexpandiconsgroup

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import androidx.core.view.children

class ExpandableLayout(context: Context?, attrs: AttributeSet?) : TableLayout(context, attrs) {

    private var expanded=false

    fun touch(){
        if(!expanded){
        val touchAnimation=AnimationUtils.loadAnimation(this.context,R.anim.touch_impact_animation)
        touchAnimation.setAnimationListener(
            object :  Animation.AnimationListener {

                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    expand()
                }

                override fun onAnimationRepeat(animation: Animation?) {}

            })
        this.startAnimation(touchAnimation)}
        else{
            shrink()
        }
    }

    fun expand(){
        expanded=true
    }

    fun shrink(){
        expanded=false
    }

}