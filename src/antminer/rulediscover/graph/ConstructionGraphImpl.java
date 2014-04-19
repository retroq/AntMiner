package antminer.rulediscover.graph;

import antminer.rulediscover.*;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class ConstructionGraphImpl implements ConstructionGraph {
    /*
        Основные данные для построения правил:

         Атрибут -> {(значение, вероятность, эвристика, феромон), ... }
     */
    private HashMap<DomainAttribute, Set<GraphElement>> attributesElements;

    Set<DomainClass> domainClasses;

    private Collection<Domain> domainCollection;
    /*
        Мультимножество для подсчёта количества доменов с одинаковыми кортежами (аттрибут, значение, класс)
        необходим для вычисления эвристик (в часности энтропии)
     */
    private Multiset<ACVMultisetEntry> attributeClassValueHistogram;

    /*
        Мультимножество для подсчёта количества доменов с одинаковыми кортежами (аттрибут, значение)
        необходим для вычисления эвристик (в часности энтропии)
     */
    private Multiset<AVEntry> attributeValueHistogram;

    /*
        Степень учёта феромона [0..1]
     */
    private double A = 1;

    /*
        Степень учёта эвристик [0..1]
     */
    private double B = 1;

    /*
        Скорость испарения феромонов
     */
    private double evaporationFactor = 0.1;

    private int min_coverage = 30;

    private int max_coverage_fails = 15;

    private Integer classesNumber = null;


    Logger log = LoggerFactory.getLogger(ConstructionGraphImpl.class);

    private static class ACVMultisetEntry {
        private DomainAttribute domainAttribute;
        private DomainClass domainClass;
        private DomainValue domainValue;

        private ACVMultisetEntry(DomainAttribute domainAttribute, DomainClass domainClass, DomainValue domainValue) {
            this.domainAttribute = domainAttribute;
            this.domainClass = domainClass;
            this.domainValue = domainValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ACVMultisetEntry that = (ACVMultisetEntry) o;

            if (!domainAttribute.equals(that.domainAttribute)) return false;
            if (!domainClass.equals(that.domainClass)) return false;
            if (!domainValue.equals(that.domainValue)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = domainAttribute.hashCode();
            result = 31 * result + domainClass.hashCode();
            result = 31 * result + domainValue.hashCode();
            return result;
        }
    }

    private static class AVEntry {
        private DomainAttribute attribute;
        private DomainValue value;

        private AVEntry(DomainAttribute attribute, DomainValue value) {
            this.attribute = attribute;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AVEntry avEntry = (AVEntry) o;

            if (!attribute.equals(avEntry.attribute)) return false;
            if (!value.equals(avEntry.value)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = attribute.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }
    }

    @Override
    public void init(Collection<Domain> domains) {

        domainClasses = new HashSet<DomainClass>();
        attributesElements = new HashMap<DomainAttribute, Set<GraphElement>>();
        attributeValueHistogram = HashMultiset.create();
        attributeClassValueHistogram = HashMultiset.create();
        domainCollection = domains;

        for (DomainAttribute domainAttribute : domains.iterator().next().getDomainAttributes()) {
            attributesElements.put(domainAttribute, new HashSet<GraphElement>());
        }

        for (Domain domain : domains) {
            domainClasses.add(domain.getDomainClass());

            for (DomainAttribute domainAttribute : domain.getDomainAttributes()) {
                DomainValue value = domain.getDomainValue(domainAttribute);
                attributesElements.get(domainAttribute).add(new GraphElement(value));

                attributeClassValueHistogram.add(new ACVMultisetEntry(domainAttribute, domain.getDomainClass(), value));

                attributeValueHistogram.add(new AVEntry(domainAttribute, value));


            }
        }

        initHeuristics();
        initPheromones();
        initProbabilities();

    }

    //todo test
    private void initHeuristics() {
        if (classesNumber == null)
            classesNumber = domainClasses.size();
        HashMap<AVEntry, Double> entropies = new HashMap<AVEntry, Double>();
        double log2m = Math.log(classesNumber) / Math.log(2);
        double heuristicDivider = 0;

        for (DomainAttribute domainAttribute : attributesElements.keySet()) {
            List<GraphElement> elements = new ArrayList<GraphElement>(attributesElements.get(domainAttribute));
            for (int i = 0; i < elements.size(); i++) {
                GraphElement element = elements.get(i);
                double lengthT = attributeValueHistogram.count(new AVEntry(domainAttribute, element.getDomainValue()));
                double entropy = 0;
                for (DomainClass domainClass : domainClasses) {
                    double freqT = attributeClassValueHistogram.count(
                            new ACVMultisetEntry(
                                    domainAttribute,
                                    domainClass,
                                    element.getDomainValue()));
                    if (freqT == 0)
                        continue;
                    double tmp = freqT / lengthT;
                    entropy += tmp * (Math.log(tmp) / Math.log(2.));
                }
                entropy = -entropy;
                entropies.put(new AVEntry(domainAttribute, element.getDomainValue()), entropy);
                heuristicDivider += (log2m - entropy);
            }
        }

        for (DomainAttribute domainAttribute : attributesElements.keySet()) {
            List<GraphElement> elements = new ArrayList<GraphElement>(attributesElements.get(domainAttribute));
            for (int i = 0; i < elements.size(); i++) {
                GraphElement element = elements.get(i);
                double currentValueEntropy = entropies.get(new AVEntry(domainAttribute, element.getDomainValue()));
                double heuristic = (log2m - currentValueEntropy) / heuristicDivider;
                if (Double.isNaN(heuristic))
                    log.error("NaN heuristic detected");
                element.setHeuristic(heuristic);
            }
        }

    }

    private void initPheromones() {
        for (DomainAttribute attribute : attributesElements.keySet()) {
            Collection<GraphElement> elements = attributesElements.get(attribute);
            for (GraphElement element : elements) {
                element.setPheromone(1. / elements.size());
            }
        }
    }

    private void initProbabilities() {
        for (DomainAttribute attribute : attributesElements.keySet()) {
            Collection<GraphElement> elements = attributesElements.get(attribute);
            double probabilitySum = 0;
            for (GraphElement element : elements) {
                double heuristic = element.getHeuristic();
                double pheromone = element.getPheromone();
                double probability = Math.pow(heuristic, A) * Math.pow(pheromone, B);
                element.setProbability(probability);
                probabilitySum += probability;
            }
            for (GraphElement element : elements) {
                element.setProbability(element.getProbability() / probabilitySum);
            }
        }
    }

    @Override
    public ClassificationRule generateRule() {
        ClassificationRule rule = new ClassificationRuleImpl();
        int coverageFails = 0;
        final int attributesNumber = attributesElements.keySet().size();
        final List<DomainAttribute> attributes = new LinkedList<DomainAttribute>(attributesElements.keySet());

        //while(rule.getTerms().size() <  attributesNumber && coverageFails <= max_coverage_fails){
        //    DomainAttribute attribute = attributes.get((int)Math.round(Math.random()*(attributes.size() - 1)));
        for (DomainAttribute attribute : attributesElements.keySet()) {
            //todo find way to optimize : use list in attributesElements
            List<GraphElement> graphElementList = new ArrayList<GraphElement>(attributesElements.get(attribute));
            List<Double> densities = new ArrayList<Double>(graphElementList.size());
            for (int i = 0; i < graphElementList.size(); i++) {
                densities.add(0.);
            }
            densities.set(0, graphElementList.get(0).getProbability());
            for (int i = 1; i < densities.size(); i++) {
                densities.set(i, densities.get(i - 1) + graphElementList.get(i).getProbability());
            }
            int selectedValueIndex = 0;
            double randomNumber = Math.random();
            while (selectedValueIndex < densities.size() && randomNumber >= densities.get(selectedValueIndex))
                selectedValueIndex++;
            Term term = new SimpleTerm(attribute, graphElementList.get(selectedValueIndex).getDomainValue());
            if (rule.getTerms().contains(term))
                continue;
            rule.addTerm(term);
            if (rule.getCoverage(domainCollection) < min_coverage) {
                rule.getTerms().remove(term);
                coverageFails++;
                continue;
            }
            attributes.remove(attribute);
        }
        return rule;
    }

    @Override
    public void updateProbabilities(ClassificationRule rule, double quality) {
        updatePheromones(rule, quality);
        initProbabilities();
    }

    private void updatePheromones(ClassificationRule rule, double quality) {

        for (Term term : rule.getTerms()) {
            Collection<GraphElement> elements = attributesElements.get(term.getAttribute());
            double pheromoneSum = 0d;
            for (GraphElement element : elements) {
                if (element.getDomainValue().equals(term.getValue())) {
                    double currentPheromone = element.getPheromone();
                    element.setPheromone((1 - evaporationFactor) * currentPheromone + (1. - 1. / (1 + quality)) * currentPheromone);
                }
                pheromoneSum += element.getPheromone();
            }
            for (GraphElement element : elements) {
                double pheromone = element.getPheromone();
                element.setPheromone(pheromone / pheromoneSum);
            }
        }
    }
}
