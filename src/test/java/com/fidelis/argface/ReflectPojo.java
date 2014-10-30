/**
 *+
 *	ReflectPojo.java
 *	1.0.0  Oct 25, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * ReflectPojo
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ReflectPojo {
	
	private boolean privateBooleanField;
	private int privateIntField;
	private String privateStringField;
	private String[] privateStringArrayField = {
			"WHISKEY", "TANGO", "XRAY"
	};
	private String[] privateStringArrayFieldText = {
			"NOVEMBER", "KILO", "QUEBEC"
	};
	
	private boolean privateBooleanMethod;
	private int privateIntMethod;
	private String privateStringMethod;
	private String[] privateStringArrayMethod = {
			"HOTEL", "BRAVO", "MIKE"
	};
	private String[] privateStringArrayMethodText = {
			"SIERRA", "ZULU", "INDIA"
	};
	
	public ReflectPojo () {
	}
	
	public ReflectPojo (boolean privateBooleanField,
			int privateIntField, String privateStringField) {
		this.privateBooleanField = privateBooleanField;
		this.privateIntField = privateIntField;
		this.privateStringField = privateStringField;
	}
	
	public boolean booleanField () {
		return privateBooleanField;
	}
	
	public int intField () {
		return privateIntField;
	}
	
	public String stringField () {
		return privateStringField;
	}

	/**
	 * @return the privateBooleanMethod
	 */
	public boolean isPrivateBooleanMethod () {
		return privateBooleanMethod;
	}

	/**
	 * Sets the privateBooleanMethod for this class.
	 *
	 * @param privateBooleanMethod the privateBooleanMethod to set
	 */
	public void setPrivateBooleanMethod (boolean privateBooleanMethod) {
		this.privateBooleanMethod = privateBooleanMethod;
	}

	/**
	 * @return the privateIntMethod
	 */
	public int getPrivateIntMethod () {
		return privateIntMethod;
	}

	/**
	 * Sets the privateIntMethod for this class.
	 *
	 * @param privateIntMethod the privateIntMethod to set
	 */
	public void setPrivateIntMethod (int privateIntMethod) {
		this.privateIntMethod = privateIntMethod;
	}

	/**
	 * @return the privateStringMethod
	 */
	public String getPrivateStringMethod () {
		return privateStringMethod;
	}

	/**
	 * Sets the privateStringMethod for this class.
	 *
	 * @param privateStringMethod the privateStringMethod to set
	 */
	public void setPrivateStringMethod (String privateStringMethod) {
		this.privateStringMethod = privateStringMethod;
	}

	/**
	 * @return the privateStringArrayMethod
	 */
	public String[] getPrivateStringArrayMethod () {
		return privateStringArrayMethod;
	}

	/**
	 * Sets the privateStringArrayMethod for this class.
	 *
	 * @param privateStringArrayMethod the privateStringArrayMethod to set
	 */
	public void setPrivateStringArrayMethod (String[] privateStringArrayMethod) {
		this.privateStringArrayMethod = privateStringArrayMethod;
	}

	/**
	 * @return the privateStringArrayMethodText
	 */
	public String[] getPrivateStringArrayMethodText () {
		return privateStringArrayMethodText;
	}

	/**
	 * Sets the privateStringArrayMethodText for this class.
	 *
	 * @param privateStringArrayMethodText the privateStringArrayMethodText to set
	 */
	public void setPrivateStringArrayMethodText (
			String[] privateStringArrayMethodText) {
		this.privateStringArrayMethodText = privateStringArrayMethodText;
	}
	
}
	