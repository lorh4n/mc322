import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class MapaTest {

    @Test
    public void raizSemEventoTemFilhos() {
        Heroi h = new Heroi("H", 10);
        Baralho b = new Baralho();
        Mapa m = Mapa.gerarPadrao(h, b);
        NoMapa raiz = m.getRaiz();
        assertNull(raiz.getEvento());
        assertFalse(raiz.getFilhos().isEmpty());
        assertEquals(0, raiz.getProfundidade());
    }

    @Test
    public void noFilhoDeveTerProfundidadeMaior() {
        NoMapa pai = new NoMapa("pai", 1, null);
        NoMapa filhoInvalido = new NoMapa("filho", 1, null);
        assertThrows(IllegalArgumentException.class, () -> pai.adicionarFilho(filhoInvalido));

        NoMapa filhoValido = new NoMapa("filho2", 2, null);
        pai.adicionarFilho(filhoValido);
        assertEquals(1, pai.getFilhos().size());
    }

    @Test
    public void marcarVisitadoAlteraEstado() {
        NoMapa n = new NoMapa("n", 1, null);
        assertFalse(n.isVisitado());
        n.marcarVisitado();
        assertTrue(n.isVisitado());
    }

    @Test
    public void noPodeConterBatalha() {
        Heroi h = new Heroi("H", 10);
        Baralho b = new Baralho();
        Batalha bat = new Batalha(h, b, () -> {
            List<Inimigo> l = new ArrayList<>();
            l.add(new Rato(5, 1));
            return l;
        });
        NoMapa n = new NoMapa("n", 1, bat);
        assertNotNull(n.getEvento());
        assertTrue(n.getEvento() instanceof Batalha);
    }

    @Test
    public void mapaPadraoContemEventosDeProgressao() {
        Heroi h = new Heroi("H", 10);
        Baralho b = new Baralho();
        Mapa m = Mapa.gerarPadrao(h, b);
        assertTrue(contemEvento(m.getRaiz(), Loja.class));
        assertTrue(contemEvento(m.getRaiz(), Escolha.class));
    }

    @Test
    public void profundidadeSempreAumentaNoMapaPadrao() {
        Heroi h = new Heroi("H", 10);
        Baralho b = new Baralho();
        Mapa m = Mapa.gerarPadrao(h, b);
        assertTrue(profundidadeCresce(m.getRaiz()));
    }

    private boolean profundidadeCresce(NoMapa n) {
        for (NoMapa f : n.getFilhos()) {
            if (f.getProfundidade() <= n.getProfundidade()) return false;
            if (!profundidadeCresce(f)) return false;
        }
        return true;
    }

    private boolean contemEvento(NoMapa n, Class<?> tipo) {
        if (n.getEvento() != null && tipo.isInstance(n.getEvento())) return true;
        for (NoMapa f : n.getFilhos()) {
            if (contemEvento(f, tipo)) return true;
        }
        return false;
    }
}
