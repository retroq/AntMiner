package antminer.rulediscover;

import java.util.Collection;
import java.util.List;

/**
 * Date: 08.04.14
 *
 * @author sipachev_ai
 */
public interface ClassificationRule {
    public boolean isCoveredByThisRule(Domain dataLine);

    public DomainClass getRuleClass();

    public List<Term> getTerms();

    public void addTerm(Term term);

    /**
     * Получить качество условия (правила) для данного набора данных domains
     */
    public double getQuality(Collection<Domain> domains, int initialSize);

    public int getCoverage(Collection<Domain> domains);

    public void prune(Collection<Domain> domains);

    /**
     * Устанавливает для данного условия (правила) характеризуемый им класс
     * Т.е. если домен покрывается данным правилом, то его следует отнести к этому классу
     * Выбирается класс, который чаще всего встречается в подмножестве всех доменов,
     * покрываемых данным правилом
     *
     * @param domains - полный набор данных (включая не покрываемые данным правилом)
     * @return класс,
     */
    public DomainClass setMostFrequentClass(Collection<Domain> domains);

    public Collection<Domain> filterUncovered(Collection<Domain> domains);
}
