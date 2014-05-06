/**
 * Copyright 2010 http://learning-adventure.fr
 * Tous droits réservés
 * 
 * 
 * ----------------------------------------------------------------------------
 * Ce fichier fait partie de LA-Client.
 *
 * LA-Client est un logiciel libre ; vous pouvez le redistribuer ou le modifier 
 * suivant les termes de la GNU General Public License telle que publiée par
 * la Free Software Foundation ; soit la version 3 de la licence, soit 
 * (à votre gré) toute version ultérieure.
 * 
 * LA-Client est distribué dans l'espoir qu'il sera utile, 
 * mais SANS AUCUNE GARANTIE ; pas même la garantie implicite de 
 * COMMERCIABILISABILITÉ ni d'ADÉQUATION à UN OBJECTIF PARTICULIER. 
 * Consultez la GNU General Public License pour plus de détails.
 * 
 * Vous devez avoir reçu une copie de la GNU General Public License 
 * en même temps que LA-Client ; si ce n'est pas le cas, 
 * consultez <http://www.gnu.org/licenses>.
 * ----------------------------------------------------------------------------
 * This file is part of LA-Client.
 *
 * LA-Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LA-Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LA-Client.  If not, see <http://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------------
 */
package client.hud.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import client.editor.annotation.AnnotationEditor;
import client.script.ScriptMethods;
import client.script.ScriptableMethod;
import client.utils.FileLoader;

/**
 * Fenetre d'edition de Script
 * <ul>
 * <li>Le menu contient les mot-clef et les methodes</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class ScriptEditWindow extends JFrame implements Runnable {
	private static final long serialVersionUID = 990163767580049982L;
	private static final Logger logger = Logger.getLogger("TextEditWindow");

	private static final String KEY_WORK_REGEX = "(player)|(gr[^\\.]*)|" +
			"(table\\d+)|(object\\d+)|(item\\d+)|(npc\\d+)|(building\\d+)|(dialog\\d+)|" +
			"(hud)|(lgf\\d+)|(sound)|(slide\\d+)|(script\\d+)|(world)|" +
			"(tool\\d+)|(source)";
	private static final String LANG_REGEX = "(if)|(else)|(for)|(while)|" +
			"(null)|(void)|(\\{)|(\\})|(true)|(false)|(switch)|(case)|" +
			"(break)|(new)|(\\(|\\))";
	private static final String STRING_REGEX = "\"[^\"]*\"";
	private static final String COMMENT_REGEX = "(//[^\\n]*\\n)|(/\\*.*\\*/)";
	

	private JPanel mainPanel;
	private JTextPane textEditor;
	private JPanel southPnal;
	private JButton cancel;
	private JButton apply;
	private Field field;
	private DefaultStyledDocument scriptDocument;
	private boolean isColoring = false;
	private Style methodStyle;
	private Style keywordStyle;
	private JScrollPane jScrollPane = null;
	private JMenuBar mainMenu;
	private Style langStyle, stringStyle;
	private AnnotationEditor editor;
	private Style errorStyle;
	private Style commentStyle;

	/**
	 * permet l'edition sous forme de text du champ field dans le model name
	 * 
	 * @param editor
	 * @param f
	 */
	public ScriptEditWindow(AnnotationEditor editor, Field f) {
		this.editor = editor;
		this.field = f;
		initialize();

	}

	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		logger.info("init script editor");
		setTitle("LA3 ScriptEditor : ");
		try {
			setIconImage(ImageIO.read(FileLoader.getResourceAsUrl(
					LaConstants.DIR_LGPL_ICON + "edit.png")));
		} catch (IOException e) {
			logger.warning("IOException : Je le savais !");
		}
		setContentPane(getMainPanel());
		setJMenuBar(getMainMenu());
		setSize(1000, 700);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		logger.info("fin init script editor");
		setVisible(true);
	}

	/**
	 * Construit le menu
	 * 
	 * @return
	 */
	private JMenuBar getMainMenu() {
		if (mainMenu == null) {
			mainMenu = new JMenuBar();

			JMenu m = new JMenu(ScriptConstants.PLAYER_KEYWORD);
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.PLAYER_KEYWORD);

			m = new JMenu(ScriptConstants.GROUP_REGEX.replaceAll("\\(|\\)", ""));
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.GROUP_REGEX);
			
			m = new JMenu(ScriptConstants.HUD_KEYWORD);
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.HUD_KEYWORD);

			m = new JMenu(ScriptConstants.SOUND_KEYWORD);
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.SOUND_KEYWORD);

			m = new JMenu(ScriptConstants.OBJECT_REGEX.replaceAll("\\(|\\)", ""));
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.OBJECT_REGEX);

			m = new JMenu(ScriptConstants.NPC_REGEX.replaceAll("\\(|\\)", ""));
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.NPC_REGEX);

			m = new JMenu(ScriptConstants.TOOL_REGEX.replaceAll("\\(|\\)", ""));
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.TOOL_REGEX);

			m = new JMenu(ScriptConstants.LGF_REGEX.replaceAll("\\(|\\)", ""));
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.LGF_REGEX);

			m = new JMenu(ScriptConstants.SLIDE_REGEX.replaceAll("\\(|\\)", ""));
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.SLIDE_REGEX);

			m = new JMenu(ScriptConstants.SCRIPT_REGEX.replaceAll("\\(|\\)", ""));
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.SCRIPT_REGEX);
			
			m = new JMenu(ScriptConstants.WORLD_KEYWORD);
			mainMenu.add(m);
			addMethodeDescription(m,ScriptConstants.WORLD_KEYWORD);
		}
		return mainMenu;
	}

	/**
	 * ajout un menu avec la liste des methode relative à un mot clef de script
	 * @param player
	 * @param currentPlayerKeyword
	 */
	private void addMethodeDescription(JMenu menu, String keyWord) {
		for (Method method : ScriptMethods.getInstance().getMethods(keyWord)) {
			final String name = method.getName();
			String desc = name +"("; 
			for (Class<?> c : method.getParameterTypes()) 
				desc += c.getSimpleName()+",";
			desc = desc.replaceAll(",$", "")+")";
			//method.getAnnotation(ScriptableMethod.class).clientSide()?desc+= "client";

			JMenuItem item = new JMenuItem(desc);
			desc = "<html>"+method.getAnnotation(ScriptableMethod.class).description();
			desc.replaceAll("\n", "<br>");
			desc += "<br>client : " +method.getAnnotation(ScriptableMethod.class).clientSide();
			desc += "<br>server : " +method.getAnnotation(ScriptableMethod.class).serverSide();
			desc+="</html>";
			item.setToolTipText(desc);

			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int pos = getTextEditor().getCaretPosition();
					try {
						getScriptDocument().insertString(pos, name,	methodStyle);
					} catch (BadLocationException e1) {
						logger
						.warning("BadLocationException : Je le savais !");
					}
				}
			});
			menu.add(item);
		}
	}

	/**
	 * renvoie le panneau de controel principal
	 * 
	 * @return
	 */
	private Container getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel(new BorderLayout());
			mainPanel.add(getJScrollPane(), BorderLayout.CENTER);
			mainPanel.add(getButtonsPanel(), BorderLayout.SOUTH);
		}
		return mainPanel;
	}

	/**
	 * Renvoie le panneau posseddant les 2 bouton de controle
	 * 
	 * @return
	 */
	private Component getButtonsPanel() {
		if (southPnal == null) {
			southPnal = new JPanel(new GridLayout(0, 2));
			southPnal.add(getCancelButton());
			southPnal.add(getApplyButton());
		}
		return southPnal;
	}

	/**
	 * @return
	 */
	private Component getApplyButton() {
		if (apply == null) {
			apply = new JButton("Apply");
			apply.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.setStr(field, getTextEditor().getText());
					setVisible(false);
					dispose();
				}
			});
		}
		return apply;
	}

	/**
	 * @return
	 */
	private Component getCancelButton() {
		if (cancel == null) {
			cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					dispose();
				}
			});
		}
		return cancel;
	}

	/**
	 * @return
	 */
	private JTextPane getTextEditor() {
		if (textEditor == null) {
			textEditor = new JTextPane(getScriptDocument());
			textEditor.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					doColoringLater();
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
		}
		return textEditor;
	}

	/**
	 * @return
	 */
	private StyledDocument getScriptDocument() {
		if (scriptDocument == null) {
			logger.info("construction du document");
			scriptDocument = new DefaultStyledDocument();
			/*try {
				scriptDocument.insertString(0, (String) model.getValue(field),
						null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}//*/
			loadStyle();
		}
		return scriptDocument;
	}

	/**
	 * Charge les styles
	 */
	private void loadStyle() {
		methodStyle = getTextEditor().addStyle("method", null);
		StyleConstants.setBold(methodStyle, false);
		StyleConstants.setItalic(methodStyle, true);
		StyleConstants.setForeground(methodStyle, new Color(.2f, .2f, .2f));

		keywordStyle = getTextEditor().addStyle("keyword", null);
		StyleConstants.setBold(keywordStyle, true);
		StyleConstants.setItalic(keywordStyle, false);
		StyleConstants.setForeground(keywordStyle, new Color(.6f, .2f, .2f));

		langStyle = getTextEditor().addStyle("lang", null);
		StyleConstants.setBold(langStyle, true);
		StyleConstants.setItalic(langStyle, false);
		StyleConstants.setForeground(langStyle, new Color(.2f, .6f, .2f));

		stringStyle = getTextEditor().addStyle("string", null);
		StyleConstants.setBold(stringStyle, false);
		StyleConstants.setItalic(stringStyle, false);
		StyleConstants.setForeground(stringStyle, new Color(.2f, .2f, .7f));

		errorStyle = getTextEditor().addStyle("error", null);
		StyleConstants.setBold(errorStyle, false);
		StyleConstants.setItalic(errorStyle, false);
		StyleConstants.setForeground(errorStyle, new Color(.8f, 0f, 0f));

		commentStyle = getTextEditor().addStyle("comment", null);
		StyleConstants.setBold(commentStyle, false);
		StyleConstants.setItalic(commentStyle, true);
		StyleConstants.setForeground(commentStyle, new Color(.4f, .4f, .4f));
		
	}

	/**
	 * invoque la methode de dessin plus tard
	 */
	private void doColoringLater() {
		if (!isColoring) {
			// SwingUtilities.invokeLater(this);
			run();
		}
	}

	/**
	 * @inheritDoc
	 */
	public void run() {
		isColoring = true;
		try {
			doColoring();
		} finally {
			logger.fine("coloring à false");
			isColoring = false;
		}
	}

	/**
	 * colorise le text..
	 */
	private void doColoring() {
		logger.fine("start coloring");
		StyledDocument document = getScriptDocument();
		logger.fine("remplacement des tab");
		//document.getText(0, document.getLength()).replaceAll("\t", "  ")
		
		Style defaultStyle = document.getStyle("error");
		logger.fine("application default style");
		document.setCharacterAttributes(getScriptDocument().getStartPosition()
				.getOffset(), getScriptDocument().getLength(), defaultStyle,
				true);
		try {
			logger.fine("recup du text");
			String text = document.getText(0, document.getLength());

			// coloring des methodes
			for (String key:ScriptConstants.KEYWORDS)
				color(key,text,document);
			for (String key:ScriptConstants.REGEXS)
				color(key,text,document);

			colorPattern(text,KEY_WORK_REGEX,keywordStyle);
			colorPattern(text,LANG_REGEX, langStyle);
			colorPattern(text,STRING_REGEX, stringStyle);
			colorPattern(text,COMMENT_REGEX, commentStyle);
			
		} catch (BadLocationException e) {
			logger.warning("BadLocationException : Je le savais !");
		} catch (Exception e) {
			logger.warning(e.getMessage()+ " dans le coloring qui fait chier !");
		}

		logger.fine("end coloring");
	}

	/**
	 * recherche et coloring d'un pattern
	 * @param text
	 * @param keyWorkRegex
	 * @param keywordStyle2
	 */
	private void colorPattern(String text, String regex, Style style) {
		Matcher m = Pattern.compile(regex).matcher(text);
		while (m.find()) {
			logger.fine("trouver mot clef " + m.group());
			getScriptDocument().setCharacterAttributes(m.start(),
					m.end() - m.start(), style, true);
		}
	}

	/**
	 * coloring des method
	 * @param regex
	 * @param text 
	 * @param document 
	 */
	private void color(String regex, String text, StyledDocument document) {
		for (String method : ScriptMethods.getInstance().getMethodNames(regex)) {
			logger.fine("recherche de la methode " + method);
			Matcher m = Pattern.compile(method).matcher(text);
			while (m.find()) {
				logger.fine("trouver methode " + method);
				document.setCharacterAttributes(m.start(), m.end()
						- m.start(), methodStyle, true);
			}
		}
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTextEditor());
		}
		return jScrollPane;
	}


	/* (non-Javadoc)
	 * @see java.awt.Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			try {
				getScriptDocument().insertString(0, editor.getString(field),null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}//*/
		}
	}
}
