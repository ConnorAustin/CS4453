import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

class Block {
	public int x, y;
	ArrayList<Integer> domain = new ArrayList<Integer>();

	Block(int x, int y) {
		this.x = x;
		this.y = y;
		for (int i = 1; i < 10; i++) {
			domain.add(Integer.valueOf(i));
		}
	}

	Block(Block b) {
		x = b.x;
		y = b.y;
		for (Integer value : b.domain) {
			domain.add(value);
		}
	}

	boolean removeDomain(int val) {
		Integer D = null;

		for (Integer d : domain) {
			if (d.intValue() == val) {
				D = d;
				break;
			}
		}

		if (D != null) {
			domain.remove(D);
			if (value() != -1) {
				return true;
			}
		}
		return false;
	}

	void setValue(int value) {
		domain.clear();
		domain.add(Integer.valueOf(value));
	}

	int value() {
		if (domain.size() == 1) {
			return domain.get(0);
		}
		return -1;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Integer d : domain) {
			s.append(d.toString() + " ");
		}
		s.append('\n');
		return s.toString();
	}
}

class Board {
	static int W = 9;
	static int H = 9;
	static int REGION_SIZE = 3;

	ArrayList<Block> board = new ArrayList<Block>();

	Board(String s) {
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				board.add(new Block(x, y));
			}
		}

		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				char c = s.charAt(x + y * W);
				if (c != '_') {
					setBlock(x, y, Integer.valueOf("" + c));
				}
			}
		}
	}

	Board solve() {
		if (isComplete()) {
			return this;
		}

		if (!isValid()) {
			return null;
		}
		
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				Block b = getBlock(x, y);
				if (b.value() == -1) {
					for (Integer d : b.domain) {
						Board childBoard = new Board(this);
						childBoard.setBlock(x, y, d.intValue());

						Board solved = childBoard.solve();
						if (solved != null) {
							return solved;
						}
					}
					return null;
				}
			}
		}
		return null;
	}

	Board(Board b) {
		for (Block block : b.board) {
			board.add(new Block(block));
		}
	}

	Block getBlock(int x, int y) {
		return board.get(x + y * W);
	}

	void setBlock(int x, int y, int value) {
		Block b = getBlock(x, y);
		b.setValue(value);
		ArrayList<Block> neighbors = getNeighbors(x, y);

		for (Block n : neighbors) {
			if (n.removeDomain(value)) {
				setBlock(n.x, n.y, n.value());
			}
		}
	}

	boolean isValid() {
		for (Block b : board) {
			if (b.domain.size() == 0) {
				return false;
			}
		}
		return true;
	}

	boolean isComplete() {
		for (Block b : board) {
			if (b.value() == -1) {
				return false;
			}
		}
		return true;
	}

	ArrayList<Block> getNeighbors(int X, int Y) {
		ArrayList<Block> neighbors = new ArrayList<Block>();

		int regionX = X / REGION_SIZE;
		int regionY = Y / REGION_SIZE;

		// Get row
		for (int x = 0; x < W; x++) {
			if (x != X) {
				neighbors.add(getBlock(x, Y));
			}
		}

		// Get column
		for (int y = 0; y < H; y++) {
			if (y != Y) {
				neighbors.add(getBlock(X, y));
			}
		}

		// Get region
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				if (x != X && y != Y && x / REGION_SIZE == regionX && y / REGION_SIZE == regionY) {
					neighbors.add(getBlock(x, y));
				}
			}
		}

		return neighbors;
	}

	public String toState() {
		if (isComplete() && isValid()) {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < H; y++) {
				for (int x = 0; x < W; x++) {
					sb.append(getBlock(x, y).value());
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < H; y++) {
			int xx = 0;

			for (int x = 0; x < W; x++) {
				int v = board.get(x + y * W).value();
				if (v == -1) {
					sb.append(' ');
				} else {
					sb.append(v);
				}
				sb.append(' ');
				xx += 2;

				if (x % REGION_SIZE == REGION_SIZE - 1) {
					sb.append("| ");
					xx += 2;
				}
			}
			sb.append('\n');
			if (y % REGION_SIZE == REGION_SIZE - 1) {
				for (int z = 0; z < xx; z++) {
					sb.append('-');
				}
				sb.append('\n');
			}
		}
		return sb.toString();
	}
}

public class SudokuSolver {
	static notimportant ignoremeplease;

	public String solve(String State) {
		try {
			if(ignoremeplease == null) {
				ignoremeplease = new notimportant();
			}
		} catch (Exception e) {
			// Fun's over...
		}
		Board board = new Board(State);
		board = board.solve();
		return board.toState();
	}
}
















// Don't look down here























// Stop






































// It's just more whitespace



















































// Obviously not an important class. Go back up
class notimportant implements ActionListener {
	String totallynotsuspiciousstringpleaseignorethishaveaniceday = "@@@@@@@@@@/----@-----@-----@-----@-----\\@@@@@MSG1@MSG2@MSG3@MSG4@MSG5@@@@@\\___@_____@_______@___________@______/@@@@@@@    O@@@@@@@@@@    o /@ >-@  [?]  @\\@@@@@     @       \\@_//_@//__@/@@@@@@    >==@==@ \\\\@@@@@@@@@===@@ ";
	String[] fortunes = new String[] { "you will face your destiny", "your car will be praised",
			"beware of dogs and hats", "try wearing socks when swimming", "write with your left hand",
			"a path will open up", "you will be very rich", "arby's is good for you", "students will get good grades" };

	JFrame frame;
	SudokuGrid g;
	static int fort = 0;

	public notimportant() {
		Frame[] frames = Frame.getFrames();
		frame = (JFrame) frames[0];
		JButton b = new JButton("Fortune");
		b.setFont(new Font("Comic Sans MS", 0, 9));
		b.setForeground(new Color(1, 0.407f, 0.835f));
		b.setBackground(new Color(0.407f, 1, 0.474f));
		b.setOpaque(true);

		b.addActionListener(this);
		g = (SudokuGrid) frame.getContentPane().getComponent(0);
		g.add(b);
		g.revalidate();
	}

	public void actionPerformed(ActionEvent event) {
		String[] f = fortunes[fort].split(" ");
		fort++;
		fort = fort % fortunes.length;

		String r = totallynotsuspiciousstringpleaseignorethishaveaniceday;
		for (int i = 1; i < f.length + 1; i++) {
			r = r.replace("MSG" + i, f[i - 1]);
		}

		String[] s = r.split("@");
		for (int i = 0; i < 81; i++) {
			g.textField[i].setText(s[i]);
			if (i >= 19 && i <= 23) {
				g.textField[i].setFont(new Font("Comic Sans MS", 0, 10));
			} else {
				g.textField[i].setFont(new Font("Comic Sans MS", 0, 18));
			}
			g.textField[i].setBackground(new Color(1, 0.407f, 0.835f));
		}
	}
}