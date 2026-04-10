/**
 * Define os tipos de eventos que podem ocorrer durante o ciclo de combate do jogo.
 * <p>
 * Este enum atua como o "vocabulário" de comunicação do Padrão Observer. 
 * Ele é utilizado pelo {@link Publisher} para avisar todos os {@link Subscriber}s 
 * (como os efeitos de status) sobre qual fase do turno está acontecendo, permitindo 
 * que eles reajam no momento exato (ex: curar no início do turno, ou tomar dano no final).
 * </p>
 */
public enum TipoEvento {
    
    /**
     * Disparado no exato momento em que o turno do Herói começa, 
     * antes de ele poder jogar qualquer carta.
     * Ideal para zerar os escudos do turno anterior ou processar efeitos de início de rodada.
     */
    INICIO_TURNO_JOGADOR,

    /**
     * Disparado no momento em que o Herói encerra sua vez (passa o turno).
     * Ideal para ativar efeitos que beneficiam ou prejudicam o herói após suas ações 
     * (como o efeito de Regeneração).
     */
    FIM_TURNO_JOGADOR,

    /**
     * Disparado no exato momento em que a fase dos Inimigos começa,
     * antes de eles executarem suas intenções (ataques ou debuffs).
     */
    INICIO_TURNO_INIMIGO,

    /**
     * Disparado após os Inimigos concluírem todas as suas ações do turno.
     * Ideal para ativar efeitos contínuos que afligem os inimigos (como Veneno),
     * fazendo com que sofram o dano antes de devolver a vez para o jogador.
     */
    FIM_TURNO_INIMIGO
}