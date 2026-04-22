import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EfeitosTest {

    @Test
    public void venenoCausaDanoNoFimTurno() {
        Heroi h = new Heroi("H", 10);
        MockPublisher pub = new MockPublisher();
        Veneno v = new Veneno(h, 3, pub);
        h.aplicarEfeito(v, pub);

        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        assertEquals(7, h.getVida());
        assertEquals(2, v.getAcumulos());
    }

    @Test
    public void venenoExpiraEDesinscreve() {
        Heroi h = new Heroi("H", 100);
        MockPublisher pub = new MockPublisher();
        Veneno v = new Veneno(h, 1, pub);
        h.aplicarEfeito(v, pub);

        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        assertEquals(0, v.getAcumulos());
        assertNull(h.buscarEfeito("Veneno"));
        assertFalse(pub.inscritos.contains(v));
    }

    @Test
    public void venenoIgnoraOutrosEventos() {
        Heroi h = new Heroi("H", 10);
        MockPublisher pub = new MockPublisher();
        h.aplicarEfeito(new Veneno(h, 3, pub), pub);
        pub.notificar(TipoEvento.INICIO_TURNO_JOGADOR);
        pub.notificar(TipoEvento.FIM_TURNO_INIMIGO);
        assertEquals(10, h.getVida());
    }

    @Test
    public void regeneracaoCuraSomenteHeroiNoFimTurno() {
        Heroi h = new Heroi("H", 10);
        h.receberDano(5);
        MockPublisher pub = new MockPublisher();
        h.aplicarEfeito(new Regeneracao(h, 2, pub), pub);
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        assertEquals(7, h.getVida());
    }

    @Test
    public void regeneracaoNaoCuraInimigo() {
        Inimigo ini = new Rato(10, 1);
        ini.receberDano(5);
        MockPublisher pub = new MockPublisher();
        Regeneracao r = new Regeneracao(ini, 3, pub);
        ini.aplicarEfeito(r, pub);
        pub.notificar(TipoEvento.FIM_TURNO_JOGADOR);
        assertEquals(5, ini.getVida(), "Regeneração só deve curar Heróis");
    }

    @Test
    public void efeitoGetStringFormatado() {
        Heroi h = new Heroi("H", 10);
        MockPublisher pub = new MockPublisher();
        Veneno v = new Veneno(h, 4, pub);
        assertEquals("Veneno (4)", v.getString());
    }
}
