package com.example.rventure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import androidx.core.widget.addTextChangedListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class enterotp : AppCompatActivity() {
    lateinit var num1: EditText
    lateinit var num2: EditText
    lateinit var num3: EditText
    lateinit var num4: EditText
    lateinit var showmobno: TextView
    lateinit var actualotp: String
    lateinit var verify: Button
    lateinit var probar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterotp)

        num1 = findViewById(R.id.otp_num1)
        num2 = findViewById(R.id.otp_num2)
        num3 = findViewById(R.id.otp_num3)
        num4 = findViewById(R.id.otp_num4)
        verify = findViewById(R.id.verify_button)
        showmobno = findViewById(R.id.mobile_number)
        actualotp = intent.getStringExtra("actualotp").toString()
        showmobno.text = String.format("+91-%s", intent.getStringExtra("mobno"))
        verify.setOnClickListener {
            matchotp()
        }
        cursormove()
        findViewById<TextView>(R.id.resend_otp).setOnClickListener {
            val fb = FirebaseAuth.getInstance()
            val mcb = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@enterotp, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    actualotp = p0
                    Toast.makeText(this@enterotp, "New OTP Sent Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            PhoneAuthOptions.newBuilder(fb).setPhoneNumber("+91" + intent.getStringExtra("mobno"))
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this@enterotp)
                .setCallbacks(mcb as PhoneAuthProvider.OnVerificationStateChangedCallbacks)
        }
    }

    private fun matchotp() {
        if (num1.text.toString().trim().isNotEmpty() && num2.text.toString().trim()
                .isNotEmpty() && num3.text.toString().trim().isNotEmpty() && num4.text.toString()
                .trim().isNotEmpty()
        ) {
            val userotp: String =
                num1.text.toString() + num2.text.toString() + num3.text.toString() + num4.text.toString()
            if (actualotp != null) {
                verify.visibility = View.GONE
                probar.visibility = View.VISIBLE
                val pac: PhoneAuthCredential = PhoneAuthProvider.getCredential(actualotp, userotp)
                val res =
                    OnCompleteListener<AuthResult> { p0 ->
                        probar.visibility = View.GONE
                        verify.visibility = View.VISIBLE
                        if (p0.isSuccessful) {
                            val intent = Intent(this@enterotp, Dashboard::class.java)
                            Toast.makeText(
                                this@enterotp,
                                "OTP Verified Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@enterotp,
                                "Incorrect OTP, Try Again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                FirebaseAuth.getInstance().signInWithCredential(pac).addOnCompleteListener(res)
            } else {
                Toast.makeText(this@enterotp, "Check Your Connection", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@enterotp, "Please Enter all Digits", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cursormove() {
        num1.addTextChangedListener {
            it?.let { string ->
                if (string.isNotEmpty()) {
                    num2.requestFocus()
                }
            }
        }
        num2.addTextChangedListener {
            it?.let { string ->
                if (string.isNotEmpty()) {
                    num3.requestFocus()
                } else {
                    num1.requestFocus()
                }
            }
        }
        num3.addTextChangedListener {
            it?.let { string ->
                if (string.isNotEmpty()) {
                    num4.requestFocus()
                } else {
                    num2.requestFocus()
                }
            }
        }
        num4.addTextChangedListener {
            it?.let { string ->
                if (string.isEmpty()) {
                    num3.requestFocus()
                }
            }
        }
    }
}