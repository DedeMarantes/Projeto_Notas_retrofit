package br.com.alura.ceep.repository

import br.com.alura.ceep.database.dao.NotaDao
import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.webclient.NotaWebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NotaRepository(private val dao: NotaDao, private val webClient: NotaWebClient) {

    fun buscaTodas(): Flow<List<Nota>> {
        return dao.buscaTodas()
    }

    private suspend fun atualizaTodas() {
        webClient.buscaTodas()?.let{notas ->
            val notaSincronizadas = notas.map{ nota ->
                nota.copy(sincronizada = true)
            }
            CoroutineScope(Dispatchers.IO).launch {
                dao.salva(notaSincronizadas)
            }
        }
    }

    fun buscaPorId(id: String): Flow<Nota> {
        return dao.buscaPorId(id)
    }

    suspend fun remove(id: String) {
        dao.desativa(id)
        if (webClient.remove(id)) {
            dao.remove(id)
        }
    }

    suspend fun salva(nota: Nota) {
        dao.salva(nota)
        if(webClient.salva(nota)) {
            val notaSincronizada = nota.copy(sincronizada = true)
            dao.salva(notaSincronizada)
        }
    }

    suspend fun sincroniza(){
        val notasDesativadas = dao.buscaDesativadas().first()
        notasDesativadas.forEach { notasDesativada ->
            remove(notasDesativada.id)
        }
        val notasNaoSincronizadas = dao.buscaNaoSincronizadas().first()
        notasNaoSincronizadas.forEach { notasNaoSincronizada ->
            salva(notasNaoSincronizada)
        }
        atualizaTodas()
    }
}