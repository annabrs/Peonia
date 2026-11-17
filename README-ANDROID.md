# Nanã: Etapa 1 (Descoberta, apresentação e cadastro)

Aplicativo Android em **Kotlin + Jetpack Compose + Material 3**, feito a partir do documento
*Jornada do Usuário – Etapa 1* (Equipe Peônia • Instituto PROA) e do protótipo no Figma.

## Como abrir
1. Android Studio (Ladybug 2024.2 ou mais recente) → **File › Open** → selecione a pasta `Nana`.
2. Espere o Gradle sincronizar. Ele usa o wrapper 8.14.3, AGP 8.7.3 e Kotlin 2.0.21.
3. Rode em um emulador ou aparelho com **Android 8.0 (API 26) ou superior**.

Para ver a jornada de novo do começo, limpe os dados do app em
Configurações › Apps › Nanã › Armazenamento.

## Jornada implementada
| # | Etapa do documento | Tela | Arquivo |
|---|---|---|---|
| 1–2 | Descoberta e boas-vindas | 01 Boas-vindas | `ui/onboarding/Apresentacao.kt` |
| 3 | Conhecer o Nanã: propósito | 02 Propósito | idem |
| 3 | Conhecer o Nanã: áreas, limites e privacidade | 03 Conteúdos · Comunidade · Profissionais · Diário | idem |
| 4 | Decidir pelo cadastro | botão "Quero me cadastrar" (também é possível voltar) | idem |
| 5 | Cadastro e personalização (3 passos curtos) | 04a Nome/Idade/Cidade · 04b Fase · 04c Interesses/Bebê/Filhos/Autodeclaração | `ui/cadastro/` |
| → | Transição para a tela inicial | 05 Início personalizado pela fase | `ui/inicio/InicioScreen.kt` |

### Telas do protótipo acrescentadas depois
| Tela | O que faz | Arquivo |
|---|---|---|
| 07 Direitos | Busca e lista dos 4 temas | `ui/direitos/DireitosScreen.kt` |
| Detalhe de um direito | Cards com a lei, o selo e o texto | `ui/direitos/DireitoDetalheScreen.kt` |
| 08 Apoio | Busca, "Seus chats" e "Descobrir chats" | `ui/apoio/ApoioScreen.kt` |
| 09 Perfil | Resumo, interesses e menu | `ui/perfil/PerfilScreen.kt` |
| Editar perfil | Altera nome, cidade, momento, filhos e interesses | `ui/perfil/EditarPerfilScreen.kt` |
| Barra inferior | Início · Diário · Direitos · Apoio · Perfil | `ui/principal/PrincipalScreen.kt` |

## Estrutura
```
app/src/main/java/com/peonia/nana/
├── MainActivity.kt
├── data/            Perfil (campos do documento), RegrasCadastro (obrigatoriedade),
│                    PerfilRepository (DataStore local), Direitos (conteúdo dos temas)
└── ui/
    ├── NanaApp.kt   rotas e navegação
    ├── theme/       cores e tipografia
    ├── components/  botões, chips, campos, busca, listas e o fundo
    ├── onboarding/  telas 01, 02, 03
    ├── cadastro/    ViewModel + telas 04a, 04b, 04c
    ├── principal/   barra inferior e navegação entre as abas
    ├── inicio/      tela 05
    ├── direitos/    tela 07 e o detalhe de cada tema
    ├── apoio/       tela 08
    └── perfil/      tela 09 e Editar perfil
```
Os textos da interface estão em `res/values/strings.xml`.

## Como os cuidados do documento foram atendidos
- **Explicação antes dos dados:** o cadastro só começa depois das telas de apresentação.
- **Clareza:** cada campo tem uma frase curta dizendo por que a informação é pedida.
- **Autonomia:** os campos opcionais aparecem marcados como "(opcional)". Escolhas únicas podem ser desmarcadas tocando de novo. É possível voltar e revisar os passos sem perder o que já foi preenchido.
- **Privacidade:** um aviso no início do cadastro lista tudo o que será perguntado e explica que, nesta versão, os dados ficam **só no aparelho** (DataStore, com backup automático desativado). Não há servidor, API nem banco externo.
- **Autodeclaração racial:** o app é voltado a mulheres negras (pretas e pardas). As opções são **Preta**, **Parda** e **Prefiro não dizer**, com a finalidade explicada. A raça nunca é inferida.
- **Imagens:** o código não desenha nenhuma forma ou ilustração. Todas as imagens vêm dos arquivos enviados pela equipe (veja abaixo).
- **Sem informação médica:** nenhum conteúdo de saúde foi criado. O destaque da tela inicial mostra só a fase informada e os interesses escolhidos.
- **Acessibilidade:** os alvos de toque têm 48–56 dp ou mais, os títulos são marcados como cabeçalho, os grupos de opções têm a semântica correta (opção única ou caixa de seleção), as mensagens de erro são lidas pelo leitor de tela, há contraste adequado e o layout se ajusta ao teclado.

## Imagens da equipe

**Já incluídas**, em `res/drawable-nodpi/`:

| Arquivo | Uso |
|---|---|
| `fundo_flores.png` (739 × 1600) | Fundo de todas as telas |
| `fundo_chat.webp` (603 × 1311) | Guardado para a tela de Chat, que ainda não existe |

Elas ficam em `drawable-nodpi` de propósito: assim o Android não as amplia pela densidade da
tela, o que gastaria muita memória num fundo de tela cheia. Para trocar, substitua o arquivo
mantendo o mesmo nome. O fundo do chat foi convertido de PNG para WebP sem perda (1,1 MB → 609 KB),
com os pixels idênticos ao original.

Faltam estas. Coloque os arquivos em `app/src/main/res/drawable/` com **exatamente** estes nomes (.png, .jpg ou .webp, só letras minúsculas, números e `_`):

| Arquivo | Onde aparece |
|---|---|
| `ilustracao_boas_vindas` | Tela 01, ilustração principal |
| `ilustracao_proposito` | Tela 02, ilustração |
| `folhas_topo` | Canto superior direito das telas |
| `folhas_base` | Canto inferior esquerdo das telas |
| `humor_bem`, `humor_calma`, `humor_ansiosa`, `humor_cansada`, `humor_triste` | Tela 05, "Como você está hoje?" |

Enquanto um arquivo não existir, o espaço dele fica vazio e o app continua funcionando. A lista está em `ui/components/Imagens.kt`.

**Ícone do app (logo):** no Android Studio, clique com o botão direito em `app` › **New › Image Asset**, escolha o arquivo da logo e conclua. Depois, no `AndroidManifest.xml`, adicione `android:icon="@mipmap/ic_launcher"` na tag `<application>`. Até lá, o app usa o ícone padrão do Android.

## Pontos que dependem de validação ou definição da equipe
1. **Obrigatoriedade dos campos.** O documento diz que ela "ainda deve ser validada". A regra está toda em `data/RegrasCadastro.kt`. O padrão provisório deixa só a **fase** como obrigatória, porque a tela inicial depende dela. Para mudar, basta editar o conjunto `camposObrigatorios`.
2. **Idade: número ou faixa etária?** O documento pede para avaliar. O campo hoje aceita um número opcional de até 2 dígitos.
3. **"Já tenho conta" (tela 01 do Figma).** Login não faz parte da jornada e não há backend, então **não foi implementado**.
4. **O que acontece ao tocar nas áreas da tela inicial e nas abas** (Diário, Direitos, Apoio, Perfil). Isso pertence à próxima etapa. Por enquanto aparece só um aviso curto, e nenhuma tela nova foi criada.
5. **Check-in "Como você está hoje?".** Mostra uma resposta acolhedora, mas não salva nada, porque o Diário ainda não foi especificado.
6. **Conteúdos de cada fase.** Os textos ainda não existem e precisam ser escritos ou revisados pela equipe e por profissionais.
7. **Editar o cadastro depois.** O documento recomenda permitir, mas a tela de Perfil é da próxima etapa.
8. **Diferença entre o Figma e o PDF.** A tela 05 do Figma mostra "Seus direitos", e o PDF lista "Profissionais" entre as áreas. O app segue o Figma na tela 05.
9. **Conteúdo dos Direitos.** Os textos das leis foram transcritos do protótipo no Figma, sem nenhum acréscimo, e estão em `res/values/strings.xml` (prefixo `dir_`). Precisam de revisão de quem é da área antes de publicar.
10. **Em destaque e chips de Categoria (tela 07).** No protótipo aparecem como "Título do destaque" e "Categoria", sem conteúdo definido. Ficaram de fora até a equipe dizer qual tema é o destaque e quais são as categorias.
11. **Chats (tela 08).** As conversas precisam de um servidor, que o app não tem. Os itens da lista são os exemplos do protótipo e tocar neles mostra o aviso de próxima etapa. A tela de conversa não foi feita, mas o fundo dela já está no projeto como `fundo_chat` (veja `ui/components/Imagens.kt`).
12. **Menu do Perfil.** "Meus dados", "Notificações", "Privacidade e segurança", "Ajuda e contato" e "Sair da conta" aparecem no protótipo, mas as telas não foram desenhadas e o app não tem login. Mostram o aviso de próxima etapa.
13. **"Alterar foto" (Editar perfil).** Não foi implementado. Guardar foto de perfil é dado pessoal sensível e precisa de decisão da equipe; além disso, o documento diz que raça nunca deve ser inferida por foto.
14. **Momento em Editar perfil.** O protótipo mostra 3 opções (Gestação, Pós-parto, Planejando), mas o documento define 5 fases. A tela usa as 5 fases do documento, para não conflitar com o cadastro.
15. **Ícones dos cartões e da barra inferior.** Hoje são os ícones prontos do Material Design. Se a equipe tiver ícones próprios, dá para trocá-los pelos arquivos dela.
16. **Fonte.** O protótipo parece usar Inter. O app usa a fonte padrão do sistema até a equipe definir a fonte e incluir o arquivo.
