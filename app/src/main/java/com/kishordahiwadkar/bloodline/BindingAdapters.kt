package com.kishordahiwadkar.bloodline

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
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
        SPLASH_SCREEN_PAGE -> {
            constraint2.clone(constraintLayout.context, R.layout.activity_main)
        }
        REQUEST_PAGE -> {
            constraint2.clone(constraintLayout.context, R.layout.layout_request)
        }
    }

    TransitionManager.beginDelayedTransition(constraintLayout)
    constraint2.applyTo(constraintLayout)
}

@BindingAdapter("bloodGroupAdapterListener")
fun switchLayout(spinner: Spinner, adapterListener: AdapterView.OnItemSelectedListener) {
    ArrayAdapter.createFromResource(
        spinner.context,
        R.array.blood_groups,
        android.R.layout.simple_spinner_item
    ).also { adapter ->
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter
    }
    spinner.onItemSelectedListener = adapterListener
}