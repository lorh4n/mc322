# MC322 — Tarefa 4: Ferramentas para Desenvolvimento

Projeto desenvolvido para a disciplina **MC322 - Programação Orientada a Objetos** da Unicamp.

Implementação de um sistema de combate por turnos inspirado em *Slay the Spire*, com herói, inimigos, cartas e um sistema de **efeitos acumuláveis** baseado no padrão de design **Observer**.

---

## Compilar e executar

**Compilar:**
```bash
./gradlew build
```

**Executar:**
```bash
./gradlew run
```

> Os comandos devem ser executados a partir da raiz do repositório.

---

## Como jogar

A cada turno, o jogador escolhe quantas cartas comprar (até 5), tem **3 de energia** para usar cartas e pode:

- **Número da carta** — usar a carta da mão
- **-1** — encerrar o turno
- **-2** — descartar uma carta voluntariamente

Após o turno do jogador, cada inimigo executa sua ação. O combate termina quando todos os inimigos ou o herói chegam a 0 de vida.

---

## Cartas

O baralho contém oito tipos de carta, sendo cinco delas novas nesta tarefa:

### Cartas base (labs anteriores)

| Carta | Efeito |
|-------|--------|
| Carta de Dano | Causa dano direto ao inimigo alvo |
| Carta de Escudo | Concede escudo ao herói |
| Carta de Efeito | Aplica Veneno no inimigo ou Regeneração no herói |

### Novas cartas (Tarefa 4)

| Carta | Custo | Comportamento em jogo |
|-------|-------|----------------------|
| **Ataque Venenoso** | 2 energia | Aplica dano imediato ao alvo **e** adiciona acúmulos de Veneno. Combina ofensividade instantânea com dano contínuo nos turnos seguintes. Herda de `Carta` e reutiliza o efeito `Veneno` já existente. |
| **Golpe Duplo** | 2 energia | Ataca o inimigo **duas vezes** em sequência na mesma jogada, cada golpe passando pela lógica de escudo separadamente. Útil contra inimigos com pouco escudo. |
| **Dreno** | 2 energia | Causa dano ao inimigo e **cura o herói** pelo mesmo valor. Permite sustentação em combates longos, interagindo com o sistema de vida e escudo. |
| **Extirpar** | 1 energia | Busca o efeito de Veneno no alvo via `buscarEfeito()`. Se encontrar, **remove todos os acúmulos** e converte a quantidade em dano imediato. Se o alvo não estiver envenenado, causa apenas 2 de dano base. Interage diretamente com o sistema de efeitos. |
| **Escudo Regenerativo** | 2 energia | Concede escudo ao herói **e** aplica o efeito de Regeneração, que cura HP a cada turno. Combina defesa imediata com recuperação contínua. Reutiliza tanto `ganharEscudo()` quanto o efeito `Regeneracao`. |

---

## Efeitos

### Veneno
Aplicado ao **inimigo**. A cada fim de turno do jogador, o alvo perde HP igual à quantidade de acúmulos, e os acúmulos reduzem em 1.

> Exemplo: 3 acúmulos de Veneno causam 3 de dano no 1º turno, 2 no 2º e 1 no 3º.

### Regeneração
Aplicado ao **herói**. A cada fim de turno, o herói recupera HP igual à quantidade de acúmulos, e os acúmulos reduzem em 1.

> Exemplo: 2 acúmulos de Regeneração curam 2 HP no 1º turno e 1 HP no 2º.

---

## Padrão Observer

Os efeitos são implementados por meio do padrão de design **Observer**:

- **Publisher** (`App`) — mantém uma lista de `Subscriber`s e os notifica quando eventos de combate ocorrem.
- **Subscriber** (`Efeito`) — cada efeito ativo é inscrito no Publisher e reage ao evento correto.
- **TipoEvento** — enum com os eventos: `INICIO_TURNO_JOGADOR`, `FIM_TURNO_JOGADOR`, `INICIO_TURNO_INIMIGO`, `FIM_TURNO_INIMIGO`.

Quando um efeito expira (acúmulos chegam a 0), ele se desinscreve automaticamente do Publisher.

---

## Inimigos

- **Rato** — alterna entre atacar (causando dano fixo) e aplicar **Veneno** no herói.

---

## Estrutura do projeto

```
mc322/
├── build.gradle
├── settings.gradle
├── gradlew / gradlew.bat
├── gradle/wrapper/
├── src/main/java/
│   ├── App.java                  # Ponto de entrada e Publisher do Observer
│   ├── Publisher.java            # Interface Publisher
│   ├── Subscriber.java           # Interface Subscriber
│   ├── TipoEvento.java           # Enum de eventos do combate
│   ├── Entidade.java             # Classe abstrata base (herói e inimigos)
│   ├── Heroi.java                # O personagem do jogador
│   ├── Inimigo.java              # Classe abstrata base dos inimigos
│   ├── Rato.java                 # Inimigo: alterna entre atacar e envenenar
│   ├── Carta.java                # Classe abstrata base das cartas
│   ├── CartaDano.java            # Carta de dano direto
│   ├── CartaEscudo.java          # Carta de escudo
│   ├── CartaEfeito.java          # Carta que aplica efeitos (Veneno/Regeneração)
│   ├── CartaAtaqueVenenoso.java  # Dano + Veneno combinados
│   ├── CartaGolpeDuplo.java      # Ataca duas vezes seguidas
│   ├── CartaDreno.java           # Causa dano e cura o herói
│   ├── CartaExtirpar.java        # Remove Veneno do alvo e converte em dano
│   ├── CartaEscudoRegenero.java  # Escudo + Regeneração combinados
│   ├── Baralho.java              # Gerenciamento do baralho
│   ├── Efeito.java               # Classe abstrata base dos efeitos (Subscriber)
│   ├── Veneno.java               # Efeito: dano por turno
│   └── Regeneracao.java          # Efeito: cura por turno
└── README.md
```
