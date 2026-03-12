# MC322 — Tarefa 1: O Início da Jornada

Projeto desenvolvido para a disciplina **MC322 - Programação Orientada a Objetos** da Unicamp.

Implementação de um sistema de batalha em turnos inspirado em *Slay the Spire*, onde um herói enfrenta um inimigo utilizando cartas de dano e escudo.

## Como jogar

A cada turno, o jogador tem **3 de energia** e pode usar cartas até a energia acabar ou encerrar o turno manualmente. Após o turno do jogador, o inimigo ataca automaticamente. A batalha termina quando o herói ou o inimigo chega a 0 de vida.

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

## Estrutura do projeto

```
tarefa1/
├── src/
│   ├── App.java
│   ├── Heroi.java
│   ├── Inimigo.java
│   ├── CartaDano.java
│   └── CartaEscudo.java
├── bin/
└── README.md
```