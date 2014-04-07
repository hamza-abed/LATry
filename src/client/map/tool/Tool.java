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
package client.map.tool;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.PckCode;
import shared.enums.LaComponent;
import shared.enums.ToolType;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
//import client.interfaces.graphic.GraphicMouseListener;
import client.interfaces.graphic.GraphicReflexEditable;
import client.interfaces.graphic.GraphicShadowCaster;
import client.map.World;
import client.script.ScriptableMethod;
import client.task.GraphicsAddRemoveSceneTask;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.scene.Spatial;
import shared.variables.Variables;
/*import client.map.tool.blackboard.BlackboardEngine;
import client.map.tool.evalfeather.EvalFeatherEngine;
import client.map.tool.feather.FeatherEngine;
import client.map.tool.fileshare.SharedFileEngine;
import client.map.tool.postitwall.PostItWallEngine;
import client.map.tool.viewer.ViewerEngine;
import client.script.ScriptableMethod;
import client.task.GraphicsAddRemoveSceneTask;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.util.GameTaskQueueManager;
*/
/**
 * Outil graphique
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Tool implements GraphicReflexEditable,
		 GraphicShadowCaster {
	private static final Logger logger = Logger.getLogger("Tool");

	/**
	 * monde conteneur
	 */
	protected World world;

	/**
	 * identiifnat de l'outil
	 */
	private int id;

	/**
	 * code version de l'outil
	 */
	private int versionCode = -1;

	/**
	 * position de l'outil
	 */
	@Editable(type=FieldEditType.real,applyMethod="rebuildGeometrics")
	private float x, y, z;
	
	@Editable(type=FieldEditType.realinterval,realMinValue=0,realMaxValue=FastMath.TWO_PI,realStepValue=FastMath.QUARTER_PI/3,applyMethod="rebuildGeometrics")
	private float rx, ry, rz;

	/**
	 * type de l'outil
	 */
	@Editable(type=FieldEditType.enumerate,applyMethod="rebuildAll")
	private ToolType type;

	/**
	 * Moteur d'execution de l'outil
	 */
	private ToolEngine engine;

	@Editable(type=FieldEditType.map,keyType=FieldEditType.string,innerType=FieldEditType.string)
	protected HashMap<String, String> datas = new HashMap<String, String>();

	private GraphicsAddRemoveSceneTask task;

	/**
	 * @param world
	 * @param parseInt
	 */
	public Tool(World world, int id) {
		this.world = world;
		this.id = id;
	}

	/**
	 * Construit l'outil
	 */
	protected void rebuildTool() {
		if (engine != null) {
			world.removeGraphics(this);
			engine.delete();
			removeFromRenderTask();
		}

		switch (type) {
		case postItWall:
			//engine = new PostItWallEngine(this);
			break;
		case blackboard:
			//engine = new BlackboardEngine(this);
			break;
		case viewer:
			//engine = new ViewerEngine(this);
			break;
		case fileShare:
			//engine = new SharedFileEngine(this);
			break;
		case feather:
			//engine = new FeatherEngine(this);
			break;
		case evalFeather :
			//engine = new EvalFeatherEngine(this);
			break;
		case none:
		default:
			//engine = new NullEngine(this);
			break;
		}

		addToRenderTask();
	}
	
	/**
	 * utilisé par introspection par l'editeur
	 */
	@SuppressWarnings("unused")
	private void rebuildAll() {
		rebuildTool();
		rebuildGeometrics();
	}
	

	/**
	 * met à jour la position de l'objet
	 */
	private void rebuildGeometrics() {
		/*Spatial s = engine.getGraphic();
		s.setLocalTranslation(x, world.getHeightAt(x, z) + y, z);

		Quaternion q = new Quaternion().fromAngleNormalAxis(rx, Vector3f.UNIT_X);
		q.multLocal(new Quaternion().fromAngleNormalAxis(ry, Vector3f.UNIT_Y));
		q.multLocal(new Quaternion().fromAngleNormalAxis(rz, Vector3f.UNIT_Z));
		
		s.setLocalRotation(q);
		s.updateGeometricState(0, true); */
	}

	/* ********************************************************** *
	 * * 					Gestion des Datas				 	* *
	 * ********************************************************** */

	/**
	 * 
	 * @param message
	 */
	public void receiveToolDataChange(ByteBuffer message) {
		int count = message.getInt();
		ArrayList<String> changed = new ArrayList<String>();
		ArrayList<String> deleted = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			String key = Pck.readString(message);
			String value = Pck.readString(message);
			if (value==null || value.equals(PckCode.TOOL_DELETED_KEY_VALUE)) {
				datas.remove(key);
				deleted.add(key);
			} else {
				datas.put(key, value);
				changed.add(key);
			}
		}
		if (engine != null) {
			if (changed.size() > 0)
				engine.dataChanged(changed);
			if (deleted.size() > 0)
				engine.dataDeleted(deleted);
		}
	}

	/**
	 * Envoie du chazngement de valeur d'un data
	 * 
	 * @param key
	 */
	private void sendDataChange(String key) {
		Pck pck = new Pck(PckCode.EXTENDED_DATA);
		pck.putString(getKey());
		pck.putInt(1);
		pck.putString(key, datas.containsKey(key) ? datas.get(key)
				: PckCode.TOOL_DELETED_KEY_VALUE);
		//world.getGame().send(pck);
                Variables.getClientConnecteur().send(pck);
	}

	/**
	 * Envoie l'enssemble des changements
	 * 
	 * @param changed
	 */
	public void sendChangeOnServer(Collection<String> changed) {
		if (changed.size() == 0) return;
		
		logger.info("send "+changed);
		
		Pck pck = new Pck(PckCode.EXTENDED_DATA);
		pck.putString(getKey());
		pck.putInt(changed.size());
		for (String key : changed)
			pck.putString(key, datas.containsKey(key) ? datas.get(key)
					: PckCode.TOOL_DELETED_KEY_VALUE);
		//world.getGame().send(pck);
                 Variables.getClientConnecteur().send(pck);
	}


	/**
	 * Liste des clef existante
	 * 
	 * @return
	 */
	public Collection<String> getDataKeys() {
		return datas.keySet();
	}

	/**
	 * Renvoie la data associé à la clef sous forme de chaine
	 * 
	 * @param key
	 * @return
	 */
	public String getData(String key) {
		return datas.get(key);
	}

	/**
	 * Change la valeure d'une clef
	 * 
	 * @param key
	 * @param text
	 */
	public void setDataAndSendOnServer(String key, String value) {
		boolean send = !value.equals(datas.get(key));
		this.datas.put(key, value);
		if (send)
			sendDataChange(key);
	}

	/**
	 * Change la valeure d'une clef
	 * 
	 * @param key
	 * @param text
	 */
	public void setDataAndNoSendOnServer(String key, String value) {
		this.datas.put(key, value);
	}

	/**
	 * Supprime une clef
	 * 
	 * @param key
	 */
	public void deleteDataAndSendOnServer(String key) {
		this.datas.remove(key);
		sendDataChange(key);
	}

	/**
	 * Change la valeure d'une clef
	 * 
	 * @param key
	 * @param text
	 */
	public void deleteDataAndNoSendOnServer(String key) {
		this.datas.remove(key);
	}

	/* ********************************************************** *
	 * * 					Sharable IMPLEMENTS 				* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putFloat(x, y, z, rx, ry, rz);
		pck.putEnum(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.tool.prefix() + id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommit(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		int newVersionCode = message.getInt();
		if (newVersionCode != versionCode) {
			this.versionCode = newVersionCode;
			this.x = message.getFloat();
			this.y = message.getFloat();
			this.z = message.getFloat();
			this.rx = message.getFloat();
			this.ry = message.getFloat();
			this.rz = message.getFloat();
			changeType(Pck.readEnum(ToolType.class, message));
			/*GameTaskQueueManager.getManager().update(new Callable<Void>() {
				public Void call() throws Exception {
					rebuildGeometrics();
					return null;
				}
			}); */
		} else {
			logger.info("reception d'un outil mais déjà jour");
		}
	}

	/* ********************************************************** *
	 * * 				Graphic - IMPLEMENTS 					* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#getGraphic()
	 */
	@Override
	public Spatial getGraphic() {
		if (engine == null)
			return null;
		//return engine.getGraphic();
            return null;
	}

	/* ********************************************************** *
	 * *				GraphicEditable - IMPLEMENTS 			* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicEditable#setGeometrics(float,
	 * float, float, float, float, float, float)
	 */
	@Override
	public void setGeometrics(float x, float y, float z, float rx, float ry,
			float rz, float scale) {
		if (!Float.isNaN(x)) this.x = x;
		if (!Float.isNaN(y)) this.y = y;
		if (!Float.isNaN(z)) this.z = z;
		if (!Float.isNaN(rx)) this.rx = rx;
		if (!Float.isNaN(ry)) this.ry = ry;
		if (!Float.isNaN(rz)) this.rz = rz;
		rebuildGeometrics();
	}


	/* ********************************************************** *
	 * * 			GraphicMouseListener - IMPLEMENTS 			* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicMouseListener#onButton(int,
	 * boolean, com.jme.math.Ray)
	 */
	//@Override
	public boolean onButton(int button, boolean pressed, Ray ray) {
	//	if (engine != null)
	//		return engine.onButton(button, pressed, ray);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.graphic.GraphicMouseListener#onMove(com.jme.math.Ray)
	 */
//	@Override
	public boolean onMove(Ray ray) {
		//if (engine != null)
			//return engine.onMove(ray);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicMouseListener#onWheel(int,
	 * com.jme.math.Ray)
	 */
	//@Override
	public boolean onWheel(int wheelDelta, Ray ray) {
		/*if (engine != null)
			return engine.onWheel(wheelDelta, ray); */
		return false;
	}

	/* ********************************************************** *
	 * *	 					Scripting	 					* *
	 * ********************************************************** */
	
	@ScriptableMethod(description="ajout une idée pour la plume d'evaluation(idee, description)")
	public void addIdea(String text, String desc) {
		/* logger.info("Ajout idée : "+text);
		if (engine!=null && engine instanceof EvalFeatherEngine) {
			((EvalFeatherEngine)engine).addIdea(text,desc,ColorRGBA.black);
		} else {
			logger.info("Ajout d'une idée à un truc qui n'est pas une plume d'evaluation");
		}
                * */
		
	}
	
	@ScriptableMethod(description="renvoie les resultats des votes")
	public HashMap<String, Integer> voteResult() {
            return null;
		/* if (engine!=null && engine instanceof EvalFeatherEngine) {
			return ((EvalFeatherEngine)engine).result();
		} else {
			logger.info("pas le bon type pour connaitre les resultat de vote");
			return null;
		}
                * */
	}
	
	
	
	/* ********************************************************** *
	 * * 					GETTERS / SETTERS 					* *
	 * ********************************************************** */
	/**
	 * Change le type de l'outil
	 * 
	 * @param toolType
	 */
	private void changeType(ToolType toolType) {
		if (toolType != this.type) {
			this.type = toolType;
		/*	GameTaskQueueManager.getManager().update(new Callable<Void>() {
				public Void call() throws Exception {
					rebuildTool();
					rebuildGeometrics();
					return null;
				}
			});
		}
                */
                }
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
		this.rebuildGeometrics();
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
		this.rebuildGeometrics();
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(float z) {
		this.z = z;
		this.rebuildGeometrics();
	}

	/**
	 * @return the rx
	 */
	public float getRx() {
		return rx;
	}

	/**
	 * @param rx
	 *            the rx to set
	 */
	public void setRx(float rx) {
		this.rx = rx;
		this.rebuildGeometrics();
	}

	/**
	 * @return the ry
	 */
	public float getRy() {
		return ry;
	}

	/**
	 * @param ry
	 *            the ry to set
	 */
	public void setRy(float ry) {
		this.ry = ry;
		this.rebuildGeometrics();
	}

	/**
	 * @return the rz
	 */
	public float getRz() {
		return rz;
	}

	/**
	 * @param rz
	 *            the rz to set
	 */
	public void setRz(float rz) {
		this.rz = rz;
		this.rebuildGeometrics();
	}

	/**
	 * @return the type
	 */
	public ToolType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ToolType type) {
		if (this.type != type) {
			changeType(type);
		}
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @return the engine
	 */
	public ToolEngine getEngine() {
		return engine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#addToRenderTask()
	 */
	@Override
	public void addToRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask(this, world);
		task.add();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#removeFromRenderTask()
	 */
	@Override
	public void removeFromRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask(this, world);
		task.remove();
	}

	/**
	 * met à jour l'outil
	 * 
	 * @param interpolation
	 */
	public void update(float interpolation) {
		if (engine != null)
			engine.update(interpolation);
	}

	/**
	 * Renvoie la liste des clef basique de l'outil
	 */
	public Collection<String> getKeysDescriptions() {
		if (engine != null)
			return engine.getKeysDescriptions();
		return new ArrayList<String>();
	}

}
