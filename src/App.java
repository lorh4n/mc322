import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // Instaciar Personagens

        int heroVida = 10;
        int enemyVida = 10;
        
        Heroi hero = new Heroi("Julio", heroVida);
        Inimigo enemy = new Inimigo("Gabriel", enemyVida, 2);

        CartaDano damageCard = new CartaDano("Carta1", 2, 2);
        CartaEscudo shieldCard = new CartaEscudo("Carta2", 2, 8);


        // Quantidade de Energia por turno
        int energyMax = 3;
        int energy = energyMax;

        // Game Loop
        while (enemy.estaVivo() && hero.estaVivo()) {
            hero.limparEscudo(); // Escudo descartado anterior
            energy = energyMax;  // Reseta energia n
            
            boolean turnoAtivo = true;
            while (turnoAtivo && enemy.estaVivo()) {
                System.out.println("\n=== SEU TURNO ===");
                System.out.printf("%s: %d HP | %d Escudo%n", hero.getNome(), hero.getVida(), hero.getEscudo());
                System.out.printf("%s: %d HP%n", enemy.getNome(), enemy.getVida());
                System.out.printf("Energia: %d/%d%n", energy, energyMax);
                
                System.out.println("1 - Usar Carta de Dano (Custo 2)");
                System.out.println("2 - Usar Carta de Escudo (Custo 2)");
                System.out.println("3 - Encerrar turno");
                System.out.print("Escolha: ");
            
                if (scanner.hasNextInt()) {
                    int escolha = scanner.nextInt(); 

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
            }

            if (enemy.estaVivo() && hero.estaVivo()) {
                System.out.println("\n╔═══\t TURNO DO INIMIGO \t \t═══╗");
                enemy.atacar(hero);
                System.out.printf("║ \t%s atacou e causou dano! \t║", enemy.getNome());
                System.out.println("\n╚════════════════════════════════\t═══╝");

            }

            if (!enemy.estaVivo()) {
                System.out.println("\nVOCE VENCEU!");
            } else if (!hero.estaVivo()) {
                System.out.println("\nVOCE PERDEU!");
            }
        }
        scanner.close();
    }
}

