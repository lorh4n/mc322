/**
 * Representa uma carta defensiva que combina escudo com regeneração de vida.
 * <p>
 * Ao ser jogada, concede imediatamente pontos de escudo ao usuário e também
 * aplica o efeito de {@link Regeneracao}, que irá curar o herói ao longo dos próximos turnos.
 * </p>
 */
public class CartaEscudoRegenero extends Carta {

    private int escudo;
    private int acumulos;

    /**
     * Construtor da Carta de Escudo com Regeneração.
     * @param nome      O nome de exibição da carta.
     * @param custo     O custo em energia para jogar a carta.
     * @param descricao O texto descritivo do efeito.
     * @param escudo    A quantidade de pontos de escudo concedida imediatamente.
     * @param acumulos  A intensidade (e duração) do efeito de Regeneração aplicado.
     */
    public CartaEscudoRegenero(String nome, int custo, String descricao, int escudo, int acumulos) {
        super(nome, custo, descricao);
        this.escudo = escudo;
        this.acumulos = acumulos;
    }

    /**
     * Concede escudo ao usuário e aplica o efeito de {@link Regeneracao}.
     * @param usuario A entidade que usa a carta e recebe os benefícios.
     * @param alvo    Ignorado por esta carta (efeito aplicado no usuário).
     * @param publisher O gerenciador de eventos do jogo.
     */
    @Override
    public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
        if (usuario != null) {
            usuario.ganharEscudo(escudo);
            Efeito regen = new Regeneracao(usuario, acumulos, publisher);
            usuario.aplicarEfeito(regen, publisher);
        }
    }
}
