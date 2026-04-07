public interface Publisher {
    void inscrever(Subscriber s);
    void desinscrever(Subscriber s);
    void notificar(TipoEvento evento);
}