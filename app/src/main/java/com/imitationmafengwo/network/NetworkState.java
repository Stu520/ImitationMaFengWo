package com.imitationmafengwo.network;

/**
 * Created with IntelliJ IDEA.
 * User: xiejm
 * Date: 8/21/12
 * Time: 3:22 PM
 */
public enum NetworkState {
    // actually it is conbination of network type and network state
    WIFI("wifi"), NET_2G("2g"), NET_2G_WAP("2g"), NET_3G("3g"), NET_4G("4g"), UNAVAILABLE("unavailable");

    private String name;
    private String operator;
    private String extra;

    NetworkState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getOperator() {
        return operator;
    }
    public String getExtra() {
        return extra;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean is234G() {
        boolean ret = false;

        if (NetworkState.NET_4G == this || NetworkState.NET_3G == this || NetworkState.NET_2G == this || NetworkState.NET_2G_WAP == this) {
            ret = true;
        }

        return ret;
    }
}
