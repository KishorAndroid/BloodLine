package com.kishordahiwadkar.bloodline

import android.app.Application
import android.view.View
import android.widget.AdapterView
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

const val SPLASH_SCREEN_PAGE = 0
const val REGISTRATION_PAGE = 1
const val REQUEST_PAGE = 2
const val T_C_PAGE = 3

class MainViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    /**
     * SPLASH_SCREEN_PAGE = Splash Screen Page
     * REGISTRATION_PAGE = Registration Page
     * REQUEST_PAGE = Request Page
     * T_C_PAGE = Terms and Conditions Page
     */
    var layoutSwitcher = ObservableField<Int>()
    var name = ObservableField<String>("")
    var phoneNumber = ObservableField<String>("")
    var bloodGroup = ObservableField<String>("")
    var statusMessage = ObservableField<String>("Please wait...")
    var bloodGroupArray: Array<String> = application.resources.getStringArray(R.array.blood_groups)
    var bloodGroupAdapterListener = object :AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(adapterView: AdapterView<*>?) {

        }

        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            bloodGroup.set(bloodGroupArray[position])
        }
    }

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun doOnStart(){
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val database = FirebaseDatabase.getInstance()
            database
                .getReference("users")
                .child(auth.uid!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)
                        user?.let {
                            if (it.bloodGroup.isNullOrBlank() ||
                                it.phoneNumber.isNullOrBlank() ||
                                it.userName.isNullOrBlank()) {
                                showRegistrationPage()
                            } else {
                                showRequestPage()
                            }
                        }
                    }
                })
        } else {
            signInAnonymously()
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showRegistrationPage()
            } else {
                //TODO Handle task unsuccessful state
            }
        }
    }

    fun showRegistrationPage() {
        statusMessage.set("Create your profile")
        layoutSwitcher.set(REGISTRATION_PAGE)
    }

    fun showRequestPage() {
        statusMessage.set("Request Blood")
        layoutSwitcher.set(REQUEST_PAGE)
    }

    fun registerUser() {
        if (inputIsValid()) {
            layoutSwitcher.set(SPLASH_SCREEN_PAGE)
            statusMessage.set("Creating your profile...")
            val user = User(name.get()!!, phoneNumber.get()!!, bloodGroup.get()!!)
            val database = FirebaseDatabase.getInstance()

            val auth = FirebaseAuth.getInstance()

            database
                .getReference("users")
                .child(auth.uid!!)
                .setValue(user)
                .addOnSuccessListener {
                    statusMessage.set("Request Blood")
                    layoutSwitcher.set(REQUEST_PAGE)
                }
                .addOnFailureListener {

                }
        } else {
            //TODO Show user input validation error
        }
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

    fun requestBlood() {

    }
}