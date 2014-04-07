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
package client.utils;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
/*
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
*/
/**
 * sauvegarde les info d'un shader 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * Code sous licence LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL P L+ E--- W++ N K- w-- M- PS PE-- Y- PGP- t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- y-
 */
public class ShaderInfo implements Savable {
	String frag="void", vert="void";

	/**
	 * 
	 */
	public ShaderInfo() {}
	
	/**
	 * @param fragmentShader
	 * @param vertexShader
	 */
	public ShaderInfo(String frag, String vert) {
		this.frag = frag;
		this.vert = vert;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.export.Savable#getClassTag()
	 */
	//@Override
	public Class<? extends ShaderInfo> getClassTag() {
		return ShaderInfo.class;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.export.Savable#read(com.jme.util.export.JMEImporter)
	 */
	/*@Override
	public void read(JMEImporter im) throws IOException {
		InputCapsule capsule = im.getCapsule(this);	
		this.frag = capsule.readString("frag", "void");
		this.vert = capsule.readString("vert", "void");
	}
*/
	/* (non-Javadoc)
	 * @see com.jme.util.export.Savable#write(com.jme.util.export.JMEExporter)
	 */
	/*@Override
	public void write(JMEExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(frag, "frag", "void");
        capsule.write(vert, "vert", "void");
	}
	*/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return frag.hashCode();
	}

    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
