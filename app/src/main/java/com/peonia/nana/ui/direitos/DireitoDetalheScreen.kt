package com.peonia.nana.ui.direitos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.peonia.nana.data.CardDireito
import com.peonia.nana.data.TemaDireito
import com.peonia.nana.ui.components.CabecalhoVoltar
import com.peonia.nana.ui.components.IconeArredondado
import com.peonia.nana.ui.components.SeloNana
import com.peonia.nana.ui.theme.NanaCores

/**
 * Detalhe de um tema de direitos: os cards definidos pela equipe no protótipo.
 */
@Composable
fun DireitoDetalheScreen(tema: TemaDireito, aoVoltar: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        CabecalhoVoltar(stringResource(tema.titulo), aoVoltar)
        Spacer(Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            tema.cards.forEach { CartaoDireito(it) }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CartaoDireito(card: CardDireito) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        shape = RoundedCornerShape(16.dp),
        color = NanaCores.Branco,
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconeArredondado {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = NanaCores.Ameixa,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        stringResource(card.titulo),
                        style = MaterialTheme.typography.titleSmall,
                        color = NanaCores.AmeixaEscura,
                    )
                    Spacer(Modifier.height(4.dp))
                    SeloNana(stringResource(card.selo))
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(card.texto),
                style = MaterialTheme.typography.bodySmall,
                color = NanaCores.Texto,
            )
        }
    }
}
