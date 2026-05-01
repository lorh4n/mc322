import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Encapsula a lógica de um combate individual entre o herói e uma lista de inimigos.
 */
public class Batalha extends Evento implements Publisher {

    private final Heroi heroi;
    private List<Inimigo> inimigos;
    private final Baralho baralho;
    private final int energiaMax;
    private final Supplier<List<Inimigo>> fabricaInimigos;

    private final List<Subscriber> subscribers = new ArrayList<>();
    private final List<Subscriber> removerFila = new ArrayList<>();

    /**
     * Cria uma nova batalha com fábrica de inimigos (Requisito Tarefa 6).
     */
    public Batalha(Heroi heroi, Baralho baralho, Supplier<List<Inimigo>> fabricaInimigos) {
        this(heroi, baralho, fabricaInimigos, 3);
    }

    public Batalha(Heroi heroi, Baralho baralho, Supplier<List<Inimigo>> fabricaInimigos, int energiaMax) {
        this.heroi = heroi;
        this.baralho = baralho;
        this.fabricaInimigos = fabricaInimigos;
        this.energiaMax = energiaMax;
    }

    /**
     * Construtor de compatibilidade — aceita lista direta de inimigos (usado em testes).
     */
    public Batalha(Heroi heroi, List<Inimigo> inimigos, Baralho baralho) {
        this(heroi, inimigos, baralho, 3);
    }

    public Batalha(Heroi heroi, List<Inimigo> inimigos, Baralho baralho, int energiaMax) {
        this.heroi = heroi;
        this.baralho = baralho;
        this.fabricaInimigos = () -> new ArrayList<>(inimigos);
        this.energiaMax = energiaMax;
        this.inimigos = inimigos; // já inicializa direto para resolverAutomatico
    }

    @Override
    public boolean iniciar(Heroi heroi, Scanner scanner) {
        // Inicializa os inimigos usando a fábrica apenas quando o evento começa
        this.inimigos = fabricaInimigos.get();
        return executar(scanner);
    }

    @Override
    public void inscrever(Subscriber s) {
        if (!subscribers.contains(s)) subscribers.add(s);
    }

    @Override
    public void desinscrever(Subscriber s) {
        removerFila.add(s);
    }

    @Override
    public void notificar(TipoEvento evento) {
        for (Subscriber s : subscribers) {
            s.serNotificado(evento);
        }
        if (!removerFila.isEmpty()) {
            subscribers.removeAll(removerFila);
            removerFila.clear();
        }
    }

    /** @return O herói desta batalha. */
    public Heroi getHeroi() { return heroi; }

    /** @return A lista de inimigos desta batalha. */
    public List<Inimigo> getInimigos() { return inimigos; }

    /** @return {@code true} se algum inimigo ainda estiver vivo. */
    public boolean algumInimigoVivo() {
        for (Inimigo i : inimigos) if (i.estaVivo()) return true;
        return false;
    }

    /**
     * Resolve a batalha sem interação do jogador. Usado principalmente em testes.
     * A cada turno o herói joga cartas da mão da esquerda para a direita até esgotar
     * a energia ou ficar sem cartas jogáveis.
     * @param maxTurnos Limite de turnos para evitar loops infinitos em testes.
     * @return {@code true} se o herói venceu, {@code false} se foi derrotado.
     */
    public boolean resolverAutomatico(int maxTurnos) {
        prepararInicio();
        int turno = 0;
        while (algumInimigoVivo() && heroi.estaVivo() && turno < maxTurnos) {
            turno++;
            heroi.setEscudo(0);
            notificar(TipoEvento.INICIO_TURNO_JOGADOR);
            baralho.comprarCartas(Math.min(3, baralho.tamanhoCompra() + baralho.tamanhoDescarte()));
            int energia = energiaMax;
            while (baralho.tamanhoMao() > 0 && energia > 0 && algumInimigoVivo() && heroi.estaVivo()) {
                Carta c = baralho.getCarta(0);
                if (c == null) break;
                if (c.getCusto() > energia) { baralho.descartarCarta(0); continue; }
                Inimigo alvo = c.precisaAlvo() ? primeiroInimigoVivo() : null;
                int custo = baralho.usarCarta(0, alvo, heroi, energia, this);
                if (custo == -1) break;
                energia -= custo;
            }
            notificar(TipoEvento.FIM_TURNO_JOGADOR);
            if (!algumInimigoVivo() || !heroi.estaVivo()) break;
            notificar(TipoEvento.INICIO_TURNO_INIMIGO);
            for (Inimigo ini : inimigos) if (ini.estaVivo()) ini.executarAcao(heroi, this);
            notificar(TipoEvento.FIM_TURNO_INIMIGO);
        }
        boolean venceu = heroi.estaVivo() && !algumInimigoVivo();
        if (venceu) {
            processarVitoria(null);
        }
        return venceu;
    }

    private Inimigo primeiroInimigoVivo() {
        for (Inimigo i : inimigos) if (i.estaVivo()) return i;
        return null;
    }

    private void prepararInicio() {
        heroi.resetarEstadoBatalha();
        baralho.resetarParaNovaBatalha();
        for (Reliquia reliquia : heroi.getReliquias()) {
            reliquia.prepararParaBatalha(this);
        }
        notificar(TipoEvento.INICIO_BATALHA);
    }

    /**
     * Executa o combate interativo no terminal.
     * Ao iniciar, reseta o estado transitório (escudo, efeitos) e o baralho.
     * @param scanner Scanner para entrada do terminal.
     * @return {@code true} se o herói venceu, {@code false} em caso de derrota.
     */
    public boolean executar(Scanner scanner) {
        prepararInicio();
        int energia = energiaMax;

        System.out.println("\n⚔️  O COMBATE COMEÇOU! ⚔️");
        System.out.println("Você enfrenta: " + descricaoInimigos());
        continuar(scanner);

        comprarCartasComEscolha(scanner);
        continuar(scanner);

        while (algumInimigoVivo() && heroi.estaVivo()) {
            limparTela(scanner);
            heroi.setEscudo(0);
            energia = energiaMax;
            notificar(TipoEvento.INICIO_TURNO_JOGADOR);

            boolean turnoAtivo = true;
            while (turnoAtivo && algumInimigoVivo() && heroi.estaVivo()) {
                exibirEstado(energia);
                baralho.mostrarMao();
                System.out.print("Escolha: ");

                String entrada = scanner.nextLine();
                if (entrada.isEmpty()) continue;

                int escolha;
                try {
                    escolha = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite um número válido.");
                    continuar(scanner);
                    continue;
                }
                System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

                if (escolha == -1) {
                    turnoAtivo = false;
                    System.out.println("Você encerrou seu turno.");
                    continuar(scanner);
                } else if (escolha == -2) {
                    tratarDescarteManual(scanner);
                    continuar(scanner);
                } else if (escolha >= baralho.tamanhoMao() || escolha < -2) {
                    System.out.println("Opção inválida!");
                    continuar(scanner);
                } else {
                    Carta carta = baralho.getCarta(escolha);
                    Inimigo alvo = (carta != null && carta.precisaAlvo())
                        ? selecionarAlvo(scanner) : null;
                    int retorno = baralho.usarCarta(escolha, alvo, heroi, energia, this);
                    if (retorno == -1) {
                        System.out.println("Energia insuficiente!");
                    } else {
                        System.out.printf("Você usou a carta %d!\n", escolha);
                        energia -= retorno;
                    }
                    continuar(scanner);
                }

                if (turnoAtivo && algumInimigoVivo() && heroi.estaVivo()) limparTela(scanner);
            }

            notificar(TipoEvento.FIM_TURNO_JOGADOR);

            if (algumInimigoVivo() && heroi.estaVivo()) {
                limparTela(scanner);
                notificar(TipoEvento.INICIO_TURNO_INIMIGO);
                System.out.println("\n" + Rato.CorOutput + "╔══════════════════════════════════════╗");
                System.out.println("║         TURNO DOS INIMIGOS           ║");
                System.out.println("╠══════════════════════════════════════╣" + Rato.Reset);
                for (Inimigo ini : inimigos) {
                    if (ini.estaVivo()) ini.executarAcao(heroi, this);
                }
                System.out.println(Rato.CorOutput + "╚══════════════════════════════════════╝" + Rato.Reset);
                continuar(scanner);
                notificar(TipoEvento.FIM_TURNO_INIMIGO);
            }

            if (algumInimigoVivo() && heroi.estaVivo()) {
                limparTela(scanner);
                System.out.println("🔄 Preparando próximo turno...");
                comprarCartasComEscolha(scanner);
                continuar(scanner);
            }
        }

        boolean venceu = heroi.estaVivo() && !algumInimigoVivo();
        limparTela(scanner);
        if (venceu) {
            System.out.println("\n🏆 [ VITÓRIA ] VOCÊ VENCEU A BATALHA!");
            processarVitoria(scanner);
        } else {
            System.out.println("\n💀 [ DERROTA ] VOCÊ PERDEU!");
        }
        continuar(scanner);
        return venceu;
    }

    private void processarVitoria(Scanner scanner) {
        notificar(TipoEvento.VITORIA_BATALHA);
        concederRecompensa(scanner);
    }

    private void concederRecompensa(Scanner scanner) {
        int ouroGanho = 20;
        heroi.adicionarOuro(ouroGanho);
        System.out.println("Recompensa: +" + ouroGanho + " ouro. Total: " + heroi.getOuro());

        if (scanner == null) return;

        List<Carta> opcoes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            opcoes.add(CartaFactory.criarAleatoria());
        }

        System.out.println("\nEscolha uma carta para adicionar ao baralho:");
        for (int i = 0; i < opcoes.size(); i++) {
            Carta carta = opcoes.get(i);
            System.out.printf("[%d] %s (Custo: %d) - %s%n",
                i, carta.getNome(), carta.getCusto(), carta.getDescricao());
        }
        System.out.println("[-1] Pular recompensa de carta");
        System.out.print("Escolha: ");

        try {
            int escolha = Integer.parseInt(scanner.nextLine().trim());
            if (escolha >= 0 && escolha < opcoes.size()) {
                Carta escolhida = opcoes.get(escolha);
                baralho.adicionarCarta(escolhida);
                System.out.println("Carta adicionada: " + escolhida.getNome());
            } else {
                System.out.println("Você pulou a recompensa de carta.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Recompensa de carta pulada.");
        }
    }

    private void exibirEstado(int energia) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.printf("║ %-36s ║%n",
            heroi.getNome() + ": " + heroi.getVida() + " HP | Escudo: " + heroi.getEscudo());
        System.out.println("╠══════════════════════════════════════╣");
        for (int i = 0; i < inimigos.size(); i++) {
            Inimigo ini = inimigos.get(i);
            if (ini.estaVivo()) {
                System.out.printf("║ [%d] " + Rato.CorOutput + "%-31s" + Rato.Reset + " ║%n",
                    i, ini.getNome() + ": " + ini.getVida() + " HP");
                System.out.printf("║     " + Rato.CorOutput + "%-31s" + Rato.Reset + " ║%n",
                    "→ " + ini.anunciarIntencao());
            }
        }
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║ %-36s ║%n", "Energia: " + energia + "/" + energiaMax);
    }

    private String descricaoInimigos() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inimigos.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(inimigos.get(i).getNome()).append("(").append(inimigos.get(i).getVida()).append(")");
        }
        return sb.toString();
    }

    private void comprarCartasComEscolha(Scanner scanner) {
        int disponiveis = baralho.tamanhoCompra() + baralho.tamanhoDescarte();
        int maxCompra = Math.min(5, disponiveis);
        if (maxCompra == 0) {
            if (scanner != null) System.out.println("Sem cartas disponíveis para comprar.");
            return;
        }
        
        if (scanner == null) {
            baralho.comprarCartas(3); // Default for automatic tests
            return;
        }

        System.out.printf("Quantas cartas deseja comprar? (1-%d): ", maxCompra);
        int quantidade = 1;
        try {
            quantidade = Integer.parseInt(scanner.nextLine().trim());
            if (quantidade < 1) quantidade = 1;
            if (quantidade > maxCompra) quantidade = maxCompra;
        } catch (NumberFormatException e) {
            quantidade = 1;
        }
        baralho.comprarCartas(quantidade);
    }

    private void tratarDescarteManual(Scanner scanner) {
        if (baralho.tamanhoMao() == 0) {
            System.out.println("Nenhuma carta na mão para descartar.");
            return;
        }
        baralho.mostrarMao();
        System.out.print("Qual carta deseja descartar? (0-" + (baralho.tamanhoMao() - 1) + "): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim());
            if (!baralho.descartarCarta(idx)) {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Entrada inválida.");
        }
    }

    private Inimigo selecionarAlvo(Scanner scanner) {
        List<Inimigo> vivos = new ArrayList<>();
        for (Inimigo i : inimigos) if (i.estaVivo()) vivos.add(i);
        if (vivos.size() == 1) return vivos.get(0);
        System.out.println("Escolha o alvo:");
        for (int i = 0; i < inimigos.size(); i++) {
            if (inimigos.get(i).estaVivo()) {
                System.out.printf("  [%d] %s (%d HP)%n",
                    i, inimigos.get(i).getNome(), inimigos.get(i).getVida());
            }
        }
        System.out.print("Alvo: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim());
            if (idx >= 0 && idx < inimigos.size() && inimigos.get(idx).estaVivo()) {
                return inimigos.get(idx);
            }
        } catch (NumberFormatException e) {}
        return vivos.get(0);
    }

    private void continuar(Scanner scanner) {
        if (scanner == null) return;
        System.out.println("\n[ Pressione ENTER para continuar... ]");
        scanner.nextLine();
    }

    private void limparTela(Scanner scanner) {
        if (scanner == null) return;
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
