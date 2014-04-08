package antminer.rulediscover;

import java.util.List;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public interface Domain {
    public DomainClass getDomainClass();
    public List<DomainAttribute> getDomainAttributes();
    public DomainValue getDomainValue(DomainAttribute attribute);
}
