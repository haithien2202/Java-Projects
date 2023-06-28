package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// TODO: Auto-generated Javadoc
/**
 * Stores images to be used in the Renderer module. Also handles scaling of the images.
 *
 * @author Alex Eastlake, 300544079
 */
public class RendererImages {
	
	/**
	 * Fields for image icons.
	 */
	public static BufferedImage freeTileIcon;
	
	/** The wall tile icon. */
	public static BufferedImage wallTileIcon;
	
	/** The info tile icon. */
	public static BufferedImage infoTileIcon;
	
	/** The exit tile icon. */
	public static BufferedImage exitTileIcon;
	
	/** The treasure tile icon. */
	public static BufferedImage treasureTileIcon;
	
	/** The extra tile icon. */
	public static BufferedImage extraTileIcon;
	
	/** The exit lock tile icon. */
	public static BufferedImage exitLockTileIcon;
	
	/** The locked door tile cyan icon. */
	public static BufferedImage lockedDoorTileCyanIcon;
	
	/** The locked door tile red icon. */
	public static BufferedImage lockedDoorTileRedIcon;
	
	/** The locked door tile green icon. */
	public static BufferedImage lockedDoorTileGreenIcon;
	
	/** The key tile cyan icon. */
	public static BufferedImage keyTileCyanIcon;
	
	/** The key tile red icon. */
	public static BufferedImage keyTileRedIcon;
	
	/** The key tile green icon. */
	public static BufferedImage keyTileGreenIcon;
	
	/** The regular chap icon R. */
	public static BufferedImage regularChapIconR;
	
	/** The regular chap icon L. */
	public static BufferedImage regularChapIconL;
	
	/** The regular chap icon U. */
	public static BufferedImage regularChapIconU;
	
	/** The regular chap icon D. */
	public static BufferedImage regularChapIconD;
	
	/** The variant chap icon R. */
	public static BufferedImage variantChapIconR;
	
	/** The variant chap icon L. */
	public static BufferedImage variantChapIconL;
	
	/** The variant chap icon U. */
	public static BufferedImage variantChapIconU;
	
	/** The variant chap icon D. */
	public static BufferedImage variantChapIconD;
	
	/** The unknown tile icon. */
	public static BufferedImage unknownTileIcon;
	
	/**
	 * Static initializer for reading the image files.
	 */
	static {
		try {
			freeTileIcon = ImageIO.read(RendererImages.class.getResource("freeTile.png"));
			wallTileIcon = ImageIO.read(RendererImages.class.getResource("wallTile.png"));
			infoTileIcon = ImageIO.read(RendererImages.class.getResource("infoTile.png"));
			exitTileIcon = ImageIO.read(RendererImages.class.getResource("exitTile.png"));
			treasureTileIcon = ImageIO.read(RendererImages.class.getResource("treasureTile.png"));
			extraTileIcon = ImageIO.read(RendererImages.class.getResource("extraTile.png"));
			exitLockTileIcon = ImageIO.read(RendererImages.class.getResource("exitLockTile.png"));
			lockedDoorTileCyanIcon = ImageIO.read(RendererImages.class.getResource("lockedDoorTileCyan.png"));
			lockedDoorTileRedIcon = ImageIO.read(RendererImages.class.getResource("lockedDoorTileRed.png"));
			lockedDoorTileGreenIcon = ImageIO.read(RendererImages.class.getResource("lockedDoorTileGreen.png"));
			keyTileCyanIcon = ImageIO.read(RendererImages.class.getResource("keyTileCyan.png"));
			keyTileRedIcon = ImageIO.read(RendererImages.class.getResource("keyTileRed.png"));
			keyTileGreenIcon = ImageIO.read(RendererImages.class.getResource("keyTileGreen.png"));
			regularChapIconR = ImageIO.read(RendererImages.class.getResource("regularChapR.png"));
			regularChapIconL = ImageIO.read(RendererImages.class.getResource("regularChapL.png"));
			regularChapIconU = ImageIO.read(RendererImages.class.getResource("regularChapU.png"));
			regularChapIconD = ImageIO.read(RendererImages.class.getResource("regularChapD.png"));
			variantChapIconR = ImageIO.read(RendererImages.class.getResource("variantChapR.png"));
			variantChapIconL = ImageIO.read(RendererImages.class.getResource("variantChapL.png"));
			variantChapIconU = ImageIO.read(RendererImages.class.getResource("variantChapU.png"));
			variantChapIconD = ImageIO.read(RendererImages.class.getResource("variantChapD.png"));
			unknownTileIcon = ImageIO.read(RendererImages.class.getResource("unknownTile.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reloads all the renderer images scaled for a given width and height.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public static void scale(int width, int height) {
		freeTileIcon = scaleImage(freeTileIcon, "freeTile.png", width, height);
		wallTileIcon = scaleImage(wallTileIcon, "wallTile.png", width, height);
		infoTileIcon = scaleImage(infoTileIcon, "infoTile.png", width, height);
		exitTileIcon = scaleImage(exitTileIcon, "exitTile.png", width, height);
		treasureTileIcon = scaleImage(treasureTileIcon, "treasureTile.png", width, height);
		extraTileIcon = scaleImage(extraTileIcon, "extraTile.png", width, height);
		exitLockTileIcon = scaleImage(exitTileIcon, "exitLockTile.png", width, height);
		lockedDoorTileCyanIcon = scaleImage(lockedDoorTileCyanIcon, "lockedDoorTileCyan.png", width, height);
		lockedDoorTileRedIcon = scaleImage(lockedDoorTileRedIcon, "lockedDoorTileRed.png", width, height);
		lockedDoorTileGreenIcon = scaleImage(lockedDoorTileGreenIcon, "lockedDoorTileGreen.png", width, height);
		keyTileCyanIcon = scaleImage(keyTileCyanIcon, "keyTileCyan.png", width, height);
		keyTileRedIcon = scaleImage(keyTileRedIcon, "keyTileRed.png", width, height);
		keyTileGreenIcon = scaleImage(keyTileGreenIcon, "keyTileGreen.png", width, height);
		regularChapIconR = scaleImage(regularChapIconR, "regularChapR.png", width, height);
		regularChapIconL = scaleImage(regularChapIconL, "regularChapL.png", width, height);
		regularChapIconU = scaleImage(regularChapIconU, "regularChapU.png", width, height);
		regularChapIconD = scaleImage(regularChapIconD, "regularChapD.png", width, height);
		variantChapIconR = scaleImage(variantChapIconR, "variantChapR.png", width, height);
		variantChapIconL = scaleImage(variantChapIconL, "variantChapL.png", width, height);
		variantChapIconU = scaleImage(variantChapIconU, "variantChapU.png", width, height);
		variantChapIconD = scaleImage(variantChapIconD, "variantChapD.png", width, height);
		unknownTileIcon = scaleImage(unknownTileIcon, "unknownTile.png", width, height);	
	}
	
	/**
	 * Takes a BufferedImage and a image file name to scale to a given width and height. Uses the passed BufferedImage as a backup if it can't scale the image file.
	 * 
	 * @param originalBufImg Backup in case of failed scaling
	 * @param imgName Name of image file to scale
	 * @param width Width to scale the image to
	 * @param height Height to scale the image to
	 * @return Scaled BufferedImage
	 */
	private static BufferedImage scaleImage(BufferedImage originalBufImg, String imgName, int width, int height) {
		Image scaledImg = null;
		
		try {
			scaledImg = ImageIO.read(GameRenderer.class.getResource(imgName)).getScaledInstance(width, height, java.awt.Image.SCALE_FAST);
		} catch (IOException e) {
			return originalBufImg;
		}
		
		BufferedImage scaledBufImg = new BufferedImage(scaledImg.getWidth(null), scaledImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
		scaledBufImg.getGraphics().drawImage(scaledImg, 0, 0, null);
		
		return scaledBufImg;
	}
}