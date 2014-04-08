package antminer.rulediscover;

/**
 * Date: 08.04.14
 * Time: 21:49
 *
 * @author anton
 */
public class SimpleTerm implements Term{
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
        return   attribute +
                " = " + value;
    }
}
