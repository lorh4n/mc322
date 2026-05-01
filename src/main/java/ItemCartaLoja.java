/**
 * Item de loja que adiciona uma carta ao baralho.
 */
public class ItemCartaLoja implements ItemLoja {

    private final Carta carta;
    private final int preco;

    public ItemCartaLoja(Carta carta, int preco) {
        this.carta = carta;
        this.preco = preco;
    }

    @Override
    public String getNome() {
        return "Carta: " + carta.getNome();
    }

    @Override
    public String getDescricao() {
        return carta.getDescricao();
    }

    @Override
    public int getPreco() {
        return preco;
    }

    @Override
    public void aplicar(Heroi heroi, Baralho baralho) {
        baralho.adicionarCarta(carta);
        System.out.println("Carta adicionada ao baralho: " + carta.getNome());
    }
}
