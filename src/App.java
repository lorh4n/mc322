import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // Instaciar Personagens

        int heroLife = 15;
        int enemyLife = 10;

        Heroi hero = new Heroi("Herói", heroLife);
        Inimigo enemy = new Inimigo("Rato", enemyLife, 4);

        CartaDano damageCard = new CartaDano("Carta Dano 1", 2, "Alvo recebe Dano", 2);
        CartaEscudo shieldCard = new CartaEscudo("Carta Proteção 1", 2, "Voce recebe Escudo", 2);

        // Quantidade de Energia por turno
        int energyMax = 3;
        int energy = energyMax;

        // Game Loop
        while (enemy.estaVivo() && hero.estaVivo()) {
            hero.setEscudo(0); // Escudo descartado anterior
            energy = energyMax; // Reseta energia

            boolean turnoAtivo = true;
            while (turnoAtivo && enemy.estaVivo()) {
                String statusHeroi = String.format("%s: %d/%d HP | Escudo: %d", hero.getNome(), hero.getVida(),
                        heroLife, hero.getEscudo());
                String statusInimigo = String.format("%s: %d/%d HP", enemy.getNome(), enemy.getVida(), enemyLife);
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
                        if (energy >= damageCard.getCusto()) {
                            damageCard.usar(enemy);
                            energy -= damageCard.getCusto();
                            System.out.println("Voce usou Carta de Dano!");
                        } else {
                            System.out.println("Energia insuficiente!");
                        }
                    } else if (escolha == 2) {
                        if (energy >= shieldCard.getCusto()) {
                            shieldCard.usar(hero);
                            energy -= shieldCard.getCusto();
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
