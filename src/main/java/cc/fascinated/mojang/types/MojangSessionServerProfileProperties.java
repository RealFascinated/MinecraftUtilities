package cc.fascinated.mojang.types;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class MojangSessionServerProfileProperties {
    private String name;
    private String value;

    public MojangSessionServerProfileProperties() {}
}
