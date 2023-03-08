package it.tndigitale.a4g.framework.client.custom;

import java.io.Serializable;
import java.util.Objects;

public class MittenteDto implements Serializable {
	private String correspondentType;

	private String address;

	private String admCode;

	private String aooCode;

	private String cap;

	private String city;

	private String description;

	private String email;

	private String fax;

	private Boolean isCommonAddress;

	private String location;

	private String name;

	private String nation;

	private String nationalIdentificationNumber;

	private String note;

	private String phoneNumber;

	private String preferredChannel;

	private String province;

	private String surname;

	private String type;

	private String vatNumber;

	public String getCorrespondentType() {
		return correspondentType;
	}

	public MittenteDto setCorrespondentType(TipologiaCorrispondente correspondentType) {
		if (correspondentType != null) {
			this.correspondentType = correspondentType.getCodicePitre();
		} else {
			this.correspondentType = null;
		}



		return this;
	}

	public String getAddress() {
		return address;
	}

	public MittenteDto setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getAdmCode() {
		return admCode;
	}

	public MittenteDto setAdmCode(String admCode) {
		this.admCode = admCode;
		return this;
	}

	public String getAooCode() {
		return aooCode;
	}

	public MittenteDto setAooCode(String aooCode) {
		this.aooCode = aooCode;
		return this;
	}

	public String getCap() {
		return cap;
	}

	public MittenteDto setCap(String cap) {
		this.cap = cap;
		return this;
	}

	public String getCity() {
		return city;
	}

	public MittenteDto setCity(String city) {
		this.city = city;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public MittenteDto setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public MittenteDto setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getFax() {
		return fax;
	}

	public MittenteDto setFax(String fax) {
		this.fax = fax;
		return this;
	}

	public Boolean getCommonAddress() {
		return isCommonAddress;
	}

	public MittenteDto setCommonAddress(Boolean commonAddress) {
		isCommonAddress = commonAddress;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public MittenteDto setLocation(String location) {
		this.location = location;
		return this;
	}

	public String getName() {
		return name;
	}

	public MittenteDto setName(String name) {
		this.name = name;
		return this;
	}

	public String getNation() {
		return nation;
	}

	public MittenteDto setNation(String nation) {
		this.nation = nation;
		return this;
	}

	public String getNationalIdentificationNumber() {
		return nationalIdentificationNumber;
	}

	public MittenteDto setNationalIdentificationNumber(String nationalIdentificationNumber) {
		this.nationalIdentificationNumber = nationalIdentificationNumber;
		return this;
	}

	public String getNote() {
		return note;
	}

	public MittenteDto setNote(String note) {
		this.note = note;
		return this;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public MittenteDto setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public String getPreferredChannel() {
		return preferredChannel;
	}

	public MittenteDto setPreferredChannel(String preferredChannel) {
		this.preferredChannel = preferredChannel;
		return this;
	}

	public String getProvince() {
		return province;
	}

	public MittenteDto setProvince(String province) {
		this.province = province;
		return this;
	}

	public String getSurname() {
		return surname;
	}

	public MittenteDto setSurname(String surname) {
		this.surname = surname;
		return this;
	}

	public String getType() {
		return type;
	}

	public MittenteDto setType(String type) {
		this.type = type;
		return this;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public MittenteDto setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MittenteDto that = (MittenteDto) o;
		return Objects.equals(address, that.address) &&
				Objects.equals(admCode, that.admCode) &&
				Objects.equals(aooCode, that.aooCode) &&
				Objects.equals(cap, that.cap) &&
				Objects.equals(city, that.city) &&
				Objects.equals(description, that.description) &&
				Objects.equals(email, that.email) &&
				Objects.equals(fax, that.fax) &&
				Objects.equals(isCommonAddress, that.isCommonAddress) &&
				Objects.equals(location, that.location) &&
				Objects.equals(name, that.name) &&
				Objects.equals(nation, that.nation) &&
				Objects.equals(nationalIdentificationNumber, that.nationalIdentificationNumber) &&
				Objects.equals(note, that.note) &&
				Objects.equals(phoneNumber, that.phoneNumber) &&
				Objects.equals(preferredChannel, that.preferredChannel) &&
				Objects.equals(province, that.province) &&
				Objects.equals(surname, that.surname) &&
				Objects.equals(type, that.type) &&
				Objects.equals(vatNumber, that.vatNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, admCode, aooCode, cap, city, description, email, fax, isCommonAddress, location, name, nation, nationalIdentificationNumber, note, phoneNumber, preferredChannel, province, surname, type, vatNumber);
	}

	public enum TipologiaCorrispondente {
		PERSONA_FISICA("P"), PERSONA_GIURIDICA("U");

		TipologiaCorrispondente(String codicePitre) {
			this.codicePitre = codicePitre;
		}

		private String codicePitre;

		String getCodicePitre() {
			return codicePitre;
		}
	}
}
