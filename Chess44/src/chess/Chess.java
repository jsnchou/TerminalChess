/**
 * Main class that runs chess game
 @author Ty Goldin and Jason Chou
 */

package chess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import pieces.*;


public class Chess {
	/** Main Method running chess application
	 * @param args String array of arguments
	 * @throws IOException catches exception
	 */
	public static void main(String[] args) throws IOException {

		Piece[][] board = Board.initializeBoard();

		Board.printBoard(board);
		boolean game = true;
		boolean drawOffered = false;

		//mapX and mapY used to translate inputs into correct array values
		int counter = 0;
		HashMap<Character, Integer> mapX = new HashMap<>();
		for(char c = 'a'; c <= 'h'; c++)
			mapX.put(c, counter++);

		counter = 7;
		HashMap<Character, Integer> mapY = new HashMap<>();
		for(char c = '1'; c <= '8'; c++)
			mapY.put(c, counter--);



		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		String move;
		String turn = "White's ";

		while(game) {

			int sPosY = -1;
			int sPosX = -1;
			int fPosY = -1;
			int fPosX = -1;
			String sPos;
			String fPos;
			String pawnPromote = null;
			boolean first = true;
			boolean illegal = false;

			if (turn.equals("White's ") && board[King.kwY][King.kwX].inCheck(board)){
				if(((King)board[King.kwY][King.kwX]).inCheckmate(board)) {

					System.out.println("Checkmate\nBlack Wins");
					return;
				}
				System.out.println("Check");
			}
			if (turn.equals("Black's ")&& board[King.kbY][King.kbX].inCheck(board)) {
				if(((King)board[King.kbY][King.kbX]).inCheckmate(board)) {
					System.out.println("Checkmate\nWhite Wins");
					return;
				}
				System.out.println("Check");
			}



			do {
				illegal = false;
				if (!first) {
					System.out.println("Illegal move, try again");
				}
				System.out.print(turn + "move: ");
				move = read.readLine().trim().toLowerCase();

				if(move.equals("resign")) {
					if(turn.equals("White's "))
						System.out.println("Black wins");
					else
						System.out.println("White wins");

					return;
				}

				if(drawOffered) {
					if(move.equals("draw")) {
						System.out.println("draw");
						return;
					}
					drawOffered = false;
				}

				if(move.indexOf("draw?") == 6)
					drawOffered = true;

				try {
					sPos = move.substring(0, move.indexOf(" "));
					fPos = move.substring(move.indexOf(" ") + 1);
				}
				catch(Exception e) {
					illegal = true;
					first = false;
					continue;
				};
				pawnPromote = null;
				if (move.length() > 6) {
					pawnPromote = move.substring(6, 7);
				}

				Pawn.promote = pawnPromote;

				if (mapX.containsKey((sPos.charAt(0)))) {
					sPosY = mapX.get(sPos.charAt(0));
				} else {
					illegal = true;
					continue;
				}

				if (mapY.containsKey(sPos.charAt(1))) {
					sPosX = mapY.get(sPos.charAt(1));
				} else {
					illegal = true;
				}
				if (mapX.containsKey(fPos.charAt(0))) {
					fPosY = mapX.get(fPos.charAt(0));
				} else {
					illegal = true;
				}
				if(mapY.containsKey(fPos.charAt(1))) {
					fPosX = mapY.get(fPos.charAt(1));
				} else {
					illegal = true;
				}
				if (board[sPosX][sPosY] == null){
					illegal = true;
				} else if (!(board[sPosX][sPosY].color.equals(turn.substring(0, 5).toLowerCase()))){
					illegal = true;
				} else if (!board[sPosX][sPosY].isValidMove(board, sPosX, sPosY, fPosX, fPosY)){
					illegal = true;
				} else {
					Piece captured = null;
					try{
						captured = board[sPosX][sPosY].move(board, sPosX, sPosY, fPosX, fPosY);
					}
					catch(Exception E) {};
					if (turn.equals("White's ")){

						if (board[King.kwY][King.kwX].inCheck(board)) {
							if(board[fPosX][fPosY] instanceof King) {

							}
							board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);

							board[fPosX][fPosY] = captured;
							illegal = true;
						}
					} else {
						if (board[King.kbY][King.kbX].inCheck(board)) {

							board[fPosX][fPosY].move(board, fPosX, fPosY, sPosX, sPosY);
							board[fPosX][fPosY] = captured;
							illegal = true;
						}
					}
				}

				first = false;

			} while (illegal);

			board[fPosX][fPosY].haveMoved = true;
			Board.printBoard(board);
			Pawn.checkEnPassante(turn);
			turn = turn.equals("White's ") ? "Black's " : "White's ";
			King.turn = turn;

		}

	}

}
