/**
 * 
 */
package com.fidelis.argface;

/**
 * Sample CLI.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class SampleCLI {
	private final String [] usageText = {
			"Usage: sample [--option] <args>...",
			"Options: --option an option"
	};
	private ArgFace argFace;
	
	public boolean init () {
		argFace = ArgPrototype.create(usageText, this);
		if (argFace == null) {
			return false;
		}
		return true;
	}

}
