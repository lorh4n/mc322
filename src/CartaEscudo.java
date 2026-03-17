public class CartaEscudo extends Carta {

   // Atributos
   private int protecao;

   // Metodos

   public CartaEscudo(String nome, int custo, String descricao, int protecao) {
      super(nome, custo, descricao);
      this.protecao = protecao;
   }

   public void usar(Entidade alvo) {
      if (alvo instanceof Heroi) {
         ((Heroi) alvo).ganharEscudo(protecao);
      }
   }
}
