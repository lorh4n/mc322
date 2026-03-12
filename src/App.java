import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // Instaciar Personagens

        int heroVida = 10;
        int enemyVida = 10;

        Heroi hero = new Heroi("Herói", heroVida);
        Inimigo enemy = new Inimigo("Rato", enemyVida, 2);

        CartaDano damageCard = new CartaDano("Carta1", 2, 2);
        CartaEscudo shieldCard = new CartaEscudo("Carta2", 2, 8);

        // Quantidade de Energia por turno
        int energyMax = 3;
        int energy = energyMax;

        // Game Loop
        while (enemy.estaVivo() && hero.estaVivo()) {
            hero.limparEscudo(); // Escudo descartado anterior
            energy = energyMax; // Reseta energia 

            boolean turnoAtivo = true;
            while (turnoAtivo && enemy.estaVivo()) {
                String statusHeroi = String.format("%s: %d/%d HP | Escudo: %d", hero.getNome(), hero.getVida(),
                        heroVida, hero.getEscudo());
                String statusInimigo = String.format("%s: %d/%d HP", enemy.getNome(), enemy.getVida(), enemyVida);
                String energiaTxt = String.format("Energia: %d/%d", energy, energyMax);

                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.printf("║ %-36s ║%n", statusHeroi);
                System.out.printf("║ VS %-33s ║%n", statusInimigo);
                System.out.println("╠══════════════════════════════════════╣");
                System.out.printf("║ %-36s ║%n", energiaTxt);
                System.out.println("╠══════════════════════════════════════╣");
                System.out.println("║ 1 - Usar Carta de Dano               ║");
                System.out.println("║ 2 - Usar Carta de Escudo             ║");
                System.out.println("║ 3 - Encerrar turno                   ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Escolha: ");

                if (scanner.hasNextInt()) {
                    int escolha = scanner.nextInt();


                    System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

                    if (escolha == 1) {
                        if (energy >= 2) {
                            damageCard.usar(enemy);
                            energy -= 2;
                            System.out.println("Voce usou Carta de Dano!");
                        } else {
                            System.out.println("Energia insuficiente!");
                        }
                    } else if (escolha == 2) {
                        if (energy >= 2) {
                            shieldCard.usar(hero);
                            energy -= 2;
                            System.out.println("Voce usou Carta de Escudo!");
                        } else {
                            System.out.println("Energia insuficiente!");
                        }
                    } else if (escolha == 3) {
                        turnoAtivo = false;
                        System.out.println("Voce encerrou seu turno.");
                    } else {
                        System.out.println("Opcao invalida!");
                    }
                } else {
                    scanner.next(); // Limpa o buffer
                }
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

            }

            if (enemy.estaVivo() && hero.estaVivo()) {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║           TURNO DO INIMIGO           ║");
                System.out.println("╠══════════════════════════════════════╣");
                enemy.atacar(hero);
                String acao = " > " + enemy.getNome() + " atacou e causou dano!";
                System.out.printf("║ %-36s ║%n", acao);
                System.out.println("╚══════════════════════════════════════╝");
            }

            if (!enemy.estaVivo()) {
                System.out.println("\n🏆 [ VITÓRIA ] VOCE VENCEU!");
            } else if (!hero.estaVivo()) {
                System.out.println("\n💀 [ DERROTA ] VOCE PERDEU!");
            }
        }
        scanner.close();
    }
}
