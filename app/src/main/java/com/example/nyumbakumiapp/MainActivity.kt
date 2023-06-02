package com.example.nyumbakumiapp


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class

MainActivity : AppCompatActivity() {
    lateinit var edtemail:EditText
    lateinit var edtpassword:EditText
    lateinit var btnreg:Button
    lateinit var txtlog:TextView
    lateinit var progress:ProgressDialog
    lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtemail = findViewById(R.id.edtemail)
        edtpassword = findViewById(R.id.edtpassword)
        btnreg = findViewById(R.id.btnreg)
        txtlog = findViewById(R.id.txtlog)
        mAuth = FirebaseAuth.getInstance()
        progress = ProgressDialog(this,)
        progress.setTitle("Loading")
        progress.setMessage("Please wait ...")
        btnreg.setOnClickListener {

            var email = edtemail.text.toString().trim()
            var password = edtpassword.text.toString().trim()

            // Check if the user is submitting empty files

            if (email.isEmpty()){
                edtemail.setError("Please fill this input")
                edtemail.requestFocus()
            }else if(password.isEmpty()){
                edtpassword.setError("Please fill the input")
                edtpassword.requestFocus()
            }else if(password.length < 6) {
                edtpassword.setError("Password is too short")
                edtpassword.requestFocus()
            }else{
                // Proceed to register  the user
                progress.show()
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    progress.dismiss()
                    if (it.isSuccessful){
                        Toast.makeText(this, "Registrationn successful",
                            Toast.LENGTH_SHORT).show()
                        mAuth.signOut()
                        startActivity(Intent(this, Login_Activity::class.java))
                        finish()

                    }else{
                        displaymessage("Error", it.exception!!.message.toString())
                    }
                }
            }
        }
        txtlog.setOnClickListener {
            startActivity(Intent(this,Login_Activity::class.java))
        }
    }
    fun displaymessage(title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Ok",null)
        alertDialog.create().show()
    }
}