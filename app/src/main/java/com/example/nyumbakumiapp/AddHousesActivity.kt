package com.example.nyumbakumiapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.FirebaseStorageKtxRegistrar
import java.io.IOException
import java.lang.ref.PhantomReference

class AddHousesActivity : AppCompatActivity() {
    lateinit var edtHouseNumber:EditText
    lateinit var edtHouseSize:EditText
    lateinit var edtHousePrice:EditText
    lateinit var imageView:ImageView
    lateinit var btnUpload:Button
    lateinit var progress:ProgressDialog
    val PICK_IMAGE_REQUEST = 100
    lateinit var filepath:Uri
    lateinit var firebaseStore: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_houses)
        edtHouseNumber = findViewById(R.id.medtNumber)
        edtHouseSize = findViewById(R.id.medtSize)
        edtHousePrice = findViewById(R.id.edtPrice)
        imageView = findViewById(R.id.edtimage)
        btnUpload = findViewById(R.id.btnupload)
        progress = ProgressDialog(this)
        progress.setTitle("Uploading...")
        progress.setMessage("PLease Wait...")

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = firebaseStore.getReference()
        mAuth = FirebaseAuth.getInstance()
        //open to select image

        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "select House Picture"),
            PICK_IMAGE_REQUEST
        )

        btnUpload.setOnClickListener {
            var houseNumber = edtHouseNumber.text.toString().trim()
            var houseSize = edtHouseSize.text.toString().trim()
            var housePrice = edtHousePrice.text.toString().trim()
            var imageid = System.currentTimeMillis().toString()
            var userid = mAuth.currentUser?.uid
            //check if the user is submitting empty files
            if (houseNumber.isEmpty()){
                edtHouseNumber.setError("Please fill this input")
                edtHouseNumber.requestFocus()
            }else if (houseSize.isEmpty()){
                edtHouseSize.setError("Please fill this input")
                edtHouseSize.requestFocus()
            }else if (housePrice.isEmpty()){
                edtHousePrice.setError("please fill this input")
                edtHousePrice.requestFocus()
            }else{
                //Proceed to upload data
                if (filepath == null){
                    Toast.makeText(this, "choose Image", Toast.LENGTH_SHORT).show()

                }else{
                    var ref = storageReference.child("House/$imageid")
                    progress.show()
                    ref.putFile(filepath).addOnCompleteListener{
                        progress.dismiss()
                        if (it.isSuccessful){
                            //proceed to store other data into db
                            ref.downloadUrl.addOnCompleteListener{
                                var imageUrl = it.toString()
                                var houseData = House(houseNumber,houseSize,housePrice,userid!!,imageid,imageUrl)
                                var dbRef = FirebaseDatabase.getInstance()
                                    .getReference().child("House/$imageid")
                                dbRef.setValue(houseData)
                                Toast.makeText(applicationContext, "Upload successful", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "it.exception!!.message",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK)
            if (data == null || data.data == null){
                return
            }
        filepath = data!!.data!!
        try {
            val bitmap = MediaStore.Images.Media.
            getBitmap(contentResolver,filepath)
            imageView.setImageBitmap(bitmap)

        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}