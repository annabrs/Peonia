package com.peonia.nana.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.peonia.nana.R
import com.peonia.nana.data.FaseMaternidade
import com.peonia.nana.data.Interesse
import com.peonia.nana.data.Perfil
import com.peonia.nana.data.QuantidadeFilhos
import com.peonia.nana.ui.components.BotaoPrincipal
import com.peonia.nana.ui.components.CabecalhoVoltar
import com.peonia.nana.ui.components.CampoTexto
import com.peonia.nana.ui.components.ChipNana
import com.peonia.nana.ui.components.LayoutComRodape
import com.peonia.nana.ui.components.TelaNana
import com.peonia.nana.ui.components.TituloSecao
import com.peonia.nana.ui.theme.NanaCores

/**
 * Editar perfil.
 *
 * Mexe só nos campos que a usuária já informou no cadastro. A autodeclaração
 * racial e a idade não aparecem aqui porque o protótipo não as inclui nesta
 * tela; elas continuam guardadas como foram informadas.
 *
 * O protótipo também mostra "Alterar foto". O app não guarda foto de perfil,
 * e isso precisa ser decidido pela equipe antes de ser implementado, por
 * envolver dado pessoal sensível. Por isso não foi incluído.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditarPerfilScreen(
    perfil: Perfil,
    aoSalvar: (Perfil) -> Unit,
    aoCancelar: () -> Unit,
) {
    var nome by rememberSaveable { mutableStateOf(perfil.nome) }
    var cidade by rememberSaveable { mutableStateOf(perfil.cidade) }
    var faseNome by rememberSaveable { mutableStateOf(perfil.fase?.name ?: "") }
    var filhosNome by rememberSaveable { mutableStateOf(perfil.quantidadeFilhos?.name ?: "") }
    var interessesNomes by rememberSaveable {
        mutableStateOf(perfil.interesses.joinToString(",") { it.name })
    }

    val interesses = interessesNomes.split(",").filter { it.isNotBlank() }.toSet()

    TelaNana {
        LayoutComRodape(
            rodape = {
                BotaoPrincipal(
                    texto = stringResource(R.string.edi_salvar),
                    onClick = {
                        aoSalvar(
                            perfil.copy(
                                nome = nome.trim(),
                                cidade = cidade.trim(),
                                fase = FaseMaternidade.entries.firstOrNull { it.name == faseNome },
                                quantidadeFilhos = QuantidadeFilhos.entries.firstOrNull { it.name == filhosNome },
                                interesses = Interesse.entries.filter { it.name in interesses }.toSet(),
                            ),
                        )
                    },
                )
                TextButton(onClick = aoCancelar, modifier = Modifier.heightIn(min = 48.dp)) {
                    Text(
                        stringResource(R.string.edi_cancelar),
                        color = NanaCores.Ameixa,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            },
        ) {
            CabecalhoVoltar(stringResource(R.string.edi_titulo), aoCancelar)
            Spacer(Modifier.height(16.dp))
            AvatarInicial(
                nome = nome,
                tamanho = 80,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(20.dp))

            TituloSecao(stringResource(R.string.edi_nome))
            Spacer(Modifier.height(8.dp))
            CampoTexto(
                nome,
                { nome = it.take(60) },
                stringResource(R.string.cad_nome_placeholder),
            )
            Spacer(Modifier.height(16.dp))

            TituloSecao(stringResource(R.string.edi_cidade))
            Spacer(Modifier.height(8.dp))
            CampoTexto(
                cidade,
                { cidade = it.take(60) },
                stringResource(R.string.cad_cidade_placeholder),
                acao = ImeAction.Done,
            )
            Spacer(Modifier.height(20.dp))

            TituloSecao(stringResource(R.string.edi_momento))
            Spacer(Modifier.height(10.dp))
            FlowRow(
                Modifier.selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FaseMaternidade.entries.forEach { fase ->
                    ChipNana(
                        stringResource(fase.rotulo),
                        selecionado = faseNome == fase.name,
                        onClick = { faseNome = fase.name },
                    )
                }
            }
            Spacer(Modifier.height(20.dp))

            TituloSecao(stringResource(R.string.edi_filhos))
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                QuantidadeFilhos.entries.forEach { q ->
                    ChipNana(
                        stringResource(q.rotulo),
                        selecionado = filhosNome == q.name,
                        onClick = { filhosNome = if (filhosNome == q.name) "" else q.name },
                    )
                }
            }
            Spacer(Modifier.height(20.dp))

            TituloSecao(stringResource(R.string.edi_procura))
            Spacer(Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Interesse.entries.forEach { i ->
                    ChipNana(
                        stringResource(i.rotulo),
                        selecionado = i.name in interesses,
                        multipla = true,
                        onClick = {
                            val novos = if (i.name in interesses) interesses - i.name else interesses + i.name
                            interessesNomes = novos.joinToString(",")
                        },
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
