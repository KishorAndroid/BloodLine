package com.kishordahiwadkar.bloodline

import android.app.Application
import android.view.View
import android.widget.AdapterView
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

const val SPLASH_SCREEN_PAGE = 0
const val REGISTRATION_PAGE = 1
const val REQUEST_PAGE = 2
const val T_C_PAGE = 3

class MainViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * 0 = Splash Screen Page
     * 1 = Registration Page
     * 2 = Request Page
     * 3 = Terms and Conditions Page
     */
    var layoutSwitcher = ObservableField<Int>()
    var name = ObservableField<String>("")
    var phoneNumber = ObservableField<String>("")
    var bloodGroup = ObservableField<String>("")

    var bloodGroupArray = application.resources.getStringArray(R.array.blood_groups)
    var bloodGroupAdapterListener = object :AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(adapterView: AdapterView<*>?) {

        }

        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            bloodGroup.set(bloodGroupArray[position])
        }
    }

    val database = FirebaseDatabase.getInstance()

    init {
        layoutSwitcher.set(SPLASH_SCREEN_PAGE)
    }

    fun registerUser() {
        if (inputIsValid()) {
            val user = User(name.get()!!, phoneNumber.get()!!, bloodGroup.get()!!)
            val database = FirebaseDatabase.getInstance()

            val auth = FirebaseAuth.getInstance()

            database.getReference("users").child(auth.uid!!).setValue(user)
        } else {
            //TODO Show user input validation error
        }
        layoutSwitcher.set(SPLASH_SCREEN_PAGE)
    }

    private fun inputIsValid(): Boolean {
        if (name.get().isNullOrBlank() ||
            phoneNumber.get().isNullOrBlank() ||
            bloodGroup.get().isNullOrBlank()
        ) {
            return true
        }
        return true
    }
}