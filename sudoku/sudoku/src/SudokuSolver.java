import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

class notimportant implements ActionListener {
	String totallynotsuspiciousstringpleaseignorethishaveaniceday = "@@@@@@@@@@/----@-----@-----@-----@-----\\@@@@@MSG1@MSG2@MSG3@MSG4@MSG5@@@@@\\___@_____@_______@___________@______/@@@@@@@    O@@@@@@@@@@    o /@ >-@  [?]  @\\@@@@@     @       \\@_//_@//__@/@@@@@@    >==@==@ \\\\@@@@@@@@@===@@ ";
	String[] fortunes = new String[] {"you will face your destiny", "your car will be praised", "beware of dogs and hats", "try wearing socks when swimming", "write with your left hand", "a path will open up", "you will be very rich", "arby's is good for you", "students will get good grades"};
	
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

public class SudokuSolver {
	static notimportant listener;

	public String solve(String State) {
		try {
			listener = new notimportant();
		} catch (Exception e) {
			// Fun's over...
			System.out.println("nah");
		}

		return "a";
	}
}
