package es.udc.fi.dc.fd.dtos;

import java.util.List;

public class BlockDto<T> {

  private List<T> elements;

  private boolean existMoreElements;

  public BlockDto() {
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

  /**
   * Default constructor of the DTO used for the block.
   *
   * @param elements          The list of the elements
   * @param existMoreElements Whether there would be more elements if the next page were queried
   */
  public BlockDto(List<T> elements, boolean existMoreElements) {
    super();
    this.elements = elements;
    this.existMoreElements = existMoreElements;
  }


}
