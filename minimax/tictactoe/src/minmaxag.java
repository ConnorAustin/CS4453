import java.util.ArrayList;

enum GameResult {
	WIN,
	LOSE,
	TIE,
	NOT_OVER
}

class GameState {
	public boolean myTurn = true;
	public ArrayList<GameState> children = new ArrayList<GameState>();
	public String state;
	GameResult result = GameResult.NOT_OVER;

	public GameState(String state, boolean myTurn) {
		this.state = state;
		this.myTurn = myTurn;
	}
}

public class minmaxag {
	GameState curState;
	
	GameResult gameResult(GameState game) {
		// Row win
		for (int y = 0; y < 3; y++) {
			boolean rowWin = true;

			char rowToken = game.state.charAt(y * 3);
			for (int x = 0; x < 3; x++) {
				char token = game.state.charAt(x + y * 3);
				if (token != rowToken) {
					rowWin = false;
					break;
				}
			}

			if (rowWin) {
				if (rowToken == 'O') {
					return GameResult.WIN;
				}
				if (rowToken == 'X') {
					return GameResult.LOSE;
				}
			}
		}

		// Column win
		for (int x = 0; x < 3; x++) {
			boolean colWin = true;

			char colToken = game.state.charAt(x);
			for (int y = 0; y < 3; y++) {
				char token = game.state.charAt(x + y * 3);
				if (token != colToken) {
					colWin = false;
					break;
				}
			}

			if (colWin) {
				if (colToken == 'O') {
					return GameResult.WIN;
				}
				if (colToken == 'X') {
					return GameResult.LOSE;
				}
			}
		}

		// Downward diagonal win
		boolean diagWin = true;
		char diagToken = game.state.charAt(0);
		for (int k = 0; k < 3; k++) {
			char token = game.state.charAt(k * 3 + k);
			if (token != diagToken) {
				diagWin = false;
				break;
			}
		}
		if (diagWin) {
			if (diagToken == 'O') {
				return GameResult.WIN;
			}
			if (diagToken == 'X') {
				return GameResult.LOSE;
			}
		}

		// Upward diagonal win
		diagWin = true;
		diagToken = game.state.charAt(2 * 3);
		for (int k = 0; k < 3; k++) {
			char token = game.state.charAt(3 * (2 - k) + k);
			if (token != diagToken) {
				diagWin = false;
				break;
			}
		}
		if (diagWin) {
			if (diagToken == 'O') {
				return GameResult.WIN;
			}
			if (diagToken == 'X') {
				return GameResult.LOSE;
			}
		}
		
		// Check if the game is not over
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (game.state.charAt(x + y * 3) == '_') {
					return GameResult.NOT_OVER;
				}
			}
		}
		return GameResult.TIE;
	}

	void expandState(GameState game) {
		char token = 'X';

		if (game.myTurn) {
			token = 'O';
		}

		for (int i = 0; i < game.state.length(); i++) {
			if (game.state.charAt(i) == '_') {
				StringBuilder b = new StringBuilder(game.state);
				b.setCharAt(i, token);
				
				GameState childState = new GameState(b.toString(), !game.myTurn);
				
				game.children.add(childState);
			}
		}
	}

	public minmaxag() {
		curState = new GameState("_________", false);
	}

	public int move(String S) {
		System.out.println(S);

		return 0;
	}
}
