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
package client.map.character;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.enums.CharacterModel;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.map.World;
import client.map.character.stats.Item;
import client.map.character.stats.PlayerItems;
import client.map.character.stats.PlayerSkills;
import client.map.character.stats.PlayerTargets;
import client.map.character.stats.PlayerTasks;
import client.map.character.stats.PlayerTokens;
import client.map.character.stats.Task;
import client.map.character.stats.UserModel;
//import client.map.object.BasicMapObject;
//import client.map.object.MapTable;
import client.script.ScriptableMethod;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import shared.variables.Variables;
//import com.jme.renderer.ColorRGBA;

/**
 * Class regroupant les fonctionnalité relative au joueurs quelque soit
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public abstract class PlayableCharacter extends AbstractCharacter {
	private static final Logger logger = Logger.getLogger("PlayableCharacter");

	protected String login;

	/**
	 * list des groups du joueur
	 */
	private String groupStrings;

	/**
	 * list des groupe du joueur
	 */
	protected ArrayList<Group> groups = new ArrayList<Group>();

	/**
	 * Liste des token du joueur
	 */
	@Editable(type=FieldEditType.editable)
	protected PlayerTokens tokens;

	/**
	 * Sac du joueur
	 */
	@Editable(type=FieldEditType.editable)
	private PlayerItems items;

	/**
	 * les taches du joueur
	 */
	@Editable(type=FieldEditType.editable)
	private PlayerTasks tasks;

	/**
	 * les cible du joueurs
	 */
	@Editable(type=FieldEditType.editable)
	private PlayerTargets targets;

	/**
	 * indique si le joueur à le droit d'administrateur
	 */
	@Editable(type=FieldEditType.bool)
	private boolean admin;

	/**
	 * les cible du joueurs
	 */
	@Editable(type=FieldEditType.editable)
	private PlayerSkills skills;

	private UserModel um;

	private boolean connected;



	/**
	 * 
	 */
	// private HashMap<String,PlayerGroup> groups =

	/**
	 * @param world
	 * @param login
	 */
	public PlayableCharacter(World world, String login) {
		super(world);
		this.login = login;
		//world.getGame().
               Variables.getClientConnecteur().updateFromServer(this);
		this.tokens = new PlayerTokens(this);
		this.items = new PlayerItems(this);
		this.tasks = new PlayerTasks(this);
		this.targets = new PlayerTargets(this);
		this.skills = new PlayerSkills(this);
		this.um = new UserModel(this);
		//world.getGame().
                Variables.getClientConnecteur().updateFromServer(this.tokens);
		//world.getGame().
                Variables.getClientConnecteur().updateFromServer(this.items);
		//world.getGame().
                Variables.getClientConnecteur().updateFromServer(this.tasks);
		//world.getGame().
                Variables.getClientConnecteur().updateFromServer(this.targets);
		//world.getGame().
                Variables.getClientConnecteur().updateFromServer(this.skills);
	}

	/* ********************************************************** *
	 * * 					Gestion des groups 					* *
	 * ********************************************************** */

	/**
	 * 
	 */
	protected void updateGroups() {
		this.groups.clear();
		for (String id : this.groupStrings.split(",")) {
			Group gr = world.getGroupBuildIfAbsent(id);
			this.groups.add(gr);
		}
		logger.fine(groupStrings+"\n"+groups);
	}

	/**
	 * marque que le joueur est dans le groupe. 
	 * @param group
	 */
	void join(Group group) {
		this.groups.add(group);
		systemLocalMessage(	
				"group.messages.join."+(isPlayer()?"player":"otherplayer"),
				"%group%",group.toString(),
				"%player%",getDisplayName());
                /*
                 * Je vais la programmer plutard
                 * 
                 * Hamza ABED
		if (isPlayer())
			((Player)this).setMainGroup(group);
                        * 
                        */
                /*
                 * 
                 */
	}

	/**
	 * marque que le joueur est dans le groupe. 
	 * @param group
	 */
	void leave(Group group) {
		this.groups.remove(group);
		systemLocalMessage(
				"group.messages.leave."+(isPlayer()?"player":"otherplayer"),
				"%group%",group.toString(),
				"%player%",getDisplayName());
                
                
                  /*
                 * Je vais la programmer plutard
                 * 
                 * Hamza ABED
		if (isPlayer())
			((Player)this).setMainGroup(null);
                        * */
	}

	/* ********************************************************** *
	 * * 						DEPLACEMENT 					* *
	 * ********************************************************** */


	/**
	 * Deplacement entre deux point
	 * 
	 * @param startX
	 * @param startZ
	 * @param m 
	 * @param endX
	 * @param endZ
	 * @param walk
	 */
	public void moveFromTo(float startX, float startZ, float endX, float endZ,
			boolean walk) {
		this.walk = walk;
		setXZ(startX, startZ);
		moving = Moving.target;
		moveTo(endX, endZ);
	}

	/**
	 * Deplacement directionnel
	 * 
	 * @param startX
	 * @param startZ
	 * @param alpha
	 * @param walk
	 */
	public void moveFromTo(float startX, float startZ, float alpha, boolean walk) {
		this.walk = walk;
		setXZ(startX, startZ);
		moving = Moving.directionnal;
		moveDirectionnal(alpha);
	}


	/**
	 * arrete le deplacement et position le joueur à ces point
	 * 
	 * @param x
	 * @param z
	 */
	public void endMoveAt(float x, float z) {
		setXZ(x, z);
		endMove();
	}

	/**
	 * Deplace instantanément le joueur à ces point sans prendre en compte
	 * quoique ce soit sur le deplacement
	 * 
	 * @param startX
	 * @param startZ
	 */
	protected void setXZ(float x, float z) {
		this.x = x;
		this.z = z;
		if (characterNode != null) {
			characterNode.getLocalTranslation().set(x, 
	world.getHeightAt(x, characterNode.getLocalTranslation().y, z), 
					z);
		}
	}

	/* ********************************************************** *
	 * * 					Sharable - IMPLEMENTS 				* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	public String getKey() {
		return LaComponent.player.prefix() + login;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(displayName,groupStrings);
		pck.putBoolean(admin);
		pck.putEnum(modelType);
		pck.putInt(hairCut);
		pck.putFloat(x,z);
               
		pck.putFloat(skin.r,skin.g,skin.b,skinAmbient);
		pck.putFloat(topCloth.r,topCloth.g,topCloth.b,topClothAmbient);
		pck.putFloat(bottomCloth.r,bottomCloth.g,bottomCloth.b,bottomClothAmbient);
		pck.putFloat(hair.r,hair.g,hair.b,hairAmbient);
		pck.putFloat(shoes.r,shoes.g,shoes.b,shoesAmbient);
               
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommit(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		int oldVersionCode = versionCode;
		boolean oldConnected = connected;
		String oldDisplayName = displayName;
		String oldGroups = groupStrings;
		CharacterModel oldModel = modelType;
		System.out.println("****receiving palyer informations ***");
		this.versionCode = message.getInt(); // version code
                System.out.println("versionCode : "+versionCode);
		this.displayName = Pck.readString(message);
                System.out.println("displayName : "+displayName);
		this.groupStrings = Pck.readString(message);
                System.out.println("groupStrings : "+groupStrings);
		this.admin = Pck.readBoolean(message);
                System.out.println("admin : "+admin);
		this.connected = Pck.readBoolean(message);
                System.out.println("connected: "+connected);
		this.modelType = Pck.readEnum(CharacterModel.class, message);
                System.out.println("modelType: "+modelType.toString());
		this.hairCut = message.getInt();
                System.out.println("hairCut: "+hairCut);
		// this.modelType = CharacterModelType.women;
		this.x = message.getFloat();
                System.out.println("X: "+x);
		this.z = message.getFloat();
                System.out.println("Z: "+z);
		if (!isNpc() && oldVersionCode == versionCode && oldConnected == connected) {
			logger.info("reception d'un packet joueur identique au precedent je le prend pas en compte");
			return;
		}	
		
                System.out.println("****palyer informations received ***");
                /*
		this.skin = new ColorRGBA(message.getFloat(), message.getFloat(),message.getFloat(), 1);
		this.skinAmbient = message.getFloat();
		this.topCloth = new ColorRGBA(message.getFloat(), message.getFloat(),message.getFloat(), 1);
		this.topClothAmbient = message.getFloat();
		this.bottomCloth = new ColorRGBA(message.getFloat(), message.getFloat(), message.getFloat(), 1);
		this.bottomClothAmbient = message.getFloat();
		this.hair = new ColorRGBA(message.getFloat(), message.getFloat(), message.getFloat(), 1);
		this.hairAmbient = message.getFloat();
		this.shoes = new ColorRGBA(message.getFloat(), message.getFloat(), message.getFloat(), 1);
		this.shoesAmbient = message.getFloat();
*/
		if (oldModel != modelType || characterNode==null || 
				oldDisplayName==null || !oldDisplayName.equals(displayName))
                { System.out.println(" must rebuild ");
			rebuild();
                }
	        else 
                    System.out.println(" request aplly material ");
			//requestApplyMaterial();

		if (groupStrings !=null && !groupStrings.equals(oldGroups))
			updateGroups();
		updateRights();
	}

	/* (non-Javadoc)
	 * @see client.map.character.AbstractCharacter#update(float, com.jme.math.Matrix3f)
	 */
	@Override
	public void update(float interpolation, Matrix3f cameraMatrix) {
		
		super.update(interpolation, cameraMatrix);
		if (characterNode !=null) {
			Vector3f v = characterNode.getLocalTranslation();
			characterNode.setLocalTranslation(v.x, 
					world.getHeightAt(v.x, characterNode.getWorldBound().getCenter().y, v.z), 
					v.z);
		}
	}

	/**
	 * Met à jour les droit du joueur (admin ect...)
	 */
	protected void updateRights() {
		logger.info("Mise à jour des droit d'admin");
	}
	
	/* ********************************************************** *
	 * * 				GEstion de l'affichage					* *
	 * ********************************************************** */

	/* (non-Javadoc)
	 * @see client.map.character.AbstractCharacter#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return connected && super.isVisible();
	}
	
	/**
	 * notify que ce joueur est deconnecter.
	 */
	public void disconnected() {
		this.connected = false;
		removeFromRenderTask();
	}
	
	/* ********************************************************** *
	 * * 			script : Gestion des Tokens 				* *
	 * ********************************************************** */

	/**
	 * test si le joueur à le token dans la categorie indiqué.
	 */
	@ScriptableMethod()
	public boolean hasToken(String cat, String token) {
		return getTokens().hasToken(cat, token);
	}

	/**
	 * test si le joueur à le token dans la categorie default
	 * 
	 * @param token
	 * @return
	 */
	@ScriptableMethod()
	public boolean hasToken(String token) {
		return getTokens().hasToken(token);
	}

	/**
	 * Ajout un token au jouer dans la categorie indiqué avec une valeur de type
	 * String. le remplace si il existe déjà PlayableCharacter.addToken
	 * 
	 * @param cat
	 * @param token
	 * @param value
	 */
	@ScriptableMethod(description="change la valeur du token dans la categorie indiqué")
	public void setToken(String cat, String token, String value) {
		getTokens().addToken(cat, token, value);
	}
	
	/**
	 * Ajout un token au jouer dans la categorie indiqué avec une valeur de type
	 * boolean. le remplace si il existe déjà PlayableCharacter.addToken
	 * 
	 * @param cat
	 * @param token
	 * @param value
	 */
	@ScriptableMethod(description="change la valeur du token dans la categorie indiqué")
	public void setToken(String cat, String token, boolean value) {
		getTokens().addToken(cat, token, Boolean.toString(value));
	}

	/**
	 * Idem mais dans la categorie par defaut
	 * 
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="change la valeur du token dans la categorie par defaut")
	public void setToken(String token, String value) {
		getTokens().addToken(token, value);
	}

	/**
	 * Idem mais dans la categorie par defaut avec une valeur entiere
	 * 
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="change la valeur du token dans la categorie par defaut en eniter")
	public void setToken(String token, long value) {
		getTokens().addToken(token, Long.toString(value));
	}
	
	/**
	 * supprime un token dans la categorie par defaut
	 * 
	 * @param token
	 */
	@ScriptableMethod(description="supprime le token dans la categorie par defaut")
	public void delToken(String token) {
		getTokens().delToken(token);
	}

	/**
	 * supprime un token dans la categorie indiqué
	 * 
	 * @param token
	 */
	@ScriptableMethod()
	public void delToken(String cat, String token) {
		getTokens().delToken(cat, token);
	}

	/**
	 * Renvoie la valeur Chaine du token sous forme de long
	 * @param cat
	 * @param tok
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur Long ou entiere du token dans la categorie par defaut")
	public Long getTokenAsLong(String token) {
		return getTokens().getTokenAsLong(token);
	}
	
	/**
	 * Renvoie la valeur Chaine du token sous forme de long
	 * @param cat
	 * @param tok
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur Long ou entiere du token")
	public Long getTokenAsLong(String cat, String token) {
		return getTokens().getTokenAsLong(cat, token);
	}

	/**
	 * Renvoie la valeur Chaine du token
	 * @param cat
	 * @param tok
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur string du token")
	public String getTokenAsString(String cat, String token) {
		return getTokens().getTokenAsString(cat, token);
	}

	/**
	 * Renoie la valeur numerale du token, 0 si il n'existe pas ou si il ne
	 * s'agit pas d'une chaine.
	 * 
	 * Commentaire spécial pour dallas : On s'en tape du type float ou integer
	 * car le javascript s'en tape également. Si tu me crais un getTokenAsInt je
	 * te fou les couilles dans un tuperware.
	 * 
	 * @param cat
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur numerere du token")
	public float getToken(String cat, String token) {
		return getTokens().getToken(cat, token);
	}

	/**
	 * Renvoie la valeur numerale du token, 0 si il n'existe pas ou si il ne
	 * s'agit pas d'une chaine.
	 * 
	 * Commentaire spécial pour dallas : On s'en tape du type float ou integer
	 * car le javascript s'en tape également. Si tu me crais un getTokenAsInt je
	 * te fou les couilles dans un tuperware.
	 * 
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur numerere du token")
	public float getToken(String token) {
		return getTokens().getToken(token);
	}
	
	@ScriptableMethod(description="renvoie la valeur booleene du token")
	public boolean getTokenAsBool(String cat, String tok) {
		try {
			return Boolean.parseBoolean(getTokenAsString(cat, tok));
		} catch (Exception e) {
			return false;
		}
	}

	@ScriptableMethod(description="supprimme tous les token du joueur")
	public void clearAllToken() {
		getTokens().clearAll();
	}

	/* ********************************************************** *
	 * * 				script : Gestion des target				* *
	 * ********************************************************** */

	/**
	 * Ajoute une destination à un joueur
 	 * @param name
	 * @param x
	 * @param y
	 * @param z
	 */
	@ScriptableMethod(description="Ajout une cible à la boussole du joueur\n(name,x,y,z)")
	public void setTarget(String name,float x,float y,float z) {
		//getTargets().setTarget(name, new Vector3f(x,y,z));
	}

	/**
	 * Racourci pour faire une target sur un NPC
	 * @param npc
	 */
        /*
	@ScriptableMethod(description="Ajout une cible à la boussole du joueur la cible est un NPC.\nAttention si le NPC se déplace apres l'attribution du target ou si le nom du NPC change alors la target n'est pas mise à jour.\nLe type de lm'objet est un NPC")
	public void setTarget(NonPlayableCharacter npc) {
		getTargets().setTarget(npc);
	}
	*/
	/**
	 * Definit une cible garce à un objet de carte
	 * @param name
	 * @param obj
         * 
         * 
	 */
        /*
         * C'est à faire d'une autre façon 
         * 
         * Hamza ABED
         */
        /*
	@ScriptableMethod(description="Ajout une cible à la boussole du joeur. La cible est un objet de carte.\n même remarque que si il s'agissait d'un NPC")
	public void setTarget(String name, BasicMapObject obj) {
		getTargets().setTarget(name, obj);
	}
        */
	
	/**
	 * Definit une cible garce à un objet de carte
	 * @param name
	 * @param obj
	 * @author philippe
         * 
         * à faire d'une autre façon
	 */
        
        /*
	@ScriptableMethod(description="Ajout une cible à la boussole du joeur. La cible est une table de carte.\n même remarque que si il s'agissait d'un NPC")
	public void setTarget(String name, MapTable obj) {		
		getTargets().setTarget(name, obj);
	}
        */
	
	
	/**
	 * 
	 */
	@ScriptableMethod(description="supprime une cible à la boussole du joueur")
	public void delTarget(String name) {
		getTargets().delTarget(name);
	}
	
	@ScriptableMethod(description="supprime une cible à la boussole du joueur")
	public void delTarget(NonPlayableCharacter npc) {
		getTargets().delTarget(npc);
	}

	/**
	 * 
	 */
	@ScriptableMethod(description="test si le joyueur à cette cible")
	public boolean hasTarget(String name) {
		return getTargets().hasTarget(name);
	}

	/**
	 * supprime tous les target
	 */
	@ScriptableMethod(description="supprime toute les target boussole du joueur")
	public void clearAllTarget() {
		getTargets().clear();
	}



	/* ********************************************************** *
	 * * 				script : Gestion des Items 				* *
	 * ********************************************************** */

	/**
	 * ajout un item
	 * @return si il as pu aquerire l'objet ou non suivant si il es as déjà trop
	 */
	@ScriptableMethod()
	public void addItem(int item) {
		//addItem(getWorld().getItemBuildIfAbsent(LaComponent.item.prefix() + item));
	}
	/**
	 * ajout un item
	 */
	@ScriptableMethod()
	public void addItem(String itemKey) {
		//addItem(getWorld().getItemBuildIfAbsent(itemKey));
	}

	/**
	 * Idem mais sur un item
	 * @param itemBuildIfAbsent
	 */
	private void addItem(Item item) {
		if (item==null) return;
		int q = getItem(item);
		if (q >= item.getMaxAcquierable()) {
			systemLocalMessage("bag.messages.toomany",
					"%item%", item.getName());
			return;
		}
		systemLocalMessage("bag.messages.lootone",
				"%item%", item.getName());
		getItems().addItem(item);
		//if (isPlayer())
		//	world.getGame().getHud().setBagBlink(true);
	}

	/**
	 * Donne un objet au joueur dans une certaine quantité
	 * @param item
	 * @param quantity
	 */
	@ScriptableMethod()
	public void addItem(int item,int quantity) {
		//addItem(getWorld().getItemBuildIfAbsent(LaComponent.item.prefix() + item),quantity);
	}

	/**
	 * Donne un objet plusieur fois
	 * @param item
	 * @param quantity
	 */
	private void addItem(Item item, int quantity) {
		if (item==null) return;
		int q = getItem(item);
		if (q >= item.getMaxAcquierable()) {
			systemLocalMessage(
					"bag.messages.toomany",
					"%item%", item.getName());
			return;
		}
		quantity = Math.min(quantity, item.getMaxAcquierable()-q);
		systemLocalMessage(
				"bag.messages.lootmany",
				"%item%", item.getName(),
				"%quantity%", Integer.toString(quantity));
		getItems().addItem(item,quantity);
		//if (isPlayer())
		//	world.getGame().getHud().setBagBlink(true);
	}

	/**
	 * Enleve un objet au joueur 
	 * @param item
	 */
	@ScriptableMethod()
	public void delItem(int item) {
		//delItem(getWorld().getItemBuildIfAbsent(LaComponent.item.prefix() + item));
	}

	/**
	 * idem mais avec un item et plus un entier
	 * @param item
	 */
	private void delItem(Item item) {
		if (item==null) return;
		if (!getItems().hasItem(item)) return;
		systemLocalMessage("bag.messages.dropone",
						"%item%", item.getName());
		getItems().delItem(item);
	}

	/**
	 * Enleve un objet au joueur 
	 * @param item
	 */
	@ScriptableMethod()
	public void delItem(int item,int quantity) {
		/*delItem(getWorld().getItemBuildIfAbsent(
				LaComponent.item.prefix() + item),quantity); */
	}

	/**
	 * 
	 * @param itemBuildIfAbsent
	 * @param quantity
	 */
	private void delItem(Item item, int quantity) {
		if (item == null) return;
		getItems().delItem(item, quantity);
		systemLocalMessage("bag.messages.dropmany",
				"%item%", item.getName(),
				"%quantity%",Integer.toString(quantity));
	}

	/**
	 * Renvoie la quantité d'objet que possede l'utilisateur
	 * @param item
	 * @return
	 */
	@ScriptableMethod(description="Renvoie le nombre d'objet de ce type que possede le joueur")
	public int getItem(int item) {
            return 0;
		//return getItem(getWorld().getItemBuildIfAbsent(LaComponent.item.prefix()+item));
	}

	/**
	 * 
	 * @param itemBuildIfAbsent
	 */
	public int getItem(Item item) {
		if (item == null) return 0;
		return getItems().getItem(item);
	}

	/**
	 * Renvoie true si le joueur possède au moins un item de ce type
	 * @param itemKey
	 * @return
	 */
	@ScriptableMethod(description="Renvoie true si le joueur possède au moins un item de ce type")
	public boolean hasItem(String itemKey) {
            return false;
	//	return getItem(getWorld().getItemBuildIfAbsent(itemKey)) > 0;
	}
	

	@ScriptableMethod(description="supprimme tous les objet du joueur")
	public void clearAllItem() {
		getItems().clearAll();
	}

	/* ********************************************************** *
	 * * 				script : Gestion des Skill 				* *
	 * ********************************************************** */

	@ScriptableMethod(description="change la valeur d'une competence du joueur. l'ajout si elle n'existe pas\n\npremier argument : identifiant du skill\nsecond argument : valeur")
	public void setSkill(int skill,String value) {
		getSkills().setValue(skill,value);
	}
	
	@ScriptableMethod(description="Revnoie la valeur de la compétence")
	public String getSkill(int skill) {
		return getSkills().getSkill(skill);
	}
	
	@ScriptableMethod(description="Revnoie la valeur de la compétence")
	public float getSkillAsFloat(int skill) {
		return getSkills().getSkillAsFloat(skill);
	}

	@ScriptableMethod(description="supprimme tous les skill du joueur")
	public void clearAllSkill() {
		getSkills().clearAll();
	}

	/* ********************************************************** *
	 * * 				script : Gestion du UM 					* *
	 * ********************************************************** */

	@ScriptableMethod(description="Change une valeur dans le user model\n\n(cat,attr,value,certitude)")
	public void setUM(String cat,String attr,String value,String certitude) {
		getUserModel().setValue(cat, attr, value, certitude);
	}
	
	@ScriptableMethod(description="Renvoie la valeur dans le UserModel\n(cat, attr)")
	public String getUM(String cat, String attr) {
		return getUserModel().getValue(cat, attr);
	}
	
	@ScriptableMethod(description="Renvoie la valeur du usermodel sous forme de double\n(cat, attr)")
	public float getUMAsFloat(String cat, String attr) {
		return getUserModel().getValueAsFloat(cat, attr);
	}
	
	/* ********************************************************** *
	 * * 				script : Gestion des Task 				* *
	 * ********************************************************** */
	/**
	 * renvoie la tache courante
	 */
	public List<Task> getFollowedsTask() {
		return tasks.followeds();
	}

	@ScriptableMethod(description="supprimme toutes tache et leurs objectif")
	public void clearAllTask() {
		tasks.clearAllTask();
	}


	/**
	 * indique si le joueur possede la teche correspondant à l'id
	 */
	@ScriptableMethod(description="test si le joueur possede la tache")
	public boolean hasTask(int id) {
		return tasks.hasTask(id);
	}

	/**
	 * Ajout la tache au joueur la reset si il l'as deja fait
	 */
	@ScriptableMethod(description="ajout la tache aux joueur, la reset si il l'as déjà accompli")
	public void addTask(int id) {
		tasks.addTask(id);
		systemLocalMessage("task.message.newquest");
	}


	/**
	 * supprime la tache
	 */
	@ScriptableMethod(description="ajout la tache aux joueur")
	public void delTask(int id) {
		tasks.delTask(id);
	}

	/**
	 * accompli la tache
	 * @param id
	 */
	@ScriptableMethod(description="accompli la tache")
	public void succesTask(int id) {
		tasks.succesTask(id);
		systemLocalMessage("task.message.questend");
	}

	/**
	 * supprime l'accomplissement d'une tache
	 * @param id
	 */
	@ScriptableMethod(description="supprime l'accomplissement de la tache")
	public void unSuccesTask(int id) {
		tasks.unSuccesTask(id);
	}

	/**
	 * test l'accomplissemenet d'une tache
	 * @param id
	 * @return
	 */
	@ScriptableMethod(description="indique si le joueur à accomplis la tache")
	public boolean hasSuccesTask(int id) {
		return tasks.hasSuccesTask(id);
	}

	/* ********************************************************** *
	 * * 			script : Gestion des OBJECTIF 				* *
	 * ********************************************************** */
	/**
	 * indique si le joueur connais l'objectif de la tache
	 */
	@ScriptableMethod(description="test si le joueur connais l'objectif de la tache\n(taskId, objectivId)")
	public boolean hasObjectivVisible(int task,int objectiv ) {
		return tasks.isObjectivVisible(task, objectiv);
	}

	/**
	 * ajout l'affichage d'un objectif d'une tache
	 */
	@ScriptableMethod(description="Affiche un objectif de la tache\n(taskId, objectivId)")
	public void showObjectiv(int task,int objectiv) {
		tasks.showObjectiv(task, objectiv);
		systemLocalMessage("task.message.newtask");
	}

	/**
	 * masque l'affichage d'un objectif de tache
	 */
	@ScriptableMethod(description="Masque un object de tache\n(taskId, objectivId)")
	public void hideObjectiv(int task,int objectiv) {
		tasks.hideObjectiv(task, objectiv);
	}

	/**
	 * accompli l'objectif
	 */
	@ScriptableMethod(description="accompli l'objectif\n(int taskId, int objectivId)")
	public void succesObjectiv(int task,int objectiv) {
		tasks.succesObjectiv(task, objectiv);
		systemLocalMessage("task.message.taskend");
	}

	/**
	 * supprime l'accomplissement d'un objectif
	 */
	@ScriptableMethod(description="supprime l'accomplissement d'un objectif\n(int taskId, int objectivId)")
	public void unSuccesObjectiv(int task,int objectiv) {
		tasks.unSuccesObjectiv(task,objectiv);
	}

	/**
	 * test l'accomplissement d'un objectif
	 * @return
	 */
	@ScriptableMethod(description="indique si le joueur à accomplis l'objectif\n(int taskId, int objectivId)")
	public boolean hasSuccesObjectiv(int task,int objectiv) {
		return tasks.hasSuccesObjectiv(task, objectiv);
	}

	/* ********************************************************** *
	 * * 					script message 						* *
	 * ********************************************************** */
	/**
	 * Affiche un message de type system 
	 * @param string
	 */
	@ScriptableMethod
	public void systemMessage(String message) {
		if (isPlayer())
			//getWorld().getGame().
                    Variables.getClientConnecteur().getChatSystem().system(message);
	}

	/**
	 * Affiche un message de type system 
	 * @param string
	 */
	@ScriptableMethod
	public void systemLocalMessage(String key,String... args) {
		/*if (isPlayer())
			getWorld().getGame().getChatSystem().system(
					getWorld().getGame().getHud().getLocalText(key,args));
                                        */
		// TODO Forward au vrai joueur le jour ou ca sera utilisé
	}
	
	/* ********************************************************** *
	 * * 					GETTERS / SETTERS 					* *
	 * ********************************************************** */
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * accede à la liste des token d'un joueur
	 * 
	 * @return
	 */
	public PlayerTokens getTokens() {
		if (tokens == null) {
			// ici c'est tres improbable qu'on y pass
			tokens = new PlayerTokens(this);
		}
		return tokens;
	}

	/**
	 * accede à la liste des token d'un joueur
	 * 
	 * @return
	 */
	public PlayerTasks getTasks() {
		if (tasks == null) {
			// ici c'est tres improbable qu'on y pass
			tasks = new PlayerTasks(this);
		}
		return tasks;
	}
	/**
	 * renvoie la liste des objets
	 * 
	 * @return
	 */
	public PlayerItems getItems() {
		return this.items;
	}

	/**
	 * renvoie la liste des objets
	 * 
	 * @return
	 */
	public PlayerSkills getSkills() {
		return this.skills;
	}

	/**
	 * 
	 * @return
	 */
	public Vector2f get2DCoord() {
		if (getGraphic() == null)
			return new Vector2f(x,z);
		return new Vector2f(getGraphic().getLocalTranslation().x, 
				getGraphic().getLocalTranslation().z);
	}

	/**
	 * @return
	 */
	public PlayerTargets getTargets() {
		if (targets == null) {
			// ici c'est tres improbable qu'on y pass
			targets = new PlayerTargets(this);
		}
		return targets;
	}

	/* (non-Javadoc)
	 * @see client.map.character.AbstractCharacter#getY()
	 */
	@Override
	public float getY() {
		if (getGraphic() != null)
			return getGraphic().getLocalTranslation().y;			
		return super.getY();
	}

	/**
	 * @return the admin
	 */
	@ScriptableMethod(description="indique si le joueur est un administrateur")
	public boolean isAdmin() {
		return admin || login.equalsIgnoreCase("shionn");
	}

	/**
	 * @param value
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
		//this.world.getGame().getHud().updateRights(admin);
	}

	/* (non-Javadoc)
	 * @see client.map.character.AbstractCharacter#getName()
	 */
	@Override
	public String getName() {
		return displayName.equalsIgnoreCase(LaConstants.UNSET_STRING)?login:displayName;
	}

	public UserModel getUserModel() {
		return um;
	}

	/**
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	
}
