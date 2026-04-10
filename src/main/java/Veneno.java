/**
 * Representa um efeito de status negativo (debuff) que causa dano ao longo do tempo.
 * <p>
 * Esta classe herda de {@link Efeito} e implementa a mecânica de dano contínuo (DoT).
 * A cada turno aplicável, a entidade afligida perde pontos de vida diretamente 
 * (ignorando completamente qualquer escudo) em uma quantidade equivalente aos acúmulos 
 * atuais de veneno. Após o dano, os acúmulos são reduzidos em 1.
 * </p>
 */
public class Veneno extends Efeito {
    
    /**
     * Referência ao gerenciador de eventos do jogo.
     * Necessário para que o efeito possa cancelar sua própria inscrição (desinscrever)
     * quando os acúmulos chegarem a zero, evitando vazamentos de memória (memory leaks).
     */
    private Publisher publisher; 

    /**
     * Construtor do efeito de Veneno.
     * * @param dono A entidade (Herói ou Inimigo) que está sofrendo o efeito do veneno.
     * @param acumulos A intensidade inicial do veneno, que dita tanto o dano imediato causado quanto a sua duração total em turnos.
     * @param publisher O gerenciador de eventos, para que o efeito possa gerenciar seu próprio ciclo de vida.
     */
    public Veneno(Entidade dono, int acumulos, Publisher publisher) {
        // Fixa o nome do efeito como "Veneno" para a classe mãe
        super("Veneno", dono, acumulos);
        this.publisher = publisher;
    }

    /**
     * Reage aos eventos do jogo disparados pelo {@link Publisher}.
     * <p>
     * O Veneno escuta especificamente o evento de {@code FIM_TURNO_JOGADOR}. Quando este evento ocorre:
     * <ol>
     * <li>Causa dano direto à vida do {@code dono} igual à quantidade de acúmulos atuais.</li>
     * <li>Subtrai 1 dos acúmulos.</li>
     * <li>Se os acúmulos zerarem, limpa o efeito da lista da Entidade e se desinscreve do Publisher.</li>
     * </ol>
     * </p>
     * * @param evento O tipo do evento do jogo que acabou de ser disparado.
     */
    @Override
    public void serNotificado(TipoEvento evento) {
        // O Veneno age no FIM do turno do jogador
        if (evento == TipoEvento.FIM_TURNO_JOGADOR) {
            
            System.out.println("\n[EFEITO] " + dono.getNome() + " sofreu " + this.acumulos + " de dano de Veneno!");
            
            // Aplica o dano direto na vida (lembra do método perderVida que criamos?)
            dono.perderVida(this.acumulos);
            
            // Reduz 1 acúmulo por turno
            this.acumulos -= 1;

            // Se o veneno acabou, limpa ele da entidade e do jogo
            if (this.acumulos <= 0) {
                System.out.println("[EFEITO] O Veneno de " + dono.getNome() + " passou!");
                dono.removerEfeito(this);     // Tira da lista da Entidade
                publisher.desinscrever(this); // Tira da lista do App (Publisher)
            }
        }
    }
}