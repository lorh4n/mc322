# MC322 — Tarefa 3: Efeitos

Projeto desenvolvido para a disciplina **MC322 - Programação Orientada a Objetos** da Unicamp.

Implementação de um sistema de combate por turnos inspirado em *Slay the Spire*, com herói, inimigos, cartas e um sistema de **efeitos acumuláveis** baseado no padrão de design **Observer**.

---

## Como jogar

A cada turno, o jogador escolhe quantas cartas comprar (até 5), tem **3 de energia** para usar cartas e pode:

- **Número da carta** — usar a carta da mão
- **-1** — encerrar o turno
- **-2** — descartar uma carta voluntariamente

Após o turno do jogador, cada inimigo executa sua ação. O combate termina quando todos os inimigos ou o herói chegam a 0 de vida.

---

## Efeitos implementados

### Veneno
Aplicado ao **inimigo**. A cada fim de turno do jogador, o alvo perde HP igual à quantidade de acúmulos, e os acúmulos reduzem em 1. O efeito termina quando os acúmulos chegam a zero.

> Exemplo: 3 acúmulos de Veneno causam 3 de dano no 1º turno, 2 no 2º e 1 no 3º.

### Regeneração
Aplicado ao **herói**. A cada fim de turno, o herói recupera HP igual à quantidade de acúmulos, e os acúmulos reduzem em 1.

> Exemplo: 2 acúmulos de Regeneração curam 2 HP no 1º turno e 1 HP no 2º.

---

## Padrão Observer

Os efeitos são implementados por meio do padrão de design **Observer**:

- **Publisher** (`App`) — mantém uma lista de `Subscriber`s e os notifica quando eventos de combate ocorrem (ex: fim do turno do jogador).
- **Subscriber** (`Efeito`) — cada efeito ativo é inscrito no Publisher e reage ao evento correto para aplicar seu comportamento.
- **TipoEvento** — enum com os eventos: `INICIO_TURNO_JOGADOR`, `FIM_TURNO_JOGADOR`, `INICIO_TURNO_INIMIGO`, `FIM_TURNO_INIMIGO`.

Quando um efeito expira (acúmulos chegam a 0), ele se desincreve automaticamente do Publisher e é removido da entidade.

---

## Inimigos

Os inimigos possuem múltiplas ações e alternam entre elas a cada turno:

- **Rato** — alterna entre atacar (causando dano fixo) e aplicar **Veneno** no herói.

---

## Cartas

O baralho contém três tipos de carta:

| Tipo | Efeito |
|------|--------|
| Carta de Dano | Causa dano direto ao inimigo alvo |
| Carta de Escudo | Concede escudo ao herói |
| Carta de Efeito | Aplica Veneno no inimigo ou Regeneração no herói |

---

## Compilar e executar

**Compilar:**
```bash
javac -d bin $(find src -name "*.java")
```

**Executar:**
```bash
java -cp bin App
```

> Os comandos devem ser executados a partir da raiz do repositório.

---

## Estrutura do projeto

```
mc322/
├── src/
│   ├── App.java           # Ponto de entrada e Publisher do Observer
│   ├── Publisher.java     # Interface Publisher
│   ├── Subscriber.java    # Interface Subscriber
│   ├── TipoEvento.java    # Enum de eventos do combate
│   ├── Entidade.java      # Classe abstrata base (herói e inimigos)
│   ├── Heroi.java         # O personagem do jogador
│   ├── Inimigo.java       # Classe abstrata base dos inimigos
│   ├── Rato.java          # Inimigo: alterna entre atacar e envenenar
│   ├── Carta.java         # Classe abstrata base das cartas
│   ├── CartaDano.java     # Carta de dano direto
│   ├── CartaEscudo.java   # Carta de escudo
│   ├── CartaEfeito.java   # Carta que aplica efeitos
│   ├── Baralho.java       # Gerenciamento do baralho
│   ├── Efeito.java        # Classe abstrata base dos efeitos (Subscriber)
│   ├── Veneno.java        # Efeito: dano por turno
│   └── Regeneracao.java   # Efeito: cura por turno
├── bin/
└── README.md
```
