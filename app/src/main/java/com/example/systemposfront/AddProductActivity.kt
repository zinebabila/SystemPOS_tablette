package com.example.systemposfront

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.systemposfront.bo.*
import com.example.systemposfront.controller.CategorieController
import com.example.systemposfront.controller.ProductController
import com.example.systemposfront.interfaces.AccountEnd
import com.example.systemposfront.security.TokenManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AddProductActivity : AppCompatActivity() {
    private var etName: EditText? = null
    private lateinit var imagefile:String
    val RESULT_LOAD_IMG = 1
    private var des: EditText? = null
    private var reduction: EditText? = null
    private var qtestock: EditText? = null
    private var etImageURL: ImageView? = null
    private var etPrice: EditText? = null
    private var cat: Spinner? = null
    private lateinit var importe:Button
    lateinit var session: TokenManager
    private var cats = listOf<Category>()
    private lateinit var apiCat: CategorieController
    private lateinit var apiProduct: ProductController
    private lateinit var category:Category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = TokenManager(applicationContext)
        setContentView(R.layout.activity_add_product)
        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        //  val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        // supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        //upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
        etName = findViewById(R.id.etName1)
        des = findViewById(R.id.descrip)
        reduction = findViewById(R.id.reduction)
        qtestock = findViewById(R.id.qtestock)
        etPrice = findViewById(R.id.Price)
        etImageURL=findViewById(R.id.imageProfil)
        cat = findViewById(R.id.dynamic_spinner)
        importe=findViewById(R.id.import_img)

     cats= getCtegories()

         category=Category()

        cat?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category.id=cats[position].id
                println("hereeeeeeeeeeeeeeeeeeeeeee"+cats[position].id)

            }

        }

        importe.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@AddProductActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askForwritePermission()


            }
            else{
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
            }
        })
        findViewById<View>(R.id.canc2).setOnClickListener {

            goToSecondActivity()
        }
        findViewById<View>(R.id.addP).setOnClickListener {
            if(CheckAllFields(etName!!,qtestock!!,etPrice!!)) {
                addProduct()
                goToSecondActivity()
            }
        }


    }

    private fun askForwritePermission() {
        ActivityCompat.requestPermissions(this,  arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);
    }

    private fun CheckAllFields(etName: EditText, qtestock: EditText, etPrice: EditText): Boolean {
        if (etName.length() == 0) {
            etName.error = "Title is required"
            return false
        }
        if (qtestock.length() == 0) {
            qtestock.error = "Stock is required"
            return false
        }
        if (etPrice.length() == 0) {
            etPrice.error = "Price is required"
            return false
        }


        return true

    }

    private fun goToSecondActivity( ) {
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)

    }
                 fun getCtegories():List<Category> {

                    AccountEnd.authToken = session.gettokenDetails()
                    apiCat = AccountEnd.retrofit.create(CategorieController::class.java)
            apiCat.getCategorie().enqueue(object : retrofit2.Callback<List<Category>> {
                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    println(t.message + "*******************************")
                    t.message?.let { Log.d("Data error", it) }
                }

                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk")
                     cats = response.body()!!


                    val arrayAdapter = ArrayAdapter(this@AddProductActivity, android.R.layout.simple_spinner_item, cats)
                    cat?.adapter  = arrayAdapter





                }
            })
                     return cats
        }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)

        if (reqCode==RESULT_LOAD_IMG&&resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                println((data.data!!).getPath()!!)
                imagefile= RealPathUtil.getRealPathFromURI_API11to18(this@AddProductActivity,data.data!!)
                var bitmap= BitmapFactory.decodeFile(imagefile)
                etImageURL?.setImageBitmap(bitmap)
            }
        }else {
            Toast.makeText(applicationContext, "Vous n'avez pas choisi d'image", Toast.LENGTH_LONG)
                .show()
        }
    }
                private fun addProduct() {
                    val file = File(imagefile)
                    val uploadFile = MultipartBody.Part.createFormData(
                        "imageFile",
                        file.name,
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    )

                    val requesttitre= RequestBody.create("multipart/form-data".toMediaTypeOrNull(),etName?.text.toString() )
                    val requestdes= RequestBody.create("multipart/form-data".toMediaTypeOrNull(),des?.text.toString() )
                    val requestred= RequestBody.create("multipart/form-data".toMediaTypeOrNull(), reduction?.text.toString())
                    val requestprix= RequestBody.create("multipart/form-data".toMediaTypeOrNull(), etPrice?.text.toString())
                    val requestqte= RequestBody.create("multipart/form-data".toMediaTypeOrNull(), qtestock?.text.toString())
                    val requestcat= RequestBody.create("multipart/form-data".toMediaTypeOrNull(), category.id.toString())
                    val requestmer= RequestBody.create("multipart/form-data".toMediaTypeOrNull(), session.getidAccount().toString())


                    AccountEnd.authToken = session.gettokenDetails()
                    apiProduct = AccountEnd.retrofit.create(ProductController::class.java)
                        apiProduct.addProduct(uploadFile,requesttitre,requestdes,requestred,requestqte,requestprix,requestcat,requestmer).enqueue(object : Callback<Product> {
                        override fun onResponse(call: Call<Product>, response: Response<Product>) {

                            print("okkkkkkkkkkkk")
                        }
                        override fun onFailure(call: Call<Product>, t: Throwable) {
                            println(t.message)
                        }
                    })


                }
}