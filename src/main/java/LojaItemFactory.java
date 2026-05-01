import java.util.List;

/**
 * Factory Method para criar ofertas de loja.
 */
public abstract class LojaItemFactory {

    public abstract List<ItemLoja> criarItens(Heroi heroi);
}
