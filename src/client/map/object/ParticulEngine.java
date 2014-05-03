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

import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.graphic.GraphicReflexEditable;
import client.map.World;
import client.task.GraphicsAddRemoveSceneTask;
import com.jme3.effect.ParticleMesh;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/*
import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Rectangle;
import com.jme.math.Ring;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.effects.particles.ParticleSystem.EmitType;
*/
/**
 * Moteur de particule configurable
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class ParticulEngine implements MapGraphics, GraphicReflexEditable,Callable<Void> {
	private static final Logger logger = Logger.getLogger("ParticulEngine");
	private World world;
	private int id;
	private int version = -1;
	private GraphicsAddRemoveSceneTask task;
	private ParticleMesh mesh;

	/**
	 * Tous les parametre du moteur de particul, ca va faire mal.
	 */
	@Editable(type=FieldEditType.enumerate,applyMethod="rebuild")
	//private EmitType emitType = EmitType.Point; // zone d'emission
	//@Editable(type=FieldEditType.integer,applyMethod="rebuild")
	private int factoryNumber = 300; // nombre de particule du moteur
	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	private float emitWidth, emitHeight; // largeur et hauteur dans le cas d'une zone en rectangle
	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	private float emitInnerRadius,emitOutterRadius; // rayon dans le cas d'un emiter en ring
	@Editable(type=FieldEditType.vertex,applyMethod="rebuild")
	private Vector3f emitDirection = new Vector3f(0, 1, 0);
	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	private float initialVelocity=.006f;
	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	private float startMass=.006f, startSize=1, endMass=0.004f, endSize=.5f;
	@Editable(type=FieldEditType.color,applyMethod="rebuild")
	private ColorRGBA startColor=new ColorRGBA(1,0,0,1), endColor=new ColorRGBA(1,0,0,.3f);
	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	private float minimumLifeTime = 1200, maximumLifeTime = 1400;
	@Editable(type=FieldEditType.realinterval,applyMethod="rebuild",realMinValue=-180,realMaxValue=0)
	private float minimumAngle = 0, maximumAngle=10;
	@Editable(type=FieldEditType.bool,applyMethod="rebuild")
	private boolean controlFlow = true, cameraFacing = true;
	@Editable(type=FieldEditType.vertex,applyMethod="rebuild")
	private Vector3f location = new Vector3f(0,0,0);
	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	private float speed=1;
	@Editable(type=FieldEditType.real,applyMethod="rebuild")
	private float particlesPerSecondVariante=0;
	@Editable(type=FieldEditType.integer,applyMethod="rebuild")
	private int particlesPerSecond = 50;



	/**
	 * @param id 
	 * @param world 
	 * 
	 */
	public ParticulEngine(World world, int id) {
            
            System.out.println("ParticleEngine-> ParticulEngine(w,int) : manquante !!");
		this.world = world;
		this.id = id;
		rebuild();
		//world.getGame().getHud().getLoading().add(this);
	}


	/**
	 * rebuild du moteur de particule
	 */
	private void rebuild() {
             System.out.println("ParticleEngine-> rebuild() : vide !!");
		//GameTaskQueueManager.getManager().update(this);
		

	}
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		logger.fine("construction de "+getKey());
		if (mesh == null) createMesh();
		/*if (emitType == EmitType.Line || emitType == EmitType.Geometry)
			emitType = EmitType.Point;
		mesh.setEmitType(emitType);
		switch (emitType) {
		case Rectangle : mesh.setGeometry(new Rectangle(
				new Vector3f(-emitWidth/2, 0, -emitHeight/2),
				new Vector3f(emitWidth/2, 0, -emitHeight/2),
				new Vector3f(-emitWidth/2, 0, emitHeight/2)));
		break;
		case Ring : mesh.setGeometry(new Ring(
				new Vector3f(), Vector3f.UNIT_Y, 
				emitInnerRadius, emitOutterRadius));
		break;
		case Line : logger.warning("Shionn n'as pas encore fait le moteur de particule en ligne");break;
		case Point:	
		default: break;
		}
		mesh.setEmissionDirection(emitDirection.normalize());

		if (mesh.getNumParticles() != factoryNumber)
			mesh.recreate(factoryNumber);
		
		mesh.setInitialVelocity(initialVelocity);
		mesh.setStartMass(startMass);
		mesh.setStartSize(startSize);
		mesh.setStartColor(startColor);
		mesh.setEndMass(endMass);
		mesh.setEndSize(endSize);
		mesh.setEndColor(endColor);

		mesh.setMinimumLifeTime(minimumLifeTime);
		mesh.setMaximumLifeTime(maximumLifeTime);
		mesh.setMinimumAngle(minimumAngle*FastMath.DEG_TO_RAD);
		mesh.setMaximumAngle(maximumAngle*FastMath.DEG_TO_RAD);

		mesh.getParticleController().setControlFlow(controlFlow);
		mesh.setCameraFacing(cameraFacing);
		mesh.setLocalTranslation(
				location.add(0,world.getHeightAt(location.x,location.z), 0));

		mesh.setSpeed(speed);
		mesh.setReleaseRate(particlesPerSecond );
		mesh.setReleaseVariance(particlesPerSecondVariante);

		mesh.setLightCombineMode(LightCombineMode.Off);
		mesh.setIsCollidable(false);
		mesh.setParticlesInWorldCoords(false);

		mesh.warmUp(factoryNumber/5);
		
		mesh.setModelBound(new OrientedBoundingBox());
		mesh.updateModelBound();
		
		world.getGame().getHud().getLoading().remove(this);	*/	return null;
	}

	/**
	 * creait la base du moteur de particul
	 */
	private void createMesh() {
            
            System.out.println("ParticulEngine->createMesh() : vide !!");
		/*mesh = ParticleFactory.buildParticles(getKey(), factoryNumber );
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setEnabled(true);
		bs.setBlendEnabled(true);
		bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		bs.setDestinationFunction(BlendState.DestinationFunction.One);
		bs.setTestEnabled(true);
		bs.setTestFunction(BlendState.TestFunction.GreaterThan);
	    mesh.setRenderState(bs);
		
		ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zs.setEnabled(true);
		zs.setWritable(false);
		mesh.setRenderState(zs);		

		mesh.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		
		Texture t = TextureManager.loadTexture("flaresmall.jpg",  Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);

		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(t);
		ts.setEnabled(true);
		mesh.setRenderState(ts);

		mesh.updateRenderState();
		
		mesh.setModelBound(new OrientedBoundingBox());
		mesh.updateModelBound();
		
		addToRenderTask();
                */
	}


	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
            System.out.println("ParticulEngine->addData(pck) : manquante!!");
		//pck.putEnum(emitType);
		pck.putFloat(location.x,location.y,location.z);
		pck.putInt(factoryNumber,particlesPerSecond);
		pck.putFloat(emitWidth,emitHeight,emitInnerRadius,emitOutterRadius);
		pck.putFloat(emitDirection.x,emitDirection.y,emitDirection.z);
		pck.putFloat(minimumAngle,maximumAngle,minimumLifeTime,maximumLifeTime);
		pck.putFloat(initialVelocity,speed,particlesPerSecondVariante);
		pck.putFloat(startMass,startSize,startColor.r,startColor.g,startColor.b,startColor.a);
		pck.putFloat(endMass,endSize,endColor.r,endColor.g,endColor.b,endColor.a);
		pck.putBoolean(controlFlow,cameraFacing);
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.particul.prefix()+id;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return version ;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		version = message.getInt();
		
		//emitType = Pck.readEnum(EmitType.class, message);
		location.x = message.getFloat();
		location.y = message.getFloat();
		location.z = message.getFloat();
		factoryNumber = message.getInt();
		particlesPerSecond = message.getInt();
		emitWidth = message.getFloat();
		emitHeight = message.getFloat();
		emitInnerRadius = message.getFloat();
		emitOutterRadius = message.getFloat();
		emitDirection.x = message.getFloat();
		emitDirection.y = message.getFloat();
		emitDirection.z = message.getFloat();
		minimumAngle = message.getFloat();
		maximumAngle = message.getFloat();
		minimumLifeTime = message.getFloat();
		maximumLifeTime = message.getFloat();
		initialVelocity = message.getFloat();
		speed = message.getFloat();
		particlesPerSecondVariante = message.getFloat();
		startMass = message.getFloat();
		startSize = message.getFloat();
		startColor.r = message.getFloat();
		startColor.g = message.getFloat();
		startColor.b = message.getFloat();
		startColor.a = message.getFloat();
		endMass = message.getFloat();
		endSize = message.getFloat();
		endColor.r = message.getFloat();
		endColor.g = message.getFloat();
		endColor.b = message.getFloat();
		endColor.a = message.getFloat();
		controlFlow = Pck.readBoolean(message);
		cameraFacing = Pck.readBoolean(message);
		
		rebuild();
		
	}

	/* ********************************************************** *
	 * * 					Graphics - IMPLEMENTS 				* *
	 * ********************************************************** */



	/* (non-Javadoc)
	 * @see client.interfaces.graphic.Graphic#addToRenderTask()
	 */
	@Override
	public void addToRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask(this, world);
		/*if (visible)*/ 
		/*if (mesh!=null && mesh.getParent() == null)
			task.add(); */
	}

	/* (non-Javadoc)
	 * @see client.interfaces.graphic.Graphic#getGraphic()
	 */
	@Override
	public Spatial getGraphic() {
		return null;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.graphic.Graphic#removeFromRenderTask()
	 */
	@Override
	public void removeFromRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask(this, world);
		task.remove();
	}
	/* ********************************************************** *
	 * * 					MapGRAPHICS - IMPLEMENTS 				* *
	 * ********************************************************** */

	/* (non-Javadoc)
	 * @see client.map.object.MapGraphics#update(float)
	 */
	@Override
	public void update(float interpolation) {
		
	}

	/* (non-Javadoc)
	 * @see client.map.object.MapGraphics#updateY()
	 */
	@Override
	public void updateY() {
		/*if (mesh != null)
			mesh.setLocalTranslation(location.add(
					0,world.getHeightAt(location.x,location.z), 0)); */
	}
	
	/* ********************************************************** *
	 * *					graphicEditable						* *
	 * ********************************************************** */

	/* (non-Javadoc)
	 * @see client.interfaces.graphic.GraphicEditable#setGeometrics(float, float, float, float, float, float, float)
	 */
	@Override
	public void setGeometrics(float x, float y, float z, float rx, float ry,
			float rz, float scale) {
		if (!Float.isNaN(x)) location.x = x;
		if (!Float.isNaN(z)) location.z = z;
		/*if (mesh != null)
			mesh.setLocalTranslation(location.add(
					0,world.getHeightAt(location.x,location.z), 0)); */

	}


	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return location.x;
	}


	@Override
	public float getZ() {
		// TODO Auto-generated method stub
		return location.z;
	}



	

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
