package antminer.rulediscover;

import antminer.rulediscover.graph.ConstructionGraph;
import antminer.rulediscover.graph.ConstructionGraphImpl;
import antminer.rulediscover.util.DataLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 09.04.14
 * Time: 0:55
 *
 * @author anton
 */
public class RuleMinerTest {
    List<Domain> fixture = new LinkedList<Domain>();
    List<DomainAttribute> attributes;
    ConstructionGraph graph;


    @Before
    public void setUp() {
        fixture.add(getDomain(new String[][]{{"Condition", "Excellent"}, {"Safety", "Bad"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Very Good"}, {"Safety", "Very Good"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Good"}, {"Safety", "Good"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Good"}, {"Safety", "Very Good"}}, "Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Very Good"}}, "Wait"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Very Good"}}, "Wait"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Good"}}, "Not Buy"));
        fixture.add(getDomain(new String[][]{{"Condition", "Bad"}, {"Safety", "Bad"}}, "Not Buy"));
        attributes = Arrays.<DomainAttribute>asList(
                new SimpleDomainAttribute("Condition"),
                new SimpleDomainAttribute("Safety")
        );
        graph = new ConstructionGraphImpl();
        graph.init(fixture);
    }

    private Domain getDomain(String[][] attributes, String domainClass) {
        HashMap<DomainAttribute, DomainValue> map = new HashMap<DomainAttribute, DomainValue>();

        for (int i = 0; i < attributes.length; i++) {
            map.put(new SimpleDomainAttribute(attributes[i][0]), new SimpleStringDomainValue(attributes[i][1]));
        }
        Domain domain = new SimpleDomain(new SimpleDomainClass(domainClass), map);
        return domain;
    }

    @Test
    public void testExtractRule() {
        RuleMiner ruleMiner = new RuleMinerImpl();
        List<ClassificationRule> rules = ruleMiner.extractRules(fixture);
        System.out.println(rules);
    }

    @Test
    public void test() throws Exception {
        RuleMiner ruleMiner = new RuleMinerImpl();
        List<Domain> domains = DataLoader.getInstance().loadDataFromArff("/home/anton/IdeaProjects/AntMiner/AntMiner/src/antminer/rulediscover/util/testData.arff");

        for (Domain domain : domains)
            System.out.println(domain);
        List<ClassificationRule> rules = ruleMiner.extractRules(domains);
        for (ClassificationRule rule : rules) {
            System.out.println("Q = " + rule.getQuality(domains, domains.size()) + "; Cov = " + rule.getCoverage(domains) + "; Rule = " + rule);
        }
    }
}
