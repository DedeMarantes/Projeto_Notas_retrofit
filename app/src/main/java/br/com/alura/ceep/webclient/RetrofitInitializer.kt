package br.com.alura.ceep.webclient

import br.com.alura.ceep.webclient.services.NotaService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RetrofitInitializer private constructor(){
    companion object {
        private lateinit var INSTANCE: Retrofit
        private fun getRetrofitInstance(): Retrofit {
//            val httpclient = OkHttpClient.Builder()
            if (!::INSTANCE.isInitialized) {
                synchronized(RetrofitInitializer::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("http://172.29.92.228:8080/")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun <C> getService(serviceClass: Class<C>) : C {
            //retorna classe generica
            return getRetrofitInstance().create(serviceClass)
        }
    }
}