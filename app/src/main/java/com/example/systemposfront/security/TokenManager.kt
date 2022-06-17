package com.example.systemposfront.security

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.systemposfront.LoginActivity
import com.example.systemposfront.bo.Role


class TokenManager {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context
    var PRIVATE_MODE: Int =0
    constructor (con: Context) {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
    companion object {
        val PREF_NAME: String = "KotlinDemo"
        val IS_LOGIN: String = "isLoggedIn"
        val KEY_NAME: String ="token"
        val KEY_Id: String ="id"
        val KEY_Type: String ="type"
        val KEY_first: String ="First"
        val KEY_last: String ="last"
    }
    fun createloginsession (name: String, id: Long, role: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_NAME, name)
        editor.putLong(KEY_Id,id)
    editor.putString(KEY_Type,role)
        editor.commit()
    }
    fun checklogin(){
        if(!this.logg()){
            var i:Intent= Intent(con,LoginActivity::class.java)
            con.startActivity (i)
        }}
    fun gettokenDetails(): String{
        return pref.getString(KEY_NAME,"").toString()

    }
    fun getRole():String{
        return pref.getString(KEY_Type,"").toString()
    }
    fun getidAccount(): Long{
        return pref.getLong(KEY_Id,0)

    }
    fun getfirst(): String{
        return pref.getString(KEY_first,"").toString()

    }
    fun getlast(): String{
        return pref.getString(KEY_last,"").toString()

    }
    fun Logoutlser (){
        editor.clear ()
        editor.commit()
        var i: Intent =Intent (con, LoginActivity::class.java)
        con.startActivity (i)

    }
    fun logg():Boolean{
        return  pref.getBoolean(IS_LOGIN,false)
    }
    fun addinfo(first:String,last:String){
        editor.putString(KEY_first,first)
        editor.putString(KEY_last,last)
        editor.commit()
    }
}