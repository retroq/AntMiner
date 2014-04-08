package antminer.rulediscover;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class SimpleStringDomainValue implements DomainValue{
    private String value;

    public SimpleStringDomainValue() {
    }

    public SimpleStringDomainValue(String value) {
        this.value = value;
    }



    @Override
    public String asString() {
        return value;
    }

    @Override
    public int compareTo(DomainValue o) {
        return value.compareTo(o.asString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleStringDomainValue that = (SimpleStringDomainValue) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return asString();
    }
}
