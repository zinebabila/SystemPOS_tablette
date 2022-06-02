package com.example.systemposfront.bo
import android.content.Context
import android.widget.Toast
import io.paperdb.Paper


class ShoppingCart {

    companion object {
        fun addItem(cartItem: CartItem, quantit:Int) {
            val cart = ShoppingCart.getCart()

            val targetItem = cart?.singleOrNull { it.product?.id == cartItem.product?.id }
            if (targetItem == null) {
                cartItem.quantity=cartItem.quantity+quantit
                if (cart != null) {
                    cart.add(cartItem)
                }
            } else {

                targetItem.quantity=targetItem.quantity+quantit
            }
            if (cart != null) {
                ShoppingCart.saveCart(cart)
            }
        }

        fun removeItem(cartItem: CartItem, context: Context) {
            val cart = ShoppingCart.getCart()

            val targetItem = cart?.singleOrNull { it.product?.id == cartItem.product?.id }

            if (cart != null) {
                cart.remove(targetItem)
                ShoppingCart.saveCart(cart)
            }

        }

        fun saveCart(cart: MutableList<CartItem>) {
            Paper.book().write("cart", cart)
        }

        fun getCart(): MutableList<CartItem>? {
            return Paper.book().read("cart", mutableListOf())
        }
        fun deleteCart() {
            return Paper.book().delete("cart")
        }

        fun getShoppingCartSize(): Int {
            var cartSize = 0
            ShoppingCart.getCart()?.forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }
         fun modifier(cart: CartItem, quantity: Int) {
            val list=ShoppingCart.getCart()
            if (list != null) {
                for(l in list){

                    if(l.product?.id == cart.product?.id) {
                        println(quantity)
                        l.quantity = quantity
                    }
                }
            }
             if (cart != null) {
                 if (list != null) {
                     ShoppingCart.saveCart(list)
                 }
             }
        }
    }

}