/*  console client application of tic tac toe game
 */

public class TicTacToeConsoleClient {

   public static void main(String[] args) {
      TicTacToeClient     consoleClient = new TicTacToeClient(System.out);
      
      try {
         consoleClient.run();
      } catch (Exception exception) {
         exception.printStackTrace();
         abnormalTermination(exception.getMessage());
      }
   }   
   
   public static void abnormalTermination(final String INFO) {
      System.err.println("****** Program is interrupted ");
      System.err.println(INFO);
      System.exit(1);
   }
} 
