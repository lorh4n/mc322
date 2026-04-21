# MC322 — Tarefa 5: Aventuras pelo Mapa

Projeto desenvolvido para a disciplina **MC322 - Programação Orientada a Objetos** da Unicamp.

Implementação de um sistema de combate por turnos inspirado em *Slay the Spire*, com progressão por um **mapa em árvore** entre múltiplas batalhas, sistema de **efeitos acumuláveis** via padrão **Observer** e **testes unitários** com JUnit + JaCoCo.

---

## Compilar e executar

**Compilar:**
```bash
./gradlew build
```

**Executar:**
```bash
./gradlew run --console=plain -q
```

**Rodar os testes:**
```bash
./gradlew test
```

**Relatório de cobertura (JaCoCo):**
```bash
./gradlew test
# Abrir build/reports/jacoco/test/html/index.html no navegador
```

> Os comandos devem ser executados a partir da raiz do repositório.

---

## Progressão pelo mapa

O jogo é organizado como uma **árvore** de nós: cada nó (exceto a raiz) é uma batalha. O jogador começa na raiz e, após vencer uma batalha, escolhe entre os nós filhos não visitados para onde progredir — sempre avançando, nunca voltando.

```
            Início (raiz)
             /      \
         Floresta  Pântano
           / \      / \
      Caverna Ruínas Torre
           \   |   |   /
            Chefe Final
```

- **Vida** e **baralho** do herói **persistem** entre batalhas.
- **Escudo** e **efeitos de status** são resetados a cada nova batalha.
- O jogo termina em **derrota** se a vida do herói chegar a 0.
- O jogo termina em **vitória** ao concluir um nó marcado como final.

---

## Como jogar

### No menu do mapa
Digite o número do nó adjacente para onde quer avançar.

### Dentro da batalha
A cada turno, escolha quantas cartas comprar (até 5), gaste **3 de energia** por turno e use:

- **Número da carta** — usar a carta da mão
- **-1** — encerrar o turno
- **-2** — descartar uma carta voluntariamente

Após o turno do jogador, cada inimigo executa sua ação.

---

## Cartas

| Carta | Efeito |
|-------|--------|
| Carta de Dano | Causa dano direto ao inimigo alvo |
| Carta de Escudo | Concede escudo ao herói |
| Carta de Efeito | Aplica Veneno no inimigo ou Regeneração no herói |
| Ataque Venenoso | Dano imediato + acúmulos de Veneno |
| Golpe Duplo | Ataca o inimigo duas vezes na mesma jogada |
| Dreno | Causa dano e cura o herói pelo mesmo valor |
| Extirpar | Remove Veneno do alvo e converte em dano imediato |
| Escudo Regenerativo | Escudo + efeito de Regeneração |

---

## Efeitos

### Veneno
Aplicado ao **inimigo**. No fim de cada turno do jogador, o alvo perde HP igual aos acúmulos, reduzindo 1 acúmulo.

### Regeneração
Aplicada ao **herói**. No fim de cada turno do jogador, cura HP igual aos acúmulos, reduzindo 1.

---

## Padrão Observer

Os efeitos usam o padrão **Observer**:

- **Publisher** — a classe `Batalha` mantém os `Subscriber`s da batalha atual e os notifica nos eventos.
- **Subscriber** — cada `Efeito` ativo é inscrito e reage ao evento correto.
- **TipoEvento** — enum: `INICIO_TURNO_JOGADOR`, `FIM_TURNO_JOGADOR`, `INICIO_TURNO_INIMIGO`, `FIM_TURNO_INIMIGO`.

Cada batalha é seu próprio `Publisher`, garantindo que efeitos de um combate não vazem para o próximo.

---

## Testes

- **Framework:** JUnit 5
- **Cobertura:** JaCoCo (relatório HTML em `build/reports/jacoco/test/html/`)
- **Cobertura atingida:** ~57% de instruções (requisito: 40%)
- **48 testes** cobrindo: `Entidade` (dano/escudo/cura/reset), `Baralho` (compra/descarte/reset), todas as cartas, efeitos (`Veneno`, `Regeneração`), `Batalha` (resolução automática, persistência de estado) e `Mapa` (regra de profundidade, alcance ao nó final).

---

## Estrutura do projeto

```
mc322/
├── build.gradle                    # Plugin application + jacoco, deps JUnit 5
├── settings.gradle
├── gradlew / gradlew.bat
├── src/main/java/
│   ├── App.java                    # Driver da progressão pelo mapa
│   ├── Batalha.java                # Combate individual (é o Publisher)
│   ├── Mapa.java                   # Árvore de progressão
│   ├── NoMapa.java                 # Nó da árvore com fábrica de inimigos
│   ├── Publisher.java              # Interface do Observer
│   ├── Subscriber.java
│   ├── TipoEvento.java
│   ├── Entidade.java               # Base de Heroi e Inimigo
│   ├── Heroi.java
│   ├── Inimigo.java
│   ├── Rato.java
│   ├── Carta.java                  # Base das cartas
│   ├── CartaDano.java / CartaEscudo.java / CartaEfeito.java
│   ├── CartaAtaqueVenenoso.java / CartaGolpeDuplo.java / CartaDreno.java
│   ├── CartaExtirpar.java / CartaEscudoRegenero.java
│   ├── Baralho.java
│   ├── Efeito.java / Veneno.java / Regeneracao.java
└── src/test/java/
    ├── MockPublisher.java
    ├── EntidadeTest.java
    ├── BaralhoTest.java
    ├── CartasTest.java
    ├── EfeitosTest.java
    ├── BatalhaTest.java
    ├── MapaTest.java
    └── RatoTest.java
```
