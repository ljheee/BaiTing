package com.baiting.font;

import java.awt.Font;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

import com.baiting.IMusic;

public class MusicFont extends Font implements IMusic {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2613253870058968928L;

	public MusicFont(String name, int style, int size) {
		super(name,style,size);
	}
	
	protected MusicFont(Font font) {
		super(font);
	}
	
	public MusicFont(Map<? extends Attribute, ?> attributes) {
		super(attributes);
	}

	
	
}
