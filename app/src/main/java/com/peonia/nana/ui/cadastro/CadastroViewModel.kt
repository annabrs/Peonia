package com.peonia.nana.ui.cadastro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peonia.nana.data.Autodeclaracao
import com.peonia.nana.data.CampoCadastro
import com.peonia.nana.data.FaseMaternidade
import com.peonia.nana.data.Interesse
import com.peonia.nana.data.Perfil
import com.peonia.nana.data.PerfilRepository
import com.peonia.nana.data.QuantidadeFilhos
import com.peonia.nana.data.RegrasCadastro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Os 3 passos curtos do cadastro e os campos de cada um. */
enum class PassoCadastro(val numero: Int, val campos: List<CampoCadastro>) {
    NOME(1, listOf(CampoCadastro.NOME, CampoCadastro.IDADE, CampoCadastro.CIDADE)),
    MOMENTO(2, listOf(CampoCadastro.FASE)),
    PERFIL(
        3,
        listOf(
            CampoCadastro.INTERESSES,
            CampoCadastro.NOME_BEBE,
            CampoCadastro.QUANTIDADE_FILHOS,
            CampoCadastro.AUTODECLARACAO,
        ),
    );

    companion object {
        val total = entries.size
    }
}

data class CadastroUiState(
    val nome: String = "",
    val idade: String = "",
    val cidade: String = "",
    val fase: FaseMaternidade? = null,
    val interesses: Set<Interesse> = emptySet(),
    val nomesBebes: String = "",
    val quantidadeFilhos: QuantidadeFilhos? = null,
    val autodeclaracao: Autodeclaracao? = null,
    val camposComErro: Set<CampoCadastro> = emptySet(),
    val salvando: Boolean = false,
)

class CadastroViewModel(app: Application) : AndroidViewModel(app) {

    private val repositorio = PerfilRepository(app)

    private val _estado = MutableStateFlow(CadastroUiState())
    val estado: StateFlow<CadastroUiState> = _estado.asStateFlow()

    fun alterarNome(v: String) = _estado.update { it.copy(nome = v.take(60), camposComErro = it.camposComErro - CampoCadastro.NOME) }

    /** Aceita só números (até 2 dígitos). */
    fun alterarIdade(v: String) = _estado.update {
        it.copy(idade = v.filter(Char::isDigit).take(2), camposComErro = it.camposComErro - CampoCadastro.IDADE)
    }

    fun alterarCidade(v: String) = _estado.update { it.copy(cidade = v.take(60), camposComErro = it.camposComErro - CampoCadastro.CIDADE) }

    fun escolherFase(f: FaseMaternidade) = _estado.update { it.copy(fase = f, camposComErro = it.camposComErro - CampoCadastro.FASE) }

    fun alternarInteresse(i: Interesse) = _estado.update {
        val novos = if (i in it.interesses) it.interesses - i else it.interesses + i
        it.copy(interesses = novos, camposComErro = it.camposComErro - CampoCadastro.INTERESSES)
    }

    fun alterarNomesBebes(v: String) = _estado.update { it.copy(nomesBebes = v.take(80), camposComErro = it.camposComErro - CampoCadastro.NOME_BEBE) }

    /** Tocar de novo na opção já marcada desmarca (o campo pode ficar sem resposta). */
    fun alternarQuantidadeFilhos(q: QuantidadeFilhos) = _estado.update {
        it.copy(
            quantidadeFilhos = if (it.quantidadeFilhos == q) null else q,
            camposComErro = it.camposComErro - CampoCadastro.QUANTIDADE_FILHOS,
        )
    }

    fun alternarAutodeclaracao(a: Autodeclaracao) = _estado.update {
        it.copy(
            autodeclaracao = if (it.autodeclaracao == a) null else a,
            camposComErro = it.camposComErro - CampoCadastro.AUTODECLARACAO,
        )
    }

    /**
     * Confere os campos obrigatórios do passo (definidos em [RegrasCadastro]).
     * Retorna true se pode seguir.
     */
    fun validarPasso(passo: PassoCadastro): Boolean {
        val s = _estado.value
        val faltando = passo.campos
            .filter { RegrasCadastro.eObrigatorio(it) && estaVazio(s, it) }
            .toSet()
        _estado.update { it.copy(camposComErro = faltando) }
        return faltando.isEmpty()
    }

    fun concluir(aoConcluir: () -> Unit) {
        if (_estado.value.salvando) return
        if (!PassoCadastro.entries.all { validarPasso(it) }) return
        _estado.update { it.copy(salvando = true) }
        viewModelScope.launch {
            val s = _estado.value
            repositorio.salvar(
                Perfil(
                    nome = s.nome,
                    idade = s.idade.toIntOrNull(),
                    cidade = s.cidade,
                    fase = s.fase,
                    interesses = s.interesses,
                    autodeclaracao = s.autodeclaracao,
                    nomesBebes = s.nomesBebes,
                    quantidadeFilhos = s.quantidadeFilhos,
                ),
            )
            _estado.update { it.copy(salvando = false) }
            aoConcluir()
        }
    }

    private fun estaVazio(s: CadastroUiState, campo: CampoCadastro): Boolean = when (campo) {
        CampoCadastro.NOME -> s.nome.isBlank()
        CampoCadastro.IDADE -> s.idade.isBlank()
        CampoCadastro.CIDADE -> s.cidade.isBlank()
        CampoCadastro.FASE -> s.fase == null
        CampoCadastro.INTERESSES -> s.interesses.isEmpty()
        CampoCadastro.NOME_BEBE -> s.nomesBebes.isBlank()
        CampoCadastro.QUANTIDADE_FILHOS -> s.quantidadeFilhos == null
        CampoCadastro.AUTODECLARACAO -> s.autodeclaracao == null
    }
}
