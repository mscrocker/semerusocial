package es.udc.fi.dc.fd.service;

import java.util.List;

public class Block<T> {
	
	private List<T> images;
    private boolean existMoreImages;

    public Block() {
    	super();
    }
    
    public Block(List<T> items, boolean existMoreImages) {
        setImages(items);
        setExistMoreImages(existMoreImages);
    }
    
	public List<T> getImages() {
		return images;
	}
	
	public void setImages(List<T> images) {
		this.images = images;
	}
    
    public boolean getExistMoreImages() {
        return existMoreImages;
    }

	public void setExistMoreImages(boolean existMoreImages) {
		this.existMoreImages = existMoreImages;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (existMoreImages ? 1231 : 1237);
		result = prime * result + ((images == null) ? 0 : images.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (existMoreImages != other.existMoreImages)
			return false;
		if (images == null) {
			if (other.images != null)
				return false;
		} else if (!images.equals(other.images))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Block [images=" + images + ", existMoreImages=" + existMoreImages + "]";
	}
	
}
