package com.example.rventure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthOptions

class Login : AppCompatActivity() {
    lateinit var pno_input: EditText
    lateinit var next_button: Button
    private var mcb: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private lateinit var fb: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pno_input = findViewById(R.id.textInputEditText)
        next_button = findViewById(R.id.next_button)
        next_button.setOnClickListener {
            sendotp()
        }
    }

    private fun sendotp() {
        val progbar: ProgressBar = findViewById(R.id.loadingOTPScreen)
        if (pno_input.text.toString().trim().isNotEmpty()) {
            if ((pno_input.text.toString().trim()).length == 10) {
                progbar.visibility = View.VISIBLE
                next_button.visibility = View.GONE
                fb = FirebaseAuth.getInstance()
                mcb = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        progbar.visibility = View.GONE
                        next_button.visibility = View.VISIBLE
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        progbar.visibility = View.GONE
                        next_button.visibility = View.VISIBLE
                        Toast.makeText(this@Login, p0.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        progbar.visibility = View.GONE
                        next_button.visibility = View.VISIBLE
                        val intent = Intent(this@Login, enterotp::class.java)
                        intent.putExtra("mobno", pno_input.text.toString())
                        intent.putExtra("actualotp", p0)
                        startActivity(intent)
                    }
                }
                PhoneAuthOptions.newBuilder(fb).setPhoneNumber("+91" + pno_input.text.toString()).
                setTimeout(60L, TimeUnit.SECONDS).setActivity(this@Login).setCallbacks(mcb as PhoneAuthProvider.OnVerificationStateChangedCallbacks).build()
            } else {
                Toast.makeText(this@Login, "Enter Valid Number", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@Login, "Enter Mobile Number", Toast.LENGTH_SHORT).show()
        }
    }
}