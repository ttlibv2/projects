package vn.conyeu.cookie;

public class SampleObject {
    private final SameSite sameSite;

    protected SampleObject(SameSite sameSite) {
        this.sameSite = sameSite;
    }

    public boolean strict() {
        return SameSite.Strict == sameSite;
    }

    public boolean lax() {
        return SameSite.Lax == sameSite;
    }

    protected final void validateCookie(String name, String value) {
        if(strict()) Helper.validateCookieWithSameSiteIsStrict(name, value);
    }

    protected StringBuilder stringBuilder() {
        return new StringBuilder();
    }
}