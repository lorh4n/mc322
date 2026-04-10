/**
 * Representa uma carta que interage diretamente com o efeito de {@link Veneno}.
 * <p>
 * Remove todos os acúmulos de Veneno do alvo e converte essa quantidade
 * em dano imediato. Se o alvo não estiver envenenado, causa um dano base fixo.
 * </p>
 */
public class CartaExtirpar extends Carta {

    /** Dano base aplicado quando o alvo não possui Veneno. */
    private static final int DANO_BASE = 2;

    /**
     * Construtor da Carta de Extirpar.
     * @param nome      O nome de exibição da carta.
     * @param custo     O custo em energia para jogar a carta.
     * @param descricao O texto descritivo do efeito.
     */
    public CartaExtirpar(String nome, int custo, String descricao) {
        super(nome, custo, descricao);
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
     * Remove os acúmulos de {@link Veneno} do alvo e converte-os em dano imediato.
     * <p>
     * Se o alvo possuir Veneno, o efeito é completamente removido e o total de
     * acúmulos é aplicado como dano direto. Caso contrário, causa {@value DANO_BASE} de dano base.
     * </p>
     * @param usuario A entidade que usa a carta.
     * @param alvo    A entidade que terá o veneno extirpado e sofrerá o dano.
     * @param publisher O gerenciador de eventos, usado para desinscrever o efeito removido.
     */
    @Override
    public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
        if (alvo == null) return;

        Efeito venenoEncontrado = null;
        for (Efeito e : alvo.efeitos) {
            if (e.getNome().equals("Veneno")) {
                venenoEncontrado = e;
                break;
            }
        }

        if (venenoEncontrado != null) {
            int dano = venenoEncontrado.getAcumulos();
            System.out.println(" > Extirpando " + dano + " acúmulos de Veneno em dano!");
            alvo.receberDano(dano);
            alvo.removerEfeito(venenoEncontrado);
            publisher.desinscrever(venenoEncontrado);
        } else {
            System.out.println(" > Alvo sem veneno. Causa " + DANO_BASE + " de dano base.");
            alvo.receberDano(DANO_BASE);
        }
    }
}
