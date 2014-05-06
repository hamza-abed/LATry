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

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.map.tool.poll.MultiIntValueQuestion;
import client.map.tool.poll.OpenQuestion;
import client.map.tool.poll.Poll;
import client.map.tool.poll.PollQuestion;
import client.map.tool.poll.QCM;
import client.utils.FileLoader;

//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BCheckBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.FocusEvent;
import com.jmex.bui.event.FocusListener;
import com.jmex.bui.event.SelectionListener;
import com.jmex.bui.event.StateChangedEvent;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * Fenetre d'ouverture d'un sondage.
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PollWindow extends BWindow implements Callable<Void> {
	private Hud hud;
	private String url;
	private BContainer panel;
	private BLabel title;
	private Poll poll;

	private Counter counter = new Counter(0);
	private int point = 0;
	private String endscript;
	
	public PollWindow(Hud hud) {
		super("Poll Window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/poll.bss")), new BorderLayout(
						Hud.SPACE, Hud.SPACE));
		this.hud = hud;	
		//		this.url = "http://manouchian.univ-savoie.fr/~la3/exemple-qcm.xml";

		initialize();
	}

	/* ********************************************************** *
	 * *				Initialise le contenu 					* *
	 * ********************************************************** */

	/**
	 * 
	 */
	private void initialize() {
		title = new BLabel(hud.getLocalText("pool.default.title"));
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);

		TableLayout layout = new TableLayout(1, Hud.SPACE, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);

		panel = new BContainer(layout);
		panel.setStyleClass("container-in-scroll-pane");

		//refreshMiddle();

		BScrollPane scrollPane = new BScrollPane(panel);
		scrollPane.setStyleClass("container-middle");
		this.add(scrollPane, BorderLayout.CENTER);

		BuiSystem.addWindow(this);
		this.pack();
		this.center();
	}

	/**
	 * rafraichi le contenu du sondage
	 */
	private void refreshMiddle() {
		poll = new Poll(url);

		panel.removeAll();
		title.setText(poll.getTitle());

		for (PollQuestion question : poll.getQuestions()) 
			if (question instanceof OpenQuestion)
				initOpen((OpenQuestion)question);
			else if (question instanceof QCM)
				initQCM((QCM)question);
			else if (question instanceof MultiIntValueQuestion)
				initMultiIntValue((MultiIntValueQuestion)question);

		LaBButton ok = new LaBButton(hud.getLocalText("pool.default.button"), new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (submit()) {
					setVisible(false);
					counter.raz();
				}
				else
					new PopupWindow(hud.getLocalText("popup.poll.belbin.error"), "Ok");
			}
		}, "ok");
		panel.add(ok);
	}

	/**
	 * ouvre une sondage depuis un HTTP
	 * @param url
	 * @param script
	 */
	public void openHttp(String url, String script) {
		this.url = url;
		this.endscript = script;
                System.out.println("PollWindow-> openHttp() : GameTaskQueueManager !!!");
		//GameTaskQueueManager.getManager().update(this);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		refreshMiddle();
		setVisible(true);
		return null;
	}

	/**
	 * initialise une question ouverte
	 * @param question
	 */
	private void initOpen(final OpenQuestion question) {
		panel.add(new BLabel(question.getQuestion()));
		final BTextField text = new BTextField();
		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent event) {
				question.setAnswer(text.getText());
			}
			@Override
			public void focusGained(FocusEvent event) {}
		});
		panel.add(text);
	}

	/**
	 * Initialise un questionnaire à choix multiple
	 * @param question
	 */
	private void initQCM(final QCM question) {
		panel.add(new BLabel(question.getQuestion()));
		int i = 0;
		for (String choice : question.getChoices()) {
			final int indice = i;
			final BCheckBox checkBox = new BCheckBox(choice);
			checkBox.addSelectionListener(new SelectionListener() {
				@Override
				public void stateChanged(StateChangedEvent event) {
					question.setAnswer(indice, checkBox.isSelected());
				}
			});
			panel.add(checkBox);
			i++;
		}
	}

	/**
	 * Initialise un questionnaire à choix multiple de questions ouvertes
	 * @param question
	 */
	private void initMultiIntValue(final MultiIntValueQuestion question) {
		BLabel  labelQuestion = new BLabel(question.getQuestion());
		labelQuestion.setStyleClass("label-question");
		panel.add(labelQuestion);
		int i = 0;
		final Counter counterPoint = new Counter(question.getMaxPoint());
		final Counter counterSelect = new Counter(question.getMaxSelectedAnswer());

		TableLayout layout = new TableLayout(3, Hud.SPACE, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.LEFT);
		BContainer panelMulti = new BContainer(layout);

		point = poll.getBelbinNbQuestion() * question.getMaxPoint();
		
		for (String choice : question.getChoices()) {
			final int indice = i;
			final BLabel label = new BLabel(choice);
			label.setFit(Fit.SCALE);
			label.setStyleClass("label-answer");
			panelMulti.add(label);

			final BLabel point = new BLabel("0");
			label.setFit(Fit.SCALE);

			LaBButton plus = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "add-10.png")),
					new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					int res = Integer.parseInt(point.getText());
					if (counterSelect.get() != 0) {
						if (res == 0 && counterPoint.get() != 0) 
							counterSelect.dec();
						if (res != question.getMaxPoint() && counterPoint.get() != 0) {
							res++;
							counterPoint.dec();
							counter.inc();
						}
					} else {
						if (res != 0 && res != question.getMaxPoint() 
								&& counterPoint.get() != 0) {
							res++;
							counterPoint.dec();
							counter.inc();
						}
					}
					point.setText(String.valueOf(res));
					question.setAnswers(indice, point.getText());
				}
			}, "add");
			plus.setStyleClass("button-icon");

			LaBButton minus = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "minus-10.png")),
					new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					int res = Integer.parseInt(point.getText());
					if (res != 0) {
						res--;
						counterPoint.inc();
						counter.dec();
						if (res == 0)
							counterSelect.inc();
					}
					point.setText(String.valueOf(res));
					question.setAnswers(indice, point.getText());
				}
			}, "minus");
			minus.setStyleClass("button-icon");

			BContainer panelButton = new BContainer(new TableLayout(1, 0, 0));
			panelButton.add(plus);
			panelButton.add(minus);
			panelMulti.add(panelButton);
			panelMulti.add(point);

			i++;
		}
		panel.add(panelMulti);
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	/**
	 * 
	 */
	private boolean submit() {
		if (poll.getBelbin().isEmpty()) {
			Variables.getWorld().getFtp().sendPollAnswer(poll);
			if (endscript!=null)
				Variables.getWorld().getScriptExecutor().execute(endscript);
			return true;
		} else {
			Logger.getLogger(this.getClass().getName()).info("Nb point : "+point);
			if (counter.get() == point) {
				Variables.getWorld().getFtp().sendPollAnswer(poll);
				if (endscript!=null)
					Variables.getWorld().getScriptExecutor().execute(endscript);
				return true;
			}
		}
		return false;
	}

	private class Counter {
		int value = 0;
		public Counter(int maxPoint) {
			value = maxPoint;
		}
		public void inc() {
			value++;
		}
		public void dec() {
			value--;
		}
		public int get() {
			return value;
		}
		public void raz() {
			value = 0;
		}
	}

}
