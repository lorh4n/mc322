import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Evento de mapa em que o jogador gasta ouro para comprar cartas/relíquias
 * ou remover cartas do baralho.
 */
public class Loja extends Evento {

    private static final int PRECO_REMOCAO = 30;

    private final Baralho baralho;
    private final List<LojaItemFactory> factories = new ArrayList<>();

    public Loja(Baralho baralho) {
        this.baralho = baralho;
        factories.add(new CartaLojaFactory());
        factories.add(new ReliquiaLojaFactory());
    }

    @Override
    public boolean iniciar(Heroi heroi, Scanner scanner) {
        System.out.println("\n=== LOJA DO CAMINHO ===");
        System.out.println("Ouro disponível: " + heroi.getOuro());

        List<ItemLoja> itens = criarEstoque(heroi);
        boolean comprando = true;
        while (comprando && scanner != null) {
            exibirMenu(itens, heroi);
            String entrada = scanner.nextLine().trim();
            int escolha = lerInteiro(entrada, -99);

            if (escolha == -1) {
                comprando = false;
            } else if (escolha == -2) {
                removerCarta(heroi, scanner);
            } else if (escolha >= 0 && escolha < itens.size()) {
                comprarItem(heroi, itens, escolha);
            } else {
                System.out.println("Opção inválida.");
            }
        }
        return heroi.estaVivo();
    }

    private List<ItemLoja> criarEstoque(Heroi heroi) {
        List<ItemLoja> itens = new ArrayList<>();
        for (LojaItemFactory factory : factories) {
            itens.addAll(factory.criarItens(heroi));
        }
        return itens;
    }

    private void exibirMenu(List<ItemLoja> itens, Heroi heroi) {
        System.out.println("\nOuro: " + heroi.getOuro());
        for (int i = 0; i < itens.size(); i++) {
            ItemLoja item = itens.get(i);
            System.out.printf("[%d] %s - %d ouro (%s)%n",
                i, item.getNome(), item.getPreco(), item.getDescricao());
        }
        System.out.println("[-2] Remover carta do baralho - " + PRECO_REMOCAO + " ouro");
        System.out.println("[-1] Sair da loja");
        System.out.print("Escolha: ");
    }

    private void comprarItem(Heroi heroi, List<ItemLoja> itens, int escolha) {
        ItemLoja item = itens.get(escolha);
        if (!heroi.removerOuro(item.getPreco())) {
            System.out.println("Ouro insuficiente.");
            return;
        }
        item.aplicar(heroi, baralho);
        itens.remove(escolha);
    }

    private void removerCarta(Heroi heroi, Scanner scanner) {
        if (!heroi.removerOuro(PRECO_REMOCAO)) {
            System.out.println("Ouro insuficiente para remover carta.");
            return;
        }

        System.out.println("\nEscolha uma carta para remover:");
        baralho.mostrarTodasCartas();
        System.out.print("Carta: ");
        int indice = lerInteiro(scanner.nextLine().trim(), -1);
        Carta removida = baralho.removerCarta(indice);
        if (removida == null) {
            heroi.adicionarOuro(PRECO_REMOCAO);
            System.out.println("Índice inválido. O ouro foi devolvido.");
        } else {
            System.out.println("Carta removida: " + removida.getNome());
        }
    }

    private int lerInteiro(String entrada, int padrao) {
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return padrao;
        }
    }
}
