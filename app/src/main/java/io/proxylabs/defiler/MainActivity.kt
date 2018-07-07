package io.proxylabs.defiler

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        tv_username.text = FirebaseAuth.getInstance().currentUser?.displayName
        tv_email.text = FirebaseAuth.getInstance().currentUser?.email
    }
}