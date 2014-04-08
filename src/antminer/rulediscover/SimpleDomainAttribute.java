package antminer.rulediscover;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class SimpleDomainAttribute implements DomainAttribute{
    private String name;

    public SimpleDomainAttribute() {
    }

    public SimpleDomainAttribute(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleDomainAttribute that = (SimpleDomainAttribute) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
