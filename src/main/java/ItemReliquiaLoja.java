/**
 * Item de loja que adiciona uma relíquia ao herói.
 */
public class ItemReliquiaLoja implements ItemLoja {

    private final Reliquia reliquia;
    private final int preco;

    public ItemReliquiaLoja(Reliquia reliquia, int preco) {
        this.reliquia = reliquia;
        this.preco = preco;
    }

    @Override
    public String getNome() {
        return "Relíquia: " + reliquia.getNome();
    }

    @Override
    public String getDescricao() {
        return reliquia.getDescricao();
    }

    @Override
    public int getPreco() {
        return preco;
    }

    @Override
    public void aplicar(Heroi heroi, Baralho baralho) {
        heroi.adicionarReliquia(reliquia);
        System.out.println("Relíquia obtida: " + reliquia.getNome());
    }
}
