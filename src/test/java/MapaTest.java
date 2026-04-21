import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class MapaTest {

    @Test
    public void raizSemBatalhaTemFilhos() {
        Mapa m = Mapa.gerarPadrao();
        NoMapa raiz = m.getRaiz();
        assertFalse(raiz.temBatalha());
        assertFalse(raiz.getFilhos().isEmpty());
        assertEquals(0, raiz.getProfundidade());
    }

    @Test
    public void noFilhoDeveTerProfundidadeMaior() {
        NoMapa pai = new NoMapa("pai", 1, null, false);
        NoMapa filhoInvalido = new NoMapa("filho", 1, null, false);
        assertThrows(IllegalArgumentException.class, () -> pai.adicionarFilho(filhoInvalido));

        NoMapa filhoValido = new NoMapa("filho2", 2, null, false);
        pai.adicionarFilho(filhoValido);
        assertEquals(1, pai.getFilhos().size());
    }

    @Test
    public void marcarVisitadoAlteraEstado() {
        NoMapa n = new NoMapa("n", 1, null, false);
        assertFalse(n.isVisitado());
        n.marcarVisitado();
        assertTrue(n.isVisitado());
    }

    @Test
    public void criarInimigosUsaFabrica() {
        NoMapa n = new NoMapa("n", 1, () -> {
            List<Inimigo> l = new ArrayList<>();
            l.add(new Rato(5, 1));
            l.add(new Rato(4, 1));
            return l;
        }, false);
        assertTrue(n.temBatalha());
        List<Inimigo> a = n.criarInimigos();
        List<Inimigo> b = n.criarInimigos();
        assertEquals(2, a.size());
        assertNotSame(a.get(0), b.get(0), "Fábrica deve gerar instâncias novas");
    }

    @Test
    public void mapaPadraoAlcancaNoFinal() {
        Mapa m = Mapa.gerarPadrao();
        boolean achouFinal = buscarFinal(m.getRaiz());
        assertTrue(achouFinal, "Mapa padrão deve conter pelo menos um nó final");
    }

    private boolean buscarFinal(NoMapa n) {
        if (n.isEhFinal()) return true;
        for (NoMapa f : n.getFilhos()) if (buscarFinal(f)) return true;
        return false;
    }

    @Test
    public void profundidadeSempreAumentaNoMapaPadrao() {
        Mapa m = Mapa.gerarPadrao();
        assertTrue(profundidadeCresce(m.getRaiz()));
    }

    private boolean profundidadeCresce(NoMapa n) {
        for (NoMapa f : n.getFilhos()) {
            if (f.getProfundidade() <= n.getProfundidade()) return false;
            if (!profundidadeCresce(f)) return false;
        }
        return true;
    }
}
