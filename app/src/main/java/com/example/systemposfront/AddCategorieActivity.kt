package com.example.systemposfront

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.systemposfront.bo.*
import com.example.systemposfront.controller.CategorieController
import com.example.systemposfront.controller.ProductController
import com.example.systemposfront.interfaces.AccountEnd
import com.example.systemposfront.security.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCategorieActivity : AppCompatActivity() {
    private var etName: EditText? = null
    private lateinit var session:TokenManager
    private lateinit var apiCat:CategorieController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cat)
        session = TokenManager(applicationContext)
        etName = findViewById(R.id.etName2)
        val toolbar: Toolbar = findViewById(R.id.toolbar1)
        //  val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        // supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        //upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)


        findViewById<View>(R.id.canc).setOnClickListener {

            goToSecondActivity()
        }


        findViewById<View>(R.id.addCattt).setOnClickListener {
            if(CheckAllFields(etName!!)) {

                addCat()
                goToSecondActivity()
            }
        }


    }
    private fun goToSecondActivity( ) {


        val intent = Intent(this, ProfilActivity::class.java)
        startActivity(intent)



    }


    private fun addCat() {
        var p = Category()
       p.nameCategory=etName?.text.toString()
        AccountEnd.authToken = session.gettokenDetails()
        apiCat = AccountEnd.retrofit.create(CategorieController::class.java)
        apiCat.addCat(p).enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {

                print("okkkkkkkkkkkk")
            }
            override fun onFailure(call: Call<Category>, t: Throwable) {
                println(t.message)
            }
        })


    }
    private fun CheckAllFields(etName: TextView): Boolean {
        if (etName.length() == 0) {
            etName.error = "Title is required"
            return false
        }

        return true
    }
}