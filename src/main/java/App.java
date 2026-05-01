import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada do jogo e gerenciador da progressão entre batalhas.
 * <p>
 * A {@code App} carrega o mapa, inicializa o herói e seu baralho, e conduz o
 * jogador através da árvore de nós. A cada nó, instancia uma {@link Batalha}
 * com o estado persistente do herói (vida e baralho) e processa o resultado.
 * </p>
 */
public class App {

    private static final int VIDA_INICIAL = 40;
    private static final int TAMANHO_BARALHO = 12;

    /**
     * Executa o loop de progressão: batalha → escolha de próximo nó → batalha,
     * até o herói morrer ou alcançar um nó final.
     */
    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        Heroi heroi = new Heroi("Herói", VIDA_INICIAL);
        Baralho baralho = new Baralho();
        baralho.popularBaralho(TAMANHO_BARALHO);
        baralho.embaralhar();

        Mapa mapa = Mapa.gerarPadrao(heroi, baralho);
        NoMapa atual = mapa.getRaiz();
        atual.marcarVisitado();

        exibirAberturaJogo();

        while (heroi.estaVivo()) {
            Evento evento = atual.getEvento();
            if (evento != null) {
                System.out.println("\n>>> Entrando em: " + atual.getNome() + " <<<");
                exibirEstadoPersistente(heroi, baralho);
                boolean sobreviveu = evento.iniciar(heroi, scanner);
                
                if (!sobreviveu || !heroi.estaVivo()) {
                    System.out.println("\n💀 FIM DE JOGO — você caiu em " + atual.getNome() + ".");
                    break;
                }
            }

            if (atual.getFilhos().isEmpty()) {
                System.out.println("\n🏆 VITÓRIA FINAL! Você completou a jornada em "
                    + atual.getNome() + " com " + heroi.getVida() + " HP restantes.");
                break;
            }

            NoMapa proximo = escolherProximoNo(scanner, atual);
            if (proximo == null) {
                System.out.println("\nSem caminhos disponíveis a partir de " + atual.getNome() + ". Fim.");
                break;
            }
            atual = proximo;
            atual.marcarVisitado();
        }

        scanner.close();
    }

    /**
     * Solicita ao jogador que escolha um dos nós filhos não visitados do nó atual.
     * @param scanner Scanner de entrada.
     * @param atual Nó atual.
     * @return Próximo nó escolhido, ou {@code null} se não houver opções.
     */
    private NoMapa escolherProximoNo(Scanner scanner, NoMapa atual) {
        List<NoMapa> opcoes = atual.getFilhos();
        List<NoMapa> disponiveis = new java.util.ArrayList<>();
        for (NoMapa n : opcoes) {
            if (!n.isVisitado()) disponiveis.add(n);
        }
        if (disponiveis.isEmpty()) return null;
        if (disponiveis.size() == 1) {
            NoMapa unico = disponiveis.get(0);
            System.out.println("\nCaminho único: " + unico.getNome());
            return unico;
        }

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         ESCOLHA O PRÓXIMO NÓ         ║");
        System.out.println("╠══════════════════════════════════════╣");
        for (int i = 0; i < disponiveis.size(); i++) {
            NoMapa n = disponiveis.get(i);
            System.out.printf("║ [%d] %-32s ║%n", i, n.getNome());
        }
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Escolha: ");

        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(entrada);
                if (idx >= 0 && idx < disponiveis.size()) return disponiveis.get(idx);
            } catch (NumberFormatException ignored) {}
            System.out.print("Entrada inválida. Escolha novamente: ");
        }
    }

    private void exibirAberturaJogo() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   AVENTURAS PELO MAPA — MC322        ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ Sua vida e baralho persistem entre   ║");
        System.out.println("║ eventos. Vença batalhas, compre      ║");
        System.out.println("║ melhorias e escolha seu caminho.     ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void exibirEstadoPersistente(Heroi heroi, Baralho baralho) {
        System.out.println("HP: " + heroi.getVida() + "/" + heroi.getVidaMaxima()
            + " | Ouro: " + heroi.getOuro()
            + " | Cartas: " + baralho.listarTodasCartas().size()
            + " | Relíquias: " + heroi.getReliquias().size());
    }

    /**
     * Ponto de entrada do programa.
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        new App().iniciar();
    }
}
