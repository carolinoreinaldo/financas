package com.financas.api.dto;

public class OptionDTO {

	private Object label;
	private Object value;

	public OptionDTO(Object label, Object value) {
		super();
		this.label = label;
		this.value = value;
	}

	public Object getLabel() {
		return label;
	}

	public void setLabel(Object label) {
		this.label = label;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "OptionDTO [label=" + label + ", value=" + value + "]";
	}

}
