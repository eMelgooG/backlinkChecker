import java.util.ArrayList;
import java.util.List;

public class Tuple {

    public static Tuple GENERIC = new Tuple("");
    public String href;
    public List < String > anchors;

    public Tuple(String k) {
        this.href = k;
        anchors = new ArrayList < > ();
    }

    public void addValue(String value) {
        anchors.add(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((href == null) ? 0 : href.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tuple other = (Tuple) obj;
        if (href == null) {
            if (other.href != null)
                return false;
        } else if (!href.equals(other.href))
            return false;
        return true;
    }



}