package antminer.rulediscover;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 08.04.14
 * Time: 20:30
 *
 * @author anton
 */
public class ClassificationRuleImpl implements ClassificationRule {
    private List<Term> terms = new LinkedList<Term>();
    private DomainClass domainClass;

    private static Logger log = LoggerFactory.getLogger(ClassificationRuleImpl.class);

    public ClassificationRuleImpl() {

    }

    @Override
    public boolean isCoveredByThisRule(Domain domain) {
        boolean covered = true;
        for (Term term : terms)
            covered &= term.test(domain);
        return covered;
    }

    @Override
    public DomainClass getRuleClass() {
        if (domainClass == null) {
            throw new IllegalStateException("Can't return domain class: Call setMostFrequentClass first");
        }
        return domainClass;
    }

    @Override
    public List<Term> getTerms() {
        return terms;
    }

    @Override
    public void addTerm(Term term) {
        terms.add(term);
    }

    @Override
    public double getQuality(Collection<Domain> domains, int initialSize) {
        int TP = 0,           //true positive
                TN = 0,       //true negative
                FP = 0,       //false positive
                FN = 0;       //false negative

        //Если после выполнения setMostFrequentClass значение не установлено,
        // значит правило не покрывает ни одного случая и его качество = 0
        if (domainClass == null)
            return 0;

        for (Domain domain : domains) {
            if (isCoveredByThisRule(domain)) {
                if (domain.getDomainClass().equals(getRuleClass()))
                    TP += 1;
                else
                    FP += 1;
            } else if (domain.getDomainClass().equals(getRuleClass()))
                FN += 1;
            else
                TN += 1;
        }
        double covered = TP + FP;

        //double quality = ((TP) / (double) (TP + FN)) * (TN / (double) (FP + TN));
        double quality = (TP) / covered + TP / initialSize;
        return quality;
    }

    @Override
    public int getCoverage(Collection<Domain> domains) {
        int covered = 0;
        for (Domain domain : domains) {
            if (isCoveredByThisRule(domain))
                covered++;
        }
        return covered;
    }

    @Override
    public void prune(Collection<Domain> domains) {

    }

    @Override
    public DomainClass setMostFrequentClass(Collection<Domain> domains) {
        Multiset<DomainClass> classes = HashMultiset.create();
        for (Domain domain : domains) {
            if (isCoveredByThisRule(domain))
                classes.add(domain.getDomainClass());
        }
        int maxCount = -1;
        DomainClass mostFrequentClass = null;
        for (DomainClass domainClass1 : classes) {
            int count = classes.count(domainClass1);
            if (count > maxCount) {
                maxCount = count;
                mostFrequentClass = domainClass1;
            }
        }
//        if (mostFrequentClass == null)
//            log.warn("No classes covered by the rule");
        domainClass = mostFrequentClass;
        return mostFrequentClass;
    }

    @Override
    public Collection<Domain> filterUncovered(Collection<Domain> domains) {
        List<Domain> filteredDomainList = new LinkedList<Domain>();
        for (Domain domain : domains) {
            if (!isCoveredByThisRule(domain))
                filteredDomainList.add(domain);
        }
        return filteredDomainList;
    }

    @Override
    public String toString() {
        return "{" +
                "terms=" + terms +
                "; domainClass=" + domainClass +
                '}';
    }
}
