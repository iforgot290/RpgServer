package me.neildennis.crypticrpg.items.attribs;

public enum AttributeType {
	
	DAMAGE("DMG: ", ""),
	HEALTH("HEALTH: ", "");
	
	private String prefix, postfix;
	
	AttributeType(String prefix, String postfix){
		this.prefix = prefix;
		this.postfix = postfix;
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public String getPostfix(){
		return postfix;
	}
	
	public static AttributeType fromPrefix(String str){
		for (AttributeType attr : AttributeType.values())
			if (str.startsWith(attr.getPrefix()))
				return attr;
		return null;
	}

}
