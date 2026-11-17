package com.peonia.nana.data

/** Campos do cadastro previstos no documento. */
enum class CampoCadastro {
    NOME, IDADE, CIDADE, FASE, INTERESSES, NOME_BEBE, QUANTIDADE_FILHOS, AUTODECLARACAO
}

/**
 * PONTO DE VALIDAÇÃO PENDENTE
 *
 * Segundo o documento, "a obrigatoriedade de cada campo ainda deve ser validada".
 * Por isso a regra fica concentrada aqui, para ser ajustada depois da escuta com as mães.
 *
 * Padrão provisório: só a FASE é necessária, porque a tela inicial personalizada
 * depende dela (documento: "É um dado central para a experiência").
 * Todos os outros campos aparecem como "(opcional)" na interface.
 */
object RegrasCadastro {
    val camposObrigatorios: Set<CampoCadastro> = setOf(CampoCadastro.FASE)

    fun eObrigatorio(campo: CampoCadastro): Boolean = campo in camposObrigatorios
}
