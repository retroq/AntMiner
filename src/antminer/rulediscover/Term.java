package antminer.rulediscover;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public interface Term {
    DomainAttribute getAttribute();
    DomainValue getValue();
    boolean test(Domain domain);
}
