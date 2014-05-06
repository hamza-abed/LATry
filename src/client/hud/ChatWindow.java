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
package client.hud;

import java.util.logging.Logger;

import shared.enums.ChatChannel;
import client.input.MouseCursor;
import client.input.MouseCursor.CursorType;
import client.utils.FileLoader;
import com.jme3.math.ColorRGBA;

//import com.jme.renderer.ColorRGBA;
import com.jmex.bui.BComboBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BScrollBar;
import com.jmex.bui.BTabbedPane;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.event.MouseListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.Justification;
import shared.variables.Variables;

/**
 * Fenetre de chat
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class ChatWindow extends BWindow implements MouseListener {
	private static final Logger logger = Logger.getLogger("ChatWindow");

	private static final int LOCAL = 0;
	private static final int GROUP = 1;
	private static final int GUILD = 2;
	private static final int WORLD = 3;
	private static final int DEBUG = 4;
	
	private static final ColorRGBA LOCAL_COLOR = ColorRGBA.White;
	private static final ColorRGBA GROUP_COLOR = new ColorRGBA(.5f, .5f, 1f, 1f);
	private static final ColorRGBA GUILD_COLOR = ColorRGBA.Green;
	private static final ColorRGBA WORLD_COLOR = new ColorRGBA(.7f, .7f, .7f, 1f);
	private static final ColorRGBA SYSTEM_COLOR = ColorRGBA.Yellow;
	private static final ColorRGBA DEBUG_COLOR = ColorRGBA.White;
	
	private static final int[] LOCAL_OUT = { LOCAL, GROUP, GUILD, WORLD };
	private static final int[] GROUP_OUT = { LOCAL, GROUP, GUILD, WORLD };
	private static final int[] GUILD_OUT = { LOCAL, GROUP, GUILD, WORLD };
	private static final int[] WORLD_OUT = { LOCAL, GROUP, GUILD, WORLD };
	private static final int[] SYSTEM_OUT = { LOCAL, GROUP, GUILD, WORLD };
	private static final int[] DEBUG_OUT = { DEBUG };
	
	private static final ChatChannel talks[] = { ChatChannel.local, ChatChannel.group, ChatChannel.guild, ChatChannel.world };
	
	/**
	 * l'interface conteneur
	 */
	private Hud hud;

	/**
	 * liste des zone de text
	 */
	private BTextArea[] textArea = new BTextArea[5];
	
	/**
	 * champ d'entrée de chat
	 */
	private BTextField input;

	/* ********************************************************** *
	 * * 				INITIALISATION 							* *
	 * ********************************************************** */
	/**
	 * Construit la fenetre de demande d'identification
	 * 
	 * @param hud
	 */
	public ChatWindow(Hud hud) {
		super("chat window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/chat.bss")), 
				new BorderLayout(Hud.SPACE,Hud.SPACE));
		this.hud = hud;
		initialize();
	}

	/**
	 * Initialise le cpntenu de la fenetre
	 */
	private void initialize() {
		logger.entering(ChatWindow.class.getName(), "initialize");

		// initialisation de la partie par omglet
		BTabbedPane tabbedPane = new BTabbedPane(Justification.LEFT, 5);
		this.add(tabbedPane, BorderLayout.CENTER);

		final BComboBox channel = new BComboBox(talks);

		
		// initialisation des panel de chat
		BScrollBar[] scrollBar = new BScrollBar[textArea.length];
		for (int i = 0; i < textArea.length; i++) {
			final int indice = i;
			textArea[i] = new BTextArea("");
			//textArea[i].appendText(hud.getLocalText("chat.tab.initial")+"\n",SYSTEM_COLOR);
                        textArea[i].appendText(hud.getLocalText("chat.tab.initial")+"\n");

			scrollBar[i] = new BScrollBar(Orientation.VERTICAL, textArea[i]
					.getScrollModel());
			// scrollBar.setStyleClass();

			BContainer tab = new BContainer(new BorderLayout(Hud.SPACE,0));
			tab.add(textArea[i], BorderLayout.CENTER);
			tab.add(scrollBar[i], BorderLayout.EAST);
			tabbedPane.addTab(hud.getLocalText("chat.tab" + i + ".title"), tab);
			
			tabbedPane.getTabButton(tab).addListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					if (indice<4)
						channel.selectValue(talks[indice]);
				}
			});
			tabbedPane.getTabButton(tab).addListener(this);
		}
		tabbedPane.selectTab(3);
		
		BContainer bottom = new BContainer(new BorderLayout(Hud.SPACE, 0));
		this.add(bottom, BorderLayout.SOUTH);
			
		channel.setOrientation(Orientation.VERTICAL);
		//channel.setText(ChatChannel.local.toString());
		channel.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Variables.getChatSystem().setChannel(
						(ChatChannel)channel.getSelectedValue());
				channel.selectValue(Variables.getChatSystem().getChannel());
			}
		});
		channel.selectValue(Variables.getChatSystem().getChannel());
		bottom.add(channel, BorderLayout.WEST);

		input = new BTextField(hud.getLocalText("chat.init.default"));
		input.addListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (input.getText().length() > 0)
					Variables.getChatSystem().input(input.getText());
				input.setText("");
			}
		});
		input.addListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent event) {}

			@Override
			public void mousePressed(MouseEvent event) {}

			@Override
			public void mouseExited(MouseEvent event) {	}

			@Override
			public void mouseEntered(MouseEvent event) {
				input.setText("");
				input.removeListener(this);
			}
		});
		bottom.add(input, BorderLayout.CENTER);

		this.setPreferredSize(400, 230);
		this.pack();
		this.setLocation(1, 1);
		BuiSystem.addWindow(this);

		logger.exiting(ChatWindow.class.getName(), "initialize");
	}

	/* ********************************************************** *
	 * * 					AJOUT DE TEXT 						* *
	 * ********************************************************** */

	/**
	 * @return the input
	 */
	public BTextField getInput() {
		return input;
	}

	/**
	 * Ajout du text en debug
	 * 
	 * @param text
	 */
	public void debug(String text) {
		for (int c : DEBUG_OUT) {
			if (textArea[c].getLineCount()>500) 
				textArea[c].setText("clear\n");
			//textArea[c].appendText(format(text),DEBUG_COLOR);
                        textArea[c].appendText(format(text));
		}
	}

	/**
	 * affiche un message d'un joueur dans le monde
	 * @param text
	 */
	public void world(String text) {
		text = format(text);
		for (int c : WORLD_OUT)
			//textArea[c].appendText(text, WORLD_COLOR);
                    textArea[c].appendText(text);
		
	}

	/**
	 * affiche un message d'un joueur dans le monde
	 * @param text
	 */
	public void local(String text) {
		text = format(text);
		for (int c : LOCAL_OUT) 
			//textArea[c].appendText(text, LOCAL_COLOR);
                    textArea[c].appendText(text);
			
	}

	/**
	 * Affiche un text en system
	 * @param text
	 */
	public void system(String text) {
		text = format(text);
		for (int c : SYSTEM_OUT)
			//textArea[c].appendText(text, SYSTEM_COLOR);
              textArea[c].appendText(text);
                    }
	
	/**
	 * Affiche un text en groupe
	 * @param text
	 */
	public void group(String text) {
		text = format(text);
		for (int c : GROUP_OUT)
			//textArea[c].appendText(text, GROUP_COLOR);
                    textArea[c].appendText(text);
	}
	
	/**
	 * Affiche un text en guilde
	 * @param text
	 */
	public void guild(String text) {
		text = format(text);
		for (int c : GUILD_OUT)
			//textArea[c].appendText(text, GUILD_COLOR);
                    textArea[c].appendText(text);
	}
	
	/**
	 * Format le text
	 * 
	 * @param text
	 * @return
	 */
	private String format(String text) {
		if (!text.endsWith("\n"))
			text += "\n";
		return text.replaceAll("\n\n", "\n \n");
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mouseEntered(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent event) {
		MouseCursor.get().switchCursor(CursorType.object);
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mouseExited(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent event) {
		MouseCursor.get().switchCursor(CursorType.base);
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mousePressed(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent event) {}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mouseReleased(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent event) {}



}
