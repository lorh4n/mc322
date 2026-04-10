/**
 * Representa um efeito de status positivo (buff) que restaura a vida da entidade ao longo do tempo.
 * <p>
 * Esta classe herda de {@link Efeito} e possui uma mecânica de cura decrescente:
 * a cada turno, a entidade é curada em um valor igual à quantidade de acúmulos atuais,
 * e em seguida, os acúmulos são reduzidos em 1. O efeito se encerra quando os acúmulos chegam a zero.
 * </p>
 */
public class Regeneracao extends Efeito {
    
    /** * Referência ao gerenciador de eventos do jogo.
     * Necessário para que o efeito possa cancelar sua própria inscrição quando expirar.
     */
    private Publisher publisher;

    /**
     * Construtor do efeito de Regeneração.
     * * @param dono A entidade (geralmente um {@link Heroi}) que receberá a cura.
     * @param acumulos A intensidade inicial da cura (que também define por quantos turnos ela vai durar).
     * @param publisher O gerenciador de eventos, para que o efeito possa se desinscrever no futuro.
     */
    public Regeneracao(Entidade dono, int acumulos, Publisher publisher) {
        // Fixa o nome do efeito como "Regeneração" para a classe mãe
        super("Regeneração", dono, acumulos);
        this.publisher = publisher;
    }

    /**
     * Reage aos eventos do jogo disparados pelo {@link Publisher}.
     * <p>
     * A Regeneração é ativada exclusivamente no evento de {@code FIM_TURNO_JOGADOR} 
     * e apenas se o dono for um {@link Heroi}. Quando ativada:
     * <ol>
     * <li>Cura o dono em uma quantidade igual aos acúmulos atuais.</li>
     * <li>Reduz os acúmulos em 1.</li>
     * <li>Se os acúmulos chegarem a zero, remove o efeito do dono e cancela a assinatura de eventos.</li>
     * </ol>
     * </p>
     * * @param evento O evento atual do ciclo do jogo.
     */
    @Override
    public void serNotificado(TipoEvento evento) {
        // Verifica se é o momento certo de curar e se o dono é realmente o Herói
        if (evento == TipoEvento.FIM_TURNO_JOGADOR && dono instanceof Heroi) {
            
            System.out.println("\n[EFEITO] " + dono.getNome() + " curou " + this.acumulos + " de HP graças à Regeneração!");
            
            dono.curar(this.acumulos);
            
            // Reduz 1 acúmulo (fazendo a cura ser menor no próximo turno)
            this.acumulos -= 1;

            // Limpeza: remove o efeito quando ele expira para evitar memory leaks (vazamento de memória)
            if (this.acumulos <= 0) {
                System.out.println("[EFEITO] A Regeneração de " + dono.getNome() + " acabou.");
                dono.removerEfeito(this);
                publisher.desinscrever(this);
            }
        }
    }
}