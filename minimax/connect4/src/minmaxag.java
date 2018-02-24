import java.util.Random;

class PoliteBanter {
	static String[] banter = new String[] { "Oh wow that was a good move!", "You're so good at this!",
			"I wish I was as good as you.", "You're so smart!", "10/10 All works!", "You might beat me!",
			"What a rational decision", "If you were a sorting algorithm, you'd be O(1)!" };
	static Random rand = new Random();

	static String getBanter() {
		String boi = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@@@@@@@@&@*                      .&&@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@@@@@&(                                      *&@@@@@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@@@@&*                                               %@@@@@@@@@@@@@@@\n"
				+ "@@@@@@@@@@@%                                                      @@@@@@@@@@@@@\n"
				+ "@@@@@@@@@#     ^             ^                                      @@@@@@@@@@@\n"
				+ "@@@@@@@@,                                                             %@@@@@@@@\n"
				+ "@@@@@@@%      \\______________/                                         @@@@@@@@\n"
				+ "@@@@@@@                                                                (@@@@@@@\n"
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

		return boi + "\n" + "Rational Agent Carl: " + banter[rand.nextInt(banter.length)];
	}
}

class GameResult {
	public static int WIN = 3;
	public static int NOT_OVER = 2;
	public static int TIE = 1;
	public static int LOSE = 0;
}

class GameState {
	public static int W = 7;
	public static int H = 6;
	int turns = 0; // How many turns it takes to get to an optimal state
	
	public boolean myTurn = true;
	public String state;
	int result = -1;
	int heuristic = -1; // For when we can't get an exact result

	int checkX = 0;
	int checkY = 0;

	public GameState(String state, boolean myTurn) {
		this.state = state;
		this.myTurn = myTurn;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				b.append(state.charAt(x + y * W));
				b.append(' ');
			}
			b.append('\n');
		}
		return b.toString();
	}

	boolean checkLine(int x, int y, int dx, int dy) {
		int Xs = 0;
		int Os = 0;

		while (x >= 0 && x < W && y >= 0 && y < H) {
			char c = state.charAt(x + y * W);

			switch (c) {
			case 'X':
				Os = 0;
				Xs++;
				if (Xs == 4) {
					result = GameResult.LOSE;
					return true;
				}
				break;
			case 'O':
				Xs = 0;
				Os++;
				if (Os == 4) {
					result = GameResult.WIN;
					return true;
				}
				break;
			case '_':
				Xs = 0;
				Os = 0;
				break;
			}

			x += dx;
			y += dy;
		}
		return false;
	}

	public int result() {
		if (result != -1) {
			return result;
		}
		result = GameResult.NOT_OVER;

		// Check rows
		for (int y = 0; y < H; y++) {
			if (checkLine(0, y, 1, 0)) {
				return result;
			}
		}

		// Check columns
		for (int x = 0; x < W; x++) {
			if (checkLine(x, 0, 0, 1)) {
				return result;
			}
		}

		// Check upper triangle upward diagonals
		for (int y = 0; y < H; y++) {
			if (checkLine(0, y, 1, -1)) {
				return result;
			}
		}

		// Check lower triangle upward diagonals
		for (int x = 1; x < W; x++) {
			if (checkLine(x, H - 1, 1, -1)) {
				return result;
			}
		}

		// Check upper triangle downward diagonals
		for (int y = 0; y < H; y++) {
			if (checkLine(W - 1, y, -1, -1)) {
				return result;
			}
		}

		// Check lower triangle downward diagonals
		for (int x = 1; x < W; x++) {
			if (checkLine(W - 1 - x, H - 1, -1, -1)) {
				return result;
			}
		}

		// Check tie
		if (!state.contains("_")) {
			result = GameResult.TIE;
		}

		return result;
	}

	void calculateHeuristic() {
		heuristic = 9999;
		for (int x = 0; x < W; x++) {
			for (int y = 0; y < H; y++) {
				char c = state.charAt(x + y * W);
				if (c == 'X') {
					heuristic--;
				}
				if (c == 'O') {
					break;
				}
			}
		}
	}

	int moveToState(GameState newState) {
		for (int x = 0; x < W; x++) {
			for (int y = 0; y < H; y++) {
				char nc = newState.state.charAt(x + y * W);
				char c = state.charAt(x + y * W);
				if (c != nc) {
					return x;
				}
			}
		}
		return 0;
	}
}

public class minmaxag {
	static int DEPTH_LIMIT = 8;

	GameState expandState(GameState game, int depth) {
		game.turns = depth;
		
		int result = game.result();
		if (result != GameResult.NOT_OVER) {
			
			game.result = result;
			return game;
		}

		if (depth == DEPTH_LIMIT) {
			game.calculateHeuristic();
			game.result = GameResult.NOT_OVER;
			return game;
		}

		char token = 'X';

		if (game.myTurn) {
			token = 'O';
		}

		GameState bestState = null;
		GameState bestHeuristicState = null;

		for (int x = 0; x < GameState.W; x++) {
			int y = GameState.H - 1;

			while (y > -1) {
				if (game.state.charAt(x + y * GameState.W) == '_') {
					break;
				}

				y--;
			}

			if (y == -1) {
				continue;
			}

			StringBuilder b = new StringBuilder(game.state);
			b.setCharAt(x + y * GameState.W, token);

			GameState childState = new GameState(b.toString(), !game.myTurn);
			expandState(childState, depth + 1);

			// Maximize
			if (game.myTurn) {
				if (bestState == null) {
					bestState = childState;
				} else {
					if (bestState.result < childState.result || 
							(bestState.result == childState.result && bestState.turns > childState.turns)) {
						bestState = childState;
					}
				}

				if (bestHeuristicState == null) {
					bestHeuristicState = childState;
				} else {
					if (bestHeuristicState.heuristic < childState.heuristic ||
							(bestState.result == childState.result && bestState.turns > childState.turns)) {
						bestHeuristicState = childState;
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

				if (bestHeuristicState == null) {
					bestHeuristicState = childState;
				} else {
					if (bestHeuristicState.heuristic > childState.heuristic) {
						bestHeuristicState = childState;
					}
				}
			}
		}
		
		game.turns = bestState.turns;
		game.result = bestState.result;
		game.heuristic = bestHeuristicState.heuristic;

		if (bestState.result == GameResult.NOT_OVER) {
			return bestHeuristicState;
		} else {
			return bestState;
		}
	}

	public minmaxag() {
//		GameState s = new GameState("O____O_O_O__XXO_XOOXOXOXXXOXOXOOXXXOOXXXOX", true);
		GameState s = new GameState("__________O______X___X_OOO__X_OXXX_OOXXXO_", true);
		expandState(s, 1);
	}

	public int move(String S) {
		GameState state = new GameState(S, true);
		GameState result = expandState(state, 1);
		System.out.println(PoliteBanter.getBanter());
		return state.moveToState(result);
	}
}
