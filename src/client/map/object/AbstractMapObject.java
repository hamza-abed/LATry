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
package client.map.object;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.graphic.GraphicCollidable;
import client.interfaces.graphic.GraphicReflexEditable;
import client.interfaces.graphic.GraphicShadowCaster;
import client.map.World;
import client.script.ScriptableMethod;
import client.task.GraphicsAddRemoveSceneTask;
import client.utils.ModelLoader;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import shared.variables.Variables;
/*
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.util.GameTaskQueueManager;
*/
/**
 * Class abstrataite regroupant les point commun entre un batiment et un objet
 * du decort présent sur une carte
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public abstract class AbstractMapObject implements MapGraphics, 
GraphicReflexEditable, GraphicCollidable, GraphicShadowCaster {
	private static final long serialVersionUID = 7095937120203516635L;

	private static final Logger logger = Logger.getLogger("MapObject");

	/**
	 * monde conteneur
	 */
	private World world;

	/**
	 * identifiant
	 */
	protected int id;

	/**
	 * code version pour la synchronisation sur le server
	 */
	private int versionCode = -1;

	/**
	 * 
	 */
	@Editable(type=FieldEditType.file, 
			fileFolder=LaConstants.DIR_JMEX_OBJECT_MODEL,
			applyMethod="rebuildTask", populus=true)
			protected String modelName;

	/**
	 * script executer quand on clic dessus
	 */
	@Editable(type=FieldEditType.action)
	protected String action;

	/**
	 * distance à partire de laquel le script action est actif
	 */
	@Editable(type=FieldEditType.real)
	protected float actionDist;

	/**
	 * position, orientation
	 */
	@Editable(type=FieldEditType.real,applyMethod="rebuildGeometrics")
	protected float x, z;

	@Editable(type=FieldEditType.real,applyMethod="rebuildGeometrics", 
			populus=true, defaultReal=0)
	protected float y;

	@Editable(type=FieldEditType.real,
			applyMethod="rebuildGeometrics", populus=true,
			defaultReal=1)
			protected float s;

	@Editable(type=FieldEditType.realinterval, realMinValue=0f, realMaxValue=FastMath.TWO_PI, realStepValue=FastMath.QUARTER_PI/8,applyMethod="rebuildGeometrics")
	private float rx, rz;

	@Editable(type=FieldEditType.realinterval, realMinValue=0f, 
			realMaxValue=FastMath.TWO_PI, 
			realStepValue=FastMath.QUARTER_PI/8,
			applyMethod="rebuildGeometrics", populus=true)
	private float ry;

	/**
	 * object graphic
	 */
	protected Spatial model, ground;

	/**
	 * indique si l'objet est colisionnable
	 */
	@Editable(type=FieldEditType.bool, populus=true, defaultBool=true)
	protected boolean collidable;
	
	@Editable(type=FieldEditType.bool, populus=true, defaultBool=false)
	protected boolean walkOver;

	/**
	 * tache d'ajout ou de suppression de la partie graphic
	 */
	private GraphicsAddRemoveSceneTask task;

	private boolean visible = true;

	/**
	 * @param world
	 * @param id
	 */
	public AbstractMapObject(World world, int id) {
		this.world = world;
		this.id = id;

		//	world.getGame().getLocalBdd().load(this);
		//world.getGame().getHud().getLoading().add(this);
	}

	/**
	 * recharge l'objet dans une tache dedier
	 */
	private void rebuildTask() {
          System.out.println("AbstractMapObject->rebuildTask() vide!!");
            /*
		          Variables.getTaskExecutor().execute(new Runnable() {
			@Override
			public void run() {
				final Spatial newModel;
				newModel = ModelLoader.get().load(modelName);

				// newModel.setIsCollidable(collidable);
				newModel.setModelBound(new OrientedBoundingBox());
				//newModel.updateRenderState();

				if (isBuilding() && newModel instanceof Node)
					ground = ((Node) newModel).getChild("ground");
				else
					ground = null;

				if (ground != null) {
					ground.setCullHint(CullHint.Always);
					ground.updateRenderState();
				}

				GameTaskQueueManager.getManager().update(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						rebuildApply(newModel);
						return null;
					}
				});

			}
		});
                */
	}

	/**
	 * Finalise le rechargement graphique
	 * 
	 * @param newModel
	 */
	private void rebuildApply(Spatial newModel) {
	
            System.out.println("AbstractMapObject->rebuildApply(s) : vide!!");
            /*	
            if (model != null) {
			world.removeGraphics(this);
			//removeFromRenderTask();
		}

		logger.fine("rebuild Apply start");

		model = newModel;

		model.lockBounds();
		model.lockShadows();
		//model.lockTransforms();
		model.lockMeshes();

		logger.fine("rebuild Apply end");

		world.getGame().getHud().getLoading().remove(this);
		addToRenderTask();
		rebuildGeometrics();
                */
	}

	/**
	 * met à jour la position de l'objet
	 */
	private void rebuildGeometrics() {
            
            System.out.println("AbstractMapObject->rebuildGeometrics() : vide!!");
            /*
		GameTaskQueueManager.getManager().update(new Callable<Void>() {
			public Void call() throws Exception {
				if (model != null) {
					int l = model.getLocks();
					if (l != 0)
						model.unlock();

					model.setLocalTranslation(x, world.getHeightAt(x, z) + y, z);
					model.setLocalScale(s);
					Quaternion q = new Quaternion().fromAngleNormalAxis(rx,
							Vector3f.UNIT_X);
					q.multLocal(new Quaternion().fromAngleNormalAxis(ry,
							Vector3f.UNIT_Y));
					q.multLocal(new Quaternion().fromAngleNormalAxis(rz,
							Vector3f.UNIT_Z));
					model.setLocalRotation(q);
					model.updateGeometricState(0, true);
					model.updateWorldData(0);
					model.updateWorldVectors(true);
					model.updateRenderState();

					/*model.updateWorldData(0);
				model.updateWorldVectors(true);
				model.updateRenderState();//*/
/*
					if (l != 0)
						model.setLocks(l);
				}
				return null;
			}
		});
*/
	}

	/**
	 * met à jour les hauteur
	 */
	public void updateY() {
		rebuildGeometrics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommit(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
            System.out.println("AbstarctMapObject->receiveCommitPck(BB) : manquante !!");
		int newVersionCode = message.getInt();

		if (newVersionCode == versionCode) {
			logger.info("reception d'un packet sur un objet identique au connu");
			return;
		}

		String newModel = Pck.readString(message);
		if (newModel == null)
			newModel = LaConstants.DEFAULT_OBJECT_MODEL;
		boolean reloadModel = !newModel.equals(modelName);

		this.modelName = newModel;
		this.action = Pck.readString(message);
		this.collidable = Pck.readBoolean(message);
		this.walkOver = Pck.readBoolean(message);

		this.x = message.getFloat();
		this.y = message.getFloat();
		this.z = message.getFloat();
		this.rx = message.getFloat();
		this.ry = message.getFloat();
		this.rz = message.getFloat();
		this.s = message.getFloat();
		this.actionDist = message.getFloat();

		if (reloadModel)
			rebuildTask();
		//else
			//addToRenderTask();
		/*	GameTaskQueueManager.getManager().update(new Callable<Void>() {
				public Void call() throws Exception {
					rebuildGeometrics();
					return null;
				}
			});//*/
		this.versionCode = newVersionCode;
	}

	/* ********************************************************** *
	 * * 				GESTION DE LA VISIBILIT2 				* *
	 * ********************************************************** */

	/**
	 * met à jour la visibilité
	 * @param equalsIgnoreCase
	 */
	private void updateVisibility(boolean visible) {
		if (versionCode==-1)
			Variables.getClientConnecteur().updateFromServer(this);

		logger.fine(this.toString()+":"+visible);
		if (this.visible != visible) {
			this.visible = visible;
			if (visible)
				addToRenderTask();
			else 
				removeFromRenderTask();
		}
	}

	/**
	 * Met à jour la visibilité des token
	 */
	public void updateVisibility() {
		updateVisibility(!world.getPlayer().hasToken(LaConstants.OBJECT_VISIBILITY_CAT_TOKEN, Integer.toString(id)) ||
				Boolean.TRUE.equals(world.getPlayer().getToken(LaConstants.OBJECT_VISIBILITY_CAT_TOKEN, Integer.toString(id))));
	}

	/**
	 * 
	 */
	@ScriptableMethod(description="Affiche l'objet pour le joueur courant")
	public void showForPlayer() {
		updateVisibility(true);
		world.getPlayer().delToken(LaConstants.OBJECT_VISIBILITY_CAT_TOKEN, Integer.toString(id));		
	}

	/**
	 * 
	 */
	@ScriptableMethod(description="Masque l'objet pour le joueur courant")
	public void hideForPlayer() {
		updateVisibility(false);
		world.getPlayer().setToken(LaConstants.OBJECT_VISIBILITY_CAT_TOKEN, Integer.toString(id), Boolean.FALSE.toString());
	}


	/* ********************************************************** *
	 * * 					Graphics - IMPLEMENTS 				* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#getGraphic()
	 */
	@Override
	public Spatial getGraphic() {
		return model;
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
		if (visible) task.add();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicEditable#setLocation(float, float)
	 */
	@Override
	public void setGeometrics(float x, float y, float z, float rx, float ry,
			float rz, float scale) {
		if (!Float.isNaN(x))
			this.x = x;
		if (!Float.isNaN(y))
			this.y = y;
		if (!Float.isNaN(z))
			this.z = z;
		if (!Float.isNaN(rx))
			this.rx = rx;
		if (!Float.isNaN(ry))
			this.ry = ry;
		if (!Float.isNaN(rz))
			this.rz = rz;
		if (!Float.isNaN(scale))
			this.s = scale;
		rebuildGeometrics();
	}

	/* ********************************************************** *
	 * * 				Sharable - IMPLEMENTS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(modelName, action);
		pck.putBoolean(collidable, walkOver);
		pck.putFloat(x, y, z, rx, ry, rz, s, actionDist);
	}

	/* ********************************************************** *
	 * * 					GETTERS / SETTERS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getKey();
	}

	/**
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * @param modelName
	 *            the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
		rebuildTask();
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the actionDist
	 */
	public float getActionDist() {
		return actionDist;
	}

	/**
	 * @param actionDist
	 *            the actionDist to set
	 */
	public void setActionDist(float actionDist) {
		this.actionDist = actionDist;
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
		rebuildGeometrics();
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
		rebuildGeometrics();
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
		rebuildGeometrics();
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
		rebuildGeometrics();
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
		rebuildGeometrics();
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
		rebuildGeometrics();
	}

	/**
	 * @return the s
	 */
	public float getS() {
		return s;
	}

	/**
	 * @param s
	 *            the s to set
	 */
	public void setS(float s) {
		this.s = s;
		rebuildGeometrics();
	}

	public void setCollidable(boolean collidable) {
            System.out.println("AbstractMapObject->setCollidable(bool) : manquante !!");
		this.collidable = collidable;
		/*if (model != null)
			model.setIsCollidable(collidable); */
	}

	/**
	 * @return the collidable
	 */
	public boolean isCollidable() {
		return collidable;
	}

	/**
	 * un Building
	 * 
	 * @return
	 */
	protected boolean isBuilding() {
		return false;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @param interpolation
	 */
	public void update(float interpolation) {

	}


}
