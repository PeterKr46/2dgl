package net.jgl2d.sprite.texture;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Texture2D
{
    private Vector hTex = new Vector();
    private int width = 0, height = 0;
    protected String zDateiname;

    public Texture2D(String pDateiname)
    {
        this.zDateiname = pDateiname;
        try {
            BufferedImage bimg = ImageIO.read(new File(zDateiname));
            width = bimg.getWidth();
            height = bimg.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGLTexture(GL2 gl)
    {
        boolean lDa = false;
        for (int i = 0; i < this.hTex.size(); i++) {
            if (((TexContainer)this.hTex.elementAt(i)).kGL2 == gl) {
                lDa = true;
            }
        }
        if (!lDa) {
            Texture lText = null;
            try {
                lText = TextureIO.newTexture(new File(this.zDateiname), true);
                lText.setTexParameteri(gl, 10242, 10497);
                lText.setTexParameteri(gl, 10243, 10497);
                lText.setTexParameterf(gl, GL.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
            } catch (Exception e) {
                System.out.println(e.toString() + ": " + this.zDateiname);
            }
            this.hTex.addElement(new TexContainer(lText, gl));
        }
    }

    public Texture getTexture(GL2 pGL2) {
        Texture glTexture = null;
        for (int i = 0; i < this.hTex.size(); i++) {
            if (((TexContainer)this.hTex.elementAt(i)).kGL2 == pGL2) {
                glTexture = ((TexContainer)this.hTex.elementAt(i)).hText;
            }
        }
        return glTexture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    class TexContainer
    {
        public Texture hText = null;
        public GL2 kGL2 = null;

        protected TexContainer(Texture pTex, GL2 pGL2) {
            this.hText = pTex; this.kGL2 = pGL2;
        }
    }
}