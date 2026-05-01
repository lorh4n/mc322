import java.util.Scanner;

/**
 * Evento narrativo simples com consequências claras para o jogador.
 */
public class Escolha extends Evento {

    private final Baralho baralho;

    public Escolha(Baralho baralho) {
        this.baralho = baralho;
    }

    @Override
    public boolean iniciar(Heroi heroi, Scanner scanner) {
        System.out.println("\n=== ALTAR ESQUECIDO ===");
        System.out.println("Você encontra um altar coberto por marcas antigas.");
        System.out.println("[0] Oferecer 6 HP e receber uma carta rara");
        System.out.println("[1] Saquear o altar e ganhar 25 ouro");
        System.out.println("[2] Descansar perto do altar e recuperar 8 HP");
        System.out.print("Escolha: ");

        int escolha = 2;
        if (scanner != null) {
            try {
                escolha = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                escolha = 2;
            }
        }

        if (escolha == 0) {
            heroi.perderVida(6);
            if (heroi.estaVivo()) {
                Carta carta = CartaFactory.criarAleatoria();
                baralho.adicionarCarta(carta);
                System.out.println("Você recebeu a carta: " + carta.getNome());
            }
        } else if (escolha == 1) {
            heroi.adicionarOuro(25);
            System.out.println("Você encontrou 25 ouro.");
        } else {
            heroi.curar(8);
        }

        return heroi.estaVivo();
    }
}
