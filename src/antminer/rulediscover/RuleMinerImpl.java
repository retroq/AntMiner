package antminer.rulediscover;

import antminer.rulediscover.graph.ConstructionGraph;
import antminer.rulediscover.graph.ConstructionGraphImpl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 09.04.14
 * Time: 0:37
 *
 * @author anton
 */
public class RuleMinerImpl implements RuleMiner {
    private int antsNumber = 20;
    private int stepsNumber = 10;
    private int maxUncoveredCases = 50;

    @Override
    public List<ClassificationRule> extractRules(Collection<Domain> domains) {
        ConstructionGraph graph = new ConstructionGraphImpl();
        List<ClassificationRule> rules = new LinkedList<ClassificationRule>();
        int datasetTotalSize = domains.size();
        while (domains.size() > maxUncoveredCases) {
            graph.init(domains);
            ClassificationRule bestRule = null;
            for (int i = 0; i < stepsNumber; i++) {
                double Qbest = 0;

                for (int j = 0; j < antsNumber; j++) {
                    ClassificationRule rule = graph.generateRule();
                    rule.setMostFrequentClass(domains);
                    double Qcurrent = rule.getQuality(domains, datasetTotalSize);
                    if (Qcurrent > Qbest) {
                        Qbest = Qcurrent;
                        bestRule = rule;
                    }
                }
                if (bestRule == null)
                    break;
                graph.updateProbabilities(bestRule, Qbest);
            }
            if (bestRule == null)
                continue;
            rules.add(bestRule);
            domains = bestRule.filterUncovered(domains);
        }
        return rules;
    }

    public static Collection<Domain> apply(List<ClassificationRule> rules, Collection<Domain> domains) {
        for (Domain domain : domains) {
            for (ClassificationRule rule : rules) {
                if (rule.isCoveredByThisRule(domain))
                    domain.setDomainClass(rule.getRuleClass());
            }
        }
        return domains;
    }
}
