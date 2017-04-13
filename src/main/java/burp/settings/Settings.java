package burp.settings;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 4/1/17.
 */
public class Settings {
    private boolean intervals1m;
    private boolean intervals2m;
    private boolean intervals4m;
    private boolean intervals6m;
    private boolean intervals8m;
    private boolean intervals12m;
    private boolean intervals16m;
    private boolean intervals20m;
    private boolean intervals24m;
    private boolean useBurp;

    boolean isIntervals1m() {
        return intervals1m;
    }

    void setIntervals1m(boolean intervals1m) {
        this.intervals1m = intervals1m;
    }

    boolean isIntervals2m() {
        return intervals2m;
    }

    void setIntervals2m(boolean intervals2m) {
        this.intervals2m = intervals2m;
    }

    boolean isIntervals4m() {
        return intervals4m;
    }

    void setIntervals4m(boolean intervals4m) {
        this.intervals4m = intervals4m;
    }

    boolean isIntervals6m() {
        return intervals6m;
    }

    void setIntervals6m(boolean intervals6m) {
        this.intervals6m = intervals6m;
    }

    boolean isIntervals8m() {
        return intervals8m;
    }

    void setIntervals8m(boolean intervals8m) {
        this.intervals8m = intervals8m;
    }

    boolean isIntervals12m() {
        return intervals12m;
    }

    void setIntervals12m(boolean intervals12m) {
        this.intervals12m = intervals12m;
    }

    boolean isIntervals16m() {
        return intervals16m;
    }

    void setIntervals16m(boolean intervals16m) {
        this.intervals16m = intervals16m;
    }

    boolean isIntervals20m() {
        return intervals20m;
    }

    void setIntervals20m(boolean intervals20m) {
        this.intervals20m = intervals20m;
    }

    boolean isIntervals24m() {
        return intervals24m;
    }

    void setIntervals24m(boolean intervals24m) {
        this.intervals24m = intervals24m;
    }

    boolean useBurp() {
        return useBurp;
    }

    void setUseBurp(boolean useBurp) {
        this.useBurp = useBurp;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "intervals1m=" + intervals1m +
                ", intervals2m=" + intervals2m +
                ", intervals4m=" + intervals4m +
                ", intervals6m=" + intervals6m +
                ", intervals8m=" + intervals8m +
                ", intervals12m=" + intervals12m +
                ", intervals16m=" + intervals16m +
                ", intervals20m=" + intervals20m +
                ", intervals24m=" + intervals24m +
                ", useBurp=" + useBurp +
                '}';
    }
}
