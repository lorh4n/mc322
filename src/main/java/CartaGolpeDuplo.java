/**
 * Representa uma carta de ataque que golpeia o inimigo duas vezes seguidas.
 * <p>
 * Cada golpe é processado individualmente, ou seja, se o primeiro golpe quebrar
 * o escudo, o segundo golpe causa dano diretamente à vida do alvo.
 * </p>
 */
public class CartaGolpeDuplo extends Carta {

    private int danoPorGolpe;

    /**
     * Construtor da Carta de Golpe Duplo.
     * @param nome        O nome de exibição da carta.
     * @param custo       O custo em energia para jogar a carta.
     * @param descricao   O texto descritivo do efeito.
     * @param danoPorGolpe O dano causado por cada um dos dois golpes.
     */
    public CartaGolpeDuplo(String nome, int custo, String descricao, int danoPorGolpe) {
        super(nome, custo, descricao);
        this.danoPorGolpe = danoPorGolpe;
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
     * Golpeia o alvo duas vezes, cada vez causando {@code danoPorGolpe} de dano.
     * @param usuario A entidade que usa a carta.
     * @param alvo    A entidade que recebe os dois golpes.
     * @param publisher O gerenciador de eventos do jogo.
     */
    @Override
    public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
        if (alvo != null) {
            System.out.println(" > Primeiro golpe!");
            alvo.receberDano(danoPorGolpe);
            System.out.println(" > Segundo golpe!");
            alvo.receberDano(danoPorGolpe);
        }
    }
}
