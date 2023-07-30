package org.springybot.model;

import org.springybot.botModel.Common;
import org.springybot.botModel.messageHandler.BasePrivateMessage_;

public class PrivateMessage_ extends BasePrivateMessage_{
    

    public PrivateMessage_(Common common) {
        super(common);
    }

    @Override
    public void handler() {
        throw new UnsupportedOperationException("Unimplemented method 'handler'");
    }
    
}
