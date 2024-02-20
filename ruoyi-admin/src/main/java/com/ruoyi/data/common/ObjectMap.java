package com.ruoyi.data.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ObjectMap {
    @Getter
    @Setter
    @JsonProperty("key")
    private String key;
    @Getter
    @Setter
    @JsonProperty("value")
    private Double value;

    public ObjectMap(String key, Double value) {
        this.key = key;
        this.value = value;
    }
}
