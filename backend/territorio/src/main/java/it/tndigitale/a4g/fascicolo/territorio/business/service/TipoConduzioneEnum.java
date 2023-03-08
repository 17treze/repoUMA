package it.tndigitale.a4g.fascicolo.territorio.business.service;

public enum TipoConduzioneEnum {
	PROPRIETA("PROPRIETA'"), // 1
	AFFITTO("AFFITTO"), // 2
	MEZZADRIA("MEZZADRIA"), // 3
	ALTRA_FORMA("ALTRA FORMA"); // 4

	public final String label;
	
	public static TipoConduzioneEnum create(final int resultNumber) {
		TipoConduzioneEnum resEnum;
		switch(resultNumber) {
			case 1: resEnum = TipoConduzioneEnum.PROPRIETA; break;
			case 2: resEnum = TipoConduzioneEnum.AFFITTO; break;
			case 3: resEnum = TipoConduzioneEnum.MEZZADRIA; break;
			case 4: resEnum = TipoConduzioneEnum.ALTRA_FORMA; break;
		default: throw new IllegalArgumentException(
			String.format("Got unexpected value '%d' from stored procedure", resultNumber));
		}
		return resEnum;
	}

	private TipoConduzioneEnum(final String label) {
		this.label = label;
	}
}
