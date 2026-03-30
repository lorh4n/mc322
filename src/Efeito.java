public abstract class Efeito implements Subscriber {
    protected String nome;
    protected Entidade dono;
    protected int acumulos; 

    public Efeito(String nome, Entidade dono, int acumulos) {
        this.nome = nome;
        this.dono = dono;
        this.acumulos = acumulos;
    }

    public String getNome() {
        return nome;
    }

    public int getAcumulos() {
        return acumulos;
    }

    public void adicionarAcumulos(int valor) {
        this.acumulos += valor;
    }

    public String getString() {
        return this.nome + " (" + this.acumulos + ")";
    }

    @Override
    public abstract void serNotificado(TipoEvento evento);
}