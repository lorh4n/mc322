import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal que atua como o Motor do Jogo (Game Engine) e gerenciador de eventos.
 * <p>
 * A classe {@code App} controla o fluxo principal (Game Loop), gerenciando os turnos,
 * renderizando a interface de texto no terminal e processando a entrada do usuário.
 * Além disso, atua como o {@link Publisher} central do Padrão Observer, notificando
 * todos os efeitos ativos sobre a passagem do tempo no combate.
 * </p>
 */
public class App implements Publisher {

    /** Lista principal de observadores (como efeitos de Veneno e Regeneração) inscritos no jogo. */
    private List<Subscriber> subscribers = new ArrayList<>();
    
    /** * Fila de segurança para remoção de observadores.
     * Evita o erro de {@code ConcurrentModificationException} que ocorreria se 
     * tentássemos remover um observador da lista principal enquanto iteramos sobre ela.
     */
    private List<Subscriber> removerFila = new ArrayList<>();

    /**
     * Pausa a execução do jogo até que o jogador pressione a tecla ENTER.
     * @param scanner O objeto Scanner usado para ler a entrada do terminal.
     */
    private void continuar(Scanner scanner) {
        System.out.println("\n[ Pressione ENTER para continuar... ]");
        scanner.nextLine();
    }

    /**
     * Limpa a tela do terminal usando códigos de escape ANSI.
     * Cria a sensação de transição de telas no jogo de texto.
     */
    private void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Inscreve um novo observador (efeito) na lista de notificações do jogo.
     * @param s O observador a ser adicionado.
     */
    @Override
    public void inscrever(Subscriber s) {
        if (!subscribers.contains(s)) {
            subscribers.add(s);
        }
    }

    /**
     * Agenda a remoção de um observador.
     * <p>
     * O observador é colocado na {@code removerFila} e será efetivamente excluído
     * de forma segura no final do ciclo de notificação atual.
     * </p>
     * @param s O observador que deseja cancelar a inscrição (ex: um veneno que acabou).
     */
    @Override
    public void desinscrever(Subscriber s) {
        removerFila.add(s);
    }

    /**
     * Dispara um evento para todos os observadores inscritos.
     * Após a notificação, limpa com segurança os observadores que pediram para sair.
     * @param evento O evento atual do jogo (ex: INICIO_TURNO_JOGADOR).
     */
    @Override
    public void notificar(TipoEvento evento) {
        // Avisa todo mundo
        for (Subscriber s : subscribers) {
            s.serNotificado(evento);
        }
        // Limpa com segurança quem pediu para sair durante a notificação
        if (!removerFila.isEmpty()) {
            subscribers.removeAll(removerFila);
            removerFila.clear();
        }
    }

    /**
     * Interface interativa para o jogador escolher quantas cartas deseja comprar no início do turno.
     * Limita a compra ao máximo de 5 cartas ou ao total de cartas disponíveis no baralho+descarte.
     * @param scanner O objeto Scanner para ler a escolha do usuário.
     * @param baralho O baralho manipulado pelo jogador.
     */
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

    /**
     * Filtra a lista de inimigos retornando apenas aqueles que ainda estão vivos.
     * @param inimigos A lista completa de inimigos da batalha.
     * @return Uma nova lista contendo apenas os inimigos com vida maior que 0.
     */
    private List<Inimigo> inimigosVivos(List<Inimigo> inimigos) {
        List<Inimigo> vivos = new ArrayList<>();
        for (Inimigo ini : inimigos) {
            if (ini.estaVivo()) vivos.add(ini);
        }
        return vivos;
    }

    /**
     * Verifica se ainda há pelo menos um inimigo vivo na batalha.
     * @param inimigos A lista de inimigos da batalha.
     * @return {@code true} se houver sobreviventes, {@code false} se todos foram derrotados.
     */
    private boolean algumVivo(List<Inimigo> inimigos) {
        return !inimigosVivos(inimigos).isEmpty();
    }

    /**
     * Abre uma interface para o jogador selecionar qual inimigo vivo receberá o alvo de uma carta.
     * Se houver apenas um inimigo vivo, ele é selecionado automaticamente.
     * @param scanner O objeto Scanner para ler a escolha.
     * @param inimigos A lista de inimigos na batalha.
     * @return O {@link Inimigo} escolhido como alvo.
     */
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
        return vivos.get(0); // Retorna o primeiro vivo como fallback caso a entrada seja inválida
    }

    /**
     * Inicia o loop principal de combate.
     * <p>
     * Este método orquestra todas as fases da batalha: 
     * inicialização do baralho, turnos do jogador (compra de cartas, uso de energia), 
     * turnos dos inimigos, sistema de eventos (Observer) e verificação de condições de vitória/derrota.
     * </p>
     * @throws Exception Caso ocorra algum erro na leitura de dados do terminal.
     */
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

        // Compra inicial
        limparTela();
        comprarCartasComEscolha(scanner, baralho);
        continuar(scanner);

        // Game Loop
        while (algumVivo(inimigos) && hero.estaVivo()) {
            limparTela();
            hero.setEscudo(0); // Zera a proteção do turno anterior
            energy = energyMax;

            // Dispara gatilhos de início de turno para o jogador
            notificar(TipoEvento.INICIO_TURNO_JOGADOR);

            boolean turnoAtivo = true;
            // Fase de Ações do Jogador
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
                            
                            // Se as ações mataram todos os inimigos, quebra o loop antecipadamente
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

            // Fim da fase do Herói (Dispara efeitos como Veneno e Regeneração)
            notificar(TipoEvento.FIM_TURNO_JOGADOR);

            // Fase dos Inimigos
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

            // Checagem de fim de batalha
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

    /**
     * Ponto de entrada do programa.
     * Instancia o motor do jogo e inicia o loop de combate.
     * @param args Argumentos de linha de comando (não utilizados).
     * @throws Exception Caso ocorra erro inesperado na execução.
     */
    public static void main(String[] args) throws Exception {
        App jogo = new App();
        jogo.iniciarCombate();
    }
}