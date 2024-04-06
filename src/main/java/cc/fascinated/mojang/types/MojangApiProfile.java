package cc.fascinated.mojang.types;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class MojangApiProfile {

    private String id;
    private String name;

    public MojangApiProfile() {}
}
