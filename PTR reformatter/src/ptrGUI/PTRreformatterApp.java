package ptrGUI;

import java.awt.EventQueue;

public class PTRreformatterApp {

		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						new MainFrame("PTR Reformatter");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}

	}
