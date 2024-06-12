package com.example.maliva.data.utils

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

fun View.setMotionVisibilities(visibility: Int) {
    val motionLayout = parent as MotionLayout
    motionLayout.constraintSetIds.forEach {
        val constraintSet = motionLayout.getConstraintSet(it) ?: return@forEach
        constraintSet.setVisibility(this.id, visibility)
        constraintSet.applyTo(motionLayout)
    }
}