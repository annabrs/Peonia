package com.peonia.nana.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peonia.nana.data.Perfil
import com.peonia.nana.data.PerfilRepository
import com.peonia.nana.ui.cadastro.CadastroMomentoScreen
import com.peonia.nana.ui.cadastro.CadastroNomeScreen
import com.peonia.nana.ui.cadastro.CadastroPerfilScreen
import com.peonia.nana.ui.cadastro.CadastroViewModel
import com.peonia.nana.ui.perfil.EditarPerfilScreen
import com.peonia.nana.ui.principal.PrincipalScreen
import com.peonia.nana.ui.onboarding.BoasVindasScreen
import com.peonia.nana.ui.onboarding.FuncionalidadesScreen
import com.peonia.nana.ui.onboarding.PropositoScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Rotas da Etapa 1 da jornada, na ordem do documento. */
object Rotas {
    const val BOAS_VINDAS = "boas_vindas"            // 01
    const val PROPOSITO = "proposito"                // 02
    const val FUNCIONALIDADES = "funcionalidades"    // 03 (+ decisão de cadastrar)
    const val CADASTRO_NOME = "cadastro_nome"        // 04a
    const val CADASTRO_MOMENTO = "cadastro_momento"  // 04b
    const val CADASTRO_PERFIL = "cadastro_perfil"    // 04c
    const val PRINCIPAL = "principal"                // 05 + abas (Início, Direitos, Apoio, Perfil)
    const val EDITAR_PERFIL = "editar_perfil"
}

sealed interface EstadoApp {
    data object Carregando : EstadoApp
    data object SemCadastro : EstadoApp
    data class Cadastrada(val perfil: Perfil) : EstadoApp
}

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val repositorio = PerfilRepository(app)

    val estado: StateFlow<EstadoApp> = repositorio.perfil
        .map { p -> if (p == null) EstadoApp.SemCadastro else EstadoApp.Cadastrada(p) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, EstadoApp.Carregando)

    /** Guarda as alterações feitas na tela Editar perfil. */
    fun salvarPerfil(perfil: Perfil) {
        viewModelScope.launch { repositorio.salvar(perfil) }
    }
}

@Composable
fun NanaApp(appVm: AppViewModel = viewModel()) {
    val estado by appVm.estado.collectAsStateWithLifecycle()

    when (val e = estado) {
        EstadoApp.Carregando -> Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        )
        else -> {
            // A rota inicial é decidida uma vez: quem já concluiu o cadastro vai direto ao Início.
            val inicio = remember { if (e is EstadoApp.Cadastrada) Rotas.PRINCIPAL else Rotas.BOAS_VINDAS }
            NavegacaoNana(
                rotaInicial = inicio,
                perfil = (e as? EstadoApp.Cadastrada)?.perfil,
                aoSalvarPerfil = appVm::salvarPerfil,
            )
        }
    }
}

@Composable
private fun NavegacaoNana(
    rotaInicial: String,
    perfil: Perfil?,
    aoSalvarPerfil: (Perfil) -> Unit,
    nav: NavHostController = rememberNavController(),
) {
    // Um único ViewModel guarda as respostas entre os 3 passos do cadastro,
    // para que a usuária possa voltar e revisar sem perder o que já preencheu.
    val cadastroVm: CadastroViewModel = viewModel()

    NavHost(navController = nav, startDestination = rotaInicial) {
        composable(Rotas.BOAS_VINDAS) {
            BoasVindasScreen(aoComecar = { nav.navigate(Rotas.PROPOSITO) })
        }
        composable(Rotas.PROPOSITO) {
            PropositoScreen(
                aoContinuar = { nav.navigate(Rotas.FUNCIONALIDADES) },
                aoVoltar = { nav.popBackStack() },
            )
        }
        composable(Rotas.FUNCIONALIDADES) {
            FuncionalidadesScreen(
                aoCadastrar = { nav.navigate(Rotas.CADASTRO_NOME) },
                aoVoltar = { nav.popBackStack() },
            )
        }
        composable(Rotas.CADASTRO_NOME) {
            CadastroNomeScreen(
                vm = cadastroVm,
                aoAvancar = { nav.navigate(Rotas.CADASTRO_MOMENTO) },
                aoVoltar = { nav.popBackStack() },
            )
        }
        composable(Rotas.CADASTRO_MOMENTO) {
            CadastroMomentoScreen(
                vm = cadastroVm,
                aoAvancar = { nav.navigate(Rotas.CADASTRO_PERFIL) },
                aoVoltar = { nav.popBackStack() },
            )
        }
        composable(Rotas.CADASTRO_PERFIL) {
            CadastroPerfilScreen(
                vm = cadastroVm,
                aoConcluir = {
                    nav.navigate(Rotas.PRINCIPAL) {
                        popUpTo(nav.graph.id) { inclusive = true }
                    }
                },
                aoVoltar = { nav.popBackStack() },
            )
        }
        composable(Rotas.PRINCIPAL) {
            // O perfil vem do armazenamento local; é atualizado assim que o cadastro é salvo.
            PrincipalScreen(
                perfil = perfil ?: Perfil(),
                aoEditarPerfil = { nav.navigate(Rotas.EDITAR_PERFIL) },
            )
        }
        composable(Rotas.EDITAR_PERFIL) {
            EditarPerfilScreen(
                perfil = perfil ?: Perfil(),
                aoSalvar = {
                    aoSalvarPerfil(it)
                    nav.popBackStack()
                },
                aoCancelar = { nav.popBackStack() },
            )
        }
    }
}
