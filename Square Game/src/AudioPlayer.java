import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer{
	private File sound;
	
	public AudioPlayer(String soundPath) {
		sound = new File(soundPath);
	}
	
	public static void main(String[] args) {
	}
	
	public void playSound() {
		try {
			Clip soundClip = AudioSystem.getClip();
			soundClip.open(AudioSystem.getAudioInputStream(sound));
			soundClip.start();
			//Thread.sleep(soundClip.getMicrosecondLength()/1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
