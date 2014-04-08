package antminer.rulediscover;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class SimpleDomain implements Domain{
    private DomainClass domainClass;
    private HashMap<DomainAttribute, DomainValue> values;

    public SimpleDomain() {
    }

    public SimpleDomain(DomainClass domainClass, HashMap<DomainAttribute, DomainValue> values) {
        this.domainClass = domainClass;
        this.values = values;
    }

    @Override
    public DomainClass getDomainClass() {
        return domainClass;
    }

    @Override
    public List<DomainAttribute> getDomainAttributes() {
        return new LinkedList<DomainAttribute>(values.keySet());
    }

    @Override
    public DomainValue getDomainValue(DomainAttribute attribute) {
        return values.get(attribute);
    }
}
