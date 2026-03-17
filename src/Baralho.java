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

    public int usarCarta(int index, Entidade alvo, Entidade heroi, int energy) {

        if (index < 0 || index >= maoJogador.size()) {
            System.out.println("Carta inválida");
            return -1;
        }

        Carta carta = maoJogador.remove(index);

        if(carta.getCusto() > energy){
            maoJogador.add(carta);
            return -1;
        }

        if(carta instanceof CartaDano) 
            carta.usar(alvo);
        else
            carta.usar(heroi);

        pilhaDescarte.add(carta);

        return carta.getCusto();
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

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           CARTAS NA MÃO             ║");
        System.out.println("╠══════════════════════════════════════╣");

        if (maoJogador.isEmpty()) {

            System.out.println("║ Nenhuma carta disponível            ║");

        } else {

            for (int i = 0; i < maoJogador.size(); i++) {

                String nome = maoJogador.get(i).getNome();
                String descricao = maoJogador.get(i).getDescricao();

                System.out.printf("║ %d : %s - %-30s ║\n", i, nome, descricao);
            }
        }

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ -1 - Encerrar turno                 ║");
        System.out.println("╚══════════════════════════════════════╝");
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