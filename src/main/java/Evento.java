import java.util.Scanner;

/**
 * Representa um evento genérico no mapa, como batalhas, lojas ou encontros.
 */
public abstract class Evento {

    /**
     * Inicia o evento.
     * @param heroi O herói que participa do evento.
     * @param scanner Scanner para entrada de dados.
     * @return true se o herói sobreviveu/completou o evento, false se morreu.
     */
    public abstract boolean iniciar(Heroi heroi, Scanner scanner);
}
