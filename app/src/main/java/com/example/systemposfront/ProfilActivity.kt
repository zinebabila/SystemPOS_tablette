package com.example.systemposfront

import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.systemposfront.bo.*
import com.example.systemposfront.bo.Currency
import com.example.systemposfront.controller.CategorieController
import com.example.systemposfront.controller.CouponController
import com.example.systemposfront.controller.MerchantController
import com.example.systemposfront.controller.ProductController
import com.example.systemposfront.interfaces.AccountEnd
import com.example.systemposfront.security.TokenManager
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ProfilActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var cart_size:TextView
    lateinit var session: TokenManager
    lateinit var valider: Button
    lateinit var couponcode:TextView
    private  var coupon : Coupon?=null
    private lateinit var apiService: ProductController
    private lateinit var apiServiceCoupon: CouponController
    private lateinit var cat: CategorieController
    private lateinit var productAdapter: ProductAdapter
    private lateinit var drawer: DrawerLayout
    private lateinit var apimerchant:MerchantController
    private lateinit var toggle: ActionBarDrawerToggle
    private var cats = listOf<Category>()
    private var products = listOf<Product>()
    lateinit var menuNav: Menu
    lateinit var mNavigationView: NavigationView
    lateinit var adapter: ShoppingCartAdapter
    lateinit var  total_price:TextView
    lateinit var checkout: Button
    private lateinit var apiServiceMer: MerchantController
    private lateinit var merchant : Merchant
    private  var currency= listOf<Currency>()

    private lateinit var curencyAdapter: CurrencyAdapter
    lateinit var currencys_recyclerview: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
        session = TokenManager(applicationContext)
        setContentView(R.layout.activity_profil)
      mNavigationView  = findViewById(R.id.nav_view)
     menuNav    = mNavigationView.menu
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        mNavigationView.setNavigationItemSelectedListener(this@ProfilActivity)
        val header = mNavigationView.getHeaderView(0)
        var imagePro=header.findViewById<ImageView>(R.id.nav_header_imageView)
        var detail=header.findViewById<TextView>(R.id.nav_header_textView)
        println(intent.hasExtra("action"))
        if (intent.hasExtra("action")) {

            var str = intent.getStringExtra("action");
            println(str)

            if(str =="succes") {
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(this@ProfilActivity)

                builder.setMessage("success")
                builder.setTitle("payment success !")
                builder.setCancelable(false)
                    .setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                            goParent()
                        })
                val alert = builder.create()
                alert.show()
            }
            if(str =="faillure") {
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(this@ProfilActivity)

                builder.setMessage("faillure")
                builder.setTitle("echec !")
                builder.setCancelable(false)
                    .setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                            goParentsho()
                        })
                val alert = builder.create()
                alert.show()
            }

        }

        /**************************************les information du compte***************************************************/
        AccountEnd.authToken = session.gettokenDetails()
        if(session.gettypeAccount()=="M") {
            apimerchant = AccountEnd.retrofit.create(MerchantController::class.java)
            apimerchant.getMerchant(session.getidAccount()).enqueue(object : retrofit2.Callback<Merchant> {
                override fun onResponse(call: Call<Merchant>, response: Response<Merchant>) {
                    if (response.body() != null) {
                        var merchant = response.body()!!

                        Picasso.get().load(merchant.urlImage).fit().into(imagePro)
                        detail.text =
                            merchant.firstName + "  " + merchant.lastName + "\n" + merchant.numTel
                        session.addinfo(merchant.firstName!!,merchant.lastName!!)

                    } else {
                        println("error")
                    }

                }

                override fun onFailure(call: Call<Merchant>, t: Throwable) {
                    println(t.message)
                }

            })
        }
        else{
            println("n'est pas merchant est un caissier")
        }


        /****************************les categories************************************/
        AccountEnd.authToken = session.gettokenDetails()
        cat = AccountEnd.retrofit.create(CategorieController::class.java)
        getCtegories()

        /***************************les produits****************************************/
        apiService = AccountEnd.retrofit.create(ProductController::class.java)
        getProducts()

        /*************************la cart********************************/
        var list: MutableList<CartItem>? =ShoppingCart.getCart()
        if(list!=null){
            adapter = ShoppingCartAdapter(this, list)}
        adapter.notifyDataSetChanged()
        var shopping_cart_recyclerView: RecyclerView = findViewById(R.id.shopping_cart_recyclerView)
        shopping_cart_recyclerView.adapter = adapter

        shopping_cart_recyclerView.layoutManager = LinearLayoutManager(this)

        modifierprix()



        valider=findViewById(R.id.validercoupon)
        valider.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View?) {
                couponcode=findViewById(R.id.coupncode)



                AccountEnd.authToken=session.gettokenDetails()
                apiServiceCoupon = AccountEnd.retrofit.create(CouponController::class.java)
                getCoupon(couponcode.text.toString())



            }
        })


        checkout=findViewById(R.id.chekout)
        checkout.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View?) {
                AccountEnd.authToken=session.gettokenDetails()
                apiServiceMer = AccountEnd.retrofit.create(MerchantController::class.java)

                afficher_devise()




                val eventSourceListener = object : EventSourceListener() {
                    override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
                        super.onOpen(eventSource, response)
                        Log.d(ContentValues.TAG, "Connection Opened")
                        println("Connection Opened")
                    }

                    override fun onClosed(eventSource: EventSource) {
                        super.onClosed(eventSource)
                        Log.d(ContentValues.TAG, "Connection Closed")
                        println("Connection Closed")
                    }

                    override fun onEvent(
                        eventSource: EventSource,
                        id: String?,
                        type: String?,
                        data: String
                    ) {
                        super.onEvent(eventSource, id, type, data)
                        Log.d(ContentValues.TAG, "On Event Received! Data -: $data")
                        println(data)


                        if(!data.equals("connexion")) {
                            var json = JSONObject(data)
                            if (json.getInt("type") == 1) {
                                goparent(
                                    json.getString("firstname"),
                                    json.getString("lastName"),
                                    json.getString("nameCurency"),
                                    json.getString("somme"),
                                )

                            }
                            if (json.getInt("type") == 0) {
                                println("faillure")
                                goechec()
                            }
                        }
                    }

                    override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) {
                        super.onFailure(eventSource, t, response)
                        Log.d(ContentValues.TAG, "On Failure -: ${response?.body}")
                        //     println(t!!.message)
                    }
                }

                val client = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .build()

                val request = Request.Builder()
                    .url("http://192.168.86.23:9090/data/subscribes")
                    // .header("Accept", "application/json; q=0.5")
                    // .addHeader("Accept", "text/event-stream")

                    .build()
                println("hello i am here")

                EventSources.createFactory(client)
                    .newEventSource(request = request, listener = eventSourceListener)

            }
        })


        /* val showCart : FloatingActionButton =findViewById(R.id.basketButton)
       // ShoppingCart.deleteCart()
         showCart.setOnClickListener {
             startActivity(Intent(this, ShoppingCartActivity::class.java))
         }*/


    }




    private fun goParentsho() {
        val intent = Intent(this, ProfilActivity::class.java)
        startActivity(intent)
    }

    private fun goParent() {
        val intent = Intent(this, ProfilActivity::class.java)
        startActivity(intent)
    }
    private fun goechec() {
        val bundle = Bundle()
        val intent = Intent(this,ProfilActivity::class.java)
        bundle.putString("action", "faillure")
        intent.putExtras(bundle)

        startActivity(intent)
    }

    private fun goparent(string: String, string1: String, string2: String, string3: String) {
        ShoppingCart.deleteCart()
        val bundle = Bundle()
        val intent = Intent(this, ProfilActivity::class.java)
        bundle.putString("action", "succes")
        bundle.putString("firstname", string)
        bundle.putString("lastName", string1)
        bundle.putString("nameCurency", string2)
        bundle.putString("somme", string3)
        intent.putExtras(bundle)

        startActivity(intent)
    }


    private fun getCoupon( code:String) {

        apiServiceCoupon.getCouponCode(code).enqueue(object : retrofit2.Callback<Coupon> {
            // apiService.getCategorie().enqueue(object : retrofit2.Callback<List<Category>> {
            override fun onFailure(call: Call<Coupon>, t: Throwable) {

                println(t.message + "*******************************")
                println("null")
                t.message?.let { Log.d("Data error", it) }
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@ProfilActivity)
                builder.setMessage("Wrong Code ?")
                builder.setTitle("Alert !")
                builder.setCancelable(false)
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })
                val alert = builder.create()
                alert.show()

            }

            override fun onResponse(call: Call<Coupon>, response: Response<Coupon>)
            {

                coupon = response.body()!!
                println(coupon)
                println("here")
                var  simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val formattedDate = simpleDateFormat.parse(coupon!!.expirationDate)
                var calendar = Calendar.getInstance()
                var dateTime = simpleDateFormat.format(calendar.time)
                var acutel=simpleDateFormat.parse(dateTime)

                if(formattedDate.before(acutel)){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@ProfilActivity)
                    builder.setMessage("Code is expired ?")
                    builder.setTitle("Alert !")
                    builder.setCancelable(false)
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                                dialog, id -> dialog.cancel()
                        })
                    val alert = builder.create()
                    alert.show()
                }
                else{
                    if(coupon!!.num_Uses==coupon!!.maxnum_Uses){
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@ProfilActivity)
                        builder.setMessage("Copon Coupon is cannot be used!!")
                        builder.setTitle("Alert !")
                        builder.setCancelable(false)
                            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                                    dialog, id -> dialog.cancel()
                            })
                        val alert = builder.create()
                        alert.show()
                    }


            else {


                        var prix = calculerprix(coupon!!.reduction)
                        val df = DecimalFormat("0.00") // import java.text.DecimalFormat;
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@ProfilActivity)
                        builder.setMessage(
                            "Code is validated \n the total price is " + df.format(
                                prix
                            ) + "$"
                        )
                        builder.setTitle("Success !")
                        builder.setCancelable(false)
                            .setNegativeButton(
                                "Cancel",
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })
                        val alert = builder.create()
                        alert.show()

                        modifierprixApresCoupon(df.format(prix))
                    }

                }
            }

        })}
    private fun modifierprixApresCoupon(totalPrice: String) {

        total_price=findViewById(R.id.total_price)
        total_price.text = "$${totalPrice}"

    }
    fun calculerprix(reduction: Double?):Double{
        var totalPrice:Double=0.0
        for(cart in ShoppingCart.getCart()!!) {
            var prixremise:Double
            if(cart.product.reduction!! >0){
                prixremise= cart.product.prix?.minus(((cart.product.prix!! * cart.product.reduction!!)/100))!!
            }
            else{
                prixremise= cart.product.prix!!
            }

            totalPrice += prixremise * cart.quantity

        }
        totalPrice -= ((totalPrice * reduction!!) / 100)

        return totalPrice
    }
    private fun afficher_devise() {
        apiServiceMer.getMerchant(1).enqueue(object : retrofit2.Callback<Merchant> {
            // apiService.getCategorie().enqueue(object : retrofit2.Callback<List<Category>> {
            override fun onFailure(call: Call<Merchant>, t: Throwable) {

                println(t.message + "*******************************")
                t.message?.let { Log.d("Data error", it) }

            }

            override fun onResponse(call: Call<Merchant>, response: Response<Merchant>)
            {
                merchant = response.body()!!
                println(merchant)
                println(merchant.currencies)


                val dialogBuilder = AlertDialog.Builder(this@ProfilActivity)
                dialogBuilder.setTitle("Currency choice")
                // set message of alert dialog

                val inflater = layoutInflater
                val dialogLayout  = inflater.inflate(R.layout.currency_rececle, null)
                val inc  = dialogLayout.findViewById<RecyclerView>(R.id.currency_rec)
                inc.layoutManager=StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                curencyAdapter = CurrencyAdapter(this@ProfilActivity, merchant.currencies)
                inc.adapter = curencyAdapter
                curencyAdapter.coupon=coupon
                dialogBuilder.setView(dialogLayout)
                    // if the dialog is cancelable
                    .setCancelable(true)

                    // positive button text and action
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                // show alert dialog
                alert.show()
            }

        })
    }


    private fun getCtegories() {
            cat.getCategorie().enqueue(object : retrofit2.Callback<List<Category>> {
                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    println(t.message + "*******************************")
                    t.message?.let { Log.d("Data error", it) }
                }

                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk")
                    cats = response.body()!!
                    println(cats)
                    menuNav.add(0, 0,0,"Tous Categorie")

                    for (name in cats) {
                        println("${name.nameCategory}" + "****************************************************************")
                          menuNav.add(0, "${name.id?.toInt()}".toInt(),0,"${name.nameCategory}")
                        // menuNav.add("${name.nameCategory}")

                    }
            }
        })
    }


        fun getProducts() {
            val recyclerView = findViewById<RecyclerView>(R.id.products_recyclerview)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            apiService.getProducts().enqueue(object : retrofit2.Callback<ArrayList<Product>> {
                // apiService.getCategorie().enqueue(object : retrofit2.Callback<List<Category>> {
                override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {

                    println(t.message + "*******************************")
                    t.message?.let { Log.d("Data error", it) }

                }

                override fun onResponse(
                    call: Call<ArrayList<Product>>,
                    response: Response<ArrayList<Product>>
                ) {

                    products = response.body()!!
                    println(products)
                    productAdapter = ProductAdapter(products as ArrayList<Product>)
                    recyclerView.adapter = productAdapter

                }

            })

        }

        override fun onPostCreate(savedInstanceState: Bundle?) {
            super.onPostCreate(savedInstanceState)
            toggle.syncState()
        }
    /*search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
        var timer = Timer()

        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            timer.cancel()
            val sleep = when(newText.length) {
                1 -> 1000L
                2,3 -> 700L
                4,5 -> 500L
                else -> 300L
            }
            timer = Timer()
           /* timer.schedule(sleep) {
                if (!newText.isNullOrEmpty()) {
                    // search
                }*/
            }
            return true
        }

    })
*/

    private fun getproductcat(id: Long): Int {
        val recyclerView = findViewById<RecyclerView>(R.id.products_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        apiService.getProductCat(id).enqueue(object : retrofit2.Callback<ArrayList<Product>> {
            // apiService.getCategorie().enqueue(object : retrofit2.Callback<List<Category>> {
            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {

                println(t.message + "*******************************")
                t.message?.let { Log.d("Data error", it) }

            }

            override fun onResponse(call: Call<ArrayList<Product>>, response: Response<ArrayList<Product>>)
            {
                products = response.body()!!
                println(products)
                productAdapter = ProductAdapter(products as ArrayList<Product>)
                recyclerView.adapter = productAdapter
            }

        })
        return 1
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId!=R.id.pp){
            if(item.itemId==0){
                apiService = AccountEnd.retrofit.create(ProductController::class.java)
                getProducts()
                drawer.closeDrawer(GravityCompat.START)
                return true
            }
            else{
                AccountEnd.authToken=session.gettokenDetails()
                apiService = AccountEnd.retrofit.create(ProductController::class.java)
                getproductcat(item.itemId.toLong())
                drawer.closeDrawer(GravityCompat.START)
                return true}
        }

        return false
    }

    fun refreshActivtiy() {
        recreate();
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchItem = menu.findItem(R.id.appSearchBar)
        val searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("newText1", query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("newText", newText)
                productAdapter.getFilter().filter(newText)
                return false
            }
        })
        return true
    }

    fun modifiercounter() {
        cart_size.text = ShoppingCart.getShoppingCartSize().toString()
        println(ShoppingCart.getShoppingCartSize().toString()+"--------------------------------------")
    }


    fun modifierprix() {
        var totalPrice:Double=0.0
        for(cart in ShoppingCart.getCart()!!) {
            var prixremise:Double
            if(cart.product.reduction!! >0){
                prixremise= cart.product.prix?.minus(((cart.product.prix!! * cart.product.reduction!!)/100))!!
            }
            else{
                prixremise= cart.product.prix!!
            }

            totalPrice += prixremise * cart.quantity

        }
        val df = DecimalFormat("0.00") // import java.text.DecimalFormat;

        total_price=findViewById(R.id.total_price)
        total_price.text = df.format(totalPrice)
    }
    /*   fun withEditText(view: View) {
           val builder = AlertDialog.Builder(this)
           val inflater = layoutInflater
           builder.setTitle("With EditText")
           val dialogLayout = inflater.inflate(R.layout.dialog_signin, null)
           val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
           builder.setView(dialogLayout)
           builder.setPositiveButton("OK") { dialogInterface, i -> Toast.makeText(applicationContext, "EditText is " + editText.text.toString(), Toast.LENGTH_SHORT).show() }
           builder.show()
       }*/
    /* override fun onOptionsItemSelected(item: MenuItem?): Boolean {
     if (toggle.onOptionsItemSelected(item)) {
         return true
     }
     return super.onOptionsItemSelected(item)
 }
 override fun onConfigurationChanged(newConfig: Configuration?) {
     if (newConfig != null) {
         super.onConfigurationChanged(newConfig)
     }
     toggle.onConfigurationChanged(newConfig)
 }

*/
    }
