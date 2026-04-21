import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RatoTest {

    @Test
    public void ratoAlternaEntreAtaqueEVeneno() {
        Rato r = new Rato(10, 3);
        Heroi h = new Heroi("H", 20);
        MockPublisher pub = new MockPublisher();

        // turno 1: ataque
        assertTrue(r.anunciarIntencao().contains("dano"));
        r.executarAcao(h, pub);
        assertEquals(17, h.getVida());

        // turno 2: veneno
        assertTrue(r.anunciarIntencao().contains("Veneno"));
        r.executarAcao(h, pub);
        assertNotNull(h.buscarEfeito("Veneno"));

        // volta a atacar
        assertTrue(r.anunciarIntencao().contains("dano"));
    }

    @Test
    public void ratoAtacarAplicaDanoNoHeroi() {
        Rato r = new Rato(10, 4);
        Heroi h = new Heroi("H", 20);
        r.atacar(h);
        assertEquals(16, h.getVida());
    }
}
