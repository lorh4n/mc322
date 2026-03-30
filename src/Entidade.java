import java.util.ArrayList;
import java.util.List;

public abstract class Entidade {

   private String nome;
   private int vida;
   private int escudo;
   private int vida_inicial;
   
   protected List<Efeito> efeitos = new ArrayList<>();

   Entidade(String nome, int vida) {
      this.nome = nome;
      this.vida = vida;
      this.vida_inicial = vida;
      this.escudo = 0;
   }

   public void aplicarEfeito(Efeito novoEfeito, Publisher publisher) {
      boolean jaTemEfeito = false;

      for (Efeito e : efeitos) {
         if (e.getNome().equals(novoEfeito.getNome())) {
            e.adicionarAcumulos(novoEfeito.getAcumulos());
            System.out.println("> " + this.getNome() + " recebeu mais " + novoEfeito.getAcumulos() + " acúmulos de " + e.getNome() + "!");
            jaTemEfeito = true;
            break;
         }
      }

      if (!jaTemEfeito) {
         efeitos.add(novoEfeito);
         publisher.inscrever(novoEfeito); // Inscreve no ciclo do jogo
         System.out.println("> " + this.getNome() + " foi afligido por " + novoEfeito.getString() + "!");
      }
   }

   // NOVO: Remove o efeito quando ele acaba (ex: veneno chega a 0)
   public void removerEfeito(Efeito e) {
      efeitos.remove(e);
   }

   // NOVO: Método para dano de efeitos que ignoram escudo (como Veneno)
   public void perderVida(int dano) {
      this.vida -= dano;
      if(this.vida <= 0) this.vida = 0;
   }

   public void receberDano(int dano) {
      if (this.escudo - dano >= 0) {
         this.escudo -= dano;
      } else {
         this.vida += this.escudo - dano;
         this.escudo = 0;
      }
   }

   public void ganharEscudo(int powerup) {
      this.escudo += powerup;
   }

   public int getVida() { 
      return vida; 
   }

   public int getEscudo() { 
      return escudo; 
   }

   public boolean estaVivo() { 
      return this.vida > 0; 
   }

   public String getNome() { 
      return nome;
   }

   public void setEscudo(int valor) { 
      this.escudo = valor; 
   }

   public void curar(int valor) {
      this.vida += valor;
      if(this.vida > this.vida_inicial) this.vida = this.vida_inicial;
      System.out.println("> " + this.getNome() + " recuperou " + valor + " de HP!");
   }
}