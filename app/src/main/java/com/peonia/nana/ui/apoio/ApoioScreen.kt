package com.peonia.nana.ui.apoio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.peonia.nana.R
import com.peonia.nana.ui.components.CampoBusca
import com.peonia.nana.ui.components.IconeArredondado
import com.peonia.nana.ui.components.TituloSecao
import com.peonia.nana.ui.theme.NanaCores

/**
 * 08 – Apoio: seus chats e chats para descobrir.
 *
 * Os itens abaixo são os exemplos do protótipo. As conversas de verdade
 * dependem de um servidor, que não faz parte do app nesta etapa, então
 * tocar em qualquer chat mostra o aviso de próxima etapa.
 */
@Composable
fun ApoioScreen(aoAvisar: () -> Unit) {
    var busca by rememberSaveable { mutableStateOf("") }

    val nomeExemplo = stringResource(R.string.apo_ex_nome)
    val termo = busca.trim()
    val combina = termo.isEmpty() || nomeExemplo.contains(termo, ignoreCase = true)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            stringResource(R.string.apo_titulo),
            style = MaterialTheme.typography.headlineSmall,
            color = NanaCores.AmeixaEscura,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(R.string.apo_apoio),
            style = MaterialTheme.typography.bodySmall,
            color = NanaCores.TextoSuave,
        )
        Spacer(Modifier.height(16.dp))
        CampoBusca(busca, { busca = it }, stringResource(R.string.dir_busca))
        Spacer(Modifier.height(20.dp))

        if (!combina) {
            Text(
                stringResource(R.string.apo_sem_resultado),
                style = MaterialTheme.typography.bodyMedium,
                color = NanaCores.TextoSuave,
            )
        } else {
            TituloSecao(stringResource(R.string.apo_seus_chats))
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(4) { LinhaSeuChat(aoAvisar) }
            }
            Spacer(Modifier.height(24.dp))
            TituloSecao(stringResource(R.string.apo_descobrir))
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(2) { LinhaDescobrirChat(aoAvisar) }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun LinhaSeuChat(aoTocar: () -> Unit) {
    Surface(
        onClick = aoTocar,
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        shape = RoundedCornerShape(16.dp),
        color = NanaCores.Branco,
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            IconeArredondado {
                Icon(Icons.Outlined.Groups, contentDescription = null, tint = NanaCores.Ameixa)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.apo_ex_nome),
                    style = MaterialTheme.typography.titleSmall,
                    color = NanaCores.AmeixaEscura,
                )
                Text(
                    stringResource(R.string.apo_ex_ultima),
                    style = MaterialTheme.typography.bodySmall,
                    color = NanaCores.TextoSuave,
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(R.string.apo_ex_hora),
                style = MaterialTheme.typography.labelSmall,
                color = NanaCores.TextoSuave,
            )
        }
    }
}

@Composable
private fun LinhaDescobrirChat(aoTocar: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = NanaCores.Branco,
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            IconeArredondado {
                Icon(Icons.Outlined.Groups, contentDescription = null, tint = NanaCores.Ameixa)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.apo_ex_nome),
                    style = MaterialTheme.typography.titleSmall,
                    color = NanaCores.AmeixaEscura,
                )
                Text(
                    stringResource(R.string.apo_ex_participantes),
                    style = MaterialTheme.typography.bodySmall,
                    color = NanaCores.TextoSuave,
                )
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = aoTocar,
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
                modifier = Modifier.heightIn(min = 40.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NanaCores.Ameixa),
                border = BorderStroke(1.5.dp, NanaCores.Ameixa),
            ) {
                Text(
                    stringResource(R.string.apo_entrar),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
