package com.mironov.animationexpandiconsgroup


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TableLayout
import android.widget.TableRow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import kotlin.math.roundToInt


class ExpandableLayout : TableLayout {


    private var expanded = false
    private var faded = false
    private var heightIncrement = 0//px
    private var widthIncrement = 0//px
    private val expandAndShrinkDuration = 200L
    private val rowsCount = 3
    private val columnsCount = 2
    private var iconSize = 0 //dp

    @SuppressLint("Recycle")
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            val a = context!!.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            if (a.hasValue(R.styleable.ExpandableLayout_expandHeightBy)) {
                val expandHeightBy =
                    a.getString(R.styleable.ExpandableLayout_expandHeightBy).toString()
                heightIncrement = dpToPx(expandHeightBy)
            }
            if (a.hasValue(R.styleable.ExpandableLayout_expandHeightBy)) {
                val expandWidthBy =
                    a.getString(R.styleable.ExpandableLayout_expandWidthBy).toString()
                widthIncrement = dpToPx(expandWidthBy)
            }
            if (a.hasValue(R.styleable.ExpandableLayout_expandHeightBy)) {
                iconSize = dpToPx(a.getString(R.styleable.ExpandableLayout_iconsSize).toString())
            }
        }
    }


    fun touch() {
        if (!expanded) {
            val touchAnimation =
                AnimationUtils.loadAnimation(this.context, R.anim.touch_impact_animation)
            touchAnimation.setAnimationListener(
                object : Animation.AnimationListener {

                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        expand()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}

                })
            this.startAnimation(touchAnimation)
        }

    }

    private fun expand() {
        expanded = true
        this.elevation = 1f
        animateHeightResize(heightIncrement)
        animateWidthResize(widthIncrement)
        translateToCenter()
    }

    fun shrink() {
        expanded = false
        this.elevation = 0f
        animateHeightResize(-heightIncrement)
        animateWidthResize(-widthIncrement)
        translateBack()
    }

    private fun translateToCenter(){

        val w=(parent as ViewGroup).width
        val h=(parent as ViewGroup).height

        var moveX = w / 2 - (this.x+(width+widthIncrement)/2)
        var moveY = h / 2 - (this.y+(width+heightIncrement)/2)
        animateTranslation(moveX,moveY)
    }

    private fun translateBack(){
        animateTranslation(0f,0f)
    }

    private fun animateTranslation(moveX:Float, moveY:Float){
        this.animate()
            .translationX(moveX)
            .translationY(moveY)
            .setDuration(expandAndShrinkDuration)
            .setInterpolator(LinearInterpolator())
            .start()
    }

    private fun animateHeightResize(increment: Int) {
        val valueAnimator =
            ValueAnimator.ofInt(this.measuredHeight, this.measuredHeight + increment)
        valueAnimator.duration = expandAndShrinkDuration
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = this.layoutParams
            layoutParams.height = animatedValue
            this.layoutParams = layoutParams

            //Add margins
            this.children.filter { it is TableRow }.forEach {
                val params = it.layoutParams as MarginLayoutParams
                params.topMargin = calculateMargin(animatedValue, rowsCount)
                it.layoutParams = params
            }
        }
        valueAnimator.start()
    }

    private fun animateWidthResize(increment: Int) {
        val valueAnimator =
            ValueAnimator.ofInt(this.measuredWidth, this.measuredWidth + increment)
        valueAnimator.duration = expandAndShrinkDuration
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = this.layoutParams
            layoutParams.width = animatedValue
            this.layoutParams = layoutParams
            //Add margins
            this.children.filter { it is TableRow }.forEach { row ->
                val rowView = row as TableRow
                rowView.children.forEach { icon ->
                    val params = icon.layoutParams as MarginLayoutParams
                    params.leftMargin = calculateMargin(animatedValue, columnsCount)
                    icon.layoutParams = params
                }
            }
        }
        valueAnimator.start()
    }

    private fun calculateMargin(animatedValue: Int, count: Int): Int {
        val allIconsSize = count * iconSize
        var margin: Int = ((animatedValue - allIconsSize) / (count + 1))
        if (margin < 0) {
            margin = 0
        }
        return margin
    }

    fun fade() {
        if (!faded) {
            faded=true
            val animation = AlphaAnimation(1f, 0f)
            animation.duration = expandAndShrinkDuration
            animation.fillAfter = true
            this.startAnimation(animation)
        }
    }

    fun show() {
        if (faded) {
            faded=false
            val animation = AlphaAnimation(0f, 1f)
            animation.duration = expandAndShrinkDuration
            animation.fillAfter = true
            this.startAnimation(animation)
        }
    }

    fun dpToPx(dp: String): Int {
        val dpNumber = dp.replace("dip", "")
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