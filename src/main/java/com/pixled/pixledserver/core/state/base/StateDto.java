package com.pixled.pixledserver.core.state.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pixled.pixledserver.core.ToggleState;

public class StateDto {

    private ToggleState toggle;

    @JsonCreator
    public StateDto(
            @JsonProperty("toggle") String toggle
    ){
        this.toggle = toggle.equals("ON") ? ToggleState.ON : ToggleState.OFF;
    }

    public StateDto(State state) {
        toggle = state.getToggleState();
    }

    public ToggleState getToggle() {
        return toggle;
    }

    public void setToggle(ToggleState toggle) {
        this.toggle = toggle;
    }
}
