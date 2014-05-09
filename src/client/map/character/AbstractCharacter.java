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

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.enums.CharacterModel;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.graphic.Graphic;
import client.interfaces.graphic.GraphicShadowCaster;
import client.interfaces.graphic.GraphicShadowed;
import client.interfaces.network.Sharable;
import client.map.World;
import client.task.GraphicsAddRemoveSceneTask;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.math.ColorRGBA;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.model.md5.controller.MD5Controller;
import shared.variables.Variables;



/**
 * Class Abstraite regroupant l'enssemble des animations relatives au personnage
 * joueur ou non
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public abstract class AbstractCharacter implements Graphic, Sharable,
GraphicShadowCaster, GraphicShadowed {
	private static final Logger logger = Logger.getLogger("AbstractCharacter");

	public enum Moving {
		stop, // le joueur est arreter
		target, // deplacement vers un point
		directionnal // deplacement vers une direction
	}

	public enum CharacterAnimation {
		walk,run,idle
	}

	protected World world;

	@Editable(type=FieldEditType.string, applyMethod="rebuild")
	protected String displayName = LaConstants.UNSET_STRING;

	@Editable(type=FieldEditType.enumerate, applyMethod="rebuild")
	protected CharacterModel modelType = CharacterModel.spirit;

	protected Node characterNode = null;

	//private MeshAnimationController controlerOgre;

	private MD5Controller controlerMD5;

	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	protected float x=-100, y, z=-100;

	protected int versionCode = -1;

	protected Moving moving = Moving.stop;

	private Vector3f movingDir; // vecteur de deplacement dans le cas d'un déplacement clavier

	protected float velocity = 15f;

	protected boolean walk = true;

	private GraphicsAddRemoveSceneTask task;

	@Editable(type=FieldEditType.color, applyMethod="rebuild")
	protected ColorRGBA topCloth = new ColorRGBA(),
	bottomCloth = new ColorRGBA(), hair = new ColorRGBA(),
	shoes = new ColorRGBA(), skin = new ColorRGBA();

	@Editable(type=FieldEditType.realinterval,realMinValue=0f,realMaxValue=1f,realStepValue=.05f, applyMethod="rebuild")
	protected float topClothAmbient = 0.2f, bottomClothAmbient = 0.2f,
	hairAmbient = 0.2f, shoesAmbient = 0.2f, skinAmbient = 0.2f;

	private Node onHead;

	// uniquement pour es NPC
	protected boolean visible = true;

	/**
	 * quaternion de calcul
	 */
	private Quaternion q = new Quaternion();

	@Editable(type=FieldEditType.integer, applyMethod="rebuild")
	int hairCut=0;

	//private Font3D npcfont;

	//private Text3D name;

	//static protected Font3D playerFont;



	/**
	 * @param world
	 */
	public AbstractCharacter(World world) {
		this.world = world;
		this.modelType = CharacterModel.unset;
	
	}

	/* ********************************************************** *
	 * * 				Construction Graphique	 				* *
	 * ********************************************************** */

	/**
	 * recharge l'apperence de l'objet dans une tache secondaire;
	 */
	   protected void rebuild() {


        Variables.getTaskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Node newModel = CharacterLoader.loadNode(AbstractCharacter.this);
                    Variables.getLaGame().enqueue(
                            new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            rebuildApply(newModel);
                            return null;
                        }
                    });

                } catch (IOException e) {
                    logger.warning("IOException : " + e.getMessage());
                }

            }
        });

    }

	/**
	 * 
	 * appelé à la fin du chargement du model Doit être appelé dans la boucle
	 * d'update
	 * 
	 * @param newModel
	 * @param newControler
	 */
	protected void rebuildApply(Node newModel) {
            
            System.out.println("AbstractCharcter -> rebuildApply() !! new Model loaded");
		if (characterNode != null) {
			characterNode.removeFromParent();
			removeFromRenderTask();
		}

		if (onHead == null) 
			onHead = new Node();
		else {
			onHead.removeFromParent();
		}

		characterNode = newModel;
		characterNode.attachChild(onHead);
                Variables.setPlayerModelLoaded(true);
               
/*
		if (playerFont == null) {
			playerFont = new Font3D(new Font("ARIAL", Font.BOLD, 16),0.04f,true,true,true);
			new Font3DGradient(Vector3f.UNIT_Y,new ColorRGBA(0.8f,0.8f,0.8f,1),ColorRGBA.white).applyEffect(playerFont);
			new Font3DBorder(0.06f,ColorRGBA.black,ColorRGBA.black,playerFont).applyEffect(playerFont);
		}

		if (npcfont == null) {
			npcfont = new Font3D(new Font("ARIAL", Font.BOLD, 16),0.04f,true,true,true);
			new Font3DGradient(Vector3f.UNIT_Y,new ColorRGBA(0.2f,0.8f,0.2f,1),ColorRGBA.white).applyEffect(npcfont);
			new Font3DBorder(0.06f,ColorRGBA.black,ColorRGBA.black,npcfont).applyEffect(npcfont);
		}

		if (displayName != null && !isPlayer()) {
			if (name !=null) name.removeFromParent();
			try {
				name = (isNpc()?npcfont:playerFont).createText(displayName, .6f, 0);
			} catch (Exception e) {
				logger.warning("erreur dans la creation du nom : "+e.getMessage());	
				name = (isNpc()?npcfont:playerFont).createText(e.getClass().getSimpleName(), .6f, 0);
			}
			name.setLocalTranslation(-name.getWidth()/2, .6f, 0);
			name.setLightCombineMode(LightCombineMode.Off);
			onHead.attachChild(name);
		} else {
			name =null;
		}

		onHead.setLocalScale(1f/CharacterLoader.getModelScale(this));
		onHead.setLocalTranslation(0,CharacterLoader.getStdHeight(this)/CharacterLoader.getModelScale(this),0);

		initControlers();

		CharacterLoader.applyMaterials(this,characterNode);

		addToRenderTask();
                */
                initControlers();
	}

	/**
	 * indique si le joueur est visible
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * affiche un objet 3D au dessus d'un personnage
	 * @param mesh
	 */
	public void addOnHeadGraphics(Spatial s) {
		if (onHead != null) {
			onHead.attachChild(s);
		}
	}

	/**
	 * enleve tous ce qui apparait au dessus de la tete du personnage
	 */
	protected void clearOnHead() {
		try {
			onHead.detachAllChildren();
			//if (name!=null)	onHead.attachChild(name);
		} catch (NullPointerException e) {
		}
	}

        private AnimControl control;
	/**
	 * initialise les controler d'animation en fonction du model
	 */
	private void initControlers() {
		

		//controlerOgre = null;
		//controlerMD5 = null;
        control = characterNode.getControl(AnimControl.class);
        control.addListener((AnimEventListener) this);
            channel = control.createChannel();

	startAnimation(CharacterAnimation.idle);

	}



	/* ********************************************************** *
	 * * 					TEST DE TYPE 						* *
	 * ********************************************************** */

	/**
	 * test si l'objet est le joueur controler
	 * 
	 * @return
	 */
	public boolean isPlayer() {
		return false;
	}

	/**
	 * test si l'objet est le joueur controler
	 * 
	 * @return
	 */
	public boolean isNpc() {
		return false;
	}

	/* ********************************************************** *
	 * * 					GESTION ANIMATION 					* *
	 * ********************************************************** */



	/**
	 * lancement des animation
	 * @param anim
	 */
	private void startAnimation(CharacterAnimation anim) {
            
            System.out.println("AbstaractCharacter -> startAnimation() : vide!!!");
		/* if (CharacterLoader.isJmexModel(this))
			return;
		else if (CharacterLoader.isMd5Model(this))
			startMd5Anim(anim); 
		/*else 
			startOgreAnim(anim); 
                        */
            startOgreAnim(anim); 
	}

	/**
	 * execute une animation depuis un model md5 MD5
	 * @param anim
	 */
	private void startMd5Anim(CharacterAnimation anim) {
	/*
            if (controlerMD5 != null)  
			controlerMD5.fadeTo(getAnimationName(anim), .1f, false);
                        */
	}

	/**
	 * execute une animation sur un model ogre
	 * @param anim
	 */
          private AnimChannel channel;  
	private void startOgreAnim(final CharacterAnimation anim) {
		/*if (controlerOgre != null) {
			controlerOgre.setAnimation(getAnimationName(anim));
			controlerOgre.setSpeed(getAnimSpeed(anim));
			controlerOgre.setRepeatType(getRepeatType(anim));
		}*/
		Variables.getLaGame().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
		if (channel != null)
			channel.setAnim(getAnimationName(anim));
		return null;
			}
		});
	}

	/**
	 * renvoie comment se lit l'animation
	 * @param anim
	 * @return
	 */
        /*
	private int getRepeatType(CharacterAnimation anim) {
		if (modelType == CharacterModel.skelet && anim==CharacterAnimation.idle)
			//return Controller.RT_CYCLE; // marche pas
			return Controller.RT_WRAP;	
		return Controller.RT_WRAP;
	}

	/**
	 * Renvoie la vitesse d'animation
	 * @param anim
	 * @return
	 */
	private float getAnimSpeed(CharacterAnimation anim) {
		if (modelType == CharacterModel.men)
			return 1.5f;
		return 1;
	}

	/**
	 * renvoie le nom correspondant à une animation de type ogre
	 * @param anim
	 * @return
	 */
	protected String getAnimationName(CharacterAnimation anim) {

		if (modelType == CharacterModel.robot) switch (anim) {
		case run: return CharacterLoader.WALK_ANIM_NAME;
		}

		if (modelType == CharacterModel.marine) switch (anim) {
		case run: return CharacterLoader.WALK_ANIM_NAME;
		}

		if (modelType == CharacterModel.tank) {
			return "run";
		}

		if (modelType == CharacterModel.arachnie) switch (anim) {
		case walk:
		case run: return "move";
		case idle : return "stand";
		}

		/*if (modelType == CharacterModel.mage)
			return "my_animation";//*/


		switch (anim) {
		case walk: return CharacterLoader.WALK_ANIM_NAME;
		case run: return CharacterLoader.RUN_ANIM_NAME;
		default: return CharacterLoader.IDLE_ANIM_NAME;
		}
	}


	/**
	 * indique si une animation est active
	 * @param anim
	 * @return
	 */
	private boolean isAnimActiv(CharacterAnimation anim) {
	/*	
            if (CharacterLoader.isJmexModel(this))
			return true;
		if (CharacterLoader.isMd5Model(this)) {
			if (controlerMD5 ==null) return false;
			return controlerMD5.getActiveAnimation().getName().equalsIgnoreCase(getAnimationName(anim));
		} else {
			if (controlerOgre==null) return false;
			return controlerOgre.getActiveAnimations()[0].equalsIgnoreCase(getAnimationName(anim));
		}
                */
            
            return false;
	}


	public void requestApplyMaterial() {
		final AbstractCharacter p = this;
		Variables.getLaGame().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				CharacterLoader.applyMaterials(p,characterNode);
				addToRenderTask();
				return null;
			}
		});
	}

	/* ********************************************************** *
	 * * 					GRAPHICS IMPLEMENTS 				* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#addToRenderTask()
	 */
	@Override
	public void addToRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask(this, world);
		if (isVisible())
			task.add();
		//world.getGame().getHud().getLoading().remove(this);
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
	 * @see client.interfaces.graphic.Graphic#getGraphic()
	 */
	
	public Node getGraphic() {
		return characterNode;
	}

	@Override
	public Collection<Spatial> getShadowed() {
		ArrayList<Spatial> out = new ArrayList<Spatial>();
		//out.add(getGraphic());
		return out;
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

	/* ********************************************************** *
	 * *					Mise à jour 						* *
	 * ********************************************************** */

	/**
	 * 
	 * @param interpolation
	 * @param cameraMatrix : orientation de la camera pour reorienté le text ver le joueur
	 */
	public void update(float interpolation,Matrix3f cameraMatrix) {
		if (characterNode != null) {
			if (moving != Moving.stop)
				updateMoving(interpolation);
			onHead.setLocalRotation(characterNode.getLocalRotation().toRotationMatrix().invert().mult(cameraMatrix));

			/*	if (isPlayer()) {
				BoundingBox bound = new BoundingBox(characterNode.getWorldBound().getCenter(), 0, 0, 0);
				bound.mergeLocal(characterNode.getWorldBound());
				System.out.println(bound.yExtent);
			}//*/
		}
	}

	/**
	 * applique une animation de déplacement
	 * @return 
	 */
	private void moveAnimation() {
		if (walk && !isAnimActiv(CharacterAnimation.walk))
			startAnimation(CharacterAnimation.walk);
		else if (!walk && !isAnimActiv(CharacterAnimation.run))
			startAnimation(CharacterAnimation.run);
	}

	/* ********************************************************** *
	 * * 			Déplacement par target						* *
	 * ********************************************************** */



	/**
	 * Requete de deplacement du joueur (controlé ou non)
	 * 
	 * @param x
	 * @param z
	 */
	public void moveTo(float x, float z) {
		this.moving = Moving.target;
		moveAnimation();

		Vector3f o = getGraphicPosition();

		// check je crois que c'est inutile
		// Rep : oui c'est utile pour le calcul de déplacement
		this.x = x;
		this.z = z;
		// fin check

		Vector3f t = new Vector3f(x, o.y, z);
		t.subtractLocal(o).normalizeLocal();

		Matrix3f m = new Matrix3f();
		m.fromStartEndVectors(Vector3f.UNIT_Z, t);

		Quaternion q = new Quaternion().fromRotationMatrix(m);
		Quaternion q2 = new Quaternion().fromRotationMatrix(m.invert());

		// correction d'un bug survenant quand la camera est dans l'axe des Z
                /*
		if (q.x > 0.5f) {
			q.y = q.x;
			q.x = 0;
			q2.y = q2.x;
			q2.x = 0;	
		}
*/
		if (characterNode != null) {
			characterNode.setLocalRotation(q);
			onHead.setLocalRotation(q2);
		}
	}

	/* ********************************************************** *
	 * * 				Déplacement par direction				* *
	 * ********************************************************** */


	/**
	 * 
	 */
	public void moveDirectionnal(float alpha) {
		this.moving = Moving.directionnal;
		moveAnimation();

		Vector3f o = getGraphicPosition();
		this.x = o.x;
		this.z = o.z;

		q.fromAngleNormalAxis(alpha, Vector3f.UNIT_Y);
		Quaternion q2 = new Quaternion().fromAngleNormalAxis(alpha, Vector3f.UNIT_Y);

		movingDir = q.mult(Vector3f.UNIT_Z);

		if (characterNode != null) {
			characterNode.setLocalRotation(q);
			onHead.setLocalRotation(q2);
		}
	}


	/**
	 * met à jour la position pendant le déplacement
	 * 
	 * @param interpolation
	 */
	protected void updateMoving(float interpolation) {
		float v = walk ? velocity / 2f : velocity;

		if (moving == Moving.target) {
			Vector3f o = characterNode.getLocalTranslation();
			Vector3f t = new Vector3f(x, o.y, z);
			Vector3f dir = t.subtract(o).normalizeLocal();

			Vector3f newPos = o.add(dir.mult(interpolation * v));
			boolean lastMove = o.distanceSquared(t) < v * interpolation;

			if (lastMove)
				newPos = t;

			if (testCollision(newPos,dir)) {
				characterNode.setLocalTranslation(o);
				endMove(); 
				return;
			}

			if (!canMoveAt(newPos)) {
				characterNode.setLocalTranslation(o);
				endMove();
				return;
			}

			characterNode.setLocalTranslation(newPos);
			if (lastMove) {
				endMove();
				// System.out.println(model.getLocalTranslation().y);
			}
		} else if (moving == Moving.directionnal) {
			Vector3f o = characterNode.getLocalTranslation();
			Vector3f newPos = o.add(movingDir.mult(interpolation * v));

			if (testCollision(newPos,movingDir) || !canMoveAt(newPos)) {
				endMove();
				return;
			}

			characterNode.setLocalTranslation(newPos);
		}
	}

	/**
	 * test si il peu se déplacer la
	 * 
	 * @param newPos
	 * @return
	 */
	protected abstract boolean canMoveAt(Vector3f newPos);

	/**
	 * Test de colision
	 * 
	 * @param dir
	 * @param dir 
	 * @return
	 */
	protected abstract boolean testCollision(Vector3f newPos, Vector3f dir);

	/**
	 * Met fin au déplacement
	 */
	protected void endMove() {
		moving = Moving.stop;
		if (characterNode != null) {
			startAnimation(CharacterAnimation.idle);
			x = characterNode.getLocalTranslation().x;
			z = characterNode.getLocalTranslation().z;
		}
	}

	/* ********************************************************** *
	 * * 					GETTERS 							* * 
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
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the modelType
	 */
	public CharacterModel getModelType() {
		return modelType;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @return the velocity
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param modelType
	 *            the modelType to set
	 */
	public void setModelType(CharacterModel modelType) {
		this.modelType = modelType;
		rebuild();
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;

	}

	/**
	 * 
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * @return the walk
	 */
	public boolean isWalk() {
		return walk;
	}

	/**
	 * the walk to set
	 * @param walk
	 */
	public void setWalk(boolean walk) {
		this.walk = walk;
	}

	/**
	 * @return the skin
	 */
	public ColorRGBA getSkin() {
		return skin;
	}

	/**
	 * @return the skinAmbient
	 */
	public float getSkinAmbient() {
		return skinAmbient;
	}

	/**
	 * @return the cloth
	 */
	public ColorRGBA getTopCloth() {
		return topCloth;
	}

	/**
	 * @return the clothAmbient
	 */
	public float getTopClothAmbient() {
		return topClothAmbient;
	}

	/**
	 * @param clothAmbient
	 *            the clothAmbient to set
	 */
	public void setTopClothAmbient(float clothAmbient) {
		this.topClothAmbient = clothAmbient;
	}

	/**
	 * @return the hairAmbient
	 */
	public float getHairAmbient() {
		return hairAmbient;
	}

	/**
	 * @param hairAmbient
	 *            the hairAmbient to set
	 */
	public void setHairAmbient(float hairAmbient) {
		this.hairAmbient = hairAmbient;
	}

	/**
	 * @return the shoesAmbient
	 */
	public float getShoesAmbient() {
		return shoesAmbient;
	}

	/**
	 * @param shoesAmbient
	 *            the shoesAmbient to set
	 */
	public void setShoesAmbient(float shoesAmbient) {
		this.shoesAmbient = shoesAmbient;
	}

	/**
	 * @return the hair
	 */
	public ColorRGBA getHair() {
		return hair;
	}

	/**
	 * @return the shoes
	 */
	public ColorRGBA getShoes() {
		return shoes;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * change la valeur de l'ambient de 
	 * @param value
	 */
	public void setSkinAmbient(float skinAmbient) {
		this.skinAmbient = skinAmbient;
	}

	/**
	 * Renvoie la position actuelle reel du personnage à l'ecran
	 * @return
	 */
	protected Vector3f getGraphicPosition() {
		return characterNode == null ? new Vector3f(this.x, world.getHeightAt(
				this.x, this.z), this.z) : characterNode.getLocalTranslation();

	}

	public int getHairCut() {
		return hairCut;
	}

	public void setHairCut(int hairCut) {
		this.hairCut = hairCut;
	}

	/**
	 * @return the bottomClothAmbient
	 */
	public float getBottomClothAmbient() {
		return bottomClothAmbient;
	}

	/**
	 * @param bottomClothAmbient the bottomClothAmbient to set
	 */
	public void setBottomClothAmbient(float bottomClothAmbient) {
		this.bottomClothAmbient = bottomClothAmbient;
	}

	/**
	 * @return the bottomCloth
	 */
	public ColorRGBA getBottomCloth() {
		return bottomCloth;
	}

	/**
	 * Renvoie le nopm afficher
	 * @return
	 */
	public String getName() {
		return displayName;
	}

	public void setName(String name) {
		this.displayName = name;
	}

}
