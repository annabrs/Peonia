package com.peonia.nana.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "perfil_nana")

/**
 * Guarda o cadastro só no aparelho (DataStore). Nenhum serviço externo é usado.
 * O backup automático do Android está desativado no Manifest, para que os dados
 * não saiam do aparelho.
 */
class PerfilRepository(private val context: Context) {

    private object Chaves {
        val CONCLUIDO = booleanPreferencesKey("cadastro_concluido")
        val NOME = stringPreferencesKey("nome")
        val IDADE = intPreferencesKey("idade")
        val CIDADE = stringPreferencesKey("cidade")
        val FASE = stringPreferencesKey("fase")
        val INTERESSES = stringSetPreferencesKey("interesses")
        val AUTODECLARACAO = stringPreferencesKey("autodeclaracao")
        val NOMES_BEBES = stringPreferencesKey("nomes_bebes")
        val QUANTIDADE_FILHOS = stringPreferencesKey("quantidade_filhos")
    }

    /** Emite null enquanto o cadastro não foi concluído. */
    val perfil: Flow<Perfil?> = context.dataStore.data.map { p ->
        if (p[Chaves.CONCLUIDO] != true) {
            null
        } else {
            Perfil(
                nome = p[Chaves.NOME].orEmpty(),
                idade = p[Chaves.IDADE],
                cidade = p[Chaves.CIDADE].orEmpty(),
                fase = p[Chaves.FASE]?.let { enumOuNulo<FaseMaternidade>(it) },
                interesses = p[Chaves.INTERESSES].orEmpty()
                    .mapNotNull { enumOuNulo<Interesse>(it) }.toSet(),
                autodeclaracao = p[Chaves.AUTODECLARACAO]?.let { enumOuNulo<Autodeclaracao>(it) },
                nomesBebes = p[Chaves.NOMES_BEBES].orEmpty(),
                quantidadeFilhos = p[Chaves.QUANTIDADE_FILHOS]?.let { enumOuNulo<QuantidadeFilhos>(it) },
            )
        }
    }

    suspend fun salvar(perfil: Perfil) {
        context.dataStore.edit { p ->
            p.clear()
            if (perfil.nome.isNotBlank()) p[Chaves.NOME] = perfil.nome.trim()
            perfil.idade?.let { p[Chaves.IDADE] = it }
            if (perfil.cidade.isNotBlank()) p[Chaves.CIDADE] = perfil.cidade.trim()
            perfil.fase?.let { p[Chaves.FASE] = it.name }
            p[Chaves.INTERESSES] = perfil.interesses.map { it.name }.toSet()
            perfil.autodeclaracao?.let { p[Chaves.AUTODECLARACAO] = it.name }
            if (perfil.nomesBebes.isNotBlank()) p[Chaves.NOMES_BEBES] = perfil.nomesBebes.trim()
            perfil.quantidadeFilhos?.let { p[Chaves.QUANTIDADE_FILHOS] = it.name }
            p[Chaves.CONCLUIDO] = true
        }
    }

    private inline fun <reified T : Enum<T>> enumOuNulo(nome: String): T? =
        enumValues<T>().firstOrNull { it.name == nome }
}
