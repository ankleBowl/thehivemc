
package net.rypixel.hiveLobby;

import org.bukkit.util.Vector;

public class Constants {

	public static Vector[] launchpads1 = new Vector[8];
	public static Vector[] launchpads2 = new Vector[8];
	public static Vector[] launchpads3 = new Vector[8];
	
	public void init() {
		launchpads1[0] = new Vector(-20.5, 21, 0.5);
		launchpads1[1] = new Vector(-14.5, 21, -14.5);
		launchpads1[2] = new Vector(0.5, 21, -19.5);
		launchpads1[3] = new Vector(15.5, 21, -14.5);
		launchpads1[4] = new Vector(21.5, 21, 0.5);
		launchpads1[5] = new Vector(15.5, 21, 15.5);
		launchpads1[6] = new Vector(0.5, 21, 20.5);
		launchpads1[7] = new Vector(-14.5, 21, 15.5);
		
		launchpads2[0] = new Vector(-45.5, 19, 0.5);
		launchpads2[1] = new Vector(-39.5, 19, -22.5);
		launchpads2[2] = new Vector(0.5, 19, -45.5);
		launchpads2[3] = new Vector(40.5, 19, -22.5);
		launchpads2[4] = new Vector(46.5, 19, 0.5);
		launchpads2[5] = new Vector(40.5, 19, 23.5);
		launchpads2[6] = new Vector(0.5, 19, 46.5);
		launchpads2[7] = new Vector(-39.5, 19, 23.5);
		
		launchpads3[0] = new Vector(0.5, 18, 68.5);
		launchpads3[1] = new Vector(-40.5, 18, 68.5);
		launchpads3[2] = new Vector(-67.5, 18, 41.5);
		launchpads3[3] = new Vector(-67.5, 18, 0.5);
		launchpads3[4] = new Vector(-40.5, 18, -67.5);
		launchpads3[5] = new Vector(0.5, 18, -67.5);
		launchpads3[6] = new Vector(41.5, 18, -67.5);
		launchpads3[7] = new Vector(68.5, 18, -40.5);
		launchpads3[8] = new Vector(68.5, 18, 0.5);
		launchpads3[9] = new Vector(68.5, 18, 41.5);
	}
	
}
