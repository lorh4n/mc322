import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BaralhoTest {

    @Test
    public void adicionarCartaEComprar() {
        Baralho b = new Baralho();
        b.adicionarCarta(new CartaDano("Golpe", 1, "dano", 3));
        b.adicionarCarta(new CartaEscudo("Defesa", 1, "escudo", 3));
        assertEquals(2, b.tamanhoCompra());
        b.comprarCartas(2);
        assertEquals(2, b.tamanhoMao());
        assertEquals(0, b.tamanhoCompra());
    }

    @Test
    public void usarCartaSemEnergiaDevolveParaMao() {
        Baralho b = new Baralho();
        b.adicionarCarta(new CartaDano("Golpe", 3, "dano", 3));
        b.comprarCartas(1);
        Heroi h = new Heroi("H", 10);
        Inimigo alvo = new Rato(5, 1);
        int custo = b.usarCarta(0, alvo, h, 1, new MockPublisher());
        assertEquals(-1, custo);
        assertEquals(1, b.tamanhoMao(), "carta deve voltar para a mão");
    }

    @Test
    public void usarCartaComEnergiaSuficienteVaiParaDescarte() {
        Baralho b = new Baralho();
        b.adicionarCarta(new CartaDano("Golpe", 1, "dano", 3));
        b.comprarCartas(1);
        Heroi h = new Heroi("H", 10);
        Inimigo alvo = new Rato(5, 1);
        int custo = b.usarCarta(0, alvo, h, 3, new MockPublisher());
        assertEquals(1, custo);
        assertEquals(0, b.tamanhoMao());
        assertEquals(1, b.tamanhoDescarte());
        assertEquals(2, alvo.getVida());
    }

    @Test
    public void descartarCartaIndiceInvalido() {
        Baralho b = new Baralho();
        assertFalse(b.descartarCarta(0));
        assertFalse(b.descartarCarta(-1));
    }

    @Test
    public void descartarMaoMoveTudoParaDescarte() {
        Baralho b = new Baralho();
        b.adicionarCarta(new CartaDano("A", 1, "", 1));
        b.adicionarCarta(new CartaDano("B", 1, "", 1));
        b.comprarCartas(2);
        b.descartarMao();
        assertEquals(0, b.tamanhoMao());
        assertEquals(2, b.tamanhoDescarte());
    }

    @Test
    public void comprarRecicalaDescarteQuandoCompraVazia() {
        Baralho b = new Baralho();
        b.adicionarCarta(new CartaDano("A", 1, "", 1));
        b.comprarCartas(1);
        b.descartarMao();
        assertEquals(0, b.tamanhoCompra());
        assertEquals(1, b.tamanhoDescarte());
        b.comprarCartas(1);
        assertEquals(1, b.tamanhoMao());
    }

    @Test
    public void resetarParaNovaBatalhaJuntaTudoNaCompra() {
        Baralho b = new Baralho();
        b.adicionarCarta(new CartaDano("A", 1, "", 1));
        b.adicionarCarta(new CartaDano("B", 1, "", 1));
        b.adicionarCarta(new CartaDano("C", 1, "", 1));
        b.comprarCartas(2);
        // descarta uma
        b.descartarCarta(0);
        assertEquals(1, b.tamanhoMao());
        assertEquals(1, b.tamanhoDescarte());
        assertEquals(1, b.tamanhoCompra());

        b.resetarParaNovaBatalha();
        assertEquals(0, b.tamanhoMao());
        assertEquals(0, b.tamanhoDescarte());
        assertEquals(3, b.tamanhoCompra());
    }

    @Test
    public void getCartaIndiceInvalidoRetornaNull() {
        Baralho b = new Baralho();
        assertNull(b.getCarta(0));
        assertNull(b.getCarta(-1));
    }

    @Test
    public void popularBaralhoGeraQuantidadeCorreta() {
        Baralho b = new Baralho();
        b.popularBaralho(10);
        assertEquals(10, b.tamanhoCompra());
    }
}
