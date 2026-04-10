/**
 * Representa uma carta híbrida que combina dano imediato com envenenamento.
 * <p>
 * Ao ser jogada, primeiro causa dano direto ao alvo e em seguida aplica
 * acúmulos de {@link Veneno}, aproveitando a infraestrutura de efeitos já existente.
 * </p>
 */
public class CartaAtaqueVenenoso extends Carta {

    private int dano;
    private int acumulos;

    /**
     * Construtor da Carta de Ataque Venenoso.
     * @param nome     O nome de exibição da carta.
     * @param custo    O custo em energia para jogar a carta.
     * @param descricao O texto descritivo do efeito.
     * @param dano     O dano imediato causado ao alvo.
     * @param acumulos A quantidade de acúmulos de Veneno aplicados após o dano.
     */
    public CartaAtaqueVenenoso(String nome, int custo, String descricao, int dano, int acumulos) {
        super(nome, custo, descricao);
        this.dano = dano;
        this.acumulos = acumulos;
    }

    /**
     * Informa que esta carta exige um alvo.
     * @return Sempre {@code true}.
     */
    @Override
    public boolean precisaAlvo() {
        return true;
    }

    /**
     * Causa dano imediato ao alvo e aplica acúmulos de {@link Veneno}.
     * @param usuario A entidade que usa a carta.
     * @param alvo    A entidade que recebe o dano e o veneno.
     * @param publisher O gerenciador de eventos do jogo.
     */
    @Override
    public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
        if (alvo != null) {
            alvo.receberDano(dano);
            Efeito veneno = new Veneno(alvo, acumulos, publisher);
            alvo.aplicarEfeito(veneno, publisher);
        }
    }
}
