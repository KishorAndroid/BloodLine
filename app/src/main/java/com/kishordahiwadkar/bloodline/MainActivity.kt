package com.kishordahiwadkar.bloodline

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.kishordahiwadkar.bloodline.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


const val RESOLVE_HINT = 1987

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private val binding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private lateinit var apiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        getCredentialApiClient()

        inputPhoneNumberEdit.setOnClickListener {
            requestHint()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.blood_groups,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerBloodGroup.adapter = adapter
        }

        spinnerBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }

        val avd = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim)
        avd?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                super.onAnimationEnd(drawable)
                imageCardioGraph.post { avd?.start() }
            }
        })
        imageCardioGraph.setImageDrawable(avd)
        avd?.start()
    }

    private fun getCredentialApiClient() {
        apiClient = GoogleApiClient.Builder(baseContext)
            .addConnectionCallbacks(this)
            .enableAutoManage(this, this)
            .addApi(Auth.CREDENTIALS_API)
            .build()
        apiClient.connect()
    }

    // Construct a request for phone numbers and show the picker
    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent = Auth.CredentialsApi.getHintPickerIntent(
            apiClient, hintRequest
        )
        startIntentSenderForResult(
            intent.intentSender,
            RESOLVE_HINT, null, 0, 0, 0
        )
    }

    // Obtain the phone number from the result
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                val credential = data!!.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                // credential.getId(); <-- E.164 format phone number on 10.2.+ devices
                inputPhoneNumberEdit.setText(credential.id)
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }
}
