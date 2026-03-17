import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Baralho {

    private ArrayList<Carta> pilhaCompra;
    private ArrayList<Carta> maoJogador;
    private ArrayList<Carta> pilhaDescarte;

    public Baralho() {

        pilhaCompra = new ArrayList<>();
        maoJogador = new ArrayList<>();
        pilhaDescarte = new ArrayList<>();
    }

    public void adicionarCarta(Carta carta) {
        pilhaCompra.add(carta);
    }

    public void embaralhar() {
        Collections.shuffle(pilhaCompra);
    }

    public void comprarCartas(int quantidade) {

        for (int i = 0; i < quantidade; i++) {

            if (pilhaCompra.isEmpty()) {
                reciclarDescarte();
            }

            if (!pilhaCompra.isEmpty()) {

                Carta carta = pilhaCompra.remove(0);

                maoJogador.add(carta);
            }
        }
    }

    public void usarCarta(int index, Entidade alvo) {

        if (index < 0 || index >= maoJogador.size()) {
            System.out.println("Carta inválida");
            return;
        }

        Carta carta = maoJogador.remove(index);

        carta.usar(alvo);

        pilhaDescarte.add(carta);
    }

    private void reciclarDescarte() {

        if (pilhaDescarte.isEmpty()) {
            return;
        }

        pilhaCompra.addAll(pilhaDescarte);

        pilhaDescarte.clear();

        Collections.shuffle(pilhaCompra);

        System.out.println("Pilha de compra reciclada!");
    }

    public void mostrarMao() {

        if (maoJogador.isEmpty()) {
            System.out.println("Mão vazia");
            return;
        }

        for (int i = 0; i < maoJogador.size(); i++) {

            System.out.println(
                    i + " - " +
                            maoJogador.get(i).getNome());
        }
    }

    public void descartarMao() {

        pilhaDescarte.addAll(maoJogador);

        maoJogador.clear();
    }

    public int tamanhoCompra() {
        return pilhaCompra.size();
    }

    public int tamanhoDescarte() {
        return pilhaDescarte.size();
    }

    public int tamanhoMao() {
        return maoJogador.size();
    }

    public void popularBaralho(int quantidade) {

        Random gerador = new Random();
        for (int i = 0; i < quantidade; i++) {
            int custo = gerador.nextInt(4);
            int valor = gerador.nextInt(7);
            boolean tipo = gerador.nextBoolean();

            if (tipo) {
                CartaEscudo carta = new CartaEscudo("Carta " + i, custo, null, valor);
                adicionarCarta(carta);
            } else {
                CartaDano carta = new CartaDano("Carta " + i, custo, null, valor);
                adicionarCarta(carta);
            }

        }

    }

    public void mostrarDescarte() {
        System.out.println("\n╔════════════════════════════╗");
        System.out.println("║     PILHA DE DESCARTE      ║");
        System.out.println("╠════════════════════════════╣");

        if (pilhaDescarte.isEmpty()) {
            System.out.println("║ (vazia)                    ║");
        } else {
            int limite = Math.min(5, pilhaDescarte.size());

            for (int i = pilhaDescarte.size() - limite; i < pilhaDescarte.size(); i++) {
                String nome = pilhaDescarte.get(i).getNome();
                System.out.printf("║ %-26s ║%n", nome);
            }

            if (pilhaDescarte.size() > 5) {
                System.out.printf("║ ... (%d cartas no total)   ║%n", pilhaDescarte.size());
            }
        }

        System.out.println("╚════════════════════════════╝");
    }
}