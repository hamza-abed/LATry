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
package client.videoPlayer;

/*
 * This file is part of VLCJ.
 * has been modified to reach LA_GAME requirements
 * @author Hamza ABED hamza.abed.professionel@gmail.com
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009, 2010, 2011, 2012, 2013, 2014 Caprica Software Limited.
 */


import client.LaGame;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Basic test showing how to use a direct media player component.
 * <p>
 * This test shows how to render the video buffer into a {@link JComponent} via a
 * {@link BufferedImage} inside a {@link RenderCallbackAdapter}.
 * <p>
 * Some applications may like to access the native video buffer directly by
 * overriding {@link DirectMediaPlayerComponent#display(com.sun.jna.Memory)} or
 * providing an implementation of a {@link RenderCallback} via {@link DirectMediaPlayerComponent#onGetRenderCallback}.
 * <p>
 * This test also shows how to paint a lightweight overlay on top of the video.
 */
@SuppressWarnings("serial")
public class DirectMediaPlayerComponentTest extends VlcjTest {

    /**
     * Media player component.
     */
    private final DirectMediaPlayerComponent mediaPlayerComponent;

    /**
     *
     */
    private final int width = 720;

    /**
     *
     */
    private final int height = 480;

    /**
     *
     */
  //  private final JPanel panel;

    /**
     *
     */
    private final BufferedImage image;

    
 //   private VideoPlayer mainGame;
    private LaGame mainGame;
    /**
     * Application entry point.
     *
     * @param args
     */
    
    /*
    public static void main(String[] args) {
        args=new String[]{"C:\\Wildlife.wmv"};
        if(args.length != 1) {
            System.out.println("Specify an mrl");
            System.exit(1);
        }
 NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC"
            );
 
        final String mrl = args[0];

     

       
                new DirectMediaPlayerComponentTest().start(mrl);
         
    }
    
 */   
     public DirectMediaPlayerComponentTest() {
     
       
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);

        BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(width, height);
            }
        };

        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
                System.out.println("onGetRenderCallback");
                return new TestRenderCallbackAdapter();
            }
        };

    }

    /**
     * Create a new test.
     */
     /*
    public DirectMediaPlayerComponentTest(VideoPlayer main) {
     
        mainGame=main;
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);

        BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(width, height);
            }
        };

        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
              
                return new TestRenderCallbackAdapter();
            }
        };

    } */
    
     public DirectMediaPlayerComponentTest(LaGame main) {
     
        mainGame=main;
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);

        BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(width, height);
            }
        };

        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
              
                return new TestRenderCallbackAdapter();
            }
        };

    }

    /**
     * Start playing a movie.
     *
     * @param mrl mrl
     */
    public void start(String mrl) {
        // One line of vlcj code to play the media...
        
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
        
        
    }
    /*
     * Pause la lecture de la vidoe
     */
   public void pause()
   {
       mediaPlayerComponent.getMediaPlayer().pause();
       
   }
   
   public void stop()
   {
       mediaPlayerComponent.getMediaPlayer().stop();
   }
   
   /*
    * conitnue la lecture de la video
    */
   public void release()
   {
       mediaPlayerComponent.getMediaPlayer().play();
   }

    
  public BufferedImage returnImage()
  { 
      return mediaPlayerComponent.getMediaPlayer().getSnapshot();
  }
    private class TestRenderCallbackAdapter extends RenderCallbackAdapter {

        private TestRenderCallbackAdapter() {
            super(new int[width * height]);
        }

        @Override
        protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
            // Simply copy buffer to the image and repaint
          //  System.out.println("new Image !!");
            
            image.setRGB(0, 0, width, height, rgbBuffer, 0, width);
            if(image==null)System.out.println("new Image null !!");
            else mainGame.apply(image);
            
            //panel.repaint();
        }
       
    }
}