package com.kishordahiwadkar.bloodline

import androidx.databinding.BindingAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager

@BindingAdapter("layoutSwitcher")
fun switchLayout(constraintLayout: ConstraintLayout, pageNumber: Int) {

    val constraint1 = ConstraintSet()
    constraint1.clone(constraintLayout)

    val constraint2 = ConstraintSet()
    when (pageNumber) {
        REGISTRATION_PAGE -> {
            constraint2.clone(constraintLayout.context, R.layout.layout_registration)
        }
    }

    TransitionManager.beginDelayedTransition(constraintLayout)
    constraint2.applyTo(constraintLayout)
}