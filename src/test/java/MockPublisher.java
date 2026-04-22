import java.util.ArrayList;
import java.util.List;

/** Publisher simples para uso nos testes, sem saída no terminal. */
public class MockPublisher implements Publisher {
    public final List<Subscriber> inscritos = new ArrayList<>();
    public final List<TipoEvento> eventos = new ArrayList<>();

    @Override
    public void inscrever(Subscriber s) {
        if (!inscritos.contains(s)) inscritos.add(s);
    }

    @Override
    public void desinscrever(Subscriber s) {
        inscritos.remove(s);
    }

    @Override
    public void notificar(TipoEvento evento) {
        eventos.add(evento);
        List<Subscriber> copia = new ArrayList<>(inscritos);
        for (Subscriber s : copia) s.serNotificado(evento);
    }
}
