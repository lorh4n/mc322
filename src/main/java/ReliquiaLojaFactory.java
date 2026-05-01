import java.util.ArrayList;
import java.util.List;

/**
 * Cria ofertas de relíquias para a loja.
 */
public class ReliquiaLojaFactory extends LojaItemFactory {

    @Override
    public List<ItemLoja> criarItens(Heroi heroi) {
        List<ItemLoja> itens = new ArrayList<>();
        itens.add(new ItemReliquiaLoja(new ReliquiaEscudoInicial(heroi), 45));
        itens.add(new ItemReliquiaLoja(new ReliquiaCuraPosBatalha(heroi), 50));
        return itens;
    }
}
