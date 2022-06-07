package com.example.systemposfront

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.systemposfront.bo.CartItem
import com.example.systemposfront.bo.Product
import com.example.systemposfront.bo.ShoppingCart
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

class ProductAdapter(itemList: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>(), Filterable {
    val  dataSet = itemList
    val  FullList = ArrayList<Product>(itemList)

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        var itemremise:TextView
        var priceremise:TextView
        var itemAdd: AppCompatImageButton


        init {
            itemImage = itemView.findViewById(R.id.product_image)
            itemTitle = itemView.findViewById(R.id.product_name)
            itemDetail = itemView.findViewById(R.id.product_price)
            itemremise = itemView.findViewById(R.id.remise)
            priceremise = itemView.findViewById(R.id.product_priceremise)
            itemAdd = itemView.findViewById(R.id.addToCart)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.product_row_item,
            parent, false
        )
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem: Product = dataSet[position]

        holder.itemTitle.text = "${currentItem.title.toString()}"
        if (currentItem.reduction!! > 0) {
            holder.itemDetail.text = "${currentItem.prix.toString()}$"
            holder.priceremise.text =
                (currentItem.prix?.minus(((currentItem.prix!! * currentItem.reduction!!) / 100))).toString() + "$"
            holder.itemremise.text = "${currentItem.reduction.toString()}"
            holder.itemremise.setBackgroundResource(R.drawable.back)

        } else {
            holder.itemDetail.text = "${currentItem.prix.toString()}$"
        }

        Picasso.get().load(currentItem.images[0].urlImage).fit().into(holder.itemImage)
        if (currentItem.qteStock != 0) {
            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {

                holder.itemAdd.setOnClickListener { view ->
                    val dialog = Dialog(holder.itemView.context as ProfilActivity, R.style.DialogStyle)
                    dialog.setContentView(R.layout.dialog_signin)
                    dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_window)
                    var texttitle:TextView=dialog.findViewById(R.id.txttiteq)
                    texttitle.text="Quantity choice"

                    var textview:TextView=dialog.findViewById(R.id.txtDescq)
                    textview.text="You want to add ${currentItem.title} to your cart" +
                            "choose quantity"
                    val inc = dialog.findViewById<LinearLayout>(R.id.inc)
                    val t2 = dialog.findViewById<TextView>(R.id.t2)
                    val t1 = dialog.findViewById<TextView>(R.id.t1)
                    val t3 = dialog.findViewById<TextView>(R.id.t3)
                    val btnClose: ImageView = dialog.findViewById(R.id.btn_closeq)
                    btnClose.setOnClickListener(View.OnClickListener { dialog.dismiss()
                    })
                    var btnYes:Button=dialog.findViewById(R.id.btn_yesq)
                    btnYes.setOnClickListener(View.OnClickListener {
                        dialog.dismiss()


                        val item = CartItem(currentItem)
                        println(t2.text.toString().toInt())
                        val cart = ShoppingCart.getCart()
                        val targetItem = cart?.singleOrNull { it.product?.id == currentItem?.id }
                        if (targetItem != null) {
                        if(targetItem.quantity+t2.text.toString().toInt()<=currentItem.qteStock){
                            Toast.makeText(
                                (holder.itemView.context as ProfilActivity).applicationContext,
                                t2.text.toString() + "  ${currentItem.title} added to your cart ",
                                Toast.LENGTH_SHORT
                            ).show()
                        ShoppingCart.addItem(item, t2.text.toString().toInt())
                        (holder.itemView.context as ProfilActivity).refreshActivtiy()}
                        else{
                            dialog.dismiss()
                            val dialog = Dialog(holder.itemView.context as ProfilActivity, R.style.DialogStyle)
                            dialog.setContentView(R.layout.layout_custom_dialog)
                            dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_window)
                            var texttitle:TextView=dialog.findViewById(R.id.txttite)
                            texttitle.text="Alert !"

                            var textview:TextView=dialog.findViewById(R.id.txtDesc)
                            textview.text="No enought Stock"
                            val btnClose: ImageView = dialog.findViewById(R.id.btn_close)

                            btnClose.setOnClickListener(View.OnClickListener { dialog.dismiss()
                            })
                            var btnYes:Button=dialog.findViewById(R.id.btn_yes)
                            btnYes.setOnClickListener(View.OnClickListener {
                                dialog.dismiss()
                            } )
                            dialog.show()
                        }
                    }
                    else{
                            Toast.makeText(
                                (holder.itemView.context as ProfilActivity).applicationContext,
                                t2.text.toString() + "  ${currentItem.title} added to your cart ",
                                Toast.LENGTH_SHORT
                            ).show()
                            ShoppingCart.addItem(item, t2.text.toString().toInt())
                            (holder.itemView.context as ProfilActivity).refreshActivtiy()
                        }

                    } )
                    dialog.show()



                    t1.setOnClickListener(View.OnClickListener {
                        var y: Int = t2.getText().toString().toInt()
                        y--
                        if(t2.getText().toString().toInt()>1) {
                            t2.setText(y.toString())
                        }
                    })

                    t3.setOnClickListener(View.OnClickListener {
                        var y: Int = t2.getText().toString().toInt()
                        y++
                        if (y > currentItem.qteStock) {
                            val builder: AlertDialog.Builder =
                                AlertDialog.Builder(holder.itemView.context as ProfilActivity)
                            builder.setMessage("No enought stock ?")
                            builder.setTitle("Alert !")
                            builder.setCancelable(false)
                                .setNegativeButton(
                                    "Cancel",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        dialog.cancel()
                                    })
                            val alert = builder.create()
                            alert.show()
                        } else {
                            t2.setText(y.toString())
                        }

                        // (itemView.context as ProfilActivity).modifiercounter()
                        // cart.quantity=t2.text.toString().toInt()


                    })


                }

            }).subscribe { cart ->

                var quantity = 0

                cart.forEach { cartItem ->

                    quantity += cartItem.quantity
                }

                (holder.itemView.context as ProfilActivity).cart_size.text = quantity.toString()

                Toast.makeText(holder.itemView.context, "Cart size $quantity", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        else{
            holder.itemAdd.setOnClickListener { view ->

                val dialog = Dialog(holder.itemView.context as ProfilActivity, R.style.DialogStyle)
                dialog.setContentView(R.layout.layout_custom_dialog)
                dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.bg_window)
                var texttitle:TextView=dialog.findViewById(R.id.txttite)
                texttitle.text="Alert !"

                var textview:TextView=dialog.findViewById(R.id.txtDesc)
                textview.text="Product is out of stock"
                val btnClose: ImageView = dialog.findViewById(R.id.btn_close)

                btnClose.setOnClickListener(View.OnClickListener { dialog.dismiss()
                })
                var btnYes:Button=dialog.findViewById(R.id.btn_yes)
                btnYes.setOnClickListener(View.OnClickListener {
                    dialog.dismiss()
                } )
                dialog.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getFilter(): Filter {
        return Searched_Filter
    }

    private val Searched_Filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: ArrayList<Product> = ArrayList<Product>()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(FullList)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in FullList) {
                    if (item.title?.toLowerCase()?.contains(filterPattern) == true) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            dataSet?.clear()
            dataSet?.addAll((results.values as ArrayList<Product>))
            notifyDataSetChanged()
        }
    }


}