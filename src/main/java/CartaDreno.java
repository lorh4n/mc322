/**
 * Representa uma carta que drena vida do inimigo para curar o herói.
 * <p>
 * Causa dano ao alvo e, ao mesmo tempo, restaura pontos de vida do usuário,
 * criando uma mecânica de sustento em combate.
 * </p>
 */
public class CartaDreno extends Carta {

    private int dano;
    private int cura;

    /**
     * Construtor da Carta de Dreno.
     * @param nome      O nome de exibição da carta.
     * @param custo     O custo em energia para jogar a carta.
     * @param descricao O texto descritivo do efeito.
     * @param dano      O dano causado ao alvo.
     * @param cura      A quantidade de HP restaurada ao usuário.
     */
    public CartaDreno(String nome, int custo, String descricao, int dano, int cura) {
        super(nome, custo, descricao);
        this.dano = dano;
        this.cura = cura;
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
     * Causa dano ao alvo e cura o usuário pela quantidade de {@code cura}.
     * @param usuario A entidade que usa a carta e recebe a cura.
     * @param alvo    A entidade que recebe o dano.
     * @param publisher O gerenciador de eventos do jogo.
     */
    @Override
    public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
        if (alvo != null) {
            alvo.receberDano(dano);
        }
        if (usuario != null) {
            usuario.curar(cura);
        }
    }
}
