package com.peonia.nana.ui.direitos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.peonia.nana.R
import com.peonia.nana.data.TemaDireito
import com.peonia.nana.ui.components.CampoBusca
import com.peonia.nana.ui.components.IconeArredondado
import com.peonia.nana.ui.components.LinhaLista
import com.peonia.nana.ui.components.TituloSecao
import com.peonia.nana.ui.theme.NanaCores

/**
 * 07 – Direitos: busca e lista de temas.
 *
 * A busca procura no título, na descrição e no texto dos cards de cada tema.
 */
@Composable
fun DireitosScreen(aoAbrirTema: (TemaDireito) -> Unit) {
    var busca by rememberSaveable { mutableStateOf("") }

    // Pré-carrega os textos para que a busca também encontre o conteúdo dos cards.
    // Usa laços simples porque stringResource só pode ser chamado em contexto Composable.
    val temas = TemaDireito.entries
    val textos = ArrayList<String>(temas.size)
    for (tema in temas) {
        val sb = StringBuilder()
        sb.append(stringResource(tema.titulo)).append(' ')
        sb.append(stringResource(tema.descricao))
        for (card in tema.cards) {
            sb.append(' ').append(stringResource(card.titulo))
            sb.append(' ').append(stringResource(card.selo))
            sb.append(' ').append(stringResource(card.texto))
        }
        textos.add(sb.toString())
    }

    val termo = busca.trim()
    val filtrados = if (termo.isEmpty()) {
        temas
    } else {
        temas.filterIndexed { i, _ -> textos[i].contains(termo, ignoreCase = true) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            stringResource(R.string.dir_titulo),
            style = MaterialTheme.typography.headlineSmall,
            color = NanaCores.AmeixaEscura,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(R.string.dir_apoio),
            style = MaterialTheme.typography.bodySmall,
            color = NanaCores.TextoSuave,
        )
        Spacer(Modifier.height(16.dp))
        CampoBusca(busca, { busca = it }, stringResource(R.string.dir_busca))
        Spacer(Modifier.height(20.dp))
        TituloSecao(stringResource(R.string.dir_lista_temas))
        Spacer(Modifier.height(10.dp))

        if (filtrados.isEmpty()) {
            Text(
                stringResource(R.string.dir_sem_resultado),
                style = MaterialTheme.typography.bodyMedium,
                color = NanaCores.TextoSuave,
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filtrados.forEach { tema ->
                    LinhaLista(
                        titulo = stringResource(tema.titulo),
                        descricao = stringResource(tema.descricao),
                        aoTocar = { aoAbrirTema(tema) },
                        icone = {
                            IconeArredondado {
                                Icon(
                                    Icons.Outlined.Description,
                                    contentDescription = null,
                                    tint = NanaCores.Ameixa,
                                )
                            }
                        },
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
