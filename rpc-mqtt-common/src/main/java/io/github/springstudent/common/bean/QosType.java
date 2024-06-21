package io.github.springstudent.common.bean;

/**
 * 服务质量枚举
 * @author ZhouNing
 **/
public enum QosType {
	
	QOS_AT_MOST_ONCE (0,"最多一次，有可能重复或丢失"), 
	
	QOS_AT_LEAST_ONCE (1,"至少一次，有可能重复"), 
	
	QOS_EXACTLY_ONCE (2,"只有一次，确保消息只到达一次");
	
	private final int type;
    private final String description;
      
    QosType(int type, String description) {
        this.type = type;  
        this.description = description;  
    }  
      
    public int type() {  
        return this.type;  
    }  
      
    public String description() {  
        return this.description;  
    }  
}
