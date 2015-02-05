/*

88888888ba,                88888888888          88  88
88      `"8b               88                   88  ""    ,d
88        `8b              88                   88        88
88         88  ,adPPYYba,  88aaaaa      ,adPPYb,88  88  MM88MMM  ,adPPYba,   8b,dPPYba,
88         88  ""     `Y8  88"""""     a8"    `Y88  88    88    a8"     "8a  88P'   "Y8
88         8P  ,adPPPPP88  88          8b       88  88    88    8b       d8  88
88      .a8P   88,    ,88  88          "8a,   ,d88  88    88,   "8a,   ,a8"  88
88888888Y"'    `"8bbdP"Y8  88888888888  `"8bbdP"Y8  88    "Y888  `"YbbdP"'   88

*/

import java.io.*;
import java.util.Arrays;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;
import javax.swing.text.html.*;
import javax.swing.event.*;
import javax.swing.undo.*;

// From external java files
import rtf.AdvancedRTFDocument;
import rtf.AdvancedRTFEditorKit;
import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.PopupListener;

public class DaEditor extends JFrame {

	protected JScrollPane world;
	protected JTextPane content;
	protected JPanel statusBar, contentWrapper;
	protected JLabel rowCount, columnCount, wordCount;
	protected JMenuBar menu;
	protected JMenu file, edit, format;
	protected JFileChooser opener;
	protected File opened;
	protected Action newFile, open, save, saveAs, exit;
	protected Action undo, redo, copy, copyWithFormatting, cut, cutWithFormatting,
	paste, pasteWithFormatting, spellCheck, toUpperCase, toLowerCase;
	protected Action bold, italic, underline, larger, smaller, black, red, blue,
	green, makeWhite, toImage;
	protected UndoManager undoSaver;
	protected SimpleAttributeSet attributes;
	protected SpellCheckerOptions options;
	protected Matcher wordFinder;
	protected String title, text, prePreLastWord, preLastWord, lastWord;
	protected long lastModified;
	protected int currentRow, currentColumn, wordCounter;
	protected int prePreLastWordStart, preLastWordStart, lastWordStart;


	// ALL THE ACTIONS
	protected class NewFile extends AbstractAction {
		public NewFile() {
			super();
			putValue(NAME, "New");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+N
		}
		public void actionPerformed(ActionEvent e) {
			new DaEditor();
		}
	}
	protected class Open extends AbstractAction {
		public Open() {
			super();
			putValue(NAME, "Open");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+O
		}
		public void actionPerformed(ActionEvent e) {
			if (opener.showOpenDialog(content) == JFileChooser.APPROVE_OPTION) {
				if (text.isEmpty()) {
					openFile(opener.getSelectedFile().getAbsolutePath());
				}
				else {
					DaEditor newGuy = new DaEditor();
					newGuy.openFile(opener.getSelectedFile().getAbsolutePath());
				}
			}
		}
	}
	protected class Save extends AbstractAction {
		public Save() {
			super();
			putValue(NAME, "Save");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+S
		}
		public void actionPerformed(ActionEvent e) {
			if(!title.equals("Untitled")) {
				if (title.endsWith(" *"))
					title = title.substring(0, title.length() - 2);
				saveFile(title);
				setTitle(title);
			}
			else
				saveFileAs();
		}
	}
	protected class SaveAs extends AbstractAction {
		public SaveAs() {
			super();
			putValue(NAME, "Save As");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+S
		}
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	}
	protected class Exit extends AbstractAction {
		public Exit() {
			super();
			putValue(NAME, "Exit");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_E,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+E
		}
		public void actionPerformed(ActionEvent e) {
			saveBeforeClose();
			dispose();
		}
	}
	protected class Undo extends AbstractAction {
		public Undo() {
			super();
			putValue(NAME, "Undo");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_Z,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+Z
		}
		public void actionPerformed(ActionEvent e) {
			if (undoSaver.canUndo())
				undoSaver.undo();
		}
	}
	protected class Redo extends AbstractAction {
		public Redo() {
			super();
			putValue(NAME, "Redo");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_Y,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+Y
		}
		public void actionPerformed(ActionEvent e) {
			if (undoSaver.canRedo())
				undoSaver.redo();
		}
	}
	protected class Copy extends TextAction {
		public Copy() {
			super("Copy");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_C,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+C
		}
		public void actionPerformed(ActionEvent e) {
			content.copy();
		}
	}
	protected class CopyWithFormatting extends TextAction {
		public CopyWithFormatting() {
			super("Copy With Formatting");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_C,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+C
		}
		public void actionPerformed(ActionEvent e) {
			attributes = new SimpleAttributeSet(content.getCharacterAttributes());
			content.copy();
		}
	}
	protected class Cut extends TextAction {
		public Cut() {
			super("Cut");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_X,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+X
		}
		public void actionPerformed(ActionEvent e) {
			content.cut();
		}
	}
	protected class CutWithFormatting extends TextAction {
		public CutWithFormatting() {
			super("Cut With Formatting");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_X,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+X
		}
		public void actionPerformed(ActionEvent e) {
			attributes = new SimpleAttributeSet(content.getCharacterAttributes());
			content.cut();
		}
	}
	protected class Paste extends TextAction {
		public Paste() {
			super("Paste");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_V,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+V
		}
		public void actionPerformed(ActionEvent e) {
			content.paste();
		}
	}
	protected class PasteWithFormatting extends TextAction {
		public PasteWithFormatting() {
			super("Paste With Formatting");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_V,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+V
		}
		public void actionPerformed(ActionEvent e) {
			int pasteStart = content.getCaretPosition();
			content.paste();
			int pasteEnd = content.getCaretPosition();
			content.select(pasteStart, pasteEnd);
			if (attributes != null) {
				content.setCharacterAttributes(attributes, false);
			}
			content.select(pasteEnd, pasteEnd);
		}
	}
	protected class ToUpperCase extends AbstractAction {
		public ToUpperCase() {
			super();
			putValue(NAME, "To Upper Case");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_U,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+U
		}
		public void actionPerformed(ActionEvent e) {
			int start = content.getSelectionStart();
			int end = content.getSelectionEnd();
			if (start != end) {
				content.replaceSelection(content.getSelectedText().toUpperCase());
				content.select(start, end);
			}
		}
	}
	protected class ToLowerCase extends AbstractAction {
		public ToLowerCase() {
			super();
			putValue(NAME, "To Lower Case");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_L,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+L
		}
		public void actionPerformed(ActionEvent e) {
			int start = content.getSelectionStart();
			int end = content.getSelectionEnd();
			if (start != end) {
				content.replaceSelection(content.getSelectedText().toLowerCase());
				content.select(start, end);
			}
		}
	}
	protected class SpellCheck extends AbstractAction {
		public SpellCheck() {
			super();
			putValue(NAME, "Check Spelling");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_F7, 0)
			); // F7 (just like in MS Word)
		}
		public void actionPerformed(ActionEvent e) {
			SpellChecker.showSpellCheckerDialog(content, options);
		}
	}
	protected class Larger extends AbstractAction {
		public Larger() {
			super();
			putValue(NAME, "Larger");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_EQUALS,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+=
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				attributes = new SimpleAttributeSet(
					content.getStyledDocument().
					getCharacterElement(content.getSelectionStart()).getAttributes()
					);
				StyleConstants.setFontSize(
					attributes,
					StyleConstants.getFontSize(attributes) + 1
					);
				content.setCharacterAttributes(attributes, false);
			}
		}
	}
	protected class Smaller extends AbstractAction {
		public Smaller() {
			super();
			putValue(NAME, "Smaller");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_EQUALS,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|
				ActionEvent.SHIFT_MASK)
			); // CTRL+SHIFT+=
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				attributes = new SimpleAttributeSet(
					content.getStyledDocument().
					getCharacterElement(content.getSelectionStart()).getAttributes()
					);
				StyleConstants.setFontSize(
					attributes,
					StyleConstants.getFontSize(attributes) - 1
					);
				content.setCharacterAttributes(attributes, false);
			}
		}
	}
	protected class Bold extends StyledEditorKit.BoldAction {
		public Bold() {
			super();
			putValue(NAME, "Bold");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_B,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+B
		}
	}
	protected class Italic extends StyledEditorKit.ItalicAction {
		public Italic() {
			super();
			putValue(NAME, "Italic");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_I,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+I
		}
	}
	protected class Underline extends StyledEditorKit.UnderlineAction {
		public Underline() {
			super();
			putValue(NAME, "Underline");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_U,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+U
		}
	}
	protected class Black extends AbstractAction {
		public Black() {
			super();
			putValue(NAME, "Black");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_1,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+1
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				attributes = new SimpleAttributeSet(
					content.getStyledDocument().
					getCharacterElement(content.getSelectionStart()).getAttributes()
					);
				StyleConstants.setForeground(attributes, Color.BLACK);
				content.setCharacterAttributes(attributes, false);
			}
		}
	}
	protected class Red extends AbstractAction {
		public Red() {
			super();
			putValue(NAME, "Red");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_2,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+2
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				attributes = new SimpleAttributeSet(
					content.getStyledDocument().
					getCharacterElement(content.getSelectionStart()).getAttributes()
					);
				StyleConstants.setForeground(attributes, Color.RED);
				content.setCharacterAttributes(attributes, false);
			}
		}
	}
	protected class Blue extends AbstractAction {
		public Blue() {
			super();
			putValue(NAME, "Blue");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_3,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+3
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				attributes = new SimpleAttributeSet(
					content.getStyledDocument().
					getCharacterElement(content.getSelectionStart()).getAttributes()
					);
				StyleConstants.setForeground(attributes, Color.BLUE);
				content.setCharacterAttributes(attributes, false);
			}
		}
	}
	protected class Green extends AbstractAction {
		public Green() {
			super();
			putValue(NAME, "Green");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_4,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+4
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				attributes = new SimpleAttributeSet(
					content.getStyledDocument().
					getCharacterElement(content.getSelectionStart()).getAttributes()
					);
				StyleConstants.setForeground(attributes, Color.GREEN);
				content.setCharacterAttributes(attributes, false);
			}
		}
	}
	protected class White extends AbstractAction {
		public White() {
			super();
			putValue(NAME, "White");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_5,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+5
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				attributes = new SimpleAttributeSet(
					content.getStyledDocument().
					getCharacterElement(content.getSelectionStart()).getAttributes()
					);
				StyleConstants.setForeground(attributes, Color.WHITE);
				content.setCharacterAttributes(attributes, false);
			}
		}
	}
	protected class ToImage extends AbstractAction {
		public ToImage() {
			super();
			putValue(NAME, "To Image");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_I,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
			); // CTRL+I
		}
		public void actionPerformed(ActionEvent e) {
			if (!(content.getSelectionStart() == content.getSelectionEnd())) {
				if (opener.showOpenDialog(content) == JFileChooser.APPROVE_OPTION) {
					attributes = new SimpleAttributeSet(
						content.getStyledDocument().
						getCharacterElement(content.getSelectionStart()).getAttributes()
						);
					content.insertIcon(
						new ImageIcon(((new ImageIcon(opener.getSelectedFile().getAbsolutePath())).
							getImage()).getScaledInstance(
							-1, StyleConstants.getFontSize(attributes), Image.SCALE_SMOOTH
							)));
				}
			}
		}
	}


	// ALL THE LISTENERS
	protected class TyperMate extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			// Auto-correct only activates after a space, enter, or tab.
			if (e.getKeyCode() == KeyEvent.VK_SPACE ||
				e.getKeyCode() == KeyEvent.VK_ENTER ||
				e.getKeyCode() == KeyEvent.VK_TAB
				) {

				wordFinder = Pattern.compile(
					"([A-Za-z'\\-_]+)[^A-Za-z'\\-_]*$"
					// Only words consisting of letters, hyphens,
					// apostrophes, and underscores are looked up.
					).matcher(content.getText()).region(0, content.getCaretPosition());
				if (wordFinder.find()) {
					lastWord = wordFinder.group(1);
					lastWordStart = wordFinder.start(1);
					if (AutoCorrect.badWords.contains(lastWord.toLowerCase())) {
						content.select(
							lastWordStart,
							lastWordStart + lastWord.length()
							);
						attributes = new SimpleAttributeSet(
							content.getStyledDocument().
							getCharacterElement(content.getSelectionStart()).getAttributes()
							);
						content.insertIcon(
							new ImageIcon(((new ImageIcon("kitten.png")).
								getImage()).getScaledInstance(
								-1, StyleConstants.getFontSize(attributes), Image.SCALE_SMOOTH
								)));
						content.setCaretPosition(lastWordStart + 2);
					}
					else if (AutoCorrect.commonMisspellings.containsKey(lastWord)) {
						try {
							attributes = new SimpleAttributeSet(
								content.getStyledDocument().
								getCharacterElement(lastWordStart).getAttributes()
								);
							content.getStyledDocument().remove(lastWordStart, lastWord.length());
							content.getStyledDocument().insertString(
								lastWordStart,
								AutoCorrect.commonMisspellings.get(lastWord),
								attributes
								);
						}
						catch (BadLocationException e1) {}
					}
					wordFinder = Pattern.compile(
						"[\\.?!](  |\\n|\\t|\\f|\\r)([a-z])[A-Za-z'\\-_]*\\s$"
						).matcher(content.getText()).region(0, content.getCaretPosition());
					if (wordFinder.find()) {
						try {
							attributes = new SimpleAttributeSet(
								content.getStyledDocument().
								getCharacterElement(wordFinder.start(2)).getAttributes()
								);
							content.getStyledDocument().remove(wordFinder.start(2), 1);
							content.getStyledDocument().insertString(
								wordFinder.start(2),
								wordFinder.group(2).toUpperCase(),
								attributes
								);
						}
						catch (BadLocationException e1) {}
					}
					wordFinder = Pattern.compile(
						"([A-Za-z'\\-_]+)\\s+([A-Za-z'\\-_]+)[^A-Za-z'\\-_]*$"
						).matcher(content.getText()).region(0, content.getCaretPosition());
					if (wordFinder.find()) {
						preLastWord = wordFinder.group(1);
						preLastWordStart = wordFinder.start(1);
						if (AutoCorrect.badWords.contains(
							preLastWord.toLowerCase() + " " + lastWord.toLowerCase()
							)) {
							content.select(
								preLastWordStart,
								preLastWordStart + preLastWord.length() + 1 + lastWord.length()
								);
							attributes = new SimpleAttributeSet(
								content.getStyledDocument().
								getCharacterElement(content.getSelectionStart()).getAttributes()
								);
							content.insertIcon(
								new ImageIcon(((new ImageIcon("kitten.png")).
									getImage()).getScaledInstance(
									-1, StyleConstants.getFontSize(attributes), Image.SCALE_SMOOTH
									)));
							content.setCaretPosition(preLastWordStart + 2);
						}
						else if (
							preLastWord.equalsIgnoreCase(lastWord) &&
							AutoCorrect.repeatedWords.contains(lastWord)
							) {
							try {
								content.getStyledDocument().remove(lastWordStart, lastWord.length() + 1);
							}
							catch (BadLocationException e1) {}
						}
						else if (
							preLastWord.matches("[Aa]") &&
							((Arrays.binarySearch(AutoCorrect.vowels, lastWord.charAt(0)) > 0 &&
							!AutoCorrect.requireA.contains(lastWord)) ||
							AutoCorrect.requireAn.contains(lastWord))
							) {
							try {
								attributes = new SimpleAttributeSet(
									content.getStyledDocument().
									getCharacterElement(preLastWordStart).getAttributes()
									);
								content.getStyledDocument().insertString(
									preLastWordStart + 1,
									"n",
									attributes
									);
							}
							catch (BadLocationException e1) {}
						}
						else if (
							preLastWord.matches("[Aa]n") &&
							((Arrays.binarySearch(AutoCorrect.consonants, lastWord.charAt(0)) > 0 &&
							!AutoCorrect.requireAn.contains(lastWord)) ||
							AutoCorrect.requireA.contains(lastWord))
							) {
							try {
								content.getStyledDocument().remove(preLastWordStart + 1, 1);
							}
							catch (BadLocationException e1) {}
						}
					}
				}

			}

			wordFinder = Pattern.compile("\\S+").matcher(content.getText());
			wordCounter = 0;
			while (wordFinder.find())
				wordCounter++;
			wordCount.setText("  Words: " + wordCounter);

			if (!save.isEnabled() && !content.getText().equals(text)) {
				save.setEnabled(true);
				if (!title.equals("Untitled")) {
					title += " *";
					setTitle(title);
				}
			}

		}
	}
	protected class WannaSave extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			saveBeforeClose();
		}
		public void windowActivated(WindowEvent e) {
			if (opened != null && opened.lastModified() != lastModified) {
				if (JOptionPane.showConfirmDialog(
					content, "Warning: " + title + " has been modified outside " +
					"of DaEditor.\nReload " + title + " ?",
					"Reload", JOptionPane.YES_NO_OPTION
					) == JOptionPane.YES_OPTION) {
					lastModified = opened.lastModified();
					openFile(opened.getAbsolutePath());
				}
				else lastModified = opened.lastModified();
			}
		}
	}
	protected class WhereYouAt implements CaretListener {
		public void caretUpdate(CaretEvent e) {
			currentRow = content.getDocument().getDefaultRootElement().
			getElementIndex(content.getCaretPosition()) + 1;
			// 1 is added so that the rowCount starts from 1, not 0.
			currentColumn = content.getCaretPosition() -
			content.getDocument().getDefaultRootElement().
			getElement(currentRow - 1).getStartOffset() + 1;
			// 1 is added so that the columnCount starts from 1, not 0.
			rowCount.setText("  Row " + currentRow + ", ");
			columnCount.setText("Column " + currentColumn + "  ");
		}
	}
	protected class UndoRedo implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			undoSaver.addEdit(e.getEdit());
		}
	}

	public DaEditor() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (
			ClassNotFoundException|
			InstantiationException|
			IllegalAccessException|
			UnsupportedLookAndFeelException e
			) {};
		// The GUI looks like other programs running on the same OS.

		opener = new JFileChooser(System.getProperty("user.home"));
		// The opener goes to the user's home directory.

		setLocationByPlatform(true);
		// The window is positioned by the OS.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// When the last displayable window is disposed of, the JVM terminates.
		setIconImage(new ImageIcon("DaEditor.png").getImage());
		title = "Untitled";
		setTitle(title);
		text = "";

		int screenWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().getDisplayMode().getWidth();
		// This is the width of the screen in pixels.
		int screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().getDisplayMode().getHeight();
		// This is the height of the screen in pixels.
		content = new JTextPane();
		content.setFont(new Font("SansSerif", Font.PLAIN, screenHeight / 70));
		// For a 1280 by 800 monitor, the font size is 11.
		content.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
		// Newlines are represented by "\n", rather than "\r\n".
		contentWrapper = new JPanel(new BorderLayout());
		contentWrapper.add(content);
		// The wrapper is used so that the content won't have word-wrapping by default.
		world = new JScrollPane(contentWrapper);
		// The scrollbars only appear when there is text beyond the borders.
		world.setPreferredSize(new Dimension(screenWidth / 2, screenHeight / 3));
		// Content takes up a sixth of the screen.
		statusBar = new JPanel();
		statusBar.setPreferredSize(new Dimension(screenWidth / 2, screenHeight / 70));
		// Status bar displays only one line of text.
		statusBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		// Everything in the status bar is left-aligned, with no spaces in between.
		add(world, BorderLayout.CENTER);
		add(statusBar, BorderLayout.PAGE_END);
		// Status bar is at the bottom of the viewable area.

		rowCount = new JLabel("  Row 1, ");
		statusBar.add(rowCount);
		columnCount = new JLabel("Column 1  ");
		statusBar.add(columnCount);
		wordCount = new JLabel("  Words: 0");
		wordCount.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
		// There is a thin border between the row/column count and the word count.
		statusBar.add(wordCount);

		menu = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");
		format = new JMenu("Format");
		menu.add(file);
		menu.add(edit);
		menu.add(format);
		setJMenuBar(menu);

		newFile = new NewFile();
		file.add(newFile);
		open = new Open();
		file.add(open);
		save = new Save();
		file.add(save);
		saveAs = new SaveAs();
		file.add(saveAs);
		exit = new Exit();
		file.add(exit);

		undo = new Undo();
		edit.add(undo);
		redo = new Redo();
		edit.add(redo);
		copy = new Copy();
		edit.add(copy);
		copyWithFormatting = new CopyWithFormatting();
		edit.add(copyWithFormatting);
		cut = new Cut();
		edit.add(cut);
		cutWithFormatting = new CutWithFormatting();
		edit.add(cutWithFormatting);
		paste = new Paste();
		edit.add(paste);
		pasteWithFormatting = new PasteWithFormatting();
		edit.add(pasteWithFormatting);
		toUpperCase = new ToUpperCase();
		edit.add(toUpperCase);
		toLowerCase = new ToLowerCase();
		edit.add(toLowerCase);
		spellCheck = new SpellCheck();
		edit.add(spellCheck);

		bold = new Bold();
		format.add(bold);
		italic = new Italic();
		format.add(italic);
		underline = new Underline();
		format.add(underline);
		larger = new Larger();
		format.add(larger);
		smaller = new Smaller();
		format.add(smaller);
		black = new Black();
		format.add(black);
		red = new Red();
		format.add(red);
		blue = new Blue();
		format.add(blue);
		green = new Green();
		format.add(green);
		makeWhite = new White();
		format.add(makeWhite);
		toImage = new ToImage();
		format.add(toImage);

		save.setEnabled(false);
		// Since the document has not been modified yet, there is
		// nothing to be saved.

		addWindowListener(new WannaSave());
		// The WindowListener checks if there is any unsaved work.
		// It also checks to see if the file has been modified
		// while the window was inactive.
		content.addKeyListener(new TyperMate());
		// The KeyListener checks for basic grammar/spelling errors
		// and auto-corrects them.
		// It also checks if the user typed anything new
		// since the last save.
		// It also keeps track of the word count.
		undoSaver = new UndoManager();
		content.getDocument().addUndoableEditListener(new UndoRedo());
		// The UndoableEditListener stores all undoable and redoable edits.
		// (This includes edits performed by the KeyListener.)
		content.addCaretListener(new WhereYouAt());
		// The CaretListener keeps track of the row and column number.

		initializeSpellChecker();
		// Ah, the glory of JOrtho...

		pack();
		setVisible(true);

	}

	private void initializeSpellChecker() {
		SpellChecker.setUserDictionaryProvider(new FileUserDictionary("dictionary/"));
		SpellChecker.registerDictionaries(getClass().getResource("dictionary/"), "en");
		SpellChecker.register(content);
		SpellChecker.setApplicationName(
			System.getProperties().get("user.name").toString() +
			"'s Spelling Is Acceptable"
			);
		// If everything is correct, the applications says, "[Username]'s Spelling Is Acceptable"
		options = new SpellCheckerOptions();
		// The following options were found at
		// http://sourceforge.net/p/freeplane/freeplane/ci/5a97d44b17edb5f888168e35294ded7c28d4fb59/
		// tree/JOrtho_0.4_freeplane/src/com/inet/jortho/SpellCheckerOptions.java
		options.setCaseSensitive(true);
		// The spell checker is case-sensitive for the first letter of each word.
		options.setIgnoreAllCapsWords(true);
		// The spell checker ignores words in ALL CAPS.
		options.setIgnoreCapitalization(false);
		// A capitalized word is not correct if the word is in the dictionary as lower-case.
		// (This does not apply to the first word in a sentence.)
		options.setIgnoreWordsWithNumbers(true);
		// The spell checker ignores words with numb3rs.
		options.setSuggestionsLimitDialog(25);
		// The spell checker gives at most 25 suggestions in the dialog box.
		options.setSuggestionsLimitMenu(15);
		// The spell checker gives at most 15 suggestions in the pop-up menu.
		options.setLanguageDisableVisible(false);
		// The spell checker only supports the English language,
		// so there is no reason to display a language selector in the pop-up menu.
		content.addMouseListener(new PopupListener(SpellChecker.createCheckerPopup(options)));
	}

	private void saveFile(String fileName) {
		try {
			if (fileName.endsWith(".rtf")) {
				AdvancedRTFEditorKit kit = new AdvancedRTFEditorKit();
				kit.write(
					fileName,
					content.getDocument()
					);
			}
			else if (fileName.endsWith(".html")) {
				OutputStream writer = new BufferedOutputStream(new FileOutputStream(fileName));
				HTMLEditorKit kit = new HTMLEditorKit();
				kit.write(
					writer,
					content.getStyledDocument(),
					content.getStyledDocument().getStartPosition().getOffset(),
					content.getStyledDocument().getLength()
					);
				writer.close();

			}
			else {
				FileWriter writer = new FileWriter(fileName);
				content.write(writer);
				writer.close();
			}
			title = fileName;
			setTitle(title);
			text = content.getText();
			save.setEnabled(false);
			lastModified = opened.lastModified();
		}
		catch (IOException|BadLocationException e) {};
	}

	private void saveFileAs() {
		if (opener.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			opened = opener.getSelectedFile();
			saveFile(opened.getAbsolutePath());
		}
	}

	private void saveBeforeClose() {
		if (!content.getText().equals(text)) {
			if (title.endsWith(" *"))
				title = title.substring(0, title.length() - 2);
			if (JOptionPane.showConfirmDialog(
				this, "Save changes to " + title + "?",
				"Save", JOptionPane.YES_NO_OPTION
				) == JOptionPane.YES_OPTION)
				if (!title.equals("Untitled"))
					saveFile(title);
				else saveFileAs();
		}
	}

	private void openFile(String fileName) {
		try {
			if (fileName.endsWith(".rtf")) {
				FileInputStream reader = new FileInputStream(fileName);
				AdvancedRTFEditorKit kit = new AdvancedRTFEditorKit();
				content.setDocument(new AdvancedRTFDocument());
				kit.read(
					reader,
					content.getDocument(),
					content.getDocument().getLength()
					);
				reader.close();
			}
			else if (fileName.endsWith(".html")) {
				content.setContentType("text/html");
				FileReader reader = new FileReader(fileName);
				content.read(reader,null);
				reader.close();
			}
			else {
				FileReader reader = new FileReader(fileName);
				content.read(reader,null);
				reader.close();
			}
			title = fileName;
			setTitle(title);
			opened = new File(fileName);
			lastModified = opened.lastModified();
			text = content.getText();
			save.setEnabled(false);
			content.setCaretPosition(content.getDocument().getLength());
			// The caret is put at the end of the opened file.
		}
		catch(IOException|BadLocationException e) {
			JOptionPane.showMessageDialog(
				this, "DaEditor can't find the file " + fileName
				);
		}
	}

	public static void main(String[] args) {
		new DaEditor();  //READY FOR TAKEOFF \_(>_<)_/
	}

}