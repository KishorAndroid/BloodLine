package com.kishordahiwadkar.bloodline

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import kotlinx.android.synthetic.main.layout_registration_field.*


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
    private lateinit var avd: AnimatedVectorDrawableCompat
    private var isProfileOpenedByUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        lifecycle.addObserver(viewModel)

        getCredentialApiClient()

        inputPhoneNumberEdit.setOnClickListener {
            requestHint()
        }

        imageProfile.setOnClickListener {
            viewModel.showRegistrationPage()
            isProfileOpenedByUser = true
        }

        setCardioImage()
        startCardioAnimation()
    }

    override fun onBackPressed() {
        if (isProfileOpenedByUser) {
            viewModel.showRequestPage()
            isProfileOpenedByUser = false
            return
        } else {
            super.onBackPressed()
        }
    }

    private fun setCardioImage() {
        avd = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim)!!
        avd?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                super.onAnimationEnd(drawable)
                imageCardioGraph.post { startCardioAnimation() }
            }
        })
        imageCardioGraph.setImageDrawable(avd)
    }

    private fun startCardioAnimation() {
        (imageCardioGraph.drawable as Animatable2Compat).start()
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
                inputPhoneNumberEdit.setText(credential.id.substring(3))
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
