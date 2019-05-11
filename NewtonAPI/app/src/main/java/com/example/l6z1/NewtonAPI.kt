package com.example.l6z1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NewtonAPI {
    @GET("/simplify/{expression}")
    fun simplify(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/factor/{expression}")
    fun factor(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/derive/{expression}")
    fun derive(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/integrate/{expression}")
    fun integrate(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/zeroes/{expression}")
    fun zeroes(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/tangent/{expression}")
    fun tangent(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/area/{expression}")
    fun area(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/cos/{expression}")
    fun cos(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/sin/{expression}")
    fun sin(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/tan/{expression}")
    fun tan(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/arccos/{expression}")
    fun arccos(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/arcsin/{expression}")
    fun arcsin(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/arctan/{expression}")
    fun arctan(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/abs/{expression}")
    fun abs(@Path("expression") expression : String) : Call<NewtonJSON>

    @GET("/log/{expression}")
    fun log(@Path("expression") expression : String) : Call<NewtonJSON>
}