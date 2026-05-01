import java.util.ArrayList;
import java.util.List;

/**
 * Cria ofertas de cartas para a loja.
 */
public class CartaLojaFactory extends LojaItemFactory {

    @Override
    public List<ItemLoja> criarItens(Heroi heroi) {
        List<ItemLoja> itens = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            itens.add(new ItemCartaLoja(CartaFactory.criarAleatoria(), 25 + (i * 5)));
        }
        return itens;
    }
}
