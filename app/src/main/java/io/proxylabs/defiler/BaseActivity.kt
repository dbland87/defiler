package io.proxylabs.defiler

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.time.Duration

open class BaseActivity : AppCompatActivity() {

    companion object {
        private val TAG = BaseActivity::class.java.simpleName
    }

    private val RC_SIGN_IN = 1337
    private val providers: List<AuthUI.IdpConfig> = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
    )
    private var auth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        currentUser = auth?.currentUser
        if (currentUser == null) {
            signIn()
        }
    }

    override fun onDestroy() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener({
                    Log.d(TAG, "Log out complete")
                })
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN ->
               onSignInResult(resultCode, data)
        }
    }

    private fun signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        )
    }

    private fun onSignInResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Sign in success")

        } else {
            Log.d(TAG, "Sign in Failure")
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
            }
            onSignInFailure()
        }
    }

    private fun onSignInFailure() {
        signIn()
    }
}
