package model;

import java.awt.image.BufferedImage;

public class Image {
	private int width = 0;
	private int height = 0;
	private String path = "";
	private BufferedImage buffer = null;
	private int matches = 0;
	
	public int getMatches() {
		return matches;
	}
	public void setMatches(int matches) {
		this.matches = matches;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public BufferedImage getBuffer() {
		return buffer;
	}
	public void setBuffer(BufferedImage buffer) {
		this.buffer = buffer;
	}

}
