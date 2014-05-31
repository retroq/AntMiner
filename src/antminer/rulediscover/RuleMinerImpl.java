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

    private double a = 1.0;
    private double b = 1.0;
    private int minCoverage = 2;
    private double evaporationFactor = 0.1;

    public RuleMinerImpl() {
    }

    public RuleMinerImpl(int antsNumber, int stepsNumber, int maxUncoveredCases, double a, double b, int minCoverage, double evaporationFactor) {
        this.antsNumber = antsNumber;
        this.stepsNumber = stepsNumber;
        this.maxUncoveredCases = maxUncoveredCases;
        this.a = a;
        this.b = b;
        this.minCoverage = minCoverage;
        this.evaporationFactor = evaporationFactor;
    }

    @Override
    public ClassificationRule extractRules(Collection<Domain> domains) {
        ConstructionGraph graph = new ConstructionGraphImpl(a,b,evaporationFactor,minCoverage);
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
        return new RuleComposite(rules);
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
