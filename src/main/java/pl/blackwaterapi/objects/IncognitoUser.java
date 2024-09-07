package pl.blackwaterapi.objects;

import com.mojang.authlib.properties.Property;
import pl.blackwaterapi.utils.IncognitoUserUtil;

public class IncognitoUser {

    private String name;
    private String changeNick;
    private Property textures;

    public IncognitoUser(String name) {
        this.name = name;
        this.changeNick = name;
        this.textures = null;
        IncognitoUserUtil.addIncognitoUser(this);
    }

    public String getChangeNick() {
        return changeNick;
    }

    public void setChangeNick(String changeNick) {
        this.changeNick = changeNick;
    }

    public Property getTextures() {
        return textures;
    }

    public void setTextures(Property textures) {
        this.textures = textures;
    }

    public String getName() {
        return name;
    }
    public Boolean runing() {
        return !getName().equalsIgnoreCase(getChangeNick());
    }

}