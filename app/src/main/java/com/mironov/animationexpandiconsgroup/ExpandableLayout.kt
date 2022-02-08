package com.mironov.animationexpandiconsgroup


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import kotlin.math.roundToInt


class ExpandableLayout : TableLayout {

    private var expanded=false
    private var heightIncrement=0
    private val expandAndShrinkDuration=200L

    @SuppressLint("Recycle")
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        var expandBy=0
        if (attrs != null) {
            val a = context!!.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            if (a.hasValue(R.styleable.ExpandableLayout_expandBy)) {
                expandBy = a.getInt(R.styleable.ExpandableLayout_expandBy,0)
            }
        }
        heightIncrement= dpToPx(expandBy).roundToInt()
    }


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

    private fun expand(){
        expanded=true

        animateHeightResize(heightIncrement)
    }

    private fun shrink(){
        expanded=false
        animateHeightResize(-heightIncrement)
    }

    private fun animateHeightResize(increment:Int){
        val valueAnimator =
            ValueAnimator.ofInt(this.measuredHeight, this.measuredHeight +increment)
        valueAnimator.duration = expandAndShrinkDuration
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = this.layoutParams
            layoutParams.height = animatedValue
            this.layoutParams = layoutParams
            this.invalidate()
        }
        valueAnimator.start()
    }

    fun dpToPx(dp: Int): Float {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        )
    }

}