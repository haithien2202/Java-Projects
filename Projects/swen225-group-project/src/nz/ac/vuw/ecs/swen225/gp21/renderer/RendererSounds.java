package nz.ac.vuw.ecs.swen225.gp21.renderer;

import java.io.IOException;

import javax.sound.sampled.*;

// TODO: Auto-generated Javadoc
/**
 * Plays sounds for a GameRenderer.
 *
 * @author Alex Eastlake, 300544079
 */
public class RendererSounds {
	
	/**
	 * Clip and AudioInputStream for footstep sound effect.
	 */
	private Clip footstepClip;
	
	/** The footstep audio stream. */
	private AudioInputStream footstepAudioStream;
	
	/**
	 * Clip and AudioInputStream for interaction sound effect.
	 */
	private Clip interactClip;
	
	/** The interact audio stream. */
	private AudioInputStream interactAudioStream;
	
	/**
	 * Creates a RendererSounds instance by obtaining clips and input streams for audio.
	 */
	public RendererSounds() {
		try {
			footstepClip = AudioSystem.getClip();
			footstepAudioStream = AudioSystem.getAudioInputStream(RendererSounds.class.getResource("footstep.wav"));
			footstepClip.open(footstepAudioStream);
			footstepClip.setFramePosition(footstepClip.getFrameLength());
			footstepClip.start();
			
			interactClip = AudioSystem.getClip();
			interactAudioStream = AudioSystem.getAudioInputStream(RendererSounds.class.getResource("interact.wav"));
			interactClip.open(interactAudioStream);
			interactClip.setFramePosition(interactClip.getFrameLength());
			interactClip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
		}
	}
	
	/**
	 * Plays the footstep sound effect.
	 */
	public void playFootstep() {
		footstepClip.setFramePosition(0);
		footstepClip.start();
	}
	
	/**
	 * Plays the interaction sound effect.
	 */
	public void playInteract() {
		interactClip.setFramePosition(0);
		interactClip.start();
	}
}