package es.udc.fi.dc.fd.model;

public enum SexCriteriaEnum {

	FEMALE("Female"), MALE("Male"), OTHER("Other"), ANY("Any");

	private String code;

	SexCriteriaEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static SexCriteriaEnum fromCode(String code) {
		for (SexCriteriaEnum criteria : SexCriteriaEnum.values()) {
			if (criteria.getCode().equals(code)) {
				return criteria;
			}
		}
		throw new UnsupportedOperationException("The code " + code + " is not supported!");
	}
}
