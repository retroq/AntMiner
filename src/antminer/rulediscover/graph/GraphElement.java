package antminer.rulediscover.graph;

import antminer.rulediscover.DomainValue;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public class GraphElement {
    private DomainValue domainValue;
    private double probability;
    private double pheromone;
    private double heuristic;

    public GraphElement() {
    }

    public GraphElement(DomainValue domainValue) {
        this.domainValue = domainValue;
    }

    public DomainValue getDomainValue() {
        return domainValue;
    }

    public void setDomainValue(DomainValue domainValue) {
        this.domainValue = domainValue;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getPheromone() {
        return pheromone;
    }

    public void setPheromone(double pheromone) {
        this.pheromone = pheromone;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphElement that = (GraphElement) o;

        if (domainValue != null ? !domainValue.equals(that.domainValue) : that.domainValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return domainValue != null ? domainValue.hashCode() : 0;
    }
}
