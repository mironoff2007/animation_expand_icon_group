package com.mironov.animationexpandiconsgroup


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.view.children
import androidx.core.view.marginTop
import kotlin.math.roundToInt


class ExpandableLayout : TableLayout {

    companion object{
        const val NAMESPACE="http://schemas.android.com/apk/res/android"
        const val LAYOUT_HEIGHT="layout_height"
    }

    private var expanded=false
    private var heightIncrement=0
    private var widthIncrement=0
    //private var heightInitial=0
    private val expandAndShrinkDuration=200L
    private val rowsCount=3
    private var iconSize=0 //dp

    @SuppressLint("Recycle")
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            val a = context!!.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            if (a.hasValue(R.styleable.ExpandableLayout_expandHeightBy)) {
                val expandHeightBy = a.getString(R.styleable.ExpandableLayout_expandHeightBy).toString()
                heightIncrement= dpToPx(expandHeightBy)
            }
            if (a.hasValue(R.styleable.ExpandableLayout_expandHeightBy)) {
                val expandWidthBy = a.getString(R.styleable.ExpandableLayout_expandWidthBy).toString()
                widthIncrement = dpToPx(expandWidthBy)
            }
            if (a.hasValue(R.styleable.ExpandableLayout_expandHeightBy)) {
                iconSize = dpToPx(a.getString(R.styleable.ExpandableLayout_iconsSize).toString())
            }
            //heightInitial=dpToPx(attrs.getAttributeValue(NAMESPACE, LAYOUT_HEIGHT))
        }


    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
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
        animateWidthResize(widthIncrement)
    }

    private fun shrink(){
        expanded=false
        animateHeightResize(-heightIncrement)
        animateWidthResize(-widthIncrement)
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

            //Add margins
            this.children.filter { it is TableRow}.forEach {
                val params=it.layoutParams as MarginLayoutParams
                params.topMargin= calculateMargin(animatedValue)
                it.layoutParams=params
            }
            this.invalidate()
        }
        valueAnimator.start()
    }

    private fun animateWidthResize(increment:Int){
        val valueAnimator =
            ValueAnimator.ofInt(this.measuredWidth, this.measuredWidth +increment)
        valueAnimator.duration = expandAndShrinkDuration
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = this.layoutParams
            layoutParams.width = animatedValue
            this.layoutParams = layoutParams
            this.invalidate()
        }
        valueAnimator.start()
    }

    private fun calculateMargin(animatedValue:Int):Int{
        val allIconsHeight=rowsCount*iconSize
        var margin:Int= ((animatedValue-allIconsHeight)/(rowsCount+1))
        if(margin<0){margin=0}
        return margin
    }

    fun dpToPx(dp: String): Int {
        val dpNumber=dp.replace("dip","")
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpNumber.toFloat(),
            r.displayMetrics
        ).roundToInt()
    }
    fun dpToPx(dp: Int): Int {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        ).roundToInt()
    }

}