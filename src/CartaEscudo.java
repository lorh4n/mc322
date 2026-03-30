public class CartaEscudo extends Carta {
   private int protecao;

   public CartaEscudo(String nome, int custo, String descricao, int protecao) {
      super(nome, custo, descricao);
      this.protecao = protecao;
   }

   @Override
   public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
      if (usuario != null) {
         usuario.ganharEscudo(protecao);
      }
   }
}