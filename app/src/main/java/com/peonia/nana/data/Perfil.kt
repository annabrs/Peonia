package com.peonia.nana.data

import androidx.annotation.StringRes
import com.peonia.nana.R

/**
 * Fases da maternidade definidas no documento "Jornada do Usuário – Etapa 1".
 */
enum class FaseMaternidade(@StringRes val rotulo: Int) {
    GESTACAO(R.string.fase_gestacao),
    POS_PARTO_0_45_DIAS(R.string.fase_0_45),
    POS_PARTO_46_DIAS_6_MESES(R.string.fase_46_6m),
    MATERNIDADE_6_MESES_1_ANO(R.string.fase_6m_1a),
    MATERNIDADE_1_2_ANOS(R.string.fase_1a_2a),
}

/** Interesses apresentados na tela 04c do protótipo. Múltipla escolha, e a usuária pode não selecionar nenhum. */
enum class Interesse(@StringRes val rotulo: Int) {
    AMAMENTACAO(R.string.int_amamentacao),
    SAUDE_MENTAL(R.string.int_saude_mental),
    MEUS_DIREITOS(R.string.int_meus_direitos),
    SONO_DO_BEBE(R.string.int_sono_bebe),
    REDE_DE_APOIO(R.string.int_rede_apoio),
    PARTO(R.string.int_parto),
}

/** Autodeclaração racial. É sempre informada pela própria usuária e nunca inferida. */
enum class Autodeclaracao(@StringRes val rotulo: Int) {
    PRETA(R.string.raca_preta),
    PARDA(R.string.raca_parda),
    PREFIRO_NAO_DIZER(R.string.raca_prefiro_nao),
}

enum class QuantidadeFilhos(@StringRes val rotulo: Int) {
    UM(R.string.filhos_1),
    DOIS(R.string.filhos_2),
    TRES_OU_MAIS(R.string.filhos_3),
}

/**
 * Dados do cadastro. Só contém os campos previstos no documento.
 * Campos não informados ficam vazios ou nulos.
 */
data class Perfil(
    val nome: String = "",
    val idade: Int? = null,
    val cidade: String = "",
    val fase: FaseMaternidade? = null,
    val interesses: Set<Interesse> = emptySet(),
    val autodeclaracao: Autodeclaracao? = null,
    val nomesBebes: String = "",
    val quantidadeFilhos: QuantidadeFilhos? = null,
)
