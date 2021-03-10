package com.example.firebaseassigment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class MainActivity : AppCompatActivity() {


    lateinit var name: EditText
    lateinit var number: EditText
    lateinit var address: EditText

    lateinit var progressbar: ProgressBar

    lateinit var datatext: TextView
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = findViewById(R.id.username)
        number = findViewById(R.id.usernumber)
        address = findViewById(R.id.useradress)
        progressbar = findViewById(R.id.progressbar)
        datatext = findViewById(R.id.productsData)

        progressbar.visibility = View.GONE

        fetchData()
    }


    private  fun fetchData() {

        db.collection("contact")
                 .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        val eventList: MutableList<String?> = ArrayList()
                        for (doc in task.result!!) {
                             eventList.add(doc.data.toString())
                        }
                           datatext.text="$eventList"
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.exception)
                    }
                })

    }


    fun saveToFirebase(v: View) {

        var username = name.text.toString()
        var usernumber = number.text.toString()
        var useraddress = address.text.toString()
        var hashMap: HashMap<String, Any> = HashMap<String, Any>()
        hashMap["name"] = username
        hashMap["number"] = usernumber
        hashMap["address"] = useraddress
        db.collection("contact").add(hashMap)
                .addOnSuccessListener {

                    Thread(Runnable {

                        this@MainActivity.runOnUiThread(java.lang.Runnable {
                            progressbar.visibility = View.VISIBLE
                        })

                        try {
                            var i = 0;
                            while (i < Int.MAX_VALUE) {
                                i++
                            }
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        this@MainActivity.runOnUiThread(java.lang.Runnable {
                            progressbar.visibility = View.GONE
                        })
                    }).start()


                    Log.e("TAG", "Add")

                }.addOnFailureListener {
                    Log.e("TAG", "Failed")


                }


    }


}