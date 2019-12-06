package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public class BlockImageByUserId<T> {
	
	ImageImpl image;
	Long prevId;
	Long nextId;
    
	public BlockImageByUserId() {
		super();
	}
	
    public BlockImageByUserId(ImageImpl image, Long prevId, Long nextId) {
        setImage(image);
        setPrevId(prevId);
        setNextId(nextId);
    }

	public ImageImpl getImage() {
		return image;
	}

	public void setImage(ImageImpl image) {
		this.image = image;
	}

	public Long getPrevId() {
		return prevId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	public Long getNextId() {
		return nextId;
	}

	public void setNextId(Long nextId) {
		this.nextId = nextId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((nextId == null) ? 0 : nextId.hashCode());
		result = prime * result + ((prevId == null) ? 0 : prevId.hashCode());
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
		BlockImageByUserId other = (BlockImageByUserId) obj;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (nextId == null) {
			if (other.nextId != null)
				return false;
		} else if (!nextId.equals(other.nextId))
			return false;
		if (prevId == null) {
			if (other.prevId != null)
				return false;
		} else if (!prevId.equals(other.prevId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BlockImageByUserId [image=" + image + ", prevId=" + prevId + ", nextId=" + nextId + "]";
	}
    
}
