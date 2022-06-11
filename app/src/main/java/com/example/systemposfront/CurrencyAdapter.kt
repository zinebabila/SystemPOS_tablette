package com.example.systemposfront

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import okhttp3.*
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.systemposfront.bo.*
import com.example.systemposfront.bo.Currency
import com.example.systemposfront.security.TokenManager
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import kotlinx.serialization.json.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xml.sax.Parser
import java.io.IOException
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class CurrencyAdapter(var context: Context, var currencys: List<Currency> = arrayListOf()) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {
    var coupon:Coupon?=null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CurrencyAdapter.ViewHolder {
        // The layout design used for each list item
        val view = LayoutInflater.from(context).inflate(R.layout.curency_item, p0, false)
        return ViewHolder(view)

    }
    override fun getItemCount(): Int = currencys.size

    override fun onBindViewHolder(viewHolder: CurrencyAdapter.ViewHolder, position: Int) {

        viewHolder.bindProduct(currencys[position])
        viewHolder.couponview=coupon


        // (context as ProfilActivity).coordinator

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var couponview:Coupon?=null
        var cart: CardView
        var itemImage: ImageView
        var itemTitle: TextView
        var curenc:Long = 0
        var nomcurenc:String ?= null
        var nomCurr:String?=null

        init {
            cart=itemView.findViewById(R.id.card)
            itemImage = itemView.findViewById(R.id.curency_image)
            itemTitle = itemView.findViewById(R.id.curency_name)

        }

        fun bindProduct(product: Currency) {

            itemTitle.text = "${product.currencyName.toString()}"

            Picasso.get().load(product.imageCurrency).fit().into(itemImage)

            curenc = product.id!!
            nomcurenc=product.symbol!!
            nomCurr=product.currencyName!!
            cart.setOnClickListener(OnClickListener {


                /*********************************************parsejson*********/
                run("http://api.coingecko.com/api/v3/simple/price?ids=Bitcoin%2CRavencoin%2CEthereum%2CTether%2Clitecoin&vs_currencies=usd")
                //  val parser:JSONObject= JSONObject(result)



            })


        }
        fun serialize(): String? {
            return Gson().toJson(this)
        }

        fun regler_payement():Data{
            var session: TokenManager
            session = TokenManager(itemView.context as ProfilActivity)
            var data=Data()
            data.account_id=session.getidAccount()
            data.currency_id=curenc
            var calendar = Calendar.getInstance()
            var  simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            var dateTime = simpleDateFormat.format(calendar.time).toString()
            data.date_commande=dateTime
            var items= ArrayList<CartItem>()
            for( cart in ShoppingCart.getCart()!!){
                items.add(cart)

            }
            data.products=items
            var totalPrice = calculerprix()
            data.somme=totalPrice
            print("*******************************************8")
            print(data.products[0].quantity)
            /*  var accou:DataController
              AccountEnd.authToken = session.gettokenDetails()
              accou = AccountEnd.retrofit.create(DataController::class.java)
              accou.addCommand(data).enqueue(object : Callback<Command> {
                  override fun onResponse(call: Call<Command>, response: Response<Command>) {

                      print("okkkkkkkkkkkk")
                  }
                  override fun onFailure(call: Call<Command>, t: Throwable) {
                      println(t.message)
                  }
              })*/
            return data
        }
        fun writeJSON(total: Double):JSONObject {
            var session: TokenManager


            session = TokenManager(itemView.context as ProfilActivity)


            var array=JSONArray()
            for( cart in ShoppingCart.getCart()!!){
                var objetcart=JSONObject()
                var objetpro=JSONObject()
                objetpro.put("id",cart.product.id)
                objetcart.put("product",objetpro)
                objetcart.put("quantity",cart.quantity)
                array.put(objetcart)
                // items.add(cart)

            }

            //  array.put(items)

            var totalPrice =calculerprix()
            val `object` = JSONObject()
            try {
                `object`.put("somme", totalPrice)
                if(couponview!=null){
                    `object`.put("sommef", calculerprixreduction(couponview!!.reduction).toDouble())
                }
                else{
                    `object`.put("sommef", totalPrice)
                }
                `object`.put("products", array)
                if(couponview?.id!=null){
                    `object`.put("coupon", couponview?.id)}
                else{
                    `object`.put("coupon", 0)
                }
                `object`.put("soldCurrency",total )
                println("heeeeeeeeeeeeeeeeeeeeeer")
                println(total)
                `object`.put("currency_id", curenc)
                `object`.put("currencyName", nomcurenc)
                `object`.put("account_id", session.getidAccount())

                `object`.put("accountFirstName", session.getfirst())
                `object`.put("accountlast_Name", session.getlast())


            } catch (e: JSONException) {
                e.printStackTrace()
            }
            println(`object`)
            return  `object`
        }

        fun calculerprixreduction(reduction: Double?):Double{
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
        fun calculerprix():Double {
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
            return  totalPrice
        }
        fun run(url: String) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    var str_response = response.body!!.string()
                    //creating json object
                    val parser: JSONObject = JSONObject(str_response)
                    var tether=parser.getJSONObject(nomCurr?.lowercase()).getString("usd").toDouble()

                    println("imppppppppppppppppppppppppppppppportant")
                    println(tether)
                    calculersommewalet(tether)



                    //creating json array
                }

            })

        }
        fun calculersommewalet(walet:Double){
            var total:Double?=0.0
            if(couponview!=null){
                total= calculerprixreduction(couponview!!.reduction)/walet
            }
            else{
                total= calculerprix()/walet
                println(total.toString()+"herrrrrrrrrrrrrrrrrrrrrrr")
            }
            total(total!!)
        }
        private fun total(total:Double) {

            /**********************afficher le code qr***********************/
            var totalPrice: String? =null
            if(couponview!=null){
                val df = DecimalFormat("0.00") // import java.text.DecimalFormat;

                totalPrice=   df.format(couponview?.reduction)
            }
            else{
                val df = DecimalFormat("0.00") // import java.text.DecimalFormat;

                totalPrice=   df.format(calculerprix())
            }
            val dialogBuilder = AlertDialog.Builder(itemView.context as ProfilActivity)
            dialogBuilder.setTitle("Checkout")
            val df = DecimalFormat("0.000000")

            dialogBuilder.setMessage("Total = "+totalPrice +"$\n total "+nomcurenc+"= "+df.format(total))


            val inflater = LayoutInflater.from(itemView.context as ProfilActivity)
            val dialogLayout = inflater.inflate(R.layout.qr_code, null)
            val image  = dialogLayout.findViewById<ImageView>(R.id.idIVQrcode)
            val multiFormatWriter = MultiFormatWriter()
            //   var string="currency="+curenc+"la somme="+totalPrice
            //var string=regler_payement()
            var string=writeJSON(total)
            // var json=JSONObject(string)

            var encrypted:String?=null
            try {
                encrypted = AESUtils.encrypt(string.toString())

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                val bitMatrix = multiFormatWriter.encode(
                    encrypted  ,
                    BarcodeFormat.QR_CODE,
                    500,
                    500
                )
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
                image!!.setImageBitmap(bitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            image.setVisibility(View.VISIBLE)

            dialogBuilder.setView(dialogLayout)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                // negative button text and action


                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            Handler(Looper.getMainLooper()).post {
                // write your code here


                val alert = dialogBuilder.create()
                // set title for alert dialog box
                // show alert dialog
                alert.show()}


        }
    }}




