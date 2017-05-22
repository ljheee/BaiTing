package com.baiting.bean;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TabMenu implements IBaseBean {
	
	private static final long serialVersionUID = 258320863135626711L;
	
	private Integer index;
	private String name;
	private String text;
	private String message;
	private JPanel panel;
	private JLabel label;
	private int onClick;
	
	public TabMenu() {
		
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public int getOnClick() {
		return onClick;
	}

	public void setOnClick(int onClick) {
		this.onClick = onClick;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

}
