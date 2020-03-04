package es.udc.fi.dc.fd.dtos;

import javax.validation.constraints.NotNull;

public class PremiumFormDto {

  @NotNull
  private boolean premium;

  public PremiumFormDto() {
    super();
  }

  public PremiumFormDto(boolean premium) {
    super();
    this.premium = premium;
  }

  public boolean isPremium() {
    return premium;
  }

  public void setPremium(boolean premium) {
    this.premium = premium;
  }

}
