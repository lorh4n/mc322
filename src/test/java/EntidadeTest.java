import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EntidadeTest {

    @Test
    public void danoAbsorvidoPorEscudo() {
        Heroi h = new Heroi("H", 10);
        h.ganharEscudo(5);
        h.receberDano(3);
        assertEquals(10, h.getVida());
        assertEquals(2, h.getEscudo());
    }

    @Test
    public void danoParcialComEscudo() {
        Heroi h = new Heroi("H", 10);
        h.ganharEscudo(3);
        h.receberDano(5);
        assertEquals(8, h.getVida());
        assertEquals(0, h.getEscudo());
    }

    @Test
    public void danoSemEscudo() {
        Heroi h = new Heroi("H", 10);
        h.receberDano(4);
        assertEquals(6, h.getVida());
    }

    @Test
    public void vidaNaoFicaNegativa() {
        Heroi h = new Heroi("H", 3);
        h.receberDano(10);
        assertEquals(0, h.getVida());
        assertFalse(h.estaVivo());
    }

    @Test
    public void perderVidaIgnoraEscudo() {
        Heroi h = new Heroi("H", 10);
        h.ganharEscudo(5);
        h.perderVida(3);
        assertEquals(7, h.getVida());
        assertEquals(5, h.getEscudo());
    }

    @Test
    public void curarNaoUltrapassaVidaMaxima() {
        Heroi h = new Heroi("H", 10);
        h.receberDano(5);
        h.curar(20);
        assertEquals(10, h.getVida());
    }

    @Test
    public void resetarEstadoBatalhaLimpaEscudoEEfeitos() {
        Heroi h = new Heroi("H", 10);
        h.ganharEscudo(5);
        MockPublisher pub = new MockPublisher();
        h.aplicarEfeito(new Veneno(h, 3, pub), pub);
        assertEquals(1, h.getEfeitos().size());

        h.resetarEstadoBatalha();
        assertEquals(0, h.getEscudo());
        assertTrue(h.getEfeitos().isEmpty());
    }

    @Test
    public void aplicarEfeitoIgualAcumulaStacks() {
        Heroi h = new Heroi("H", 10);
        MockPublisher pub = new MockPublisher();
        h.aplicarEfeito(new Veneno(h, 2, pub), pub);
        h.aplicarEfeito(new Veneno(h, 3, pub), pub);
        assertEquals(1, h.getEfeitos().size());
        assertEquals(5, h.buscarEfeito("Veneno").getAcumulos());
    }

    @Test
    public void buscarEfeitoInexistenteRetornaNull() {
        Heroi h = new Heroi("H", 10);
        assertNull(h.buscarEfeito("Veneno"));
    }
}
