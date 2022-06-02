package com.example.systemposfront



 import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.systemposfront.bo.JwtRequest
import com.example.systemposfront.bo.JwtResponse
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.interfaces.AccountEnd
import com.example.systemposfront.interfaces.AuthenEnd
import com.example.systemposfront.security.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
   public val MyPREFERENCES:String? = "MyPrefs"
    lateinit var preferences: TokenManager
    private var button_login_login: Button? = null
    private var editText_login_username: EditText? = null
    private var editText_login_password: EditText? = null
    private var singupbtn:Button?=null
    private var username: String? = null
    private var password: String? = null
    private var baseUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    preferences= TokenManager(applicationContext)
         baseUrl=" ********* "
        editText_login_password=findViewById(R.id.password) as EditText
        editText_login_username=findViewById(R.id.email)as EditText
        button_login_login=findViewById(R.id.loginbtn) as Button
        singupbtn=findViewById(R.id.Signbtn) as Button
        singupbtn!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?) {
               goToSingUpActivity()
            }

        })

        button_login_login!!.setOnClickListener(object : View.OnClickListener {


            override  fun onClick(view: View?) {

                if(CheckAllFields(editText_login_password!!, editText_login_username!!)){
                    username = editText_login_username!!.getText().toString()
                    password = editText_login_password!!.getText().toString()
                    var loginRegistration: JwtRequest = setLoginRegistrationData()
                    val registrationEndPoint: AccountController = AuthenEnd.retrofit.create(
                        AccountController::class.java
                    )
                    var addNewUser: Call<JwtResponse> = registrationEndPoint.login(loginRegistration)

                    addNewUser.enqueue(object : Callback<JwtResponse> {
                        override fun onResponse(call: Call<JwtResponse>, response: Response<JwtResponse>) {
                           println("okkkkkkkkkkk")
                          val jwt: JwtResponse
                           // Log.d(TAG, "ResponseBody: ${response.body().toString()}")
                            if(response.body()==null){
                                goLogin()
                            }
                            else{
                            jwt =  response.body()!!
                            println(jwt.jwtToken.toString()+"*************")
                          preferences.createloginsession(jwt.jwtToken.toString(), jwt.user?.id!!,jwt.user?.type!!)

                            goToSecondActivity()}
                        }

                        override fun onFailure(call: Call<JwtResponse>, t: Throwable) {
                           println(t.message)
                        }
                    })



                    }
   else{
       if(!editText_login_username!!.isEmailValid())
       editText_login_username!!.error=getString(R.string.error)

     //  editText_login_password!!.backgroundTintList= resources.getColor(R.color.red)
                }

                               }

        })



}

    private fun goLogin() {
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("Email and password failed ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                    dialog, id -> finish()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Failed")
        // show alert dialog
        alert.show()

    }

    private fun setLoginRegistrationData(): JwtRequest {
var account=JwtRequest()
account.userName=username;
        account.userPassword=password
        println(account.toString()+"*******************************")
        return account

    }

    private fun goToSingUpActivity() {
        val intent = Intent(this, SingUpMerchant::class.java)

        startActivity(intent)
    }
    private fun goToSecondActivity( ) {
        val bundle = Bundle()
        bundle.putString("username", username)
        bundle.putString("password", password)
        bundle.putString("baseUrl", baseUrl)
        val intent = Intent(this, ProfilActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun TextView.isEmailValid(): Boolean {
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(this.text)
        return matcher.matches()
    }

    private fun CheckAllFields(etPassword: TextView, etEmail: TextView): Boolean {
        if (etEmail.length() == 0) {
            etEmail.error = "Email is required"
            return false
        }
        if (etPassword.length() == 0) {
            etPassword.error = "Password is required"
            return false
        }
        return true
    }

}

