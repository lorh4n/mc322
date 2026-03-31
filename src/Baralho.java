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
                System.out.println(" > Você comprou: " + carta.getNome());
            }
        }
    }

    public int usarCarta(int index, Entidade alvo, Entidade heroi, int energy, Publisher publisher) {

        if (index < 0 || index >= maoJogador.size()) {
            System.out.println("Carta inválida");
            return -1;
        }

        Carta carta = maoJogador.remove(index);

        if(carta.getCusto() > energy){
            maoJogador.add(carta);
            return -1;
        }

        carta.usar(heroi, alvo, publisher);

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
        System.out.println("║            CARTAS NA MÃO             ║");
        System.out.println("╠══════════════════════════════════════╣");

        if (maoJogador.isEmpty()) {

            System.out.println("║ Nenhuma carta disponível             ║");

        } else {

            for (int i = 0; i < maoJogador.size(); i++) {

                Carta carta = maoJogador.get(i);
                String line1 = String.format("%d : %s (Custo: %d)", i, carta.getNome(), carta.getCusto());
                String line2 = "    " + carta.getDescricao();

                System.out.printf("║ %-36s ║\n", line1);
                System.out.printf("║ %-36s ║\n", line2);
            }
        }

        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ -1 : Encerrar turno                  ║");
        System.out.println("║ -2 : Descartar uma carta             ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    public boolean descartarCarta(int index) {
        if (index < 0 || index >= maoJogador.size()) {
            return false;
        }
        Carta carta = maoJogador.remove(index);
        pilhaDescarte.add(carta);
        System.out.println(" > Você descartou: " + carta.getNome());
        return true;
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

    public Carta getCarta(int index) {
        if (index < 0 || index >= maoJogador.size()) return null;
        return maoJogador.get(index);
    }

    public void popularBaralho(int quantidade) {

        String[] nomesDano = {"Golpe", "Apunhalada", "Explosão Sangrenta", "Corte Rápido", "Fúria"};
        String[] nomesEscudo = {"Defesa", "Barreira", "Escudo de Ferro", "Esquiva", "Muralha"};
        // NOVO: Nomes para as cartas de efeito
        String[] nomesEfeitoVeneno = {"Frasco Venenoso", "Nuvem Tóxica", "Toque Letal"}; 
        String[] nomesEfeitoCura = {"Poção de Vida", "Bandagem Mágica", "Regeneração"};

        Random gerador = new Random();
        for (int i = 0; i < quantidade; i++) {
            int custo = gerador.nextInt(3) + 1;
            int valor = gerador.nextInt(6) + 2;
            
            int tipoDeCarta = gerador.nextInt(3); 

            if (tipoDeCarta == 0) {
                String nome = nomesDano[gerador.nextInt(nomesDano.length)];
                CartaDano carta = new CartaDano(nome, custo, "Causa " + valor + " de dano ao inimigo", valor);
                adicionarCarta(carta);

            } else if (tipoDeCarta == 1) {
                String nome = nomesEscudo[gerador.nextInt(nomesEscudo.length)];
                CartaEscudo carta = new CartaEscudo(nome, custo, "Ganha " + valor + " de Escudo", valor);
                adicionarCarta(carta);

            } else {
                boolean sorteiaEfeito = gerador.nextBoolean();
                
                if (sorteiaEfeito) {
                    String nome = nomesEfeitoVeneno[gerador.nextInt(nomesEfeitoVeneno.length)];
                    int acumulos = (valor / 2) + 1; 
                    CartaEfeito carta = new CartaEfeito(nome, custo, "Aplica " + acumulos + " de Veneno", acumulos, "Veneno");
                    adicionarCarta(carta);
                } else {
                    String nome = nomesEfeitoCura[gerador.nextInt(nomesEfeitoCura.length)];
                    int acumulos = (valor / 2) + 1;
                    CartaEfeito carta = new CartaEfeito(nome, custo, "Ganha " + acumulos + " de Regeneração", acumulos, "Regeneracao");
                    adicionarCarta(carta);
                }
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