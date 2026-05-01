/**
 * Item comprável em uma loja.
 */
public interface ItemLoja {

    String getNome();

    String getDescricao();

    int getPreco();

    void aplicar(Heroi heroi, Baralho baralho);
}
