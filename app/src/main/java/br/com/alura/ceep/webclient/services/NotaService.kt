package br.com.alura.ceep.webclient.services

import br.com.alura.ceep.webclient.model.NotaRequisicao
import br.com.alura.ceep.webclient.model.NotaResposta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotaService {

    //funcao getall usando coroutines
    @GET("notas")
    suspend fun getAll(): List<NotaResposta>

    @PUT("notas/{id}")
    suspend fun salva(@Path("id") id: String, @Body nota: NotaRequisicao): Response<NotaResposta>

    @DELETE("nota/{id}")
    suspend fun remove(@Path("id") id: String): Response<Void>
}