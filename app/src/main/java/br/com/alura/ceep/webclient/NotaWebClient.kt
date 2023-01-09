package br.com.alura.ceep.webclient

import android.util.Log
import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.webclient.model.NotaRequisicao
import br.com.alura.ceep.webclient.services.NotaService
import java.net.ConnectException

class NotaWebClient {

    private val notasService = RetrofitInitializer.getService(NotaService::class.java)
    suspend fun buscaTodas(): List<Nota>? {
        return try {
            val notasResposta = notasService.getAll()
             notasResposta.map{ it.nota }
        } catch (e: ConnectException) {
            Log.e("NotaWebClient", "Falha ao conectar a Internet")
            null
        } catch (e: Exception) {
            Log.e("NotaWebClient", "buscaTodas", e)
            null
        }
    }

    suspend fun salva(nota: Nota) : Boolean{
        try {
            val resposta = notasService.salva(nota.id, NotaRequisicao(
                titulo = nota.titulo,
                descricao = nota.descricao,
                imagem = nota.imagem))
           return resposta.isSuccessful
        } catch (e: Exception) {
            Log.e("NotaWebClient", "Falha ao salvar", e)
        }
        return false
    }

    suspend fun remove(id: String): Boolean {
        try {
            notasService.remove(id)
            return true
        } catch (e: Exception) {
            Log.e("NotaWebClient", "Falha ao tentar remover nota", e)
        }
        return false
    }
}