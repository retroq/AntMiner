package antminer.rulediscover;

import java.util.Collection;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public interface ClassificationRule {
    public boolean isCoveredByThisRule(Domain dataLine);
    public DomainClass getRuleClass();
    public void addTerm(Term term);
    public double getQuality(Collection<Domain> domains);
    public void prune(Collection<Domain> domains);
    //----------?????-------------
    public DomainClass getMostFrequentClass(Collection<Domain> domains);
    public Collection<Domain> filterUncovered(Collection<Domain> domains);
}
