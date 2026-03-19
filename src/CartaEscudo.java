public class CartaEscudo extends Carta {

   // Atributos
   private int protecao;

   // Metodos

   public CartaEscudo(String nome, int custo, String descricao, int protecao) {
      super(nome, custo, descricao);
      this.protecao = protecao;
   }

   public void usar(Entidade usuario, Entidade alvo) {
      if (usuario != null) {
         usuario.ganharEscudo(protecao);
      }
   }
}
