package antminer.rulediscover;

/**
 * Date: 08.04.14
 * Time: 21:49
 *
 * @author anton
 */
public class SimpleTerm implements Term {
    private DomainAttribute attribute;
    private DomainValue value;

    public SimpleTerm() {
    }

    public SimpleTerm(DomainAttribute attribute, DomainValue value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public DomainAttribute getAttribute() {
        return attribute;
    }

    @Override
    public DomainValue getValue() {
        return value;
    }

    @Override
    public boolean test(Domain domain) {
        return domain.getDomainValue(attribute).equals(value);
    }

    @Override
    public String toString() {
        return attribute +
                " = " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleTerm that = (SimpleTerm) o;

        if (attribute != null ? !attribute.equals(that.attribute) : that.attribute != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attribute != null ? attribute.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
