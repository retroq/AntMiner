package antminer.rulediscover;

import java.util.Collection;
import java.util.List;

/**
 * Date: 21.04.14
 * Time: 21:41
 *
 * @author anton
 */
public class RuleComposite implements ClassificationRule{
    private List<ClassificationRule> rules;
    private DomainClass domainClass;
    public RuleComposite(List<ClassificationRule> rules) {
        assert rules != null;
        this.rules = rules;
    }

    @Override
    public boolean isCoveredByThisRule(Domain dataLine) {
        for (ClassificationRule rule : rules)
            if (rule.isCoveredByThisRule(dataLine)){
                domainClass = rule.getRuleClass();
                return true;
            }
        return false;
    }

    public List<ClassificationRule> getRules() {
        return rules;
    }

    @Override
    public DomainClass getRuleClass() {
         return domainClass;
    }

    @Override
    public List<Term> getTerms() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addTerm(Term term) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getQuality(Collection<Domain> domains, int initialSize) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCoverage(Collection<Domain> domains) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void prune(Collection<Domain> domains) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DomainClass setMostFrequentClass(Collection<Domain> domains) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<Domain> filterUncovered(Collection<Domain> domains) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
