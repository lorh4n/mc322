/**
 * Interface que define o publicador (Subject) no Padrão de Projeto Observer.
 * <p>
 * O {@code Publisher} é o componente central do sistema de eventos do jogo. 
 * Ele é responsável por gerenciar uma lista de observadores ({@link Subscriber}) 
 * e notificá-los em massa sempre que um evento importante ocorrer (como a virada 
 * de um turno ou a morte de uma entidade).
 * </p>
 */
public interface Publisher {
    
    /**
     * Inscreve um novo observador para começar a receber as notificações do jogo.
     * <p>
     * Exemplo prático: quando um efeito de Veneno é criado, ele deve ser inscrito 
     * aqui para saber quando o turno acaba e causar dano ao alvo.
     * </p>
     * @param s O observador (Subscriber) que será adicionado à lista de ouvintes.
     */
    void inscrever(Subscriber s);

    /**
     * Remove um observador da lista, cancelando sua assinatura de eventos.
     * <p>
     * É fundamental chamar este método quando um objeto deixa de existir no jogo 
     * (ex: o Veneno acabou ou a entidade morreu) para evitar erros e vazamento 
     * de memória (memory leaks).
     * </p>
     * @param s O observador (Subscriber) que deixará de receber notificações.
     */
    void desinscrever(Subscriber s);

    /**
     * Dispara um aviso para todos os observadores inscritos na lista atual.
     * <p>
     * A classe que implementar esta interface deverá percorrer todos os seus 
     * {@link Subscriber}s salvos e chamar o método correspondente neles, 
     * repassando qual foi o evento gerado.
     * </p>
     * @param evento O tipo do evento que acabou de acontecer (ex: FIM_DE_TURNO).
     */
    void notificar(TipoEvento evento);
}