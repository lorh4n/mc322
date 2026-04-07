import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class App implements Publisher {

    private List<Subscriber> subscribers = new ArrayList<>();
    private List<Subscriber> removerFila = new ArrayList<>();

    private void continuar(Scanner scanner) {
        System.out.println("\n[ Pressione ENTER para continuar... ]");
        scanner.nextLine();
    }

    private void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void inscrever(Subscriber s) {
        if (!subscribers.contains(s)) {
            subscribers.add(s);
        }
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

    private void comprarCartasComEscolha(Scanner scanner, Baralho baralho) {
        int disponiveis = baralho.tamanhoCompra() + baralho.tamanhoDescarte();
        int maxCompra = Math.min(5, disponiveis);

        if (maxCompra == 0) {
            System.out.println("Sem cartas disponíveis para comprar.");
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

    private List<Inimigo> inimigosVivos(List<Inimigo> inimigos) {
        List<Inimigo> vivos = new ArrayList<>();
        for (Inimigo ini : inimigos) {
            if (ini.estaVivo()) vivos.add(ini);
        }
        return vivos;
    }

    private boolean algumVivo(List<Inimigo> inimigos) {
        return !inimigosVivos(inimigos).isEmpty();
    }

    private Inimigo selecionarAlvo(Scanner scanner, List<Inimigo> inimigos) {
        List<Inimigo> vivos = inimigosVivos(inimigos);

        if (vivos.size() == 1) return vivos.get(0);

        System.out.println("Escolha o alvo:");
        for (int i = 0; i < inimigos.size(); i++) {
            if (inimigos.get(i).estaVivo()) {
                System.out.printf("  [%d] %s (%d HP)%n", i, inimigos.get(i).getNome(), inimigos.get(i).getVida());
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

    public void iniciarCombate() throws Exception {
        Scanner scanner = new Scanner(System.in);

        int heroLife = 15;
        Heroi hero = new Heroi("Herói", heroLife);

        List<Inimigo> inimigos = new ArrayList<>();
        inimigos.add(new Rato(10, 4));
        inimigos.add(new Rato(8, 3));

        Baralho baralho = new Baralho();
        baralho.popularBaralho(10);
        baralho.embaralhar();

        int energyMax = 3;
        int energy = energyMax;

        limparTela();
        System.out.println("⚔️  O COMBATE COMEÇOU! ⚔️");
        continuar(scanner);

        limparTela();
        comprarCartasComEscolha(scanner, baralho);
        continuar(scanner);

        while (algumVivo(inimigos) && hero.estaVivo()) {
            limparTela();
            hero.setEscudo(0);
            energy = energyMax;

            notificar(TipoEvento.INICIO_TURNO_JOGADOR);

            boolean turnoAtivo = true;
            while (turnoAtivo && algumVivo(inimigos) && hero.estaVivo()) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.printf("║ %-36s ║%n",
                    hero.getNome() + ": " + hero.getVida() + "/" + heroLife + " HP | Escudo: " + hero.getEscudo());
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
                System.out.printf("║ %-36s ║%n", "Energia: " + energy + "/" + energyMax);

                baralho.mostrarMao();
                System.out.print("Escolha: ");

                String entrada = scanner.nextLine();
                if (entrada.isEmpty()) continue;

                try {
                    int escolha = Integer.parseInt(entrada);
                    System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

                    if (escolha == -1) {
                        turnoAtivo = false;
                        System.out.println("Você encerrou seu turno.");
                        continuar(scanner);
                    } else if (escolha == -2) {
                        if (baralho.tamanhoMao() == 0) {
                            System.out.println("Nenhuma carta na mão para descartar.");
                        } else {
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
                        continuar(scanner);
                    } else if (escolha >= baralho.tamanhoMao() || escolha < -2) {
                        System.out.println("Opção inválida!");
                        continuar(scanner);
                    } else {
                        Carta carta = baralho.getCarta(escolha);
                        Inimigo alvo = (carta != null && carta.precisaAlvo())
                            ? selecionarAlvo(scanner, inimigos)
                            : null;
                        int retorno = baralho.usarCarta(escolha, alvo, hero, energy, this);
                        if (retorno == -1) {
                            System.out.println("Energia insuficiente!");
                            continuar(scanner);
                        } else {
                            System.out.printf("Você usou a carta %d!\n", escolha);
                            energy -= retorno;
                            if (!algumVivo(inimigos) || !hero.estaVivo()) {
                                continuar(scanner);
                                break;
                            }
                            continuar(scanner);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite um número válido.");
                    continuar(scanner);
                }

                if (turnoAtivo && algumVivo(inimigos) && hero.estaVivo()) {
                    limparTela();
                }
            }

            notificar(TipoEvento.FIM_TURNO_JOGADOR);

            if (algumVivo(inimigos) && hero.estaVivo()) {
                limparTela();
                notificar(TipoEvento.INICIO_TURNO_INIMIGO);

                System.out.println("\n" + Rato.CorOutput + "╔══════════════════════════════════════╗");
                System.out.println("║         TURNO DOS INIMIGOS           ║");
                System.out.println("╠══════════════════════════════════════╣" + Rato.Reset);

                for (Inimigo ini : inimigos) {
                    if (ini.estaVivo()) {
                        ini.executarAcao(hero, this);
                    }
                }

                System.out.println(Rato.CorOutput + "╚══════════════════════════════════════╝" + Rato.Reset);
                continuar(scanner);

                notificar(TipoEvento.FIM_TURNO_INIMIGO);
            }

            if (!algumVivo(inimigos)) {
                limparTela();
                System.out.println("\n🏆 [ VITÓRIA ] VOCÊ VENCEU!");
                continuar(scanner);
            } else if (!hero.estaVivo()) {
                limparTela();
                System.out.println("\n💀 [ DERROTA ] VOCÊ PERDEU!");
                continuar(scanner);
            } else {
                limparTela();
                System.out.println("🔄 Preparando próximo turno...");
                comprarCartasComEscolha(scanner, baralho);
                continuar(scanner);
            }
        }
        scanner.close();
    }

    public static void main(String[] args) throws Exception {
        App jogo = new App();
        jogo.iniciarCombate();
    }
}
