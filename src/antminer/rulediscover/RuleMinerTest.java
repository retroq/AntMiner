package antminer.rulediscover;

import antminer.rulediscover.graph.ConstructionGraph;
import antminer.rulediscover.graph.ConstructionGraphImpl;
import antminer.rulediscover.util.DataLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

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
        ClassificationRule rules = ruleMiner.extractRules(fixture);
        System.out.println(rules);
    }

    @Test
    public void test() throws Exception {
        RuleMiner ruleMiner = new RuleMinerImpl(10,20,10,1d,1d,35,0.1);
        List<Domain> domains = DataLoader.getInstance().loadDataFromArff("/home/anton/IdeaProjects/AntMiner/AntMiner/src/antminer/rulediscover/util/testData.arff");
//        final List<Domain> domains = domainsAll.stream()
//                .filter(domain ->
//                        domain.getDomainClass().equals(new SimpleDomainClass("unacc")))
//                .collect(Collectors.<Domain>toList());

        Collections.shuffle(domains);


        int trainingSetSize = domains.size()*9/10;
        List<Domain> trainingSet = domains.subList(0, trainingSetSize);


        final List<Domain> testSet = domains.subList(trainingSetSize, domains.size() - 1);


        ClassificationRule rules = ruleMiner.extractRules(trainingSet);


        int covered = 0;
        int correct = 0;
        int incorrect = 0;
        int uncovered = 0;
        for (Domain domain : testSet){
            if (rules.isCoveredByThisRule(domain)){
                covered++;
                if (rules.getRuleClass().equals(domain.getDomainClass()))
                    correct++;
                else
                    incorrect++;
            }
            else uncovered++;
        }

        final int rulesSize = ((RuleComposite) rules).getRules().size();
        System.out.println("rulesSize = " + rulesSize);

        System.out.println("covered = " + covered);
        System.out.println("uncovered = " + uncovered);
        System.out.println("correct = " + correct);
        System.out.println("incorrect = " + incorrect);
        System.out.println("coverage = " + covered/(double)testSet.size());
        System.out.println("accuracy = " + correct/(double)covered);

    }
}
