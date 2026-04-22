import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartasTest {

    @Test
    public void cartaDanoAplicaDano() {
        CartaDano c = new CartaDano("Golpe", 1, "d", 4);
        Heroi h = new Heroi("H", 10);
        Inimigo alvo = new Rato(10, 1);
        c.usar(h, alvo, new MockPublisher());
        assertEquals(6, alvo.getVida());
        assertTrue(c.precisaAlvo());
    }

    @Test
    public void cartaDanoAlvoNullNaoQuebra() {
        CartaDano c = new CartaDano("Golpe", 1, "d", 4);
        Heroi h = new Heroi("H", 10);
        c.usar(h, null, new MockPublisher()); // não deve lançar
        assertEquals(10, h.getVida());
    }

    @Test
    public void cartaEscudoConcedeProtecao() {
        CartaEscudo c = new CartaEscudo("Defesa", 1, "e", 5);
        Heroi h = new Heroi("H", 10);
        c.usar(h, null, new MockPublisher());
        assertEquals(5, h.getEscudo());
        assertFalse(c.precisaAlvo());
    }

    @Test
    public void cartaEfeitoAplicaVeneno() {
        CartaEfeito c = new CartaEfeito("Frasco", 1, "v", 3, "Veneno");
        Heroi h = new Heroi("H", 10);
        Inimigo alvo = new Rato(10, 1);
        MockPublisher pub = new MockPublisher();
        c.usar(h, alvo, pub);
        assertNotNull(alvo.buscarEfeito("Veneno"));
        assertEquals(3, alvo.buscarEfeito("Veneno").getAcumulos());
    }

    @Test
    public void cartaEfeitoAplicaRegeneracaoNoUsuario() {
        CartaEfeito c = new CartaEfeito("Poção", 1, "r", 2, "Regeneracao");
        Heroi h = new Heroi("H", 10);
        MockPublisher pub = new MockPublisher();
        c.usar(h, null, pub);
        assertNotNull(h.buscarEfeito("Regeneração"));
    }

    @Test
    public void cartaAtaqueVenenosoCausaDanoEAplicaVeneno() {
        CartaAtaqueVenenoso c = new CartaAtaqueVenenoso("Lâmina", 2, "d", 3, 2);
        Heroi h = new Heroi("H", 10);
        Inimigo alvo = new Rato(10, 1);
        c.usar(h, alvo, new MockPublisher());
        assertEquals(7, alvo.getVida());
        assertEquals(2, alvo.buscarEfeito("Veneno").getAcumulos());
        assertTrue(c.precisaAlvo());
    }

    @Test
    public void cartaDrenoCausaDanoECuraUsuario() {
        CartaDreno c = new CartaDreno("Sifão", 1, "d", 4, 2);
        Heroi h = new Heroi("H", 10);
        h.receberDano(5); // vida = 5
        Inimigo alvo = new Rato(10, 1);
        c.usar(h, alvo, new MockPublisher());
        assertEquals(6, alvo.getVida());
        assertEquals(7, h.getVida());
        assertTrue(c.precisaAlvo());
    }

    @Test
    public void cartaGolpeDuploAtaca2x() {
        CartaGolpeDuplo c = new CartaGolpeDuplo("Duplo", 1, "d", 3);
        Heroi h = new Heroi("H", 10);
        Inimigo alvo = new Rato(20, 1);
        c.usar(h, alvo, new MockPublisher());
        assertEquals(14, alvo.getVida());
    }

    @Test
    public void cartaEscudoRegeneroConcedeEscudoEEfeito() {
        CartaEscudoRegenero c = new CartaEscudoRegenero("Bastião", 2, "d", 4, 2);
        Heroi h = new Heroi("H", 10);
        c.usar(h, null, new MockPublisher());
        assertEquals(4, h.getEscudo());
        assertNotNull(h.buscarEfeito("Regeneração"));
    }

    @Test
    public void cartaExtirparConverteVenenoEmDano() {
        CartaExtirpar c = new CartaExtirpar("Extirpar", 1, "d");
        Heroi h = new Heroi("H", 10);
        Inimigo alvo = new Rato(20, 1);
        MockPublisher pub = new MockPublisher();
        alvo.aplicarEfeito(new Veneno(alvo, 5, pub), pub);
        c.usar(h, alvo, pub);
        assertEquals(15, alvo.getVida(), "5 de dano por stacks de veneno");
        assertNull(alvo.buscarEfeito("Veneno"));
    }

    @Test
    public void cartaGettersBasicos() {
        CartaDano c = new CartaDano("Golpe", 2, "causa dano", 4);
        assertEquals("Golpe", c.getNome());
        assertEquals(2, c.getCusto());
        assertEquals("causa dano", c.getDescricao());
    }
}
