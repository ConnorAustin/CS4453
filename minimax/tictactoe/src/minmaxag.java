import java.util.Random;

class PoliteBanter {
	static String[] banter = new String[] { "0/10 Nothing Works.", "You're so bad at this.", "Too easy!",
			"Come on. You can do better than this", "Need an extra turn?",
			"If you were a sorting algorithm, you would be Bogo Sort.", "I wouldn't have chosen that",
			"Tsk. Tsk. Not rational at all." };
	static Random rand = new Random();

	static String getBanter() {
		String boi = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@&@*                      .&&@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@&(                                      *&@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@&*                                               %@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@%                                                      @@@@@@@@@@@@@\n"
				+ "@@@@@@@@@#     .\\       /.                                          @@@@@@@@@@@\n"
				+ "@@@@@@@@,                                                             %@@@@@@@@\n"
				+ "@@@@@@@%       ____________                                           @@@@@@@@\n"
				+ "@@@@@@@       /            \\                                           (@@@@@@@\n"
				+ "@@@@@@@.                                                               @@@@@@@@\n"
				+ "@@@@@@@@                                                              @@@@@@@@@\n"
				+ "@@@@@@@@@/               &,,,.                                       @@@@@@@@@@\n"
				+ "@@@@@@@@@@@/          @,,,,&     /&@      /@@ @,#(/@               @@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@*,     @,,,,(     %,,,,     .*, ,@ ,*              @@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@&@,,,,@     #,,,,,@                        @@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@&(,,,,,&,,%@@@,   @,,,,,@                   %%@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@,,,,,,,,&,@@@@@@@@@@,,,,,.*%@&&&&%&//,,,@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@,,,,@@@@@@@@@@@@@(,,,@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@,,,@@@@@@@@@@@@@@/,&,,&@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@*,,,@@@@@@@@@@@@@@@&,,,,@,&@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@,,,,@@@@@@@@@@@@@@@@@@@,@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@/,,&@@@@@@@@@@@@@@@@/%@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@&,,*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";

		return boi + "\n" + "Rational Agent Keith: " + banter[rand.nextInt(banter.length)];
	}
}

class GameResult {
	public static int WIN = 3;
	public static int NOT_OVER = 2;
	public static int TIE = 1;
	public static int LOSE = 0;
}

class GameState {
	public boolean myTurn = true;
	public String state;
	int result = GameResult.NOT_OVER;

	public GameState(String state, boolean myTurn) {
		this.state = state;
		this.myTurn = myTurn;
	}
}

public class minmaxag {
	GameState curState;

	int gameResult(GameState game) {
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

	GameState expandState(GameState game) {
		int result = gameResult(game);
		if (result != GameResult.NOT_OVER) {
			game.result = result;
			return game;
		}

		char token = 'X';

		if (game.myTurn) {
			token = 'O';
		}

		GameState bestState = null;

		for (int i = 0; i < game.state.length(); i++) {
			if (game.state.charAt(i) == '_') {
				StringBuilder b = new StringBuilder(game.state);
				b.setCharAt(i, token);

				GameState childState = new GameState(b.toString(), !game.myTurn);
				expandState(childState);

				// Maximize
				if (game.myTurn) {
					if (bestState == null) {
						bestState = childState;
					} else {
						if (bestState.result < childState.result) {
							bestState = childState;
						}
					}
				} else {
					if (bestState == null) {
						bestState = childState;
					} else {
						if (bestState.result > childState.result) {
							bestState = childState;
						}
					}
				}
			}
		}
		game.result = bestState.result;
		return bestState;
	}

	public minmaxag() {
	}

	int moveToState(GameState cur, GameState dest) {
		for (int i = 0; i < cur.state.length(); i++) {
			if (cur.state.charAt(i) != dest.state.charAt(i)) {
				return i;
			}
		}
		return 0;
	}

	public int move(String S) {
		GameState state = new GameState(S, true);
		GameState bestState = expandState(state);
		System.out.println(PoliteBanter.getBanter());
		return moveToState(state, bestState);
	}
}
