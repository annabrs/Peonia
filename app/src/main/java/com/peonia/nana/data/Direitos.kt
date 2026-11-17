package com.peonia.nana.data

import androidx.annotation.StringRes
import com.peonia.nana.R

/**
 * Um card dentro de um tema de direitos: título, selo (lei ou observação) e texto.
 */
data class CardDireito(
    @StringRes val titulo: Int,
    @StringRes val selo: Int,
    @StringRes val texto: Int,
)

/**
 * Temas da tela de Direitos.
 *
 * ATENÇÃO: todo o conteúdo abaixo foi transcrito do protótipo da equipe no Figma,
 * sem acréscimos. Nenhuma informação jurídica ou médica foi criada aqui.
 * Antes de publicar, os textos devem ser revisados por quem é da área.
 */
enum class TemaDireito(
    @StringRes val titulo: Int,
    @StringRes val descricao: Int,
    val cards: List<CardDireito>,
) {
    ACOMPANHANTE(
        titulo = R.string.dir_acomp_titulo,
        descricao = R.string.dir_acomp_desc,
        cards = listOf(
            CardDireito(R.string.dir_acomp_1_titulo, R.string.dir_acomp_1_selo, R.string.dir_acomp_1_texto),
            CardDireito(R.string.dir_acomp_2_titulo, R.string.dir_acomp_2_selo, R.string.dir_acomp_2_texto),
        ),
    ),
    DIGNIDADE(
        titulo = R.string.dir_dign_titulo,
        descricao = R.string.dir_dign_desc,
        cards = listOf(
            CardDireito(R.string.dir_dign_1_titulo, R.string.dir_dign_1_selo, R.string.dir_dign_1_texto),
            CardDireito(R.string.dir_dign_2_titulo, R.string.dir_dign_2_selo, R.string.dir_dign_2_texto),
            CardDireito(R.string.dir_dign_3_titulo, R.string.dir_dign_3_selo, R.string.dir_dign_3_texto),
        ),
    ),
    VINCULO(
        titulo = R.string.dir_vinc_titulo,
        descricao = R.string.dir_vinc_desc,
        cards = listOf(
            CardDireito(R.string.dir_vinc_1_titulo, R.string.dir_vinc_1_selo, R.string.dir_vinc_1_texto),
            CardDireito(R.string.dir_vinc_2_titulo, R.string.dir_vinc_2_selo, R.string.dir_vinc_2_texto),
            CardDireito(R.string.dir_vinc_3_titulo, R.string.dir_vinc_3_selo, R.string.dir_vinc_3_texto),
        ),
    ),
    POS_PARTO(
        titulo = R.string.dir_pos_titulo,
        descricao = R.string.dir_pos_desc,
        cards = listOf(
            CardDireito(R.string.dir_pos_1_titulo, R.string.dir_pos_1_selo, R.string.dir_pos_1_texto),
            CardDireito(R.string.dir_pos_2_titulo, R.string.dir_pos_2_selo, R.string.dir_pos_2_texto),
            CardDireito(R.string.dir_pos_3_titulo, R.string.dir_pos_3_selo, R.string.dir_pos_3_texto),
        ),
    );

    companion object {
        fun porNome(nome: String?): TemaDireito? = entries.firstOrNull { it.name == nome }
    }
}
