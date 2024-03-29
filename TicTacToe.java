/*  class of tic tac toe game
 */

import java.util.ArrayList;

public class TicTacToe {
   private final GameBoard GAME_BOARD;
   private byte  turn;
   private GameStatus gameStatus = GameStatus.CONTINUE;
   
   public  static final byte NUMBER_OF_CELLS = GameBoard.getNUMBER_OF_CELLS();
   private static final byte MIN_TURN_TO_VICTORY = (byte)(2 * GameBoard.getSQUARE_SIZE() - 1);  // numberOfPlayers = 2
   private static final byte MIN_TURN_TO_DRAW    = (byte)(NUMBER_OF_CELLS - GameBoard.getSQUARE_SIZE());
   public  static final byte NUMBER_OF_CELLS_TO_ALMOST_WIN = (byte)(GameBoard.getSQUARE_SIZE() - 1);
   
   public TicTacToe() {
      turn = 0;
      gameStatus = GameStatus.CONTINUE;
      GAME_BOARD = new GameBoard();
   }
   
   public TicTacToe(TicTacToe ticTacToe) {
      if (null == ticTacToe) {
         throw new NullPointerException("ticTacToe can not be null");
      }
      turn = ticTacToe.getTurn();
      gameStatus = ticTacToe.getGameStatus();
      
      CellValue [][] board = ticTacToe.getBoard();
      GAME_BOARD = new GameBoard(board);
   }
   
   public GameStatus getGameStatus() {
      return gameStatus;
   }
   
   public boolean isGameOver() {
      return gameStatus != GameStatus.CONTINUE;
   }
   
   GameBoard getGameBoard() {
      return GAME_BOARD;
   }
   
   CellValue[][] getBoard() {
      return GAME_BOARD.getBoard();
   }
   
   public byte getTurn() {
      return turn;
   }
   
   ArrayList<ByteIntegersPair> getAllowedCellsCoordinations() {
      return GAME_BOARD.getAllowedCellsCoordinations();
   }
   
   public void restart() {
      GAME_BOARD.restart();
      
      turn = 0;
      gameStatus = GameStatus.CONTINUE;
   }
   
   public static byte getSQUARE_SIZE() {
      return GameBoard.getSQUARE_SIZE();
   }
   
   public static boolean validateRowOrColumn(byte number) {
      // public methods in class TicTacToe assume that first index of array is 1
      return GameBoard.validateRowOrColumn(number);
   }
   
   public MoveStatus validatePosition(byte row, byte column) {
      return GAME_BOARD.validatePosition(row, column);
   }
   
   public MoveStatus move(byte row, byte column) {
      if (true == isGameOver()) {
         return MoveStatus.AFTER_GAME_OVER;
      }
      
      // public methods in class TicTacToe assume that first index of array is 1
      MoveStatus moveStatus = GAME_BOARD.validatePosition(row, column);
      if (MoveStatus.CORRECT == moveStatus) {
         turn++;
         // otherwise private methods in class TicTacToe assume that first index of array is 0
         markBoardCell(--row, --column);  
         
         if (NUMBER_OF_CELLS - 1 == turn && gameStatus == GameStatus.CONTINUE) {
            performLastMoveAutomatically();
         }
      } 
    
      return moveStatus;
   }
   
   private void markBoardCell(byte row, byte column) {
      if (1 == turn % 2) {
         GAME_BOARD.markBoardCell(row, column, CellValue.X);
      } 
      else {
         GAME_BOARD.markBoardCell(row, column, CellValue.O);
      }
      if (turn >= MIN_TURN_TO_VICTORY) {
         checkGameStatus();
      }
   }
   
   private void checkGameStatus() {
      CellValue winner = GAME_BOARD.getWinnerInRows();
      
      if (CellValue.EMPTY == winner) {
         winner = GAME_BOARD.getWinnerInColumns();
      }
      if (CellValue.EMPTY == winner) {
         winner = GAME_BOARD.getWinnerInDiagonals();
      }
      
      if (CellValue.EMPTY != winner) {
         setWinner(winner);
      } 
      else if (turn >= MIN_TURN_TO_DRAW) {
         if (NUMBER_OF_CELLS == turn) {
            this.gameStatus = GameStatus.DRAW;
         }
         else {
            checkEarlyGameTermination();
         }
      }
   }
   
   private void checkEarlyGameTermination() {
      if (MIN_TURN_TO_DRAW > turn) {
         return;
      }
      
      int firstPlayerVictoryChances  = GAME_BOARD.calculateVictoryChance(CellValue.X);
      int secondPlayerVictoryChances = GAME_BOARD.calculateVictoryChance(CellValue.O);
      /*
      GlobalLogger.logInfo("Victory chances: firstPlayer  = ", firstPlayerVictoryChances, 
                                          " secondPlayer = ", secondPlayerVictoryChances);*/
      
      if (firstPlayerVictoryChances == 0 && secondPlayerVictoryChances == 0) {
         if (MIN_TURN_TO_DRAW < turn || 
            (MIN_TURN_TO_DRAW == turn && false == GAME_BOARD.areAdjacentCells(CellValue.EMPTY, 2))) {
         
            this.gameStatus = GameStatus.DRAW;
         }
      }
      else {
         checkEarlyGameVictory();
      }
   }
   
   private void checkEarlyGameVictory() {
      int firstPlayerVictoryChances  = GAME_BOARD.calculateVictoryChance(CellValue.X);
      int firstPlayerRemainedMoves   = getRemainedMoves(CellValue.X);
      int secondPlayerVictoryChances = GAME_BOARD.calculateVictoryChance(CellValue.O);
      int secondPlayerRemainedMoves  = getRemainedMoves(CellValue.O);
      
      /*
      GlobalLogger.logInfo("Remained moves:  firstPlayer  = ", firstPlayerRemainedMoves, 
                                          " secondPlayer = ", secondPlayerRemainedMoves);*/
      
      boolean afterFirstPlayerMove = turn % 2 == 1;
      int remainedTurns = NUMBER_OF_CELLS - turn;
      if (true == afterFirstPlayerMove) {
         if (secondPlayerVictoryChances > firstPlayerRemainedMoves && 0 == firstPlayerVictoryChances) { // delete this if when SQUARE_SIZE > 3
               
            this.gameStatus = GameStatus.SECOND_PLAYER_WIN;
         }
      }
      else {   // if (false == afterFirstPlayerMove) {
         if (firstPlayerVictoryChances > secondPlayerRemainedMoves && 0 == secondPlayerVictoryChances) { // delete this if when SQUARE_SIZE > 3
         
            this.gameStatus = GameStatus.FIRST_PLAYER_WIN;
         }
      }
      
   }
   
   private void performLastMoveAutomatically() {
      turn++;
      GAME_BOARD.markBoardEmptyCell(CellValue.X);
      checkGameStatus();
   }
   
   private int getRemainedMoves(CellValue cellValue) {
      if (cellValue == CellValue.EMPTY) {
         throw new IllegalArgumentException("cellValue " + cellValue);
      }
      
      int remainedMoves = (NUMBER_OF_CELLS - turn) / 2;
      
      if (NUMBER_OF_CELLS % 2 == 1 && CellValue.X == cellValue && turn % 2 == 0 ||
          NUMBER_OF_CELLS % 2 == 0 && CellValue.O == cellValue && turn % 2 == 1)  {
             
         remainedMoves++;
      } 
      
      return remainedMoves;
   }
   
   public static CellValue getOtherPlayerCellValue(CellValue cell) {
      switch (cell) {
         case O:
            return CellValue.X;
         case X:
            return CellValue.O;
            
         case EMPTY:
         default:
            assert(false) : "CellValue's enum of' " + cell + " should not be here as argument";
            throw new IllegalArgumentException("CellValue's enum of' " + cell);
      }
   } 
   
   private void setWinner(CellValue cell) {
      switch (cell) {
         case O:
            gameStatus = GameStatus.SECOND_PLAYER_WIN;
            break;
         case X:
            gameStatus = GameStatus.FIRST_PLAYER_WIN;
            break;
            
         case EMPTY:
         default:
            assert(false) : "CellValue's enum of' " + cell + " should not be here as argument";
            throw new IllegalArgumentException("CellValue's enum of' " + cell);
      }
   }
} 
