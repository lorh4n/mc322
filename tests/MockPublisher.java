import java.util.ArrayList;
import java.util.List;

public class MockPublisher implements Publisher {
    public List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void inscrever(Subscriber s) {
        if (!subscribers.contains(s)) subscribers.add(s);
    }

    @Override
    public void desinscrever(Subscriber s) {
        subscribers.remove(s);
    }

    @Override
    public void notificar(TipoEvento evento) {
        // Copia a lista para evitar ConcurrentModificationException quando
        // um Subscriber se desinscreve durante a notificação
        for (Subscriber s : new ArrayList<>(subscribers)) {
            s.serNotificado(evento);
        }
    }
}
