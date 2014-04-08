package antminer.rulediscover.graph;

import antminer.rulediscover.*;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.*;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class AbstractConstructionGraph implements ConstructionGraph{
    /*
        Основные данные для построения правил Атрибут -> (значение, вероятность, эвристика, феромон)
     */
    private HashMap<DomainAttribute, List<GraphElement>> attributesElements;

    Set<DomainClass> domainClasses;
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
        Значения энтропии для соответствующих пар (Аттрибут->Значение)
     */
    private HashMap<AVEntry, Double> entropies;

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
    public void init(List<DomainAttribute> attributes, Collection<Domain> domains) {

        domainClasses = new HashSet<DomainClass>();
        attributesElements = new HashMap<DomainAttribute, List<GraphElement>>();
        attributeValueHistogram = HashMultiset.create();
        attributeClassValueHistogram = HashMultiset.create();

        for (DomainAttribute domainAttribute : attributes){
            attributesElements.put(domainAttribute, new LinkedList<GraphElement>());
        }

        for (Domain domain : domains){
            domainClasses.add(domain.getDomainClass());

            for (DomainAttribute domainAttribute : domain.getDomainAttributes()){
                DomainValue value = domain.getDomainValue(domainAttribute);
                attributesElements.get(domainAttribute).add(new GraphElement(value));

                attributeClassValueHistogram.add(new ACVMultisetEntry(domainAttribute, domain.getDomainClass(), value));

                attributeValueHistogram.add(new AVEntry(domainAttribute, value));


            }
        }


    }

    private void initHeuristic(){
        entropies = new HashMap<AVEntry, Double>();

        for (DomainAttribute domainAttribute : attributesElements.keySet()){
            List<GraphElement> elements = new ArrayList<GraphElement>(attributesElements.get(domainAttribute));
            for (int i = 0; i < elements.size(); i++) {
                GraphElement element = elements.get(i);
                double lengthT = attributeValueHistogram.count(new AVEntry(domainAttribute, element.getDomainValue()));
                double entropy = 0;
                for (DomainClass domainClass : domainClasses){
                    double freqT = attributeClassValueHistogram.count(new ACVMultisetEntry(domainAttribute, domainClass, element.getDomainValue()));
                    double tmp = freqT/lengthT;
                    entropy += tmp* (Math.log(tmp)/Math.log(2.));
                }
                entropy = -entropy;
                entropies.put(new AVEntry(domainAttribute, element.getDomainValue()), entropy);
            }
        }




    }

    @Override
    public  ClassificationRule generateRule() {
        return null;
    }

    @Override
    public void updateProbabilities(ClassificationRule rule) {

    }
}
