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
            subscribers.add(s); // [cite: 89]
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
    // ----------------------------

    public void iniciarCombate() throws Exception {
        Scanner scanner = new Scanner(System.in);

        int heroLife = 15;
        int enemyLife = 10;

        Heroi hero = new Heroi("Herói", heroLife);
        Inimigo enemy = new Rato(enemyLife, 4);

        Baralho baralho = new Baralho();
        baralho.popularBaralho(10);
        baralho.embaralhar();

        int energyMax = 3;
        int energy = energyMax;

        limparTela();
        System.out.println("⚔️  O COMBATE COMEÇOU! ⚔️");
        continuar(scanner);

        limparTela();
        baralho.comprarCartas(1);
        continuar(scanner);

        // Game Loop
        while (enemy.estaVivo() && hero.estaVivo()) {
            limparTela();
            hero.setEscudo(0); 
            energy = energyMax; 

            notificar(TipoEvento.INICIO_TURNO_JOGADOR);

            boolean turnoAtivo = true;
            while (turnoAtivo && enemy.estaVivo() && hero.estaVivo()) {
                String statusHeroi = String.format("%s: %d/%d HP | Escudo: %d", hero.getNome(), hero.getVida(), heroLife, hero.getEscudo());
                String statusInimigo = String.format(Rato.CorOutput + "%s: %d/%d HP" + Rato.Reset, enemy.getNome(), enemy.getVida(), enemyLife);
                String intencaoInimigo = String.format(Rato.CorOutput + "Intenção: %s" + Rato.Reset, enemy.anunciarIntencao());
                String energiaTxt = String.format("Energia: %d/%d", energy, energyMax);

                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.printf("║ %-36s ║%n", statusHeroi);
                System.out.printf("║ VS %-33s ║%n", statusInimigo);
                System.out.printf("║ %-36s ║%n", intencaoInimigo);
                System.out.println("╠══════════════════════════════════════╣");
                System.out.printf("║ %-36s ║%n", energiaTxt);

                baralho.mostrarMao();
                System.out.print("Escolha: ");

                String entrada = scanner.nextLine();
                if (entrada.isEmpty()) continue;

                try {
                    int escolha = Integer.parseInt(entrada);
                    System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

                    if(escolha == -1){
                        turnoAtivo = false;
                        System.out.println("Você encerrou seu turno.");
                        continuar(scanner);
                    } else if(escolha >= baralho.tamanhoMao() || escolha < -1) {
                        System.out.println("Opção inválida!");
                        continuar(scanner);
                    } else {
                        int retorno = baralho.usarCarta(escolha, enemy, hero, energy, this);
                        if(retorno == -1) {
                            System.out.println("Energia insuficiente!");
                            continuar(scanner);
                        } else { 
                            System.out.printf("Você usou a carta %d!\n", escolha); 
                            energy -= retorno;
                            if (!enemy.estaVivo() || !hero.estaVivo()) {
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
                
                if (turnoAtivo && enemy.estaVivo() && hero.estaVivo()) {
                    limparTela();
                }
            }
       
            notificar(TipoEvento.FIM_TURNO_JOGADOR);

            // Turno do Inimigo
            if (enemy.estaVivo() && hero.estaVivo()) {
                limparTela();
                
                notificar(TipoEvento.INICIO_TURNO_INIMIGO);

                System.out.println("\n" + Rato.CorOutput + "╔══════════════════════════════════════╗");
                System.out.println("║           TURNO DO INIMIGO           ║");
                System.out.println("╠══════════════════════════════════════╣" + Rato.Reset);
                
                enemy.atacar(hero);
                String acao = " > " + enemy.getNome() + " atacou e causou dano!";
                System.out.printf("║ %-36s ║%n", acao);
                System.out.println(Rato.CorOutput + "╚══════════════════════════════════════╝" + Rato.Reset);
                continuar(scanner);

                notificar(TipoEvento.FIM_TURNO_INIMIGO);
            }

            if (!enemy.estaVivo()) {
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
                baralho.comprarCartas(1);
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