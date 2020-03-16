package main.java.com.ha.enums;

/**
 * Description:
 * User: Ellison
 * Date: 2019-09-03
 * Time: 9:43
 * Modified:
 */
public enum typeEnum {
    type0("0"),
    type1("1"),
    type2("2");

    private String typeName;

    typeEnum(String typeName) {
        this.typeName = typeName;
    }
    public String getTypeName() {
        return this.typeName;
    }
}
