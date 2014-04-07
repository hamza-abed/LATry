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
package client.map.data;

import java.nio.ByteBuffer;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.map.World;
import client.script.ScriptableMethod;

/**
 * suite d'image ouvert en HUD et du son et du scripting 
 * <ul>
 * <li>chaque slide est un image stocker à l'url de base sous la forme slide-x.jpg x allant de 0 à count-1</li>
 * <li>idem pour les son sous la forme de sound-x.ogg</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class SlideShow implements SharableReflexEditable {
	/**
	 * url d'acces aux slide
	 */
	@Editable(type=FieldEditType.string)
	private String baseUrl = LaConstants.DEFAULT_SLIDES_BASE_URL;
	
	/**
	 * nombre de slide
	 */
	@Editable(type=FieldEditType.integer)
	private int count = 5;
	
	/**
	 * identifiant
	 */
	private int id;

	/**
	 * code version du shared
	 */
	private int versionCode = -1;

	private World world;

	@Editable(type=FieldEditType.action)
	private String onEnd = ScriptConstants.VOID_SCRIPT;
	
	@Editable(type=FieldEditType.string)
	private String title = LaConstants.UNSET_STRING; 

	@Editable(type=FieldEditType.integerinterval,intMinValue=0,intMaxValue=100)
	private int w = 100, h = 100;

	@Editable(type=FieldEditType.integerinterval,intMinValue=0,intMaxValue=100)
	private int x = 50, y = 50;
	
	@Editable(type=FieldEditType.bool)
	private boolean displayButtons; 

	/**
	 * @param i 
	 * @param world 
	 * 
	 */
	public SlideShow(World world, int id) {
		this.id = id;
		this.world = world;
	}
	
	/**
	 * 
	 */
	public void end() {
		world.getScriptExecutor().execute(onEnd);
	}

	/* ********************************************************** *
	 * *				SHARABLE - IMPLEMENTS					* *
	 * ********************************************************** */
	
	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(baseUrl);
		pck.putInt(count);
		pck.putString(title,onEnd);
		pck.putInt(x,y,w,h);
		pck.putBoolean(displayButtons);
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.slides.prefix()+id;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode ;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		this.versionCode = message.getInt(); 
		this.baseUrl = Pck.readString(message);
		this.count = message.getInt();
		this.title = Pck.readString(message);
		this.onEnd = Pck.readString(message);
		this.x = message.getInt();
		this.y = message.getInt();
		this.w = message.getInt();
		this.h = message.getInt();
		this.displayButtons = Pck.readBoolean(message);
	}

	/* ********************************************************** *
	 * *					OUVERTURE DE SLIDE					* *
	 * ********************************************************** */

	/**
	 * ouvre le slideshow
	 */
	@ScriptableMethod(description="ouvre le slide show")
	public void open() {
		//world.getGame().getHud().openSlideShow(this,0);
	}
	
	@ScriptableMethod(description="affiche la page correspondant du slide")
	public void display(int page) {
		//world.getGame().getHud().openSlideShow(this,page);
	}
	
	@ScriptableMethod(description="ferme le slide")
	public void close(int page) {
		//world.getGame().getHud().closeSlideShow(this);
	}
	
	/* ********************************************************** *
	 * *					GETTERS / SETTERS					* *
	 * ********************************************************** */
	/**
	 * indique le nombre d'image présente dans le slide
	 * @return
	 */
	public int getSlideCount() {
		return count;
	}
	
	/**
	 * Renvoie l'url d'acces à la slide
	 * @param index
	 * @return
	 */
	public String getSlideUrl(int index) {
		return baseUrl+"/slide-"+index+".jpg";
	}
	
	/**
	 * Renvoie l'url d'acces au son d'une slide
	 * @param index
	 * @return
	 */
	public String getSlideSound(int index) {
		return baseUrl+"/sound-"+index+".ogg";
		//return "sound-"+index+".ogg";
	}

	/**
	 * @return
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the onEnd
	 */
	public String getOnEnd() {
		return onEnd;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the w
	 */
	public int getW() {
		return w;
	}

	/**
	 * @return the h
	 */
	public int getH() {
		return h;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the displayButtons
	 */
	public boolean isDisplayButtons() {
		return displayButtons;
	}



	
}
