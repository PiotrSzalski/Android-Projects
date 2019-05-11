package com.example.l6z1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var newton : NewtonAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newton.now.sh")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newton = retrofit.create(NewtonAPI::class.java)
    }

    fun compute(view: View) {
        var expression  = expression.text.toString()
        val call = when (view) {
            simplyify -> newton.simplify(expression)
            factor -> newton.factor(expression)
            integrate -> newton.integrate(expression)
            find0 -> newton.zeroes(expression)
            findTangent -> {
                expression = expression.replace(" ","|", true)
                newton.tangent(expression)
            }
            logharitm -> {
                expression = expression.replace(" ","|",true)
                newton.log(expression)
            }

            cosine -> newton.cos(expression)
            sine -> newton.sin(expression)
            tangent -> newton.tan(expression)
            arccosine -> newton.arccos(expression)
            arcsine -> newton.arcsin(expression)
            arctan -> newton.arctan(expression)
            absoluteValue -> newton.abs(expression)
            derive -> newton.derive(expression)
            else -> {
                val arr = expression.split(" ")
                if(arr.size != 3) {
                    result.text = "Error"
                    return
                }
                expression = arr[0]+":"+arr[1]+"|"+arr[2]
                newton.area(expression)
            }
        }

        call.enqueue( object : Callback<NewtonJSON> {
            override fun onFailure(call: Call<NewtonJSON>, t: Throwable) {
                result.text = "Error"
            }

            override fun onResponse(call: Call<NewtonJSON>, response: Response<NewtonJSON>) {
                val body = response.body()
                when {
                    body == null -> result.text = "Error"
                    body.result == null -> result.text = "Error"
                    else -> result.text = body.result.toString()
                }
            }

        })

    }
}
