/**
 * Interface que define um observador (Observer/Subscriber) no Padrão de Projeto Observer.
 * <p>
 * Qualquer classe do jogo que precise reagir a eventos (como a classe base de Efeitos) 
 * deve implementar esta interface. Ela trabalha em conjunto com o {@link Publisher}, 
 * garantindo que objetos diferentes possam se comunicar sem estarem fortemente acoplados.
 * </p>
 */
public interface Subscriber {
    
    /**
     * Método chamado automaticamente pelo {@link Publisher} para avisar que um evento ocorreu.
     * <p>
     * A classe que implementa este método (como um efeito de Veneno ou Regeneração) 
     * recebe o aviso e deve verificar o tipo do evento para decidir se precisa realizar 
     * alguma ação (ex: causar dano se o evento for "FIM_DE_TURNO") ou apenas ignorar.
     * </p>
     * * @param evento O tipo do evento do jogo que acabou de ser disparado.
     */
    void serNotificado(TipoEvento evento);
}