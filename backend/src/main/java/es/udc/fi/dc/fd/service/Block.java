package es.udc.fi.dc.fd.service;

import java.util.List;

public class Block<T> {

	private List<T> elements;

	private boolean existMoreElements;

	public Block() {
		super();
	}

	public List<T> getElements() {
		return elements;
	}

	public void setElements(List<T> elements) {
		this.elements = elements;
	}

	public boolean isExistMoreElements() {
		return existMoreElements;
	}

	public void setExistMoreElements(boolean existMoreElements) {
		this.existMoreElements = existMoreElements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
		result = prime * result + (existMoreElements ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Block other = (Block) obj;
		if (elements == null) {
			if (other.elements != null) {
				return false;
			}
		} else if (!elements.equals(other.elements)) {
			return false;
		}
		if (existMoreElements != other.existMoreElements) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Block [elements=" + elements + ", existMoreElements=" + existMoreElements + "]";
	}

	public Block(List<T> elements, boolean existMoreElements) {
		super();
		this.elements = elements;
		this.existMoreElements = existMoreElements;
	}


}
